
/**
 * @author Jon Griesen (griesenj@mail.gvsu.edu)
 */

public class Controller {

	private Model model;
	
	public Controller(Model model) {
		this.model = model;
	}
	
	public void incrementValueTwoStates(String key) {
		Integer value = model.getValue(key);
		
		if (value == 0) {
			value = 1;
		} else {
			value = 0;
		}
		model.setValue(key, value);
	}
	
	public void incrementValueThreeStates(String key) {
		Integer value = model.getValue(key);
		
		if (value == 0) {
			value = 1;
		} else if (value == 1) {
			value = 2;
		} else {
			value = 0;
		}
		model.setValue(key, value);
	}
	
	public void incrementValueFourStates(String key) {
		Integer value = model.getValue(key);
		
		if (value == 0) {
			value = 1;
		} else if (value == 1) {
			value = 2;
		} else if (value == 2) {
			value = 3;
		} else {
			value = 0;
		}
		model.setValue(key, value);
	}
	
	public void populateMap() {
		model.populateMapFromFile();
	}
	
	public void resetMap() {
		model.resetMapToDefault();
		model.writeFileFromMap();
	}
	
	public void writeFile() {
		model.writeFileFromMap();
		model.populateMapFromFile();
	}
	
}