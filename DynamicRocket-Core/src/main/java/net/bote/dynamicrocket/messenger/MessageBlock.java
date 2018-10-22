package net.bote.dynamicrocket.messenger;

import com.google.gson.*;
import net.bote.dynamicrocket.console.LogLevel;
import net.bote.dynamicrocket.console.Logger;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Elias Arndt
 * created on 23.09.2018 at 10:38
 */

public class MessageBlock {

    private String name;
    protected JsonObject data;
    public Gson gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().setPrettyPrinting().create();
    public static JsonParser PARSER;

    public MessageBlock(String name) {
        this.name = name;
        this.data = new JsonObject();
    }

    public MessageBlock() {
        this.data = new JsonObject();
    }

    public MessageBlock(final JsonObject source) {
        this.data = source;
    }

    public MessageBlock append(String arg0, String arg1) {
        if(arg1 == null) {
            new IllegalArgumentException("Value can't be null!");
            return this;
        }
        this.data.addProperty(arg0, arg1);
        return this;
    }

    public MessageBlock append(String arg0, Number arg1) {
        if(arg1 == null) {
            new IllegalArgumentException("Value can't be null!");
            return this;
        }
        this.data.addProperty(arg0, arg1);
        return this;
    }



    public MessageBlock append(String arg0, Boolean arg1) {
        if(arg1 == null) {
            new IllegalArgumentException("Value can't be null!");
            return this;
        }
        this.data.addProperty(arg0, arg1);
        return this;
    }

    public MessageBlock append(String arg0, JsonElement arg1) {
        if(arg1 == null) {
            new IllegalArgumentException("Value can't be null!");
            return this;
        }
        this.data.add(arg0, arg1);
        return this;
    }

    public MessageBlock append(String arg0, List<String> arg1) {
        if(arg1 == null) {
            new IllegalArgumentException("Value can't be null!");
            return this;
        }
        JsonArray jsonElements = new JsonArray();
        for (String str : arg1) {
            jsonElements.add(str);
        }
        this.data.add(arg0, jsonElements);
        return this;
    }

    @Override
    public String toString() { return this.data.toString(); }

    public MessageBlock killKey(String arg0) { this.data.remove(arg0); return this; }

    public boolean containsKey(String arg0) { return this.data.has(arg0); }

    public Double getDouble(String arg0) {
        if(arg0 == null) {
            new IllegalArgumentException("Value can't be null!");
            return null;
        }
        return this.data.get(arg0).getAsDouble();
    }

    public String getString(String arg0) {
        if(arg0 == null) {
            new IllegalArgumentException("Value can't be null!");
            return null;
        }
        return this.data.get(arg0).getAsString();
    }

    public <T> T getObject(String arg0, Class<T> clazz)
    {
        if(arg0 == null) {
            new IllegalArgumentException("Value can't be null!");
            return null;
        }
        JsonElement ele = this.data.get(arg0);
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(ele, clazz);
    }

    public Integer getInteger(String arg0) {
        if(arg0 == null) {
            new IllegalArgumentException("Value can't be null!");
            return null;
        }
        return this.data.get(arg0).getAsInt();
    }

    public Boolean getBoolean(String arg0) {
        if(arg0 == null) {
            new IllegalArgumentException("Value can't be null!");
            return null;
        }
        return this.data.get(arg0).getAsBoolean();
    }

    public Long getLong(String arg0) {
        if(arg0 == null) {
            new IllegalArgumentException("Value can't be null!");
            return null;
        }
        return this.data.get(arg0).getAsLong();
    }

    public Set<String> getKeys() {
        Set<String> set = new HashSet<>();
        for (Map.Entry<String, JsonElement> x : this.data.entrySet()) { set.add(x.getKey()); }
        return set;
    }

    public MessageBlock clear() {
        getKeys().forEach(key -> {
            killKey(key);
        });
        return this;
    }

    public void saveInConfig(java.io.File file) {

        try {
            final PrintWriter writer = new PrintWriter(file.getAbsolutePath(), "UTF-8");
            writer.println(toJSON());
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            new Logger(ex.getMessage(), LogLevel.ERROR).log();
            return;
        }

    }

    public String toJSON() {
        return gson.toJson(this.data);
    }

    public static MessageBlock load(final java.io.File backend) {
        try {
            return new MessageBlock(MessageBlock.PARSER.parse(new String(Files.readAllBytes(backend.toPath()), StandardCharsets.UTF_8)).getAsJsonObject());
        }
        catch (Exception ex) {
            new Logger(ex.getMessage(), LogLevel.ERROR).log();
        }
        return new MessageBlock();
    }

    static {
        PARSER = new JsonParser();
    }

}
