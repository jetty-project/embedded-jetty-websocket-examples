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
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import org.eclipse.jetty.util.component.LifeCycle;

public class EventClient
{
    public static void main(String[] args)
    {
        EventClient client = new EventClient();
        URI uri = URI.create("ws://localhost:8080/events/");
        try
        {
            client.run(uri);
        }
        catch (Throwable t)
        {
            t.printStackTrace(System.err);
        }
    }

    public void run(URI uri) throws InterruptedException, IOException, DeploymentException
    {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        try
        {
            // Create client side endpoint
            EventSocket clientEndpoint = new EventSocket();

            // Attempt Connect
            Session session = container.connectToServer(clientEndpoint,uri);

            // Send a message
            session.getBasicRemote().sendText("Hello");

            // Send another message
            session.getBasicRemote().sendText("Goodbye");

            // Wait for remote to close
            clientEndpoint.awaitClosure();

            // Close session
            session.close();
        }
        finally
        {
            // Force lifecycle stop when done with container.
            // This is to free up threads and resources that the
            // JSR-356 container allocates. But unfortunately
            // the JSR-356 spec does not handle lifecycles (yet)
            LifeCycle.stop(container);
        }
    }
}
