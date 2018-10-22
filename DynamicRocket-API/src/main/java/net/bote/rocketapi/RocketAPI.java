package net.bote.rocketapi;

import com.google.gson.Gson;
import net.bote.rocketapi.bridge.Client;
import net.bote.rocketapi.events.ConnectListener;
import net.bote.rocketapi.lib.Document;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * @author Elias Arndt
 * created on 20.10.2018 at 18:32
 * <p>
 * This is the main class of this plugin.
 */

public class RocketAPI extends Plugin {

    private static RocketAPI instance;
    private Client client;
    private Gson gson;


    @Override
    public void onEnable() {
        instance = this;
        gson = new Gson();
        try {
            client = new Client(InetAddress.getLocalHost().getHostAddress());
            client.connect(() -> {
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

        ProxyServer.getInstance().getPluginManager().registerListener(this, new ConnectListener());

        ArrayList<Object> list = new ArrayList<Object>(BungeeCord.getInstance().config.getListeners());

        /*
        System.out.println("Size: " + list.size());
        list.forEach(key -> System.out.println(key));
        */
    }

    @Override
    public void onDisable() {
        client.getExecutorService().execute(() -> client.getSocket().emit("ServerStop", new Document().append("name", "Proxy").convertToJson()));
        client.getSocket().disconnect();
    }

    public Client getClient() {
        return client;
    }

    public static RocketAPI getInstance() {
        return instance;
    }

    public Gson getGson() {
        return gson;
    }
}
