package net.bote.dynamicrocket.messenger;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import io.socket.client.Socket;
import lombok.Getter;
import net.bote.dynamicrocket.DynamicRocketCore;
import net.bote.dynamicrocket.connection.RocketPlayer;
import net.bote.dynamicrocket.lib.Document;
import org.json.JSONObject;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Elias Arndt
 * created on 23.09.2018 at 12:14
 *
 * This is the master of communication in the network. This server receiving all emits of the
 * minecraft servers and manage this events.
 */

public class CommunicationServer {

    private int port = 1337;
    private String serverip = DynamicRocketCore.getInstance().getFileManager().getConfig().getString("IPAdresse");
    private boolean async = DynamicRocketCore.getInstance().getFileManager().getConfig().getBoolean("AsnycCommunication");

    @Getter
    protected Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().serializeNulls().create();

    @Getter
    protected ExecutorService executorService = Executors.newCachedThreadPool();

    @Getter
    protected SocketIOServer server;

    /**
     * Starting the server. This must be executed before the client wanna connect.
     * @throws InterruptedException if any thread has interrupted the current thread.
     */
    public void startServer() throws InterruptedException {
        DynamicRocketCore.getInstance().getConsole().sendMessage("Trying to start DynamicRocket Core Communication Server....");

        Thread.sleep(350L);

        Configuration cfg = new Configuration();
        cfg.setHostname(this.serverip);
        cfg.setPort(port);

        this.server = new SocketIOServer(cfg);

        server.addEventListener("ClientConnected", String.class, (((client, data, ackSender) -> {
            executorService.execute(() -> {
                JSONObject object = new JSONObject(data);
                String name = object.getString("name");
                DynamicRocketCore.getInstance().getConsole().sendMessage("Server "+name+" [" + client.getRemoteAddress().toString() + "] connected to communication server.");
            });
        })));

        server.addEventListener(Socket.EVENT_DISCONNECT, String.class, (((client, data, ackSender) -> {
            executorService.execute(() -> {
                JSONObject object = new JSONObject(data);
                String name = object.getString("name");
                DynamicRocketCore.getInstance().getConsole().sendMessage("Server " + name + " disconnected.");
            });
        })));

        server.addEventListener("PlayerInfoCallback", String.class, (((client, data, ackSender) -> {
            executorService.execute(() -> {

                // Add new player to onplayers arraylist
                if (data == null) return;
                String o = data;
                RocketPlayer player = null;
                try {
                    player = gson.fromJson(o, RocketPlayer.class);
                } catch (JsonParseException ignored) {}

                System.out.println("COMMUNIATIONSERVER INFOCALLBACK output: " + player.toString());

                DynamicRocketCore.getInstance().getOnlinePlayers().add(player);

        });
        })));

        server.addEventListener("ServerStop", String.class, ((((client, data, ackSender) -> {
            executorService.execute(() -> {
                JSONObject object = new JSONObject(data);
                DynamicRocketCore.getInstance().getConsole().sendMessage("Server "
                        + object.getString("name") + " stopped.");
            });
        }))));

        server.addEventListener("RegisterPlayer", String.class, ((((client, data, ackSender) -> {
            executorService.execute(() -> {
                JSONObject object = new JSONObject(data);
                if(DynamicRocketCore.getInstance().playerExists(UUID.fromString(object.getString("uuid")))) return;

                DynamicRocketCore.getInstance().getCommunicationServer().getServer().getBroadcastOperations().
                        sendEvent("PlayerInfo", new Document().append("uuid", object.getString("uuid")));
            });
        }))));

        server.addEventListener("PlayerDisconnect", String.class, ((((client, data, ackSender) -> {
            executorService.execute(() -> {
                JSONObject object = new JSONObject(data);
                DynamicRocketCore.getInstance().getOnlinePlayers().remove(DynamicRocketCore.getInstance().getPlayer(UUID.fromString(object.getString("uuid"))));
            });
        }))));

        server.start();

        DynamicRocketCore.getInstance().getConsole().sendMessage("Communication-Server is now listening on port " + port + ".");
    }

    /**
     * Constructor. Executes automatically the {@link #startServer()} method
     * @throws InterruptedException if any thread has interrupted the current thread.
     */
    public CommunicationServer() throws InterruptedException {
        startServer();
    }

    /**
     * @return server address as string
     */
    public String getAddresse() { return this.serverip; }

    /**
     * @return running port of the server.
     */
    public Integer getPort() {
        return this.port;
    }

    /**
     * Getting the SocketIO server
     * @return SocketIO server
     */
    public SocketIOServer getServer() {
        return server;
    }
}
