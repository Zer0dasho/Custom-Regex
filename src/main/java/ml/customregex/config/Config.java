package ml.customregex.config;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import ml.customregex.Main;
import ml.customregex.utils.ConfigUtils;
import ml.customregex.utils.ResourceUtils;


public class Config<T extends Settings> {

	private T settings;
	private Class<?> clazz;
	private File configFile;
	
	public Config(File directory, String fileName, Class<?> clazz) {
		this(new File(directory, fileName), clazz);
	}
	
	public Config(File configFile, Class<?> clazz) {
		this.configFile = configFile;
		this.clazz = clazz;
		try {
			reload();
		} catch (IOException ex) {
			Logger logger = Main.getPlugin().getLogger();
			logger.log(Level.SEVERE, "Couldn't read from '{}'.".replace("{}", configFile.getName()));
			ex.printStackTrace();
		} catch (Exception ex) {
			Logger logger = Main.getPlugin().getLogger();
			logger.log(Level.SEVERE, "YAML syntax incorrect for failed for '{}'.".replace("{}", configFile.getName()));
			ex.printStackTrace();
		} 
	}
	
	@SuppressWarnings("unchecked")
	public void reload() throws JsonSyntaxException, IOException, Exception {
		File parentFile = configFile.getParentFile();
		if(!parentFile.exists()) parentFile.mkdirs();
		if(!configFile.exists()) Main.getPlugin(Main.class).saveResource(configFile.getName(), false);
		
		Gson gson = ConfigUtils.GSON;
		settings = (T) gson.fromJson(ConfigUtils.yamlToJson(ResourceUtils.readFile(configFile)), clazz);

	}
	
	public File getFile() {
		return configFile;
	}
	
	public T getSettings() {
		return settings;
	}
	
}