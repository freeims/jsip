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
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
*******************************************************************************/
package com.github.freeims.javax.sip.header;

import javax.sip.*;

/**
 * Expires SIP Header.
 *
 * @version 1.2 $Revision: 1.7 $ $Date: 2010-05-06 14:07:46 $
 * @since 1.1
 *
 * @author M. Ranganathan <br/>
 *
 *
 */
public class Expires
        extends SIPHeader
        implements javax.sip.header.ExpiresHeader {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3134344915465784267L;

    /**
     * expires field
     */
    protected int expires;

    /**
     * default constructor
     */
    public Expires() {
        super(NAME);
    }

    /**
     * Return canonical form.
     * 
     * @return String
     */
    public String encodeBody() {
        return encodeBody(new StringBuilder()).toString();
    }

    protected StringBuilder encodeBody(StringBuilder buffer) {
        return buffer.append(expires);
    }

    /**
     * Gets the expires value of the ExpiresHeader. This expires value is
     *
     * relative time.
     *
     *
     *
     * @return the expires value of the ExpiresHeader.
     *
     *
     */
    public int getExpires() {
        return expires;
    }

    /**
     * Sets the relative expires value of the ExpiresHeader.
     * The expires value MUST be greater than zero and MUST be
     * less than 2**31.
     *
     * @param expires - the new expires value of this ExpiresHeader
     *
     * @throws InvalidArgumentException if supplied value is less than zero.
     *
     *
     */
    public void setExpires(int expires) throws InvalidArgumentException {
        if (expires < 0)
            throw new InvalidArgumentException("bad argument " + expires);
        this.expires = expires;
    }
}
