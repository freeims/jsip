package com.github.freeims.javax.sip.header;

import java.text.ParseException;

import javax.sip.InvalidArgumentException;
import javax.sip.address.Address;
import javax.sip.header.Header;
import javax.sip.header.HeaderFactory;

import com.github.freeims.javax.sip.header.extensions.JoinHeader;
import com.github.freeims.javax.sip.header.extensions.ReferencesHeader;
import com.github.freeims.javax.sip.header.extensions.ReferredByHeader;
import com.github.freeims.javax.sip.header.extensions.ReplacesHeader;
import com.github.freeims.javax.sip.header.extensions.SessionExpiresHeader;
import com.github.freeims.javax.sip.header.ims.PAccessNetworkInfoHeader;
import com.github.freeims.javax.sip.header.ims.PAssertedIdentityHeader;
import com.github.freeims.javax.sip.header.ims.PAssertedServiceHeader;
import com.github.freeims.javax.sip.header.ims.PAssociatedURIHeader;
import com.github.freeims.javax.sip.header.ims.PCalledPartyIDHeader;
import com.github.freeims.javax.sip.header.ims.PChargingFunctionAddressesHeader;
import com.github.freeims.javax.sip.header.ims.PChargingVectorHeader;
import com.github.freeims.javax.sip.header.ims.PMediaAuthorizationHeader;
import com.github.freeims.javax.sip.header.ims.PPreferredIdentityHeader;
import com.github.freeims.javax.sip.header.ims.PPreferredServiceHeader;
import com.github.freeims.javax.sip.header.ims.PProfileKeyHeader;
import com.github.freeims.javax.sip.header.ims.PServedUserHeader;
import com.github.freeims.javax.sip.header.ims.PUserDatabaseHeader;
import com.github.freeims.javax.sip.header.ims.PVisitedNetworkIDHeader;
import com.github.freeims.javax.sip.header.ims.PathHeader;
import com.github.freeims.javax.sip.header.ims.PrivacyHeader;
import com.github.freeims.javax.sip.header.ims.SecurityClientHeader;
import com.github.freeims.javax.sip.header.ims.SecurityServerHeader;
import com.github.freeims.javax.sip.header.ims.SecurityVerifyHeader;
import com.github.freeims.javax.sip.header.ims.ServiceRouteHeader;

/**
 * Header factory extensions. These will be included in the next release of
 * JAIN-SIP.
 * 
 * @since 2.0
 *
 */
public interface HeaderFactoryExt extends HeaderFactory {

        /**
         * Create a RequestLine from a String
         * 
         * @throws ParseException
         */
        public SipRequestLine createRequestLine(String requestLine) throws ParseException;

        /**
         * Create a StatusLine from a String.
         */
        public SipStatusLine createStatusLine(String statusLine) throws ParseException;

        /**
         * Create a ReferredBy Header.
         *
         * @param address --
         *                address for the header.
         *
         */
        public ReferredByHeader createReferredByHeader(Address address);

        /**
         *
         * Create a Replaces header with a call Id, to and from tag.
         *
         * @param callId  -
         *                the call id to use.
         * @param toTag   -
         *                the to tag to use.
         * @param fromTag -
         *                the fromTag to use.
         *
         */
        public ReplacesHeader createReplacesHeader(String callId, String toTag,
                        String fromTag) throws ParseException;

        /**
         * creates a P-Access-Network-Info header.
         *
         * @return newly created P-Access-Network-Info header
         */
        public PAccessNetworkInfoHeader createPAccessNetworkInfoHeader();

        /**
         * P-Asserted-Identity header
         *
         * @param address -
         *                Address
         * @return newly created P-Asserted-Identity header
         * @throws ParseException
         * @throws NullPointerException
         */
        public PAssertedIdentityHeader createPAssertedIdentityHeader(Address address)
                        throws NullPointerException, ParseException;

        /**
         * Creates a new P-Associated-URI header based on the supplied address
         *
         * @param assocURI -
         *                 Address
         * @return newly created P-Associated-URI header
         * @throws NullPointerException
         *                              if the supplied address is null
         * @throws ParseException
         */
        public PAssociatedURIHeader createPAssociatedURIHeader(Address assocURI);

        /**
         * P-Called-Party-ID header
         *
         * @param address -
         *                Address
         * @return newly created P-Called-Party-ID header
         * @throws NullPointerException
         * @throws ParseException
         */
        public PCalledPartyIDHeader createPCalledPartyIDHeader(Address address);

