package dbf;
import java.io.*;
import java.lang.StringBuffer;
import java.util.ArrayList;
import java.net.URL;
import java.net.URLConnection;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;

public class JsonGrabber {

	public JsonGrabber() {
		// Constructor
	}

	public File grabJfile(URL url, int cntFile) {
		JSONParser parser = new JSONParser();

		// try {
		// 	// Open Connection, get stream
		// 	URLConnection site = url.openConnection();
		// 	InputStream in = site.getInputStream();
			
		// 	// Create file name
		// 	StringBuilder fn = new StringBuilder("dbJobs.");
		// 	cntFile ++;
		// 	fn.append(String.format("%03d", cntFile)).append(".json");

		// 	// Create file
		// 	FileOutputStream fos = new FileOutputStream(new File(fn.toString()));

		// 	// Copy contents to created file
		// 	byte[] buf = new byte[512];
		// 	while (true) {
		// 	    int len = in.read(buf);
		// 	    if (len == -1) {
		// 	        break;
		// 	    }
		// 	    fos.write(buf, 0, len);
		// 	}

		// 	// Clean up
		// 	in.close();
		// 	fos.flush();
		// 	fos.close();

		// 	return fos;
		// }catch(FileNotFoundException e) {

		// }catch(IOException e) {

		// }catch(ParseException e) {

		// }catch(Exception e) {
		// 	System.out.println(e.getMessage());
		// 	System.out.println(e.getStackTrace());
		// }
		// return Null;
		File f = new File(url.getFile());
		return f;
	}

}