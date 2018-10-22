package net.bote.dynamicrocket.server;

import net.bote.dynamicrocket.lib.Document;
import net.bote.dynamicrocket.messenger.MessageBlock;
import java.io.*;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Elias Arndt
 * created on 30.09.2018 at 17:35
 *
 * This object contains all informations of a server group.
 */

public class GroupConfiguration {

    private int slots;
    private int ram;
    private String title;
    private int newstart;
    private int globalnewstart;
    private boolean lobby;

    /**
     * Constructor
     * @param slots Slots of an server of the group
     * @param ram RAM per server
     * @param title name of the group
     * @param newstart how many players must be in the
     *                 group to start new server of the group?
     * @param globalnewstart how many players must be on
     *                       the server to start new server of the group?
     * @param lobby if it's a lobby server
     */
    public GroupConfiguration(int slots, int ram, String title, int newstart, int globalnewstart, boolean lobby) {
        this.slots = slots;
        this.ram = ram;
        this.title = title;
        this.newstart = newstart;
        this.globalnewstart = globalnewstart;
        this.lobby = lobby;
    }

    public boolean isLobby() {
        return lobby;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public String getName() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNewstart() {
        return newstart;
    }

    public void setNewstart(int newstart) {
        this.newstart = newstart;
    }

    public int getGlobalnewstart() {
        return globalnewstart;
    }

    public void setGlobalnewstart(int globalnewstart) {
        this.globalnewstart = globalnewstart;
    }

    public int getSlots() {
        return slots;
    }

    /**
     * static method to get GroupConfiguration
     * @param name name of the servergroup
     * @return new GroupConfiguration
     */
    public static GroupConfiguration getGroupConfiguration(String name) {
        Document doc = Document.loadDocument(new File("groups/" + name + ".json"));
        return new GroupConfiguration(
                doc.getInt("slots"),
                doc.getInt("ram"),
                doc.getString("name"),
                doc.getInt("groupstart"),
                doc.getInt("globalstart"),
                doc.getBoolean("isLobby"));
    }
}
