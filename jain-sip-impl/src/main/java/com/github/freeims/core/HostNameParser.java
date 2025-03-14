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
/*
 *
 * IPv6 Support added by Emil Ivov (emil_ivov@yahoo.com)<br/>
 * Network Research Team (http://www-r2.u-strasbg.fr))<br/>
 * Louis Pasteur University - Strasbourg - France<br/>
 *
 *Bug fixes for corner cases were contributed by Thomas Froment.
 */
package com.github.freeims.core;

import java.text.ParseException;

/**
 * Parser for host names.
 *
 * @version 1.2
 *
 * @author M. Ranganathan
 */

public class HostNameParser extends ParserCore {

    /**
     * Determines whether or not we should tolerate and strip address scope
     * zones from IPv6 addresses. Address scope zones are sometimes returned
     * at the end of IPv6 addresses generated by InetAddress.getHostAddress().
     * They are however not part of the SIP semantics so basically this method
     * determines whether or not the parser should be stripping them (as
     * opposed simply being blunt and throwing an exception).
     */
    private static boolean stripAddressScopeZones = false;

    static {
        stripAddressScopeZones = Boolean.getBoolean("org.freeims.core.STRIP_ADDR_SCOPES");
    }

    public HostNameParser(String hname) {
        this.lexer = new LexerCore("charLexer", hname);
    }

    /**
     * The lexer is initialized with the buffer.
     */
    public HostNameParser(LexerCore lexer) {
        this.lexer = lexer;
        lexer.selectLexer("charLexer");
    }

    private static final char[] VALID_DOMAIN_LABEL_CHAR =
            // Add underscore as allowed character https://java.net/jira/browse/JSIP-506
            new char[] { LexerCore.ALPHADIGIT_VALID_CHARS, '-', '.', '_' };

    protected void consumeDomainLabel() throws ParseException {
        if (debug)
            dbg_enter("domainLabel");
        try {
            lexer.consumeValidChars(VALID_DOMAIN_LABEL_CHAR);
        } finally {
            if (debug)
                dbg_leave("domainLabel");
        }
    }

    protected String ipv6Reference() throws ParseException {
        StringBuilder retval = new StringBuilder();
        if (debug)
            dbg_enter("ipv6Reference");

        try {

            if (stripAddressScopeZones) {
                while (lexer.hasMoreChars()) {
                    char la = lexer.lookAhead(0);
                    // '%' is ipv6 address scope zone. see detail at
                    // java.sun.com/j2se/1.5.0/docs/api/java/net/Inet6Address.html
                    if (LexerCore.isHexDigit(la) || la == '.' || la == ':'
                            || la == '[') {
                        lexer.consume(1);
                        retval.append(la);
                    } else if (la == ']') {
                        lexer.consume(1);
                        retval.append(la);
                        return retval.toString();
                    } else if (la == '%') {
                        // we need to strip the address scope zone.
                        lexer.consume(1);

                        String rest = lexer.getRest();

                        if (rest == null || rest.length() == 0) {
                            // head for the parse exception
                            break;
                        }

                        // we strip everything until either the end of the string
                        // or a closing square bracket (])
                        int stripLen = rest.indexOf(']');

                        if (stripLen == -1) {
                            // no square bracket -> not a valid ipv6 reference
                            break;
                        }

                        lexer.consume(stripLen + 1);
                        retval.append("]");
                        return retval.toString();

                    } else
                        break;
                }
            } else {
                while (lexer.hasMoreChars()) {
                    char la = lexer.lookAhead(0);
                    if (LexerCore.isHexDigit(la) || la == '.'
                            || la == ':' || la == '[') {
                        lexer.consume(1);
                        retval.append(la);
                    } else if (la == ']') {
                        lexer.consume(1);
                        retval.append(la);
                        return retval.toString();
                    } else
                        break;
                }
            }

            throw new ParseException(
                    lexer.getBuffer() + ": Illegal Host name ",
                    lexer.getPtr());
        } finally {
            if (debug)
                dbg_leave("ipv6Reference");
        }
    }

    public Host host() throws ParseException {
        if (debug)
            dbg_enter("host");
        try {
            String hostname;

            // IPv6 referene
            if (lexer.lookAhead(0) == '[') {
                hostname = ipv6Reference();
            }
            // IPv6 address (i.e. missing square brackets)
            else if (isIPv6Address(lexer.getRest())) {
                int startPtr = lexer.getPtr();
                lexer.consumeValidChars(
                        new char[] { LexerCore.ALPHADIGIT_VALID_CHARS, ':' });
                hostname = new StringBuilder("[").append(
                        lexer.getBuffer().substring(startPtr, lexer.getPtr()))
                        .append("]").toString();
            }
            // IPv4 address or hostname
            else {
                int startPtr = lexer.getPtr();
                consumeDomainLabel();
                hostname = lexer.getBuffer().substring(startPtr, lexer.getPtr());
            }

            if (hostname.length() == 0)
                throw new ParseException(
                        lexer.getBuffer() + ": Missing host name",
                        lexer.getPtr());
            else
                return new Host(hostname);
        } finally {
            if (debug)
                dbg_leave("host");
        }
    }

