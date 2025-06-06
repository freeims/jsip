/*
* Conditions Of Use
*
* This software was developed by employees of the National Institute of
* Standards and Technology (NIST), an agency of the Federal Government,
* and others.
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

/************************************************************************************************
 * PRODUCT OF PT INOVACAO - EST DEPARTMENT and Telecommunications Institute (Aveiro, Portugal)  *
 ************************************************************************************************/

package com.github.freeims.javax.sip.header.ims;

import java.text.ParseException;

import javax.sip.header.ExtensionHeader;

/**
 * Security-Verify header
 * - sec-agree: RFC 3329 + 3GPP TS33.203 (Annex H).
 *
 * <p>
 * </p>
 *
 * @author Miguel Freitas (IT) PT-Inovacao
 */

public class SecurityVerify
        extends SecurityAgree
        implements SecurityVerifyHeader, ExtensionHeader {

    // TODO serialVersionUID

    public SecurityVerify() {
        super(SecurityVerifyHeader.NAME);

    }

    public void setValue(String value) throws ParseException {
        throw new ParseException(value, 0);
    }

}
