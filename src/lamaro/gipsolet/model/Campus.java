package lamaro.gipsolet.model;

import java.util.List;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

public class Campus {

	public static String name;
	public static LatLng latlng = new LatLng(43.632643, 3.864352);
	public static PolygonOptions coordinates = new PolygonOptions().add(
			new LatLng(43.630966, 3.861495), new LatLng(43.631241, 3.862241),
			new LatLng(43.630743, 3.865948), new LatLng(43.630534, 3.866543),
			new LatLng(43.630208, 3.867026), new LatLng(43.629774, 3.867235),
			new LatLng(43.629878, 3.867573), new LatLng(43.629845, 3.867793),
			new LatLng(43.630005, 3.868184), new LatLng(43.630449, 3.868045),
			new LatLng(43.630883, 3.868978), new LatLng(43.631137, 3.869),
			new LatLng(43.632681, 3.867922), new LatLng(43.633228, 3.867852),
			new LatLng(43.634408, 3.86805), new LatLng(43.634606, 3.867959),
			new LatLng(43.63465, 3.867761), new LatLng(43.634633, 3.866795),
			new LatLng(43.633937, 3.864038), new LatLng(43.63386, 3.863646),
			new LatLng(43.634014, 3.863153), new LatLng(43.634376, 3.862584),
			new LatLng(43.634796, 3.862133), new LatLng(43.634253, 3.860063),
			new LatLng(43.633286, 3.860326), new LatLng(43.631989, 3.860417),
			new LatLng(43.631533, 3.860275), new LatLng(43.631684, 3.859629),
			new LatLng(43.630951, 3.859792), new LatLng(43.630895, 3.8599),
			new LatLng(43.63076, 3.85987), new LatLng(43.630702, 3.859945),
			new LatLng(43.630548, 3.860232), new LatLng(43.630696, 3.860425),
			new LatLng(43.630691, 3.860535), new LatLng(43.630825, 3.860535),
			new LatLng(43.630825, 3.860535), new LatLng(43.630817, 3.861337));

	public static boolean onCampus(Location location) {
		boolean onCampus = false;
		List<LatLng> points = coordinates.getPoints();

		if (location != null) {
			for (int i = 0, j = points.size() - 1; i < points.size(); j = i++) {
				if ((points.get(i).longitude < location.getLongitude() && points
						.get(j).longitude >= location.getLongitude())
						|| (points.get(j).longitude < location.getLongitude() && points
								.get(i).longitude >= location.getLongitude())) {
					if (points.get(i).latitude
							+ (location.getLongitude() - points.get(i).longitude)
							/ (points.get(j).longitude - points.get(i).longitude)
							* (points.get(j).latitude - points.get(i).latitude) < location
								.getLatitude()) {
						onCampus = !onCampus;
					}
				}
			}
		}

		return onCampus;
	}
}
