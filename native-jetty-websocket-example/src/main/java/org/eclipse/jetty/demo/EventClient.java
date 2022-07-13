//
//  ========================================================================
//  Copyright (c) Mort Bay Consulting Pty Ltd and others.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.demo;

import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpClientTransport;
import org.eclipse.jetty.client.http.HttpClientTransportOverHTTP;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class EventClient
{
    public static void main(String[] args) throws Exception
    {
        URI uri = URI.create("ws://localhost:8080/events/");

        ExecutorService executor = Executors.newFixedThreadPool(8, r -> {
            Thread thread = new Thread(r);
            thread.setName("EventClientThread-");
            return thread;
        });

        int selectorCount = 1;
        HttpClientTransportOverHTTP transport = new HttpClientTransportOverHTTP(selectorCount);
        HttpClient httpClient = new HttpClient(transport);
        httpClient.setExecutor(executor);
        try
        {
            httpClient.start();

            WebSocketClient wsClient = new WebSocketClient(httpClient);
            try
            {
                wsClient.start();
                // The socket that receives events
                EventSocket socket = new EventSocket();
                // Attempt Connect
                Future<Session> fut = wsClient.connect(socket,uri);
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
                LifeCycle.stop(wsClient);
            }
        }
        catch (Throwable t)
        {
            t.printStackTrace(System.err);
        }
        finally
        {
            LifeCycle.stop(httpClient);
        }
        System.exit(0);
    }
}
