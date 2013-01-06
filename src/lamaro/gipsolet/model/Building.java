package lamaro.gipsolet.model;

import misc.Polygon;

public class Building {
	public Integer id;
	public Polygon zone;
	public Number latitude;
	public Number longitude;
	public Integer number;
	public String label;
	public String keywords;
	
	@Override
	public String toString() {
		String result = id + "\n";
		result += zone + "\n";
		result += latitude + "/" + longitude + "\n";
		result += "Batiment " + number + "\n";
		result += label + "\n";
		result += keywords + "\n";
		
		return result;
	}
}
