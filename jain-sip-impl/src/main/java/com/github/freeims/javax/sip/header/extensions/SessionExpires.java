
/*******************************************************************************
* Product of NIST/ITL Advanced Networking Technologies Division (ANTD).        *
*******************************************************************************/

package com.github.freeims.javax.sip.header.extensions;

import java.text.ParseException;

import javax.sip.InvalidArgumentException;
import javax.sip.header.ExtensionHeader;

import com.github.freeims.javax.sip.header.ParametersHeader;

/**
 * ReferredBy SIP Header.
 *
 * @version JAIN-SIP-1.1 $Revision: 1.6 $ $Date: 2010-05-06 14:07:56 $
 *
 * @author Peter Musgrave.
 *
 */
public final class SessionExpires
        extends ParametersHeader implements ExtensionHeader, SessionExpiresHeader {

    // TODO: Need a unique UID
    private static final long serialVersionUID = 8765762413224043300L;

    // TODO: When the MinSEHeader is added to javax - move this there...pmusgrave
    public static final String NAME = "Session-Expires";

    public int expires;

    public static final String REFRESHER = "refresher";

    /**
     * default Constructor.
     */
    public SessionExpires() {
        super(NAME);
    }

    /**
     * Gets the expires value of the SessionExpiresHeader. This expires value is
     * relative time.
     *
     *
     *
     * @return the expires value of the ExpiresHeader.
     *
     * @since JAIN SIP v1.1
     *
     */
    public int getExpires() {
        return expires;
    }

    /**
     * Sets the relative expires value of the SessionExpiresHeader.
     * The expires value MUST be greater than zero and MUST be
     * less than 2**31.
     *
     * @param expires - the new expires value
     *
     * @throws InvalidArgumentException if supplied value is less than zero.
     *
     * @since JAIN SIP v1.1
     *
     */
    public void setExpires(int expires) throws InvalidArgumentException {
        if (expires < 0)
            throw new InvalidArgumentException("bad argument " + expires);
        this.expires = expires;
    }

    public void setValue(String value) throws ParseException {
        // not implemented.
        throw new ParseException(value, 0);

    }

    /**
     * Encode the header content into a String.
     * 
     * @return String
     */
    protected StringBuilder encodeBody(StringBuilder retval) {

        retval.append(Integer.toString(expires));

        if (!parameters.isEmpty()) {
            retval.append(SEMICOLON);
            parameters.encode(retval);
        }
        return retval;
    }

    public String getRefresher() {
        return parameters.getParameter(REFRESHER);
    }

    public void setRefresher(String refresher) {
        this.parameters.set(REFRESHER, refresher);
    }
}
