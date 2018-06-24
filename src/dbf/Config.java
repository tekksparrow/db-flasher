package dbf;
import java.util.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Config {

	Properties prop;
	InputStream input;

	// Note for clean up - convert this to singleton pattern

	public Config() {

		prop = new Properties();
		input = null;

		// Note for clean up - allow for dev config file
		// Found stack over flow post that advises against nesting try/catches and one user suggested making 
		// a separate function to keep the code cleaner and more readable.
		// https://stackoverflow.com/questions/10674474/java-is-it-bad-practice-to-do-a-try-catch-inside-a-try-catch

		try {

			// Set source
			input = new FileInputStream("../ntrack/config.cfg");

			// Load Properties into file
			prop.load(input);

		} catch (Exception e1) {
			System.out.println(e1.getMessage());
			System.out.println(e1.getStackTrace());
		}
	}

	public String getProperty(String key) {
		String value = this.prop.getProperty(key);
		return value;
	}

	// Clean up notes
	// Look at overriding i think it is so that we can set strings and ints with the same method.

	public void setProperty(String key, String value) {
		this.prop.setProperty(key, value);

	}
}