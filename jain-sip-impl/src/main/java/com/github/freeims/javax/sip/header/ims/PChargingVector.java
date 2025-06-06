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
 * PRODUCT OF PT INOVACAO - EST DEPARTMENT *
 *******************************************/

package com.github.freeims.javax.sip.header.ims;

import java.text.ParseException;

import javax.sip.SipException;
import javax.sip.header.ExtensionHeader;

import com.github.freeims.javax.sip.header.ims.PChargingVectorHeader;
import com.github.freeims.javax.sip.header.ims.ParameterNamesIms;

/**
 * P-Charging-Vector header SIP Private Header: RFC 3455.
 *
 * @author ALEXANDRE MIGUEL SILVA SANTOS
 */

public class PChargingVector extends com.github.freeims.javax.sip.header.ParametersHeader
        implements PChargingVectorHeader, SIPHeaderNamesIms, ExtensionHeader {

    /**
     * Default Constructor
     */
    public PChargingVector() {

        super(P_CHARGING_VECTOR);
    }

    /*
     * (non-Javadoc)
     *
     * @see gov.nist.javax.sip.header.ParametersHeader#encodeBody()
     */
    public StringBuilder encodeBody(StringBuilder encoding) {

        // StringBuilder encoding = new StringBuilder();
        /*
         * no need to check for the presence of icid-value. According to the
         * spec above this is a mandatory field. if it does not exist, then we
         * should throw an exception
         *
         * JvB 26/5: fix for issue #159, check for quotes around icid value
         * 
         * Aayush: 29th November 2011 : Added fix for P-Charging-Vector header's
         * parameters to accept quoted string values.
         */
        com.github.freeims.core.NameValue nv = getNameValue(ParameterNamesIms.ICID_VALUE);
        if (nv != null)
            this.parameters.encode(encoding);
        else
            try {
                throw new SipException("icid-value is mandatory");
            } catch (SipException e) {
                e.printStackTrace();

            }

        return encoding;
    }

    /**
     * <p>
     * Get the icid-value parameter value
     * </p>
     *
     * @return the value of the icid-value parameter
     */
    public String getICID() {

        return getParameter(ParameterNamesIms.ICID_VALUE);
    }

    /**
     * <p>
     * Set the icid-value parameter
     * </p>
     *
     * @param icid -
     *             value to set in the icid-value parameter
     * @throws ParseException
     */
    public void setICID(String icid) throws ParseException {

        if (icid == null)
            throw new NullPointerException(
                    "JAIN-SIP Exception, "
                            + "P-Charging-Vector, setICID(), the icid parameter is null.");

        setParameter(ParameterNamesIms.ICID_VALUE, icid);

    }

    /**
     * <p>
     * Get the icid-generated-at parameter value
     * </p>
     *
     * @return the icid-generated-at parameter value
     */
    public String getICIDGeneratedAt() {

        return getParameter(ParameterNamesIms.ICID_GENERATED_AT);

    }

    /**
     * <p>
     * Set the icid-generated-at parameter
     * </p>
     *
     * @param host -
     *             value to set in the icid-generated-at parameter
     * @throws ParseException
     */
    public void setICIDGeneratedAt(String host) throws ParseException {

        if (host == null)
            throw new NullPointerException(
                    "JAIN-SIP Exception, "
                            + "P-Charging-Vector, setICIDGeneratedAt(), the host parameter is null.");

        setParameter(ParameterNamesIms.ICID_GENERATED_AT, host);

    }

    /**
     * <p>
     * Get the orig-ioi parameter value
     * </p>
     *
     * @return the orig-ioi parameter value
     */
    public String getOriginatingIOI() {

        return getParameter(ParameterNamesIms.ORIG_IOI);
    }

    /**
     * <p>
     * Set the orig-ioi parameter
     * </p>
     *
     * @param origIOI -
     *                value to set in the orig-ioi parameter. If value is null or
     *                empty, the parameter is removed
     * @throws ParseException
     */
    public void setOriginatingIOI(String origIOI) throws ParseException {

        if (origIOI == null || origIOI.length() == 0) {
            removeParameter(ParameterNamesIms.ORIG_IOI);
        } else
            this.parameters.set(ParameterNamesIms.ORIG_IOI, origIOI);

    }

    /**
     * <p>
     * Get the term-ioi parameter value
     * </p>
     *
     * @return the term-ioi parameter value
     */
    public String getTerminatingIOI() {

        return getParameter(ParameterNamesIms.TERM_IOI);
    }

    /**
     * <p>
     * Set the term-ioi parameter
     * </p>
     *
     * @param termIOI -
     *                value to set in the term-ioi parameter. If value is null or
     *                empty, the parameter is removed
     * @throws ParseException
     */
    public void setTerminatingIOI(String termIOI) throws ParseException {

        if (termIOI == null || termIOI.length() == 0) {
            removeParameter(ParameterNamesIms.TERM_IOI);
        } else
            this.parameters.set(ParameterNamesIms.TERM_IOI, termIOI);

    }

    public void setValue(String value) throws ParseException {
        throw new ParseException(value, 0);

    }

}
