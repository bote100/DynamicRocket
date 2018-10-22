package net.bote.dynamicrocket.files;

import net.bote.dynamicrocket.console.LogLevel;
import net.bote.dynamicrocket.console.Logger;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * @author Elias Arndt
 */
public class PropertiesEditor {
	
	private int port;
	private String name;
	private File file;
	private int slots;
	private Properties prop;
	private InputStream in = null;
	private OutputStream out = null;

	/**
	 * Creates a new PropertiesEditor and downloads new server.properties file from
	 * bote100.eu server if the file not exists.
	 *
	 * @param serverport 	new server port
	 * @param title 		Name of the server
	 * @param maxslots 		Maxslots of the server
	 * @param file	 		The name of the file to use as the destination of this PropertiesEditor.
	 */
	public PropertiesEditor(int serverport, String title, int maxslots, File file) {
		this.port = serverport;
		this.name = title;
		this.file = file;
		this.slots = maxslots;
		this.prop = new Properties();

		// downloading server.properties from bote100.eu server
		if(!file.exists()) {
			try {
				URL bungee_download = new URL("http://bote100.eu/server.properties");
				ReadableByteChannel rbc = Channels.newChannel(bungee_download.openStream());
				FileOutputStream fos = new FileOutputStream("running/"+title+"/server.properties");
				fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			} catch (IOException e) {
				new Logger("Couldn't download server.properties....", LogLevel.ERROR).log();
				new Logger(e.getMessage(), LogLevel.ERROR).log();
			}
		}

		// loading streams
		try {
			this.in = new FileInputStream(file.getAbsolutePath());
			out = new FileOutputStream(file.getAbsolutePath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}


	/**
	 * This method editing the server properties and replace it with the parameters of the constructor.
	 */
	public void editServerProperities() {
		try {
			// load properties file
			prop.load(in);

			// get the property value and overwrite it.
			prop.setProperty("max-players", String.valueOf(this.slots));
			prop.setProperty("server-port", String.valueOf(port));
			prop.setProperty("online-mode", String.valueOf(false));
			prop.setProperty("motd", name);

			// store values
			prop.store(out, null);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
