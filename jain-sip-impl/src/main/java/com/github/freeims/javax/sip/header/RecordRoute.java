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

import com.github.freeims.javax.sip.address.*;

/**
 * The Request-Route header is added to a request by any proxy that insists on
 * being in the path of subsequent requests for the same call leg.
 *
 * @version 1.2 $Revision: 1.7 $ $Date: 2010-05-06 14:07:48 $
 *
 * @author M. Ranganathan <br/>
 *
 *
 *
 */
public class RecordRoute
        extends AddressParametersHeader
        implements javax.sip.header.RecordRouteHeader {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 2388023364181727205L;

    /**
     * constructor
     * 
     * @param address address to set
     */
    public RecordRoute(AddressImpl address) {
        super(NAME);
        this.address = address;
    }

    /**
     * default constructor
     */
    public RecordRoute() {
        super(RECORD_ROUTE);

    }

    /**
     * Encode into canonical form.
     * 
     * @return String containing the canonicaly encoded header.
     */
    public String encodeBody() {
        return encodeBody(new StringBuilder()).toString();
    }

    protected StringBuilder encodeBody(StringBuilder buffer) {
        if (address.getAddressType() == AddressImpl.ADDRESS_SPEC) {
            buffer.append(LESS_THAN);
        }
        address.encode(buffer);
        if (address.getAddressType() == AddressImpl.ADDRESS_SPEC) {
            buffer.append(GREATER_THAN);
        }

        if (!parameters.isEmpty()) {
            buffer.append(SEMICOLON);
            this.parameters.encode(buffer);
        }
        return buffer;
    }
}
