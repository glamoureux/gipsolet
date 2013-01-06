package lamaro.gipsolet.model;

public class Service {
	public Integer id;
	public Number latitude;
	public Number longitude;
	public String description;
	public Building building;
	public Integer floor;
	
	@Override
	public String toString() {
		String result = id + "\n";
		result += latitude + "/" + longitude + "\n";
		result += "Etage " + floor + "\n";
		result += description + "\n";
		
		return result;
	}
}
