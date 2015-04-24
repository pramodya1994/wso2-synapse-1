/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.synapse.transport.nhttp;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.description.ParameterInclude;
import org.apache.axis2.description.TransportInDescription;
import org.apache.http.HttpHost;
import org.apache.synapse.transport.http.conn.Scheme;
import org.apache.synapse.transport.nhttp.config.ServerConnFactoryBuilder;
import org.apache.synapse.transport.nhttp.util.MultiSSLProfileReloader;
import org.apache.synapse.transport.dynamicconfigurations.ListenerProfileReloader;
import org.apache.synapse.transport.dynamicconfigurations.SSLProfileLoader;

public class HttpCoreNIOMultiSSLListener extends HttpCoreNIOListener implements SSLProfileLoader {

    public void init(ConfigurationContext ctx, TransportInDescription transportIn)
            throws AxisFault {
        super.init(ctx, transportIn);
        new MultiSSLProfileReloader(this, transportIn);
        new ListenerProfileReloader(this, transportIn);
    }
    @Override
    protected Scheme initScheme() {
        return new Scheme("https", 443, true);
    }

    @Override
    protected ServerConnFactoryBuilder initConnFactoryBuilder(
            final TransportInDescription transportIn, final HttpHost host) throws AxisFault {
        return new ServerConnFactoryBuilder(transportIn, host)
            .parseSSL()
            .parseMultiProfileSSL();
    }

    /**
     * Reload SSL profiles and reset connections in HttpCoreNIOMultiSSLListener
     * @param transport TransportInDescription of the configuration
     * @throws AxisFault
     */
    public void reloadConfig(ParameterInclude transport) throws AxisFault{
        reloadDynamicSSLConfig((TransportInDescription)transport);
    }
    
}
