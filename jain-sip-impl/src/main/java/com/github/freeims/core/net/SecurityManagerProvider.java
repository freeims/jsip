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
package com.github.freeims.core.net;

import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * Override this interface to provide custom keys and trust stores for TLS.
 * 
 * @author Alexander Saveliev (Avistar)
 *
 */
public interface SecurityManagerProvider {

    void init(Properties properties) throws GeneralSecurityException, IOException;

    KeyManager[] getKeyManagers(boolean client);

    TrustManager[] getTrustManagers(boolean client);
}
