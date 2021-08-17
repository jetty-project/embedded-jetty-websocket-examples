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

import java.net.URI;
import java.util.concurrent.Future;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;

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

    public void run(URI uri) throws Exception
    {
        WebSocketClient client = new WebSocketClient();

        try
        {
            client.start();
            // The socket that receives events
            EventSocket socket = new EventSocket();
            // Attempt Connect
            Future<Session> fut = client.connect(socket, uri);
            // Wait for Connect
            Session session = fut.get();

            // Send a message
            session.getRemote().sendString("Hello");

            // Send another message
            session.getRemote().sendString("Goodbye");

            // Wait for other size to close
            socket.awaitClosure();

            // Close session
            session.close();
        }
        finally
        {
            client.stop();
        }
    }
}
