package org.eclipse.jetty.demo;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import java.util.concurrent.CountDownLatch;

public class EventSocket extends WebSocketAdapter
{
    CountDownLatch countDownLatch;
    public EventSocket(CountDownLatch countDownLatch) {
        this.countDownLatch=countDownLatch;
    }

    @Override
    public void onWebSocketConnect(Session sess)
    {
        super.onWebSocketConnect(sess);
        System.out.println("Socket Connected: " + sess);
    }
    
    @Override
    public void onWebSocketText(String message)
    {
        super.onWebSocketText(message);
        System.out.println("Received TEXT message: " + message);
    }
    
    @Override
    public void onWebSocketClose(int statusCode, String reason)
    {
        super.onWebSocketClose(statusCode,reason);
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
        countDownLatch.countDown();
    }
    
    @Override
    public void onWebSocketError(Throwable cause)
    {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
        countDownLatch.countDown();
    }
}
