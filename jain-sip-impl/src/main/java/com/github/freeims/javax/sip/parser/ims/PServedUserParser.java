package com.github.freeims.javax.sip.parser.ims;

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

import java.text.ParseException;

import com.github.freeims.javax.sip.header.SIPHeader;
import com.github.freeims.javax.sip.header.ims.PServedUser;
import com.github.freeims.javax.sip.parser.AddressParametersParser;
import com.github.freeims.javax.sip.parser.Lexer;

/**
 *
 * @author aayush.bhatnagar
 *
 */
public class PServedUserParser extends AddressParametersParser {

    protected PServedUserParser(Lexer lexer) {
        super(lexer);
    }

    public PServedUserParser(String servedUser) {
        super(servedUser);
    }

    public SIPHeader parse() throws ParseException {

        if (debug)
            dbg_enter("PServedUser.parse");

        try {
            PServedUser servedUser = new PServedUser();
            headerName(P_SERVED_USER);
            super.parse(servedUser);
            this.lexer.match('\n');
            return servedUser;
        } finally {
            if (debug)
                dbg_leave("PServedUser.parse");
        }
    }
}
