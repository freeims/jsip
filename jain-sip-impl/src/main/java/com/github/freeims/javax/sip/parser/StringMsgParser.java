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

/*******************************************************************************
 * Product of NIST/ITL Advanced Networking Technologies Division (ANTD)        *
 ******************************************************************************/

package com.github.freeims.javax.sip.parser;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
/*
 * Acknowledgement: 1/12/2007: Yanick Belanger rewrote the parsing loops to make them
 * simpler and quicker.
 */

import com.github.freeims.core.CommonLogger;
import com.github.freeims.core.Host;
import com.github.freeims.core.HostNameParser;
import com.github.freeims.core.StackLogger;
import com.github.freeims.javax.sip.SIPConstants;
import com.github.freeims.javax.sip.address.AddressImpl;
import com.github.freeims.javax.sip.address.GenericURI;
import com.github.freeims.javax.sip.address.SipUri;
import com.github.freeims.javax.sip.address.TelephoneNumber;
import com.github.freeims.javax.sip.header.ExtensionHeaderImpl;
import com.github.freeims.javax.sip.header.NameMap;
import com.github.freeims.javax.sip.header.RequestLine;
import com.github.freeims.javax.sip.header.SIPHeader;
import com.github.freeims.javax.sip.header.StatusLine;
import com.github.freeims.javax.sip.message.SIPMessage;
import com.github.freeims.javax.sip.message.SIPRequest;
import com.github.freeims.javax.sip.message.SIPResponse;

/**
 * Parse SIP message and parts of SIP messages such as URI's etc from memory and
 * return a structure. Intended use: UDP message processing. This class is used
 * when you have an entire SIP message or SIPHeader or SIP URL in memory and you
 * want to generate a parsed structure from it. For SIP messages, the payload
 * can be binary or String. If you have a binary payload, use
 * parseSIPMessage(byte[]) else use parseSIPMessage(String) The payload is
 * accessible from the parsed message using the getContent and getContentBytes
 * methods provided by the SIPMessage class. If SDP parsing is enabled using the
 * parseContent method, then the SDP body is also parsed and can be accessed
 * from the message using the getSDPAnnounce method. Currently only eager
 * parsing of the message is supported (i.e. the entire message is parsed in one
 * feld swoop).
 *
 *
 * @version 1.2 $Revision: 1.28 $ $Date: 2010-05-06 14:07:44 $
 *
 * @author M. Ranganathan <br/>
 *
 *
 */
public class StringMsgParser implements MessageParser {

    protected static boolean computeContentLengthFromMessage = false;

    private static StackLogger logger = CommonLogger.getLogger(StringMsgParser.class);

    /**
     * @since v0.9
     */
    public StringMsgParser() {
        super();
    }

