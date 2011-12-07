/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2011 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.plugin.staticsecurity.ldap;

import com.unboundid.ldap.sdk.BindResult;
import com.unboundid.ldap.sdk.Filter;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SearchRequest;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;
import com.unboundid.util.ssl.SSLUtil;
import com.unboundid.util.ssl.TrustAllTrustManager;
import org.geomajas.plugin.staticsecurity.configuration.AuthorizationInfo;
import org.geomajas.plugin.staticsecurity.configuration.UserInfo;
import org.geomajas.plugin.staticsecurity.security.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * {@link AuthenticationService} for linking to an LDAP store of users.
 *
 * @author Joachim Van der Auwera
 */
public class LdapAuthenticationService implements AuthenticationService {

	private final Logger log = LoggerFactory.getLogger(LdapAuthenticationService.class);

	@NotNull
	private String serverHost;

	private int serverPort = 636; // default to secure port

	private boolean allowAllSocketFactory;

	@NotNull
	private String userDnTemplate;

	private String givenNameAttribute;
	private String surNameAttribute;
	private String localeAttribute;
	private String organizationAttribute;
	private String divisionAttribute;
	private String rolesAttribute;

	private Map<String, List<AuthorizationInfo>> roles;

	/**
	 * Set the server host for the LDAP service.
	 *
	 * @param serverHost server host
	 */
	public void setServerHost(String serverHost) {
		this.serverHost = serverHost;
	}

	/**
	 * Set the port on which the LDAP server can be found.
	 *
	 * @param serverPort server port
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * Set to true to make sure that all SSL checks are ignored. While this setting is insecure, it avoids the need
	 * to install root certificates etc. Setting this to true forces the use of SSL to connect to the server.
	 *
	 * @param allowAllSocketFactory should all SSL checks be ignored
	 */
	public void setAllowAllSocketFactory(boolean allowAllSocketFactory) {
		this.allowAllSocketFactory = allowAllSocketFactory;
	}

	/**
	 * Set the template to build the DN for the user. Any "{}" in the string is replaced by the user name.
	 *
	 * @param userDnTemplate template to build the user DN
	 */
	public void setUserDnTemplate(String userDnTemplate) {
		this.userDnTemplate = userDnTemplate;
	}

	/**
	 * Attribute for the given name (if any).
	 *
	 * @param givenNameAttribute attribute name
	 */
	public void setGivenNameAttribute(String givenNameAttribute) {
		this.givenNameAttribute = givenNameAttribute;
	}

	/**
	 * Attribute for the surname (if any).
	 *
	 * @param surNameAttribute attribute name
	 */
	public void setSurNameAttribute(String surNameAttribute) {
		this.surNameAttribute = surNameAttribute;
	}

	/**
	 * Attribute for locale (if any).
	 *
	 * @param localeAttribute attribute name
	 */
	public void setLocaleAttribute(String localeAttribute) {
		this.localeAttribute = localeAttribute;
	}

	/**
	 * Attribute for the organization (if any).
	 *
	 * @param organizationAttribute attribute name
	 */
	public void setOrganizationAttribute(String organizationAttribute) {
		this.organizationAttribute = organizationAttribute;
	}

	/**
	 * Attribute for the division (if any).
	 *
	 * @param divisionAttribute attribute name
	 */
	public void setDivisionAttribute(String divisionAttribute) {
		this.divisionAttribute = divisionAttribute;
	}

	/**
	 * Attribute for the roles (if any).
	 *
	 * @param rolesAttribute attribute name
	 */
	public void setRolesAttribute(String rolesAttribute) {
		this.rolesAttribute = rolesAttribute;
	}

	/**
	 * Set the authorizations for the roles which may be defined.
	 *
	 * @param roles map with roles, keys are the values for {@link #rolesAttribute}, probably DN values
	 */
	public void setRoles(Map<String, List<AuthorizationInfo>> roles) {
		this.roles = roles;
	}

	/** {@inheritDoc} */
	public String convertPassword(String user, String password) {
		return password;
	}

	/** {@inheritDoc} */
	public UserInfo isAuthenticated(String user, String password) {
		String userDn = userDnTemplate.replace("{}", user);
		LDAPConnection connection = null;
		try {
			if (allowAllSocketFactory) {
				SSLUtil sslUtil = new SSLUtil(new TrustAllTrustManager());
				connection = new LDAPConnection(sslUtil.createSSLSocketFactory(), serverHost, serverPort);
			} else {
				connection = new LDAPConnection(serverHost, serverPort);
			}

			BindResult auth = connection.bind(userDn, password);
			if (auth.getResultCode().isConnectionUsable()) {
				List<String> attributes = new ArrayList<String>();
				attributes.add("cn");
				addAttribute(attributes, givenNameAttribute);
				addAttribute(attributes, surNameAttribute);
				addAttribute(attributes, localeAttribute);
				addAttribute(attributes, organizationAttribute);
				addAttribute(attributes, divisionAttribute);
				addAttribute(attributes, rolesAttribute);
				SearchRequest request = new SearchRequest(userDn, SearchScope.SUB,
						Filter.createEqualityFilter("objectclass", "person"),
						attributes.toArray(new String[attributes.size()]));
				return getUserInfo(user, connection.search(request));
			}
		} catch (LDAPException le) {
			String message = le.getMessage();
			if (!message.startsWith("Unable to bind as user ")) {
				log.error(le.getMessage(), le);
			}
		} catch (GeneralSecurityException gse) {
			log.error(gse.getMessage(), gse);
		} finally {
			if (null != connection) {
				connection.close();
			}
		}
		return null;  // not logged in
	}

	private UserInfo getUserInfo(String userId, SearchResult search) {
		if (search.getEntryCount() > 0) {
			SearchResultEntry entry = search.getSearchEntries().get(0);
			UserInfo result = new UserInfo();
			result.setUserId(userId);
			String name = entry.getAttributeValue(givenNameAttribute);
			String name2 = entry.getAttributeValue(surNameAttribute);
			if (null != name) {
				if (null != name2) {
					name += " " + name2;
				}
			} else {
				name = name2;
			}
			result.setUserName(name);
			result.setUserLocale(entry.getAttributeValue(localeAttribute));
			result.setUserOrganization(entry.getAttributeValue(organizationAttribute));
			result.setUserDivision(entry.getAttributeValue(divisionAttribute));
			result.setAuthorizations(getAuthorizations(entry));
			return result;
		}
		return null;
	}

	private List<AuthorizationInfo> getAuthorizations(SearchResultEntry entry) {
		List<AuthorizationInfo> auths = new ArrayList<AuthorizationInfo>();
		String[] attributes = entry.getAttributeValues(rolesAttribute);
		if (null != attributes) {
			for (String attr : attributes) {
				List<AuthorizationInfo> auth = roles.get(attr);
				if (null != auth) {
					auths.addAll(auth);
				}
			}
		}
		return auths;
	}

	private void addAttribute(List<String> attributes, String attribute) {
		if (null != attribute) {
			attributes.add(attribute);
		}
	}
}