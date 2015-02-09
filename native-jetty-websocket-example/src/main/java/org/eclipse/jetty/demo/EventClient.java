package org.eclipse.jetty.demo;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;

public class EventClient
{
    public static void main(String[] args)
    {

        final CountDownLatch countDownLatch=new CountDownLatch(1);
        URI uri = URI.create("ws://echo.websocket.org");
        //URI uri = URI.create("ws://localhost:8080/events/");

        WebSocketClient client = new WebSocketClient();
        try
        {
            try
            {
                client.start();
                // The socket that receives events
                EventSocket socket = new EventSocket(countDownLatch);
                // Attempt Connect
                Future<Session> fut = client.connect(socket,uri);
                // Wait for Connect
                Session session = fut.get();
                // Send a message
                session.getRemote().sendString("Hello");
                // Close session
                session.close();
            }
            finally
            {
                countDownLatch.await();
                client.stop();
            }
        }
        catch (Throwable t)
        {
            t.printStackTrace(System.err);
        }
    }
}