    /**
     * Parse a buffer containing a single SIP Message where the body is an array
     * of un-interpreted bytes. This is intended for parsing the message from a
     * memory buffer when the buffer. Incorporates a bug fix for a bug that was
     * noted by Will Sullin of Callcast
     *
     * @param msgBuffer
     *                  a byte buffer containing the messages to be parsed. This can
     *                  consist of multiple SIP Messages concatenated together.
     * @return a SIPMessage[] structure (request or response) containing the
     *         parsed SIP message.
     * @exception ParseException
     *                           is thrown when an illegal message has been
     *                           encountered
     *                           (and the rest of the buffer is discarded).
     * @see ParseExceptionListener
     */
    public SIPMessage parseSIPMessage(byte[] msgBuffer, boolean readBody, boolean strict,
            ParseExceptionListener parseExceptionListener) throws ParseException {
        if (msgBuffer == null || msgBuffer.length == 0)
            return null;

        int i = 0;

        // Squeeze out any leading control character.
        try {
            while (msgBuffer[i] < 0x20)
                i++;
        } catch (ArrayIndexOutOfBoundsException e) {
            // Array contains only control char, return null.
            if (logger.isLoggingEnabled(StackLogger.TRACE_DEBUG)) {
                logger.logDebug("handled only control char so returning null");
            }
            return null;
        }

        // Iterate thru the request/status line and headers.
        String currentLine = null;
        String currentHeader = null;
        boolean isFirstLine = true;
        SIPMessage message = null;
        do {
            int lineStart = i;

            // Find the length of the line.
            try {
                while (msgBuffer[i] != '\r' && msgBuffer[i] != '\n')
                    i++;
            } catch (ArrayIndexOutOfBoundsException e) {
                // End of the message.
                break;
            }
            int lineLength = i - lineStart;

            // Make it a String.
            try {
                currentLine = new String(msgBuffer, lineStart, lineLength, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new ParseException("Bad message encoding!", 0);
            }

            currentLine = trimEndOfLine(currentLine);

            if (currentLine.length() == 0) {
                // Last header line, process the previous buffered header.
                if (currentHeader != null && message != null) {
                    processHeader(currentHeader, message, parseExceptionListener, msgBuffer);
                }

            } else {
                if (isFirstLine) {
                    message = processFirstLine(currentLine, parseExceptionListener, msgBuffer);
                } else {
                    char firstChar = currentLine.charAt(0);
                    if (firstChar == '\t' || firstChar == ' ') {
                        if (currentHeader == null)
                            throw new ParseException("Bad header continuation.", 0);

                        // This is a continuation, append it to the previous line.
                        currentHeader += currentLine.substring(1);
                    } else {
                        if (currentHeader != null && message != null) {
                            processHeader(currentHeader, message, parseExceptionListener, msgBuffer);
                        }
                        currentHeader = currentLine;
                    }
                }
            }

            if (msgBuffer[i] == '\r' && msgBuffer.length > i + 1 && msgBuffer[i + 1] == '\n')
                i++;

            i++;

            isFirstLine = false;
        } while (currentLine.length() > 0); // End do - while

        if (message == null)
            throw new ParseException("Bad message", 0);
        message.setSize(i);

        // Check for content legth header
        if (readBody && message.getContentLength() != null) {
            if (message.getContentLength().getContentLength() != 0) {
                int bodyLength = msgBuffer.length - i;

                byte[] body = new byte[bodyLength];
                System.arraycopy(msgBuffer, i, body, 0, bodyLength);
                message.setMessageContent(body, !strict, computeContentLengthFromMessage,
                        message.getContentLength().getContentLength());
            } else if (!computeContentLengthFromMessage
                    && message.getContentLength().getContentLength() == 0 & strict) {
                String last4Chars = new String(msgBuffer, msgBuffer.length - 4, 4);
                if (!"\r\n\r\n".equals(last4Chars)) {
                    throw new ParseException("Extraneous characters at the end of the message ", i);
                }
            }

        }

        return message;
    }

    protected static String trimEndOfLine(String line) {
        if (line == null)
            return line;

        int i = line.length() - 1;
        while (i >= 0 && line.charAt(i) <= 0x20)
            i--;

        if (i == line.length() - 1)
            return line;

        if (i == -1)
            return "";

        return line.substring(0, i + 1);
    }

    protected SIPMessage processFirstLine(String firstLine, ParseExceptionListener parseExceptionListener,
            byte[] msgBuffer) throws ParseException {
        SIPMessage message;
        if (!firstLine.startsWith(SIPConstants.SIP_VERSION_STRING)) {
            message = new SIPRequest();
            try {
                RequestLine requestLine = new RequestLineParser(firstLine + "\n")
                        .parse();
                ((SIPRequest) message).setRequestLine(requestLine);
            } catch (ParseException ex) {
                if (parseExceptionListener != null)
                    try {
                        parseExceptionListener.handleException(ex, message,
                                RequestLine.class, firstLine, new String(msgBuffer, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                else
                    throw ex;

            }
        } else {
            message = new SIPResponse();
            try {
                StatusLine sl = new StatusLineParser(firstLine + "\n").parse();
                ((SIPResponse) message).setStatusLine(sl);
            } catch (ParseException ex) {
                if (parseExceptionListener != null) {
                    try {
                        parseExceptionListener.handleException(ex, message,
                                StatusLine.class, firstLine, new String(msgBuffer, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else
                    throw ex;

            }
        }
        return message;
    }

    protected void processHeader(String header, SIPMessage message, ParseExceptionListener parseExceptionListener,
            byte[] rawMessage) throws ParseException {
        if (header == null || header.length() == 0)
            return;

        HeaderParser headerParser = null;
        try {
            headerParser = ParserFactory.createParser(header + "\n");
        } catch (ParseException ex) {
            // https://java.net/jira/browse/JSIP-456
            if (parseExceptionListener != null) {
                parseExceptionListener.handleException(ex, message, null,
                        header, null);
                return;
            } else {
                throw ex;
            }
        }

        try {
            SIPHeader sipHeader = headerParser.parse();
            message.attachHeader(sipHeader, false);
        } catch (ParseException ex) {
            if (parseExceptionListener != null) {
                String headerName = Lexer.getHeaderName(header);
                Class headerClass = NameMap.getClassFromName(headerName);
                if (headerClass == null) {
                    headerClass = ExtensionHeaderImpl.class;

                }
                try {
                    parseExceptionListener.handleException(ex, message,
                            headerClass, header, new String(rawMessage, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * Parse an address (nameaddr or address spec) and return and address
     * structure.
     *
     * @param address
     *                is a String containing the address to be parsed.
     * @return a parsed address structure.
     * @since v1.0
     * @exception ParseException
     *                           when the address is badly formatted.
     */
    public AddressImpl parseAddress(String address) throws ParseException {
        AddressParser addressParser = new AddressParser(address);
        return addressParser.address(true);
    }

    /**
     * Parse a host:port and return a parsed structure.
     *
     * @param hostport
     *                 is a String containing the host:port to be parsed
     * @return a parsed address structure.
     * @since v1.0
     * @exception throws
     *                   a ParseException when the address is badly formatted.
     *
     *                   public HostPort parseHostPort(String hostport) throws
     *                   ParseException {
     *                   Lexer lexer = new Lexer("charLexer", hostport);
     *                   return new HostNameParser(lexer).hostPort();
     * 
     *                   }
     */

    /**
     * Parse a host name and return a parsed structure.
     *
     * @param host
     *             is a String containing the host name to be parsed
     * @return a parsed address structure.
     * @since v1.0
     * @exception ParseException
     *                           a ParseException when the hostname is badly
     *                           formatted.
     */
    public Host parseHost(String host) throws ParseException {
        Lexer lexer = new Lexer("charLexer", host);
        return new HostNameParser(lexer).host();

    }

    /**
     * Parse a telephone number return a parsed structure.
     *
     * @param telephone_number
     *                         is a String containing the telephone # to be parsed
     * @return a parsed address structure.
     * @since v1.0
     * @exception ParseException
     *                           a ParseException when the address is badly
     *                           formatted.
     */
    public TelephoneNumber parseTelephoneNumber(String telephone_number)
            throws ParseException {
        // Bug fix contributed by Will Scullin
        return new URLParser(telephone_number).parseTelephoneNumber(true);

    }

    /**
     * Parse a SIP url from a string and return a URI structure for it.
     *
     * @param url
     *            a String containing the URI structure to be parsed.
     * @return A parsed URI structure
     * @exception ParseException
     *                           if there was an error parsing the message.
     */

    public SipUri parseSIPUrl(String url) throws ParseException {
        try {
            return new URLParser(url).sipURL(true);
        } catch (ClassCastException ex) {
            throw new ParseException(url + " Not a SIP URL ", 0);
        }
    }

    /**
     * Parse a uri from a string and return a URI structure for it.
     *
     * @param url
     *            a String containing the URI structure to be parsed.
     * @return A parsed URI structure
     * @exception ParseException
     *                           if there was an error parsing the message.
     */

    public GenericURI parseUrl(String url) throws ParseException {
        return new URLParser(url).parse();
    }

    /**
     * Parse an individual SIP message header from a string.
     *
     * @param header
     *               String containing the SIP header.
     * @return a SIPHeader structure.
     * @exception ParseException
     *                           if there was an error parsing the message.
     */
    public static SIPHeader parseSIPHeader(String header) throws ParseException {
        int start = 0;
        int end = header.length() - 1;
        try {
            // Squeeze out any leading control character.
            while (header.charAt(start) <= 0x20)
                start++;

            // Squeeze out any trailing control character.
            while (header.charAt(end) <= 0x20)
                end--;
        } catch (ArrayIndexOutOfBoundsException e) {
            // Array contains only control char.
            throw new ParseException("Empty header.", 0);
        }

        StringBuilder buffer = new StringBuilder(end + 1);
        int i = start;
        int lineStart = start;
        boolean endOfLine = false;
        while (i <= end) {
            char c = header.charAt(i);
            if (c == '\r' || c == '\n') {
                if (!endOfLine) {
                    buffer.append(header.substring(lineStart, i));
                    endOfLine = true;
                }
            } else {
                if (endOfLine) {
                    endOfLine = false;
                    if (c == ' ' || c == '\t') {
                        buffer.append(' ');
                        lineStart = i + 1;
                    } else {
                        lineStart = i;
                    }
                }
            }

            i++;
        }
        buffer.append(header.substring(lineStart, i));
        buffer.append('\n');

        HeaderParser hp = ParserFactory.createParser(buffer.toString());
        if (hp == null)
            throw new ParseException("could not create parser", 0);
        return hp.parse();
    }

    /**
     * Parse the SIP Request Line
     *
     * @param requestLine
     *                    a String containing the request line to be parsed.
     * @return a RequestLine structure that has the parsed RequestLine
     * @exception ParseException
     *                           if there was an error parsing the requestLine.
     */

    public RequestLine parseSIPRequestLine(String requestLine)
            throws ParseException {
        requestLine += "\n";
        return new RequestLineParser(requestLine).parse();
    }

    /**
     * Parse the SIP Response message status line
     *
     * @param statusLine
     *                   a String containing the Status line to be parsed.
     * @return StatusLine class corresponding to message
     * @exception ParseException
     *                           if there was an error parsing
     * @see StatusLine
     */

    public StatusLine parseSIPStatusLine(String statusLine)
            throws ParseException {
        statusLine += "\n";
        return new StatusLineParser(statusLine).parse();
    }

    public static void setComputeContentLengthFromMessage(
            boolean computeContentLengthFromMessage) {
        StringMsgParser.computeContentLengthFromMessage = computeContentLengthFromMessage;
    }

    /**
     * Test code.
     */
    public static void main(String[] args) throws ParseException {
        String messages[] = {
                "SIP/2.0 200 OK\r\n"
                        + "To: \"The Little Blister\" <sip:LittleGuy@there.com>;tag=469bc066\r\n"
                        + "From: \"The Master Blaster\" <sip:BigGuy@here.com>;tag=11\r\n"
                        + "Via: SIP/2.0/UDP 139.10.134.246:5060;branch=z9hG4bK8b0a86f6_1030c7d18e0_17;received=139.10.134.246\r\n"
                        + "Call-ID: 1030c7d18ae_a97b0b_b@8b0a86f6\r\n"
                        + "CSeq: 1 SUBSCRIBE\r\n"
                        + "Contact: <sip:172.16.11.162:5070>\r\n"
                        + "Content-Length: 0\r\n\r\n",

                "SIP/2.0 180 Ringing\r\n"
                        + "Via: SIP/2.0/UDP 172.18.1.29:5060;branch=z9hG4bK43fc10fb4446d55fc5c8f969607991f4\r\n"
                        + "To: \"0440\" <sip:0440@212.209.220.131>;tag=2600\r\n"
                        + "From: \"Andreas\" <sip:andreas@e-horizon.se>;tag=8524\r\n"
                        + "Call-ID: f51a1851c5f570606140f14c8eb64fd3@172.18.1.29\r\n"
                        + "CSeq: 1 INVITE\r\n" + "Max-Forwards: 70\r\n"
                        + "Record-Route: <sip:212.209.220.131:5060>\r\n"
                        + "Content-Length: 0\r\n\r\n",
                "REGISTER sip:nist.gov SIP/2.0\r\n"
                        + "Via: SIP/2.0/UDP 129.6.55.182:14826\r\n"
                        + "Max-Forwards: 70\r\n"
                        + "From: <sip:mranga@nist.gov>;tag=6fcd5c7ace8b4a45acf0f0cd539b168b;epid=0d4c418ddf\r\n"
                        + "To: <sip:mranga@nist.gov>\r\n"
                        + "Call-ID: c5679907eb954a8da9f9dceb282d7230@129.6.55.182\r\n"
                        + "CSeq: 1 REGISTER\r\n"
                        + "Contact: <sip:129.6.55.182:14826>;methods=\"INVITE, MESSAGE, INFO, SUBSCRIBE, OPTIONS, BYE, CANCEL, NOTIFY, ACK, REFER\"\r\n"
                        + "User-Agent: RTC/(Microsoft RTC)\r\n"
                        + "Event:  registration\r\n"
                        + "Allow-Events: presence\r\n"
                        + "Content-Length: 0\r\n\r\n"
                        + "INVITE sip:littleguy@there.com:5060 SIP/2.0\r\n"
                        + "Via: SIP/2.0/UDP 65.243.118.100:5050\r\n"
                        + "From: M. Ranganathan  <sip:M.Ranganathan@sipbakeoff.com>;tag=1234\r\n"
                        + "To: \"littleguy@there.com\" <sip:littleguy@there.com:5060> \r\n"
                        + "Call-ID: Q2AboBsaGn9!?x6@sipbakeoff.com \r\n"
                        + "CSeq: 1 INVITE \r\n"
                        + "Content-Length: 247\r\n\r\n"
                        + "v=0\r\n"
                        + "o=4855 13760799956958020 13760799956958020 IN IP4  129.6.55.78\r\n"
                        + "s=mysession session\r\n" + "p=+46 8 52018010\r\n"
                        + "c=IN IP4  129.6.55.78\r\n" + "t=0 0\r\n"
                        + "m=audio 6022 RTP/AVP 0 4 18\r\n"
                        + "a=rtpmap:0 PCMU/8000\r\n"
                        + "a=rtpmap:4 G723/8000\r\n"
                        + "a=rtpmap:18 G729A/8000\r\n" + "a=ptime:20\r\n" };

        class ParserThread implements Runnable {
            String[] messages;

            public ParserThread(String[] messagesToParse) {
                this.messages = messagesToParse;
            }

            public void run() {
                for (int i = 0; i < messages.length; i++) {
                    StringMsgParser smp = new StringMsgParser();
                    try {
                        SIPMessage sipMessage = smp
                                .parseSIPMessage(messages[i].getBytes(), true, false, null);
                        System.out.println(" i = " + i + " branchId = "
                                + sipMessage.getTopmostVia().getBranch());
                        // System.out.println("encoded " +
                        // sipMessage.toString());
                    } catch (ParseException ex) {

                    }

                    // System.out.println("dialog id = " +
                    // sipMessage.getDialogId(false));
                }
            }
        }

        for (int i = 0; i < 20; i++) {
            new Thread(new ParserThread(messages)).start();
        }

    }

}
