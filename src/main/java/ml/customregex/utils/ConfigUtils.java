package ml.customregex.utils;

import java.util.Map;

import org.json.simple.JSONObject;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ConfigUtils {

	public static final Yaml YAML = new Yaml(getDumperOptions());
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	public static String yamlToJson(String yamlSrc) throws Exception {
		 @SuppressWarnings("unchecked")
		 Map<String,Object> map = (Map<String, Object>) YAML.load(yamlSrc);
		
		 JSONObject jsonObject = new JSONObject(map);
		 return jsonObject.toString();
	}
	
	public static String jsonToYaml(String json) {
		  return YAML.dump(json);
	}
	
	private static DumperOptions getDumperOptions() {
		  DumperOptions options = new DumperOptions();
	      options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
	      options.setPrettyFlow(true);
	      return options;
	}
	
	
	
	
}
