package net.bote.rocketapi.bridge.server;

import net.bote.rocketapi.bridge.MinecraftServer;

import java.util.ArrayList;

/**
 * @author Elias Arndt
 * created on 22.10.2018 at 14:23
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

}
