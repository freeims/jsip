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
* of the terms of this agreement.
*
*/
package com.github.freeims.javax.sip.header;

import java.text.ParseException;

import com.github.freeims.core.*;

/**
 * Authentication info SIP Header.
 *
 * @author M. Ranganathan NIST/ITL/ANTD
 * @since 1.1
 * @version 1.2 $Revision: 1.10 $ $Date: 2010-05-06 14:07:50 $
 *
 *
 */
public final class AuthenticationInfo
        extends ParametersHeader
        implements javax.sip.header.AuthenticationInfoHeader {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -4371927900917127057L;

    /**
     * Authentication scheme, some implemenentations require it to be set (NTLM)
     */
    private String scheme;

    /**
     * Default contstructor.
     */
    public AuthenticationInfo() {
        super(NAME);
        parameters.setSeparator(COMMA); // Odd ball.
    }

    public void add(NameValue nv) {
        parameters.set(nv);
    }

    /**
     * Value of header encoded in canonical form.
     */

    protected StringBuilder encodeBody(StringBuilder buffer) {
        if (scheme != null)
            buffer.append(scheme).append(' ');
        return parameters.encode(buffer);

    }

    /**
     * Get the name value pair for a given authentication info parameter.
     *
     * @param name is the name for which we want to retrieve the name value
     *             list.
     */

    public NameValue getAuthInfo(String name) {
        return parameters.getNameValue(name);
    }

    /**
     * Returns the AuthenticationInfo value of this AuthenticationInfoHeader.
     *
     *
     *
     * @return the String representing the AuthenticationInfo
     *
     *
     *
     */
    public String getAuthenticationInfo() {
        return this.encodeBody(new StringBuilder()).toString();
    }

    /**
     * Returns the CNonce value of this AuthenticationInfoHeader.
     *
     * @return the String representing the cNonce information, null if value is
     *         not set.
     * @since v1.1
     */
    public String getCNonce() {
        return this.getParameter(ParameterNames.CNONCE);
    }

    /**
     * Returns the nextNonce value of this AuthenticationInfoHeader.
     *
     * @return the String representing the nextNonce
     *         information, null if value is not set.
     * @since v1.1
     */
    public String getNextNonce() {
        return this.getParameter(ParameterNames.NEXT_NONCE);
    }

    /**
     * Returns the Nonce Count value of this AuthenticationInfoHeader.
     *
     * @return the integer representing the nonceCount information, -1 if value is
     *         not set.
     * @since v1.1
     */
    public int getNonceCount() {
        return this.getParameterAsInt(ParameterNames.NONCE_COUNT);
    }

    /**
     * Returns the messageQop value of this AuthenticationInfoHeader.
     *
     * @return the string representing the messageQop information, null if the
     *         value is not set.
     * @since v1.1
     */
    public String getQop() {
        return this.getParameter(ParameterNames.QOP);
    }

    /**
     * Returns the Response value of this AuthenticationInfoHeader.
     *
     * @return the String representing the Response information.
     * @since v1.1
     */
    public String getResponse() {
        return this.getParameter(ParameterNames.RESPONSE_AUTH);
    }

    /**
     * Returns the "snum" value of this AuthenticationInfoHeader.
     * 
     * @return the String representing the snum information.
     */
    public String getSNum() {
        return this.getParameter(ParameterNames.SNUM);
    }

    /**
     * Returns the "srand" value of this AuthenticationInfoHeader.
     * 
     * @return the String representing the srand information.
     */
    public String getSRand() {
        return this.getParameter(ParameterNames.SRAND);
    }

    /**
     * Returns the "targetname" value of this AuthenticationInfoHeader.
     * 
     * @return the String representing the targetname information.
     */
    public String getTargetName() {
        return this.getParameter(ParameterNames.TARGET_NAME);
    }

    /**
     * Returns the authentication scheme of this AuthenticationInfoHeader.
     * 
     * @return the String representing the scheme information or null if scheme
     *         wasn't set explicitly.
     */
    public String getScheme() {
        return scheme;
    }

    /**
     * Sets the CNonce of the AuthenticationInfoHeader to the <var>cNonce</var>
     * parameter value.
     *
     * @param cNonce - the new cNonce String of this AuthenticationInfoHeader.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the cNonce value.
     * @since v1.1
     */
    public void setCNonce(String cNonce) throws ParseException {
        this.setParameter(ParameterNames.CNONCE, cNonce);
    }

    /**
     * Sets the NextNonce of the AuthenticationInfoHeader to the
     * <var>nextNonce</var>
     * parameter value.
     *
     * @param nextNonce - the new nextNonce String of this AuthenticationInfoHeader.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the nextNonce value.
     * @since v1.1
     */
    public void setNextNonce(String nextNonce) throws ParseException {
        this.setParameter(ParameterNames.NEXT_NONCE, nextNonce);
    }

    /**
     * Sets the Nonce Count of the AuthenticationInfoHeader to the
     * <var>nonceCount</var>
     * parameter value.
     *
     * @param nonceCount - the new nonceCount integer of this
     *                   AuthenticationInfoHeader.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the nonceCount value.
     * @since v1.1
     */
    public void setNonceCount(int nonceCount) throws ParseException {
        if (nonceCount < 0)
            throw new ParseException("bad value", 0);
        String nc = Integer.toHexString(nonceCount);

        String base = "00000000";
        nc = base.substring(0, 8 - nc.length()) + nc;
        this.setParameter(ParameterNames.NC, nc);
    }

    /**
     * Sets the Qop value of the AuthenticationInfoHeader to the new
     * <var>qop</var> parameter value.
     *
     * @param qop - the new Qop string of this AuthenticationInfoHeader.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the Qop value.
     * @since v1.1
     */
    public void setQop(String qop) throws ParseException {
        this.setParameter(ParameterNames.QOP, qop);
    }

    /**
     * Sets the Response of the
     * AuthenticationInfoHeader to the new <var>response</var>
     * parameter value.
     *
     * @param response - the new response String of this
     *                 AuthenticationInfoHeader.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the Response.
     * @since v1.1
     */
    public void setResponse(String response) throws ParseException {
        this.setParameter(ParameterNames.RESPONSE_AUTH, response);
    }

    /**
     * Sets the SNum of the AuthenticationInfoHeader to the <var>sNum</var>
     * parameter value.
     *
     * @param sNum - the new sNum String of this AuthenticationInfoHeader.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the sNum value.
     */
    public void setSNum(String sNum) throws ParseException {
        this.setParameter(ParameterNames.SNUM, sNum);
    }

    /**
     * Sets the SRand of the AuthenticationInfoHeader to the <var>sRand</var>
     * parameter value.
     *
     * @param sRand - the new sRand String of this AuthenticationInfoHeader.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the sRand value.
     */
    public void setSRand(String sRand) throws ParseException {
        this.setParameter(ParameterNames.SRAND, sRand);
    }

    /**
     * Sets the TargetName of the AuthenticationInfoHeader to the
     * <var>targetName</var>
     * parameter value.
     *
     * @param targetName - the new targetName String of this
     *                   AuthenticationInfoHeader.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the targetName value.
     */
    public void setTargetName(String targetName) throws ParseException {
        this.setParameter(ParameterNames.TARGET_NAME, targetName);
    }

    /**
     * Sets authentication scheme name of the AuthenticationInfoHeader to the
     * <var>scheme</var>
     * parameter value.
     *
     * @param scheme - the new scheme String of this AuthenticationInfoHeader.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the targetName value.
     */
    public void setScheme(String scheme) throws ParseException {
        this.scheme = scheme;
    }

    public void setParameter(String name, String value) throws ParseException {
        if (name == null)
            throw new NullPointerException("null name");
        NameValue nv = super.parameters.getNameValue(name.toLowerCase());
        if (nv == null) {
            nv = new NameValue(name, value);
            if (name.equalsIgnoreCase(ParameterNames.QOP)
                    || name.equalsIgnoreCase(ParameterNames.NEXT_NONCE)
                    || name.equalsIgnoreCase(ParameterNames.REALM)
                    || name.equalsIgnoreCase(ParameterNames.CNONCE)
                    || name.equalsIgnoreCase(ParameterNames.NONCE)
                    || name.equalsIgnoreCase(ParameterNames.OPAQUE)
                    || name.equalsIgnoreCase(ParameterNames.USERNAME)
                    || name.equalsIgnoreCase(ParameterNames.DOMAIN)
                    || name.equalsIgnoreCase(ParameterNames.RESPONSE_AUTH)
                    || name.equalsIgnoreCase(ParameterNames.SRAND)
                    || name.equalsIgnoreCase(ParameterNames.SNUM)
                    || name.equalsIgnoreCase(ParameterNames.TARGET_NAME)) {
                if (value == null)
                    throw new NullPointerException("null value");
                if (value.startsWith(Separators.DOUBLE_QUOTE))
                    throw new ParseException(
                            value + " : Unexpected DOUBLE_QUOTE",
                            0);
                nv.setQuotedValue();
            }
            super.setParameter(nv);
        } else
            nv.setValueAsObject(value);
    }
}
