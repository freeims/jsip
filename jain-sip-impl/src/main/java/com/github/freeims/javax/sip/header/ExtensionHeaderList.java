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

import java.util.ListIterator;

import javax.sip.header.ExtensionHeader;
import javax.sip.header.Header;

/**
 * A generic extension header list.
 * 
 * @version 1.2 $Revision: 1.9 $ $Date: 2010-05-06 14:07:49 $
 * @since 1.1
 */
public class ExtensionHeaderList extends SIPHeaderList<ExtensionHeaderImpl> {

    private static final long serialVersionUID = 4681326807149890197L;

    public Object clone() {
        ExtensionHeaderList retval = new ExtensionHeaderList(headerName);
        retval.clonehlist(this.hlist);
        return retval;
    }

    public ExtensionHeaderList(String hName) {
        super(ExtensionHeaderImpl.class, hName);
    }

    public ExtensionHeaderList() {
        super(ExtensionHeaderImpl.class, null);
    }

    public String encode() {
        StringBuilder retval = new StringBuilder();
        ListIterator<ExtensionHeaderImpl> it = this.listIterator();
        while (it.hasNext()) {
            ExtensionHeaderImpl eh = (ExtensionHeaderImpl) it.next();
            retval.append(eh.encode());
        }
        return retval.toString();
    }
}
