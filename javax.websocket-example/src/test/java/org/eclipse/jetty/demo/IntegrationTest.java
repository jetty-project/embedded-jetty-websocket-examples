//
// ========================================================================
// Copyright (c) Mort Bay Consulting Pty Ltd and others.
//
// This program and the accompanying materials are made available under the
// terms of the Eclipse Public License v. 2.0 which is available at
// https://www.eclipse.org/legal/epl-2.0, or the Apache License, Version 2.0
// which is available at https://www.apache.org/licenses/LICENSE-2.0.
//
// SPDX-License-Identifier: EPL-2.0 OR Apache-2.0
// ========================================================================
//

package org.eclipse.jetty.demo;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import javax.websocket.DeploymentException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IntegrationTest
{
    private EventServer server;

    @BeforeEach
    public void setupServer() throws Exception
    {
        server = new EventServer();
        server.setPort(0);
        server.start();
    }

    @AfterEach
    public void stopServer() throws Exception
    {
        server.stop();
    }

    @Test
    public void testServer() throws DeploymentException, IOException, InterruptedException, URISyntaxException
    {
        URI destUri = server.getURI().resolve("/events/");
        URI wsUri = new URI("ws", destUri.getUserInfo(), destUri.getHost(), destUri.getPort(),
            destUri.getRawPath(), destUri.getQuery(), destUri.getFragment());
        EventClient client = new EventClient();
        client.run(wsUri);
    }
}
