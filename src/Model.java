import java.util.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * @author Jon Griesen (griesenj@mail.gvsu.edu)
 */

public class Model {
	
	// LinkedHashMap preserves insertion order (necessary for view updates)
	private LinkedHashMap<String, Integer> collectables;
	private ArrayList<String> keys;
	
	public Model() {
		collectables = new LinkedHashMap<String,Integer>();
		keys = new ArrayList<String>();
	}
	
	public void setValue(String key, Integer value) {
		collectables.put(key, value); 
	}
	
	public Integer getValue(String key) {
		return collectables.get(key);
	}
	
	public LinkedHashMap<String, Integer> getCollectables() {
		return collectables;
	}
	
	public ArrayList<String> getKeyStrings() {
		return keys;
	}
	
	
	/**
	 * Populates LinkedHashMap & ArrayList from saved contents of collectables.txt file
	 */
	public void populateMapFromFile() {
		try {		
			
			FileInputStream input = new FileInputStream("src/collectables.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
			String keyValueText;
			while ((keyValueText = reader.readLine()) != null) {
				String[] keyValueTextArray = keyValueText.split(", ");
				String key = keyValueTextArray[0];
				Integer value = Integer.valueOf(keyValueTextArray[1]);
				collectables.put(key, value);
				keys.add(key);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Resets values for all items in LinkedHashMap to default state of not yet collected.
	 */
	public void resetMapToDefault() {
		collectables.entrySet().forEach(entry -> {
			collectables.put(entry.getKey(), 0);
		});
	}
	
	/**
	 * Writes contents of LinkedHashMap to collectables.txt file
	 */
	public void writeFileFromMap() {
		try {
			PrintWriter writer = new PrintWriter("src/collectables.txt", "UTF-8");
			collectables.entrySet().forEach(entry -> {
				writer.println(entry.getKey() + ", " + entry.getValue());
			});
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
}