    /**
     * Tries to determine whether the address in <tt>uriHeader</tt> could be
     * an IPv6 address by counting the number of colons that appear in it.
     *
     * @param uriHeader the string (supposedly the value of a URI header) that
     *                  we have received for parsing.
     *
     * @return true if the host part of <tt>uriHeader</tt> could be an IPv6
     *         address (i.e. contains at least two colons) and false otherwise.
     */
    private boolean isIPv6Address(String uriHeader) {
        // Issue 275 https://jain-sip.dev.java.net/issues/show_bug.cgi?id=275
        // we check if the uriHeader includes a comma, if that's the case we are
        // potentially parsing
        // a multi header so strip everything after it
        String hostName = uriHeader;
        int indexOfComma = uriHeader.indexOf(",");
        if (indexOfComma != -1) {
            hostName = uriHeader.substring(0, indexOfComma);
        }
        // approximately detect the end the host part.
        // first check if we have an uri param
        int hostEnd = hostName.indexOf(Lexer.QUESTION);

        // if not or if it appears after a semi-colon then the end of the
        // address would be a header param.
        int semiColonIndex = hostName.indexOf(Lexer.SEMICOLON);
        if (hostEnd == -1
                || (semiColonIndex != -1 && hostEnd > semiColonIndex))
            hostEnd = semiColonIndex;

        // if there was no header param either the address
        // continues until the end of the string
        if (hostEnd == -1)
            hostEnd = hostName.length();

        // extract the address
        String host = hostName.substring(0, hostEnd);

        int firstColonIndex = host.indexOf(Lexer.COLON);

        if (firstColonIndex == -1)
            return false;

        int secondColonIndex = host.indexOf(Lexer.COLON, firstColonIndex + 1);

        if (secondColonIndex == -1)
            return false;

        return true;
    }

    /**
     * Parses a host:port string
     *
     * @param allowWS - whether whitespace is allowed around ':', only true for Via
     *                headers
     * @return
     * @throws ParseException
     */
    public HostPort hostPort(boolean allowWS) throws ParseException {
        if (debug)
            dbg_enter("hostPort");
        try {
            Host host = this.host();
            HostPort hp = new HostPort();
            hp.setHost(host);
            // Has a port?
            if (allowWS)
                lexer.SPorHT(); // white space before ":port" should be accepted
            if (lexer.hasMoreChars()) {
                char la = lexer.lookAhead(0);
                switch (la) {
                    case ':':
                        lexer.consume(1);
                        if (allowWS)
                            lexer.SPorHT(); // white space before port number should be accepted
                        try {
                            String port = lexer.number();
                            hp.setPort(Integer.parseInt(port));
                        } catch (NumberFormatException nfe) {
                            throw new ParseException(
                                    lexer.getBuffer() + " :Error parsing port ",
                                    lexer.getPtr());
                        }
                        break;

                    case ',': // allowed in case of multi-headers, e.g. Route
                              // Could check that current header is a multi hdr

                    case ';': // OK, can appear in URIs (parameters)
                    case '?': // same, header parameters
                    case '>': // OK, can appear in headers
                    case ' ': // OK, allow whitespace
                    case '\t':
                    case '\r':
                    case '\n':
                    case '/': // e.g. http://[::1]/xyz.html
                        break;
                    case '%':
                        if (stripAddressScopeZones) {
                            break;// OK,allow IPv6 address scope zone
                        }

                    default:
                        if (!allowWS) {
                            throw new ParseException(lexer.getBuffer() +
                                    " Illegal character in hostname:" + lexer.lookAhead(0),
                                    lexer.getPtr());
                        }
                }
            }
            return hp;
        } finally {
            if (debug)
                dbg_leave("hostPort");
        }
    }

    public static void main(String args[]) throws ParseException {
        String hostNames[] = {
                "foo.bar.com:1234",
                "proxima.chaplin.bt.co.uk",
                "129.6.55.181:2345",
                ":1234",
                "foo.bar.com:         1234",
                "foo.bar.com     :      1234   ",
                "MIK_S:1234"
        };

        for (int i = 0; i < hostNames.length; i++) {
            try {
                HostNameParser hnp = new HostNameParser(hostNames[i]);
                HostPort hp = hnp.hostPort(true);
                System.out.println("[" + hp.encode() + "]");
            } catch (ParseException ex) {
                System.out.println("exception text = " + ex.getMessage());
            }
        }

    }
}
