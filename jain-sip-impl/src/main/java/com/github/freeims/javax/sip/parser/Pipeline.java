/*
 * Conditions Of Use
 *
 * This software was developed by employees of the National Institute of
 * Standards and Technology (NIST), an agency of the Federal Government.
 * Pursuant to title 15 Untied States Code Section 105, works of NIST
 * employees are not subject to copyright protection in the United States
 * and are considered to be in the public domain.  As a result, a formal
 * license is not needed to use the software.
 *
 * This software is provided by NIST as a service and is expressly
 * provided "AS IS."  NIST MAKES NO WARRANTY OF ANY KIND, EXPRESS, IMPLIED
 * OR STATUTORY, INCLUDING, WITHOUT LIMITATION, THE IMPLIED WARRANTY OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, NON-INFRINGEMENT
 * AND DATA ACCURACY.  NIST does not warrant or make any representations
 * regarding the use of the software or the results thereof, including but
 * not limited to the correctness, accuracy, reliability or usefulness of
 * the software.
 *
 * Permission to use this software is contingent upon your acceptance
 * of the terms of this agreement
 *
 * .
 *
 */
package com.github.freeims.javax.sip.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import com.github.freeims.core.InternalErrorHandler;
import com.github.freeims.javax.sip.stack.SIPStackTimerTask;
import com.github.freeims.javax.sip.stack.timers.SipTimer;

/**
 * Input class for the pipelined parser. Buffer all bytes read from the socket
 * and make them available to the message parser.
 *
 * @author M. Ranganathan (Contains a bug fix contributed by Rob Daugherty (
 *         Lucent Technologies) )
 *
 */

public class Pipeline extends InputStream {
    private LinkedList buffList;

    private Buffer currentBuffer;

    private boolean isClosed;

    private SipTimer timer;

    private InputStream pipe;

    private int readTimeout;

    private SIPStackTimerTask myTimerTask;

    class MyTimer extends SIPStackTimerTask {
        Pipeline pipeline;

        private boolean isCancelled;

        protected MyTimer(Pipeline pipeline) {
            this.pipeline = pipeline;
        }

        public void runTask() {
            if (this.isCancelled) {
                this.pipeline = null;
                return;
            }

            try {
                pipeline.close();
            } catch (IOException ex) {
                InternalErrorHandler.handleException(ex);
            }
        }

        @Override
        public void cleanUpBeforeCancel() {
            this.isCancelled = true;
            this.pipeline = null;
            super.cleanUpBeforeCancel();
        }

    }

    class Buffer {
        byte[] bytes;

        int length;

        int ptr;

        public Buffer(byte[] bytes, int length) {
            ptr = 0;
            this.length = length;
            this.bytes = bytes;
        }

        public int getNextByte() {
            return (int) bytes[ptr++] & 0xFF;
        }

    }

    public void startTimer() {
        if (this.readTimeout == -1)
            return;
        // TODO make this a tunable number. For now 4 seconds
        // between reads seems reasonable upper limit.
        this.myTimerTask = new MyTimer(this);
        this.timer.schedule(this.myTimerTask, this.readTimeout);
    }

    public void stopTimer() {
        if (this.readTimeout == -1)
            return;
        if (this.myTimerTask != null)
            this.timer.cancel(myTimerTask);
    }

    public Pipeline(InputStream pipe, int readTimeout, SipTimer timer) {
        // pipe is the Socket stream
        // this is recorded here to implement a timeout.
        this.timer = timer;
        this.pipe = pipe;
        buffList = new LinkedList();
        this.readTimeout = readTimeout;
    }

    public void write(byte[] bytes, int start, int length) throws IOException {
        if (this.isClosed)
            throw new IOException("Closed!!");
        Buffer buff = new Buffer(bytes, length);
        buff.ptr = start;
        synchronized (this.buffList) {
            buffList.add(buff);
            buffList.notifyAll();
        }
    }

    public void write(byte[] bytes) throws IOException {
        if (this.isClosed)
            throw new IOException("Closed!!");
        Buffer buff = new Buffer(bytes, bytes.length);
        synchronized (this.buffList) {
            buffList.add(buff);
            buffList.notifyAll();
        }
    }

    public void close() throws IOException {
        this.isClosed = true;
        synchronized (this.buffList) {
            this.buffList.notifyAll();
        }

        // JvB: added
        this.pipe.close();
    }

    public int read() throws IOException {
        // if (this.isClosed) return -1;
        synchronized (this.buffList) {
            if (currentBuffer != null
                    && currentBuffer.ptr < currentBuffer.length) {
                int retval = currentBuffer.getNextByte();
                if (currentBuffer.ptr == currentBuffer.length)
                    this.currentBuffer = null;
                return retval;
            }
            // Bug fix contributed by Rob Daugherty.
            if (this.isClosed && this.buffList.isEmpty())
                return -1;
            try {
                // wait till something is posted.
                while (this.buffList.isEmpty()) {
                    this.buffList.wait();
                    // jeand : Issue 314 : return -1 only is the buffer is empty
                    if (this.buffList.isEmpty() && this.isClosed)
                        return -1;
                }
                currentBuffer = (Buffer) this.buffList.removeFirst();
                int retval = currentBuffer.getNextByte();
                if (currentBuffer.ptr == currentBuffer.length)
                    this.currentBuffer = null;
                return retval;
            } catch (InterruptedException ex) {
                throw new IOException(ex.getMessage());
            } catch (NoSuchElementException ex) {
                ex.printStackTrace();
                throw new IOException(ex.getMessage());
            }
        }
    }

    /**
     * @return the isClosed
     */
    public boolean isClosed() {
        return isClosed;
    }

}