        /**
         * P-Charging-Function-Addresses header
         *
         * @return newly created P-Charging-Function-Addresses header
         */
        public PChargingFunctionAddressesHeader createPChargingFunctionAddressesHeader();

        /**
         * P-Charging-Vector header
         *
         * @param icid -
         *             icid string
         * @return newly created P-Charging-Vector header
         * @throws NullPointerException
         * @throws ParseException
         */
        public PChargingVectorHeader createChargingVectorHeader(String icid) throws ParseException;

        /**
         * P-Media-Authorization header
         * 
         * @param token - token string
         * @return newly created P-Media-Authorizarion header
         * @throws InvalidArgumentException
         * @throws ParseException
         */
        public PMediaAuthorizationHeader createPMediaAuthorizationHeader(String token)
                        throws InvalidArgumentException, ParseException;

        /**
         * P-Preferred-Identity header
         * 
         * @param address - Address
         * @return newly created P-Preferred-Identity header
         * @throws NullPointerException
         */
        public PPreferredIdentityHeader createPPreferredIdentityHeader(Address address);

        /**
         * P-Visited-Network-ID header
         * 
         * @return newly created P-Visited-Network-ID header
         */
        public PVisitedNetworkIDHeader createPVisitedNetworkIDHeader();

        /**
         * PATH header
         * 
         * @param address - Address
         * @return newly created Path header
         * @throws NullPointerException
         * @throws ParseException
         */
        public PathHeader createPathHeader(Address address);

        /**
         * Privacy header
         * 
         * @param privacyType - privacy type string
         * @return newly created Privacy header
         * @throws NullPointerException
         */
        public PrivacyHeader createPrivacyHeader(String privacyType);

        /**
         * Service-Route header
         * 
         * @param address - Address
         * @return newly created Service-Route header
         * @throws NullPointerException
         */
        public ServiceRouteHeader createServiceRouteHeader(Address address);

        /**
         * Security-Server header
         * 
         * @return newly created Security-Server header
         */
        public SecurityServerHeader createSecurityServerHeader();

        /**
         * Security-Client header
         * 
         * @return newly created Security-Client header
         */
        public SecurityClientHeader createSecurityClientHeader();

        /**
         * Security-Verify header
         * 
         * @return newly created Security-Verify header
         */
        public SecurityVerifyHeader createSecurityVerifyHeader();

        /**
         * Creates a new SessionExpiresHeader based on the newly supplied expires value.
         *
         * @param expires - the new integer value of the expires.
         * @throws InvalidArgumentException if supplied expires is less
         *                                  than zero.
         * @return the newly created SessionExpiresHeader object.
         *
         */
        public SessionExpiresHeader createSessionExpiresHeader(int expires) throws InvalidArgumentException;

        /**
         *
         * Create a Join header with a call Id, to and from tag.
         *
         * @param callId  -
         *                the call id to use.
         * @param toTag   -
         *                the to tag to use.
         * @param fromTag -
         *                the fromTag to use.
         *
         */
        public JoinHeader createJoinHeader(String callId, String toTag,
                        String fromTag) throws ParseException;

        /**
         * Create a P-User-Database header.
         * 
         * @return the newly created P-User-Database header
         * @param the database name, that may be an IP:port or a domain name.
         */
        public PUserDatabaseHeader createPUserDatabaseHeader(String databaseName);

        /**
         * Create a P-Profile-Key header.
         * 
         * @param address
         * @return The newly created P-Profile-Key header
         */
        public PProfileKeyHeader createPProfileKeyHeader(Address address);

        /**
         * Create a P-Served-User header.
         * 
         * @param address of the served user.
         * @return The newly created P-Served-User Header.
         */
        public PServedUserHeader createPServedUserHeader(Address address);

        /**
         * Create a P-Preferred-Service header.
         * 
         * @return The newly created P-Preferred-Service Header.
         */
        public PPreferredServiceHeader createPPreferredServiceHeader();

        /**
         * Create an AssertedService Header
         *
         * @return The newly created P-Asserted-Service Header.
         */
        public PAssertedServiceHeader createPAssertedServiceHeader();

        /**
         * Create a References header.
         * 
         * @param callId -- the referenced call Id.
         * @param rel    -- the rel parameter of the references header.
         * 
         * @return the newly created References header.
         */
        public ReferencesHeader createReferencesHeader(String callId, String rel) throws ParseException;

        /**
         * Create a header from a string. The string is assumed to be in the
         * name:value format. The trailing CRLF (if any ) will be stripped
         * before parsing this. The header should be a singleton.
         */
        public Header createHeader(String header) throws ParseException;

}
