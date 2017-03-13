/**
 * Copyright (C) 2011 ConnId (connid-dev@googlegroups.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tirasa.connid.bundles.soap;

import java.net.URL;
import org.identityconnectors.framework.spi.AbstractConfiguration;
import org.identityconnectors.framework.spi.ConfigurationProperty;
import org.identityconnectors.common.StringUtil;

/**
 * Extends the {@link AbstractConfiguration} class to provide all the necessary parameters to initialize the WebService
 * Connector.
 */
public class WebServiceConfiguration extends AbstractConfiguration {

    /*
     * Web Service Endpoint.
     */
    private String endpoint = null;

    /*
     * Public Web Service interface class
     */
    private String servicename = null;

    /*
     * Connection timeout
     */
    private String connectionTimeout = "30";

    /*
     * Receive timeout
     */
    private String receiveTimeout = "60";

    /*
     * Receive timeout
     */
    private String soapActionUriPrefix = null;

    /**
     * Accessor for the example property. Uses ConfigurationProperty annotation to provide property metadata to the
     * application.
     */
    @ConfigurationProperty(displayMessageKey = "ENDPOINT_DISPLAY",
            helpMessageKey = "ENDPOINT_HELP",
            confidential = false, order = 1, required = true)
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * Setter for the example property.
     */
    public void setEndpoint(final String endpoint) {
        this.endpoint = endpoint;
    }

    /**
     * Accessor for the example property. Uses ConfigurationProperty annotation to provide property metadata to the
     * application.
     */
    @ConfigurationProperty(displayMessageKey = "CLASSNAME_DISPLAY",
            helpMessageKey = "CLASSNAME_HELP", confidential = false, order = 2, required = true)
    public String getServicename() {
        return servicename;
    }

    public void setServicename(final String classname) {
        this.servicename = classname;
    }

    @ConfigurationProperty(displayMessageKey = "CONNECTIONTIMEOUT_DISPLAY",
            helpMessageKey = "CONNECTIONTIMEOUT_HELP", confidential = false, order = 3)
    public String getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(final String connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    @ConfigurationProperty(displayMessageKey = "RECEIVETIMEOUT_DISPLAY",
            helpMessageKey = "RECEIVETIMEOUT_HELP", confidential = false, order = 4)
    public String getReceiveTimeout() {
        return receiveTimeout;
    }

    public void setReceiveTimeout(final String receiveTimeout) {
        this.receiveTimeout = receiveTimeout;
    }

    @ConfigurationProperty(displayMessageKey = "SOAPACTION_DISPLAY",
            helpMessageKey = "SOAPACTION_HELP", confidential = false, order = 5)
    public String getSoapActionUriPrefix() {
        return soapActionUriPrefix;
    }

    public void setSoapActionUriPrefix(final String soapActionUriPrefix) {
        this.soapActionUriPrefix = soapActionUriPrefix;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void validate() {
        // Check if endpoint has been specified.
        if (StringUtil.isBlank(endpoint)) {
            throw new IllegalArgumentException("Endpoint cannot be null or empty.");
        }

        // Check if servicename has been specified.
        if (StringUtil.isBlank(servicename)) {
            throw new IllegalArgumentException("Service name cannot be null or empty.");
        }

        // Check if servicename has been specified.
        if (StringUtil.isBlank(connectionTimeout)) {
            connectionTimeout = "30";
        }

        try {
            Long.parseLong(connectionTimeout);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The specified connection timeout is not valid.");
        }

        if (StringUtil.isBlank(receiveTimeout)) {
            receiveTimeout = "60";
        }

        try {
            Long.parseLong(receiveTimeout);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The specified receive timeout is not valid.");
        }

        try {
            // Check if the specified endpoint is a well-formed URL
            final URL endpointURL = new URL(endpoint);
            endpointURL.toURI();
        } catch (Exception e) {
            throw new IllegalArgumentException("The specified endpoint is not a valid URL.");
        }
    }
}
