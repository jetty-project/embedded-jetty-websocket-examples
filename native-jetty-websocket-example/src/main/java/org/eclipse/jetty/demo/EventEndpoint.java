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

import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import org.eclipse.jetty.websocket.api.Callback;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketOpen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventEndpoint
{
    private static final Logger LOG = LoggerFactory.getLogger(EventEndpoint.class);
    private final CountDownLatch closureLatch = new CountDownLatch(1);
    private Session session;

    @OnWebSocketOpen
    public void onWebSocketOpen(Session sess)
    {
        LOG.debug("Endpoint open: {}", sess);
        this.session = sess;
    }

    @OnWebSocketMessage
    public void onWebSocketText(String message)
    {
        LOG.debug("Received TEXT message: {}", message);

        if (message.toLowerCase(Locale.US).contains("bye"))
        {
            this.session.close(StatusCode.NORMAL, "Thanks", Callback.NOOP);
        }
    }

    @OnWebSocketClose
    public void onWebSocketClose(int statusCode, String reason)
    {
        LOG.debug("Endpoint Close: [{}] {}", statusCode, reason);
        closureLatch.countDown();
    }

    @OnWebSocketError
    public void onWebSocketError(Throwable cause)
    {
        cause.printStackTrace(System.err);
    }

    public void awaitClosure() throws InterruptedException
    {
        LOG.debug("Awaiting closure from remote");
        closureLatch.await();
    }
}
