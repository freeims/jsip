package com.github.freeims.javax.sip;

/**
 * The interface for a log record. The log records are generated by calling the
 * LogReocrdFactory instance that is registered with the stack.
 *
 * @author M. Ranganathan
 *
 */
public interface LogRecord {

    public abstract boolean equals(Object other);

    /**
     * Get an XML String for this message
     */

    public abstract String toString();

}
