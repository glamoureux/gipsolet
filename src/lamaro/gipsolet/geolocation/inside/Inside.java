package lamaro.gipsolet.geolocation.inside;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.location.Location;

import lamaro.gipsolet.data.Database;
import lamaro.gipsolet.geolocation.Geolocation;
import lamaro.gipsolet.geolocation.IGeolocation;
import lamaro.gipsolet.geolocation.IGeolocationListener;
import lamaro.gipsolet.model.Building;
import lamaro.gipsolet.model.Campus;

public class Inside implements IInside, IGeolocationListener {
	
	private static IInside inside = null;
	private IGeolocation geolocation = null;
	private Set<IInsideListener> insideListeners = null;
	private List<Building> buildings = null;
	
	private Inside() {
		geolocation = Geolocation.getInstance();
		geolocation.addListener(this);
	}

	public static IInside getInstance() {
		if(inside == null) {
			inside = new Inside();
		}
		
		return inside;
	}
	
	@Override
	public void setContext(Context context) {
		if(context != null) {
			if(insideListeners == null) {
				insideListeners = new HashSet<IInsideListener>();
			}
			buildings = new Database(context).getBuildings();
		}
	}

	@Override
	public void addListener(IInsideListener listener) {
		insideListeners.add(listener);
	}

	@Override
	public void removeListener(IInsideListener listener) {
		insideListeners.remove(listener);
	}

	@Override
	public void positionChanged(Location location) {
		boolean onCampus = Campus.onCampus(location);
		Building insideOfBuilding = null;
		System.out.println("HELLO");
		if (onCampus) {
			insideOfBuilding = null;
			for (Building building : buildings) {
				if (building.isInBuilding(location)) {
					insideOfBuilding = building;
					break;
				}
			}
		}
		
		for(IInsideListener listener: insideListeners) {
			System.out.println("PIPO");
			listener.insideStateChanged(onCampus, insideOfBuilding);
		}
	}

}
