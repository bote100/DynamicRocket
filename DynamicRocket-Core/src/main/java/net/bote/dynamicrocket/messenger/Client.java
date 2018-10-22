package net.bote.dynamicrocket.messenger;

import com.google.gson.Gson;
import io.socket.client.IO;
import io.socket.client.Socket;
import net.bote.dynamicrocket.DynamicRocketCore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Elias Arndt
 * created on 23.09.2018 at 10:36
 *
 * This is the DynamicRocket Client which is used,
 * to communicate to the Core. Clients are used
 * on MinecraftServers otherwise the Server is running
 * on the DynamicRocket application
 */

public class Client{

    private ExecutorService executorService = Executors.newCachedThreadPool();
    private Socket socket;
    private String host;
    private Gson gson = new Gson();
    private CommunicationServer communicationServer = DynamicRocketCore.getInstance().getCommunicationServer();

    public Client(String host) { this.host = "http://" + host + ":1337"; }

    /**
     * This method let the client connect to the server. The Runnable will running,
     * if the Client calling the {@link Socket} event Socket.EVENT_CONNECT_ERROR
     * When the client connect successfully, the client sending a {@link MessageBlock}
     * with the name of the server. On sending, the server prints a message in console and
     * sending a broadcast to all {@link net.bote.dynamicrocket.connection.RocketPlayer} with
     * permission "rocket.admin"
     *
     * @param runnable Runnable for errors.
     * @throws Exception
     *                  If URI throws Exception
     *                  If any thread has interrupted the current thread.
     */
    public void connect(Runnable runnable) throws Exception {

        IO.Options options = new IO.Options();
        options.forceNew = true;
        options.reconnection = false;

        socket = IO.socket(this.host, options);

        this.socket.on(Socket.EVENT_CONNECT, (obj -> {
            DynamicRocketCore.getInstance().getConsole().sendMessage
                    ("Successfully connected to Communication-Server! ["
                            + communicationServer.getAddresse() +
                            ":" + communicationServer.getPort() +"]");
        }));

        socket.on(Socket.EVENT_CONNECT_ERROR, (objects -> runnable.run()));

        MessageBlock block = new MessageBlock();
        block.append("name", "DynamicRocket Core");

        socket.connect();

        System.out.println(socket.connected());

        Thread.sleep(2000);

        executorService.execute(() -> socket.emit("ClientConnected", block.toJSON()));

    }

}
