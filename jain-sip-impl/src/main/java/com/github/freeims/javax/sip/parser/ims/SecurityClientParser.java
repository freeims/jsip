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
/************************************************************************************************
 * PRODUCT OF PT INOVACAO - EST DEPARTMENT and Telecommunications Institute (Aveiro, Portugal)  *
 ************************************************************************************************/

package com.github.freeims.javax.sip.parser.ims;

import java.text.ParseException;

import com.github.freeims.core.Token;
import com.github.freeims.javax.sip.header.SIPHeader;
import com.github.freeims.javax.sip.header.ims.SecurityClient;
import com.github.freeims.javax.sip.header.ims.SecurityClientList;
import com.github.freeims.javax.sip.parser.Lexer;
import com.github.freeims.javax.sip.parser.TokenTypes;

public class SecurityClientParser extends SecurityAgreeParser {

    public SecurityClientParser(String security) {
        super(security);
    }

    protected SecurityClientParser(Lexer lexer) {
        super(lexer);
    }

    public SIPHeader parse() throws ParseException {
        dbg_enter("SecuriryClient parse");
        try {

            headerName(TokenTypes.SECURITY_CLIENT);
            SecurityClient secClient = new SecurityClient();
            SecurityClientList secClientList = (SecurityClientList) super.parse(secClient);
            return secClientList;

        } finally {
            dbg_leave("SecuriryClient parse");
        }
    }

}
