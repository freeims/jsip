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

import java.text.ParseException;
import javax.sip.header.*;

/**
 * ProxyRequire Header.
 *
 * @version 1.2 $Revision: 1.6 $ $Date: 2010-05-06 14:07:47 $
 *
 * @author M. Ranganathan <br/>
 * @author Olivier Deruelle <br/>
 *
 *
 *
 */
public class ProxyRequire extends SIPHeader implements ProxyRequireHeader {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -3269274234851067893L;
    /**
     * Optiontag field
     */
    protected String optionTag;

    /**
     * Default Constructor
     */
    public ProxyRequire() {
        super(PROXY_REQUIRE);
    }

    /**
     * Constructor
     * 
     * @param s String to set
     */
    public ProxyRequire(String s) {
        super(PROXY_REQUIRE);
        optionTag = s;
    }

    /**
     * Encode in canonical form.
     * 
     * @return String
     */
    public StringBuilder encodeBody(StringBuilder buffer) {
        return buffer.append(optionTag);
    }

    /**
     * Sets the option tag value to the new supplied <var>optionTag</var>
     * parameter.
     *
     * @param optionTag - the new string value of the option tag.
     * @throws ParseException which signals that an error has been reached
     *                        unexpectedly while parsing the optionTag value.
     */
    public void setOptionTag(String optionTag) throws ParseException {
        if (optionTag == null)
            throw new NullPointerException(
                    "JAIN-SIP Exception, ProxyRequire, setOptionTag(), the optionTag parameter is null");
        this.optionTag = optionTag;
    }

    /**
     * Gets the option tag of this OptionTag class.
     *
     * @return the string that identifies the option tag value.
     */
    public String getOptionTag() {
        return optionTag;
    }
}
/*
 * $Log: not supported by cvs2svn $
 * Revision 1.5 2009/07/17 18:57:34 emcho
 * Converts indentation tabs to spaces so that we have a uniform indentation
 * policy in the whole project.
 *
 * Revision 1.4 2006/07/13 09:01:26 mranga
 * Issue number:
 * Obtained from:
 * Submitted by: jeroen van bemmel
 * Reviewed by: mranga
 * Moved some changes from jain-sip-1.2 to java.net
 *
 * CVS: ----------------------------------------------------------------------
 * CVS: Issue number:
 * CVS: If this change addresses one or more issues,
 * CVS: then enter the issue number(s) here.
 * CVS: Obtained from:
 * CVS: If this change has been taken from another system,
 * CVS: then name the system in this line, otherwise delete it.
 * CVS: Submitted by:
 * CVS: If this code has been contributed to the project by someone else; i.e.,
 * CVS: they sent us a patch or a set of diffs, then include their name/email
 * CVS: address here. If this is your work then delete this line.
 * CVS: Reviewed by:
 * CVS: If we are doing pre-commit code reviews and someone else has
 * CVS: reviewed your changes, include their name(s) here.
 * CVS: If you have not had it reviewed then delete this line.
 *
 * Revision 1.3 2006/06/19 06:47:26 mranga
 * javadoc fixups
 *
 * Revision 1.2 2006/06/16 15:26:28 mranga
 * Added NIST disclaimer to all public domain files. Clean up some javadoc.
 * Fixed a leak
 *
 * Revision 1.1.1.1 2005/10/04 17:12:35 mranga
 *
 * Import
 *
 *
 * Revision 1.2 2004/01/22 13:26:29 sverker
 * Issue number:
 * Obtained from:
 * Submitted by: sverker
 * Reviewed by: mranga
 *
 * Major reformat of code to conform with style guide. Resolved compiler and
 * javadoc warnings. Added CVS tags.
 *
 * CVS: ----------------------------------------------------------------------
 * CVS: Issue number:
 * CVS: If this change addresses one or more issues,
 * CVS: then enter the issue number(s) here.
 * CVS: Obtained from:
 * CVS: If this change has been taken from another system,
 * CVS: then name the system in this line, otherwise delete it.
 * CVS: Submitted by:
 * CVS: If this code has been contributed to the project by someone else; i.e.,
 * CVS: they sent us a patch or a set of diffs, then include their name/email
 * CVS: address here. If this is your work then delete this line.
 * CVS: Reviewed by:
 * CVS: If we are doing pre-commit code reviews and someone else has
 * CVS: reviewed your changes, include their name(s) here.
 * CVS: If you have not had it reviewed then delete this line.
 *
 */
