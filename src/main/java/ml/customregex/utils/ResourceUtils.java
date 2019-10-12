package ml.customregex.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResourceUtils {

	public static String readFile(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		return readStream(fis);
	}
	
	public static String readStream(InputStream is) throws IOException {
		int bit = -1;
		StringBuilder builder = new StringBuilder();
		while((bit = is.read()) != -1) builder.append((char)bit);
		is.close();
		return builder.toString();
	}	
}