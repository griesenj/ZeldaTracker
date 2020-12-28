
/**
 * @author Jon Griesen (griesenj@mail.gvsu.edu)
 */

public class Driver {

	public static void main(String[] args) {
		
		Model zeldaModel = new Model();
		Controller zeldaController = new Controller(zeldaModel);
		new View(zeldaModel, zeldaController);
		
	}
	
}
