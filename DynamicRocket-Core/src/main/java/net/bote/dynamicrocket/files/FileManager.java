package net.bote.dynamicrocket.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import net.bote.dynamicrocket.DynamicRocketCore;
import net.bote.dynamicrocket.console.LogLevel;
import net.bote.dynamicrocket.console.Logger;
import net.bote.dynamicrocket.messenger.MessageBlock;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

/**
 * @author Elias Arndt
 *
 * This file manager contains methods to create all required folders or managing the file trafic of DynamicRocket
 */
public class FileManager {

	/**
	 * Instance of FileManager
	 */
	private static FileManager instance = new FileManager();

	/*
	 * Constructor
	 */
	public FileManager() {}

	/**
	 * Get the instance of {@link FileManager}
	 * @return instance of FileManager
	 */
	public static FileManager getInstance() {
		return instance;
	}

	/**
	 * Get the main .yml configuration of DynamicRocket
	 * @return main {@link YamlConfiguration} of DynamicRocket
	 */
	public Configuration getConfig() {
		Configuration cfg = null;
		File file = new File("database", "config.yml");
		
		if(!new File("database").exists()) {
			createDirectionary("database");
		}
		
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return cfg;
		
	}

	/**
	 * @return if this is the first run of DynamicRocket
	 */
	public Boolean isFirstStart() {
		return !new File("database", "config.yml").exists();
	}

	/**
	 * Creating all required configs and files
	 */
	public void createConfigs() {
		Configuration cfg = null;
		File file = new File("database", "config.yml"); // main config

		/*
			The next two blocks of methods creating a directionary for the config, if it not exists.
		 */
		if(!new File("database").exists()) createDirectionary("database");
		
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/*
		 create Bungee.json file
		 This is the configuration file for the server group
		  */
		MessageBlock block = new MessageBlock();
		block.append("name", "Bungee");
		block.append("maxram" , 508);
		block.append("startPort", 25577);
		block.append("maxPlayers", 1000);

		block.saveInConfig(new File("groups/Bungee.json"));
		
		try {
			cfg = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		cfg.set("BungeeListenPort", 25577);
		cfg.set("AsnycCommunication", true);
		cfg.set("NettyCommunicationIP", "127.0.0.1");
		try {
			cfg.set("IPAdresse", InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException unknownhost) {
			cfg.set("IPAdresse", "localhost");
		}
		
		try {
			new YamlConfiguration().save(cfg, file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// -----------------| Bungee-Version |---------------------- //
		
		file = new File("bungee-version", "messages.yml");
		
		if(!new File("bungee-version").exists()) {
			createDirectionary("bungee-version");
			
			DynamicRocketCore.getInstance().getConsole().sendMessage("Downloading BungeeCord...");
			
			try {
				URL bungee_download = new URL("https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar");
				ReadableByteChannel rbc = Channels.newChannel(bungee_download.openStream());
				FileOutputStream fos = new FileOutputStream("bungee-version/BungeeCord.jar");
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			} catch (IOException e) {
				new Logger("BungeeCord couldn't downloaded from the server! Please try again later or update DynamicRocket!", LogLevel.ERROR).log();
				new Logger(e.getMessage(), LogLevel.ERROR).log();
			}
		}
		
		// -------------------| Template folder & groups & servers |-------------------------- //
		
		if(!new File("templates").exists()) {
			createDirectionary("templates");
			
			createDirectionary("templates/BungeeCord");
			createDirectionary("templates/BungeeCord/plugins");
			
			createDirectionary("groups");
			
			createDirectionary("running");
			
		}
		
	}

	/**
	 * Create a new directionary at the location given as parameter
	 * @param path location of new folder
	 */
	public void createDirectionary(String path) {
		File file = new File(path);
		if(!file.exists()) {
			file.mkdirs();
		}
	}
}
