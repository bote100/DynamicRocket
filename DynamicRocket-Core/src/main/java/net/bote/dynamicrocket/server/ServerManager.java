package net.bote.dynamicrocket.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import net.bote.dynamicrocket.files.PropertiesEditor;
import org.apache.commons.io.FileUtils;

import net.bote.dynamicrocket.DynamicRocketCore;
import net.bote.dynamicrocket.console.LogLevel;
import net.bote.dynamicrocket.console.Logger;

/**
 * @author Elias Arndt
 *
 * This class contains methods for server-managing. You can pick free ports ({@link #getFreePort()})
 * You can start with {@link #startRocketServer(GroupConfiguration)} a new {@link MinecraftServer}.
 * While the server starts, the thread is sleeping and wait for the start process of the new started server.
 * With {@link #startProxy(GroupConfiguration)} the proxy is starting. This system only can run one proxy.
 * If a proxy server already running, the method is returning.
 */

public class ServerManager {

	/*
	 * Variables
	 */
	private ArrayList<MinecraftServer> runningservers = new ArrayList<>();
	private HashMap<String, ArrayList<MinecraftServer>> serveringroups = new HashMap<>();
	private static ServerManager manager = null;

	/**
	 * Constructor for this class
	 */
	public ServerManager() {}

	public static ServerManager getInstance() {
		if(manager == null) manager = new ServerManager();
		return manager;
	}


	/**
	 * Getting a free port of local machine. If random picked port is already used, the system picks
	 * a new one and do this so long until a free port was found.
	 * @return free port
	 */
	public int getFreePort() {
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(0);
			socket.setReuseAddress(true);
			int port = socket.getLocalPort();
			try {
				socket.close();
			} catch (IOException e) {
				// Ignore IOException on close()
			}
			return port;
		} catch (IOException e) { 
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
		throw new IllegalStateException("Could not find a free TCP/IP port to start embedded Jetty HTTP Server on");
}

	/**
	 * Start the proxy server of this system. If a proxy-server already running, this method returns and send
	 * a warning log to the console.
	 *
	 * @param cfg settings for start process
	 */
	public void startProxy(GroupConfiguration cfg) {

		File template = new File("templates/BungeeCord");
		Long starttime = System.currentTimeMillis();
		String title = cfg.getName();
		Integer ram = cfg.getRam();

		// check if running another proxy
		if (new File("running/Bungee").exists()) {
			new Logger("Can not start second bungeecord server!", LogLevel.WARNING);
			return;
		}

		// Create bungee server and copy template
		DynamicRocketCore.getInstance().getFileManager().createDirectionary("running/BungeeCord");
		try {
			FileUtils.copyFileToDirectory(new File("bungee-version/BungeeCord.jar"), new File("running/BungeeCord"));
		} catch (IOException e3) {
			e3.printStackTrace();
		}

		// get template and move to running/BungeeCord
		try {
			FileUtils.copyDirectory(template, new File("running/BungeeCord"));
		} catch (IOException e1) {
			new Logger("BungeeCord template or bungeecord file with name 'BungeeCord.jar' doesn't exists!", LogLevel.ERROR);
			return;
		}

		if (!new File("bungee-version/BungeeCord.jar").exists()) {
			new Logger("BungeeCord version not found! Please install manually or restart the rocket!", LogLevel.WARNING).log();
			return;
		}

		// starting proxy
		final String uuid = "bungeestart";
		final File start = new File(uuid + ".bat");
		if (!start.exists()) {
			try {

				final PrintWriter writer = new PrintWriter(String.valueOf(uuid) + ".bat", "UTF-8");
				writer.println("title " + title);
				writer.println("cd running/BungeeCord");
				writer.println("start call java -Xmx" + ram + "M -Xms" + ram + "M -jar BungeeCord.jar");
				writer.close();
				final Runtime runtime = Runtime.getRuntime();
				final Process process = runtime.exec(String.valueOf(uuid) + ".bat");
				process.waitFor();
				start.delete();
				long diff = System.currentTimeMillis() - starttime;
				DynamicRocketCore.getInstance().getConsole().sendMessage("Started Bungee-1 on port "
						+ DynamicRocketCore.getInstance().getFileManager().getConfig().getInt("BungeeListenPort") + " in " + diff + "ms");

			} catch (IOException e) { e.printStackTrace(); } catch (InterruptedException e2) { e2.printStackTrace(); }
		}
	}

