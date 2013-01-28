package lamaro.gipsolet.model;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.model.LatLng;

public class ContainerEntity {

	public static boolean contains(LatLng latlng, List<LatLng> shape) {
		boolean in = false;

		if(latlng != null) {
			for (int i = 0, j = shape.size() - 1; i < shape.size(); j = i++) {
				if ((shape.get(i).longitude < latlng.longitude && shape.get(j).longitude >= latlng.longitude)
					|| (shape.get(j).longitude < latlng.longitude && shape.get(i).longitude >= latlng.longitude)) {
					if (shape.get(i).latitude
						+ (latlng.longitude - shape.get(i).longitude)
						/ (shape.get(j).longitude - shape.get(i).longitude)
						* (shape.get(j).latitude - shape.get(i).latitude) < latlng.latitude) {
						in = !in;
					}
				}
			}
		}
		
		return in;
	}
	
	public static List<LatLng> stringToShape(String string) {
		List<LatLng> shape = new ArrayList<LatLng>();
		String[] stringPoints = string.split(",");

		for (String stringPoint: stringPoints) {
			String[] stringCoords = stringPoint.split(" ");
			shape.add(new LatLng(Float.parseFloat(stringCoords[0]), Float.parseFloat(stringCoords[1])));
		}

		return shape;
	}
}