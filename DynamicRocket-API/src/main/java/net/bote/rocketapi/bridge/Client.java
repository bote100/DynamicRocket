package net.bote.rocketapi.bridge;

import com.google.gson.Gson;
import io.socket.client.IO;
import io.socket.client.Socket;
import net.bote.rocketapi.lib.Document;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Elias Arndt
 * created on 20.10.2018 at 18:56
 *
 * This is the DynamicRocket Client which is used,
 * to communicate to the Core. Clients are used
 * on MinecraftServers otherwise the Server is running
 * on the DynamicRocket application
 */

public class Client {

    private ExecutorService executorService = Executors.newCachedThreadPool();
    private Socket socket;
    private String host;
    private Gson gson = new Gson();

    public Client(String host) {
        this.host = "http://" + host + ":1337";
    }

    /**
     * This method let the client connect to the server. The Runnable will running,
     * if the Client calling the {@link Socket} event Socket.EVENT_CONNECT_ERROR
     * When the client connect successfully, the client sending a {@link Document}
     * with the name of the server. On sending, the server prints a message in console and
     * sending a broadcast to all Players with
     * permission "rocket.admin"
     *
     * @param runnable Runnable for errors.
     * @throws Exception If URI throws Exception
     *                   If any thread has interrupted the current thread.
     */
    public void connect(Runnable runnable) throws Exception {

        IO.Options options = new IO.Options();
        options.forceNew = true;
        options.reconnection = false;

        socket = IO.socket(this.host, options);

        this.socket.on(Socket.EVENT_CONNECT, (obj -> {

        }));

        socket.on(Socket.EVENT_CONNECT_ERROR, (objects -> runnable.run()));

        Document doc = new Document();
        doc.append("name", "DynamicRocket Core");

        socket.connect();

        System.out.println(socket.connected());

        Thread.sleep(2000);

        executorService.execute(() -> socket.emit("ClientConnected", doc.convertToJson()));

    }

    public Gson getGson() { return gson; }
    public ExecutorService getExecutorService() { return executorService; }
    public Socket getSocket() {
        return this.socket;
    }
}