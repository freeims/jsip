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
/*******************************************
 * PRODUCT OF PT INOVAO - EST DEPARTMENT *
 *******************************************/
package com.github.freeims.javax.sip.header.ims;

import com.github.freeims.javax.sip.header.SIPHeaderList;

/**
 * @author ALEXANDRE MIGUEL SILVA SANTOS - Nú 10045401
 */

public class ServiceRouteList extends SIPHeaderList<ServiceRoute> {

    private static final long serialVersionUID = -4264811439080938519L;

    /**
     * Default constructor
     */
    public ServiceRouteList() {
        super(ServiceRoute.class, ServiceRouteHeader.NAME);
    }

    public Object clone() {
        ServiceRouteList retval = new ServiceRouteList();
        return retval.clonehlist(this.hlist);
    }

}
