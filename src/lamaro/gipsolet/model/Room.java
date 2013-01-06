package lamaro.gipsolet.model;

public class Room {
	public Integer id;
	public Number latitude;
	public Number longitude;
	public Building building;
	public Integer floor;
	public int type;
	public String label;

	@Override
	public String toString() {
		String result = id + "\n";
		result += latitude + "/" + longitude + "\n";
		result += "Etage " + floor + "\n";
		result += label + "\n";
		result += type + "\n";
		
		return result;
	}
}