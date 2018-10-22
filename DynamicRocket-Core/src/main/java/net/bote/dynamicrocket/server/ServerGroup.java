package net.bote.dynamicrocket.server;

import net.bote.dynamicrocket.DynamicRocketCore;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * @author Elias Arndt
 * created on 30.09.2018 at 22:29
 */

public class ServerGroup {

    private GroupConfiguration configuration;

    /**
     * Get the configuration of the server-group
     * @return configuration
     */
    public GroupConfiguration getConfiguration() {
        return configuration;
    }

    /**
     * Set the configuration for this servergroup
     * @param configuration config to set
     */
    public void setConfiguration(GroupConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Constructor for this class.
     * The {@link GroupConfiguration} class contains all imported settings for this server group.
     * The GroupConfiguration gets all informations out of the .json file in the groups folder, created
     * by the system, when the ServerGroup is created by a user.
     *
     * @param configuration informations
     */
    public ServerGroup(GroupConfiguration configuration) {
        this.configuration = configuration;
    }

    /**
     * Returns all online {@link MinecraftServer} of this server group.
     * @return online servers of this group
     */
    public ArrayList<MinecraftServer> getOnlineServers() {
        if(DynamicRocketCore.getInstance().getServerManager().getServersInGroup().containsKey(this.configuration.getName()))
            return DynamicRocketCore.getInstance().getServerManager().getServersInGroup().get(this.configuration.getName());

        ArrayList<MinecraftServer> empty = new ArrayList<MinecraftServer>();
        DynamicRocketCore.getInstance().getServerManager().getServersInGroup().put(this.configuration.getName(), empty);
        return empty;

    }

    /**
     * Amount of all online servers in this group
     * @return online servers amount
     */
    public Integer getServerAmount() {
        return getOnlineServers().size();
    }

}