	/**
	 * Start a new minecraft server. Here the method copy the template into the running/NAME folder, register
	 * the server in the config.yml of the bungeecord and downloading and starting spigot. After starting, calling
	 * the #onStart(millis) method of {@link MinecraftServerCallback} and print into the console a status response.
	 *
	 * @param cfg configuration of the servergroup
	 * @return the createad minecraft server
	 */
	public MinecraftServer startRocketServer(GroupConfiguration cfg) {

		// some variables
		ServerGroup group = new ServerGroup(cfg);

		int count = group.getServerAmount() + 1;

		// name of the new server
		String title = cfg.getName() + "-" + count;

		MinecraftServer server = new MinecraftServer(cfg, title);

		long startime = System.currentTimeMillis();

		File template = new File("templates/" + cfg.getName());

		// create new template
		if(!template.exists()) {
			createDirectionary("templates/"+cfg.getName()+"/plugins");
		}

		DynamicRocketCore.getInstance().getConsole().sendMessage("Starting server " + title +"...");

		// getting free port
		int port = getFreePort();

		// create directory in /running
		DynamicRocketCore.getInstance().getFileManager().createDirectionary("running/" + title);
		try {
			FileUtils.copyDirectory(template, new File("running/" + title));
		} catch (IOException e1) {
			e1.printStackTrace();
			new Logger( title + " template or spigot file with name 'spigot.jar' doesn't exists!", LogLevel.ERROR);
			return null;
		}

		// downloading spigot and move to /running/TITLE
		try {
			if(!FileUtils.directoryContains(new File("running/" + title), new File("running/"+title+"/spigot.jar"))) {
				
				DynamicRocketCore.getInstance().getConsole().sendMessage("Downloading Spigot for server " + title + "...");
				
				try {
					URL bungee_download = new URL("https://cdn.getbukkit.org/spigot/spigot-1.8.8-R0.1-SNAPSHOT-latest.jar");
					ReadableByteChannel rbc = Channels.newChannel(bungee_download.openStream());
					FileOutputStream fos = new FileOutputStream("running/"+title+"/spigot.jar");
					fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
				} catch (IOException e) {
					new Logger("Spigot couldn't downloaded from the server! Please try again later or update DynamicRocket!", LogLevel.ERROR).log();
					new Logger(e.getMessage(), LogLevel.ERROR).log();
				}

				PropertiesEditor editor = new PropertiesEditor(port, title, cfg.getSlots(), new File("running/" + title + "/server.properties"));
				editor.editServerProperities();
			}

			// create eula which accepts it.
			if(!FileUtils.directoryContains(new File("running/" + title), new File("running/"+title+"/eula.txt"))) {
				final PrintWriter writer = new PrintWriter("running/"+title+"/eula.txt", "UTF-8");
                writer.println("#By changing the setting below to TRUE you are indicating your agreement to our EULA (https://account.mojang.com/documents/minecraft_eula).");
                
                LocalDateTime now = LocalDateTime.now();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                String formatDateTime = now.format(formatter);
                
                writer.println("#" + formatDateTime);
                writer.println("eula=true");
                writer.close();
			}

		} catch (IOException e1) {
			e1.printStackTrace();
			new Logger("Please reinstall Rocket!", LogLevel.ERROR).log();
		}

		// starting server
		final String uuid = title;
        final File start = new File("running/" + title + "/" + uuid + ".bat");
        if (!start.exists()) {
			try {
				final PrintWriter writer = new PrintWriter(String.valueOf(uuid) + ".bat", "UTF-8");
				writer.println("title " + title);
				writer.println("cd running/" + title);
				writer.println("start call java -Xmx" + cfg.getRam() + "M -Xms" + cfg.getRam() + "M -jar spigot.jar");
				writer.close();
				final Runtime runtime = Runtime.getRuntime();
				final Process process = runtime.exec(String.valueOf(uuid) + ".bat");
				process.waitFor();
				start.delete();
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e2) {
				e2.printStackTrace();
			}
		}

        /*

		while(!start.delete() || start.exists()) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) { e.printStackTrace(); }
		}

		*/

        boolean logexists = false;

        /*
        Waiting so long, until the latest.log file has created
         */
		while (!logexists) {
			logexists = new File("running/" + title + "/logs/latest.log").exists();
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		String log = server.getLogToString(title);
		System.out.println(log);
        while(!log.contains("For help, type")) {
        	log = server.getLogToString(title);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Calling #onStart(millis) method
		server.onStart(System.currentTimeMillis() - startime);

		server.registerServer(port);

		DynamicRocketCore.getInstance().getServerManager().getRunningServers().add(server);

		ArrayList<MinecraftServer> runningServerInGroup = null;

		if(DynamicRocketCore.getInstance().getServerManager().getServersInGroup().containsKey(server.getGroup().getConfiguration().getName())) {
			runningServerInGroup = DynamicRocketCore.getInstance().getServerManager().getServersInGroup().get(server.getGroup().getConfiguration().getName());
		} else {
			runningServerInGroup = new ArrayList<MinecraftServer>();
		}

		runningServerInGroup.add(server);
		DynamicRocketCore.getInstance().getServerManager().getServersInGroup().put(server.getGroup().getConfiguration().getName(), runningServerInGroup);

		return server;

    }

	/**
	 * Creating new folder
	 * @param path location
	 */
	public void createDirectionary(String path) {
		File file = new File(path);
		if(!file.exists()) {
			file.mkdirs();
		}
	}

    public ArrayList<MinecraftServer> getRunningServers() { return runningservers; }

    public HashMap<String, ArrayList<MinecraftServer>> getServersInGroup() { return serveringroups; }
}
