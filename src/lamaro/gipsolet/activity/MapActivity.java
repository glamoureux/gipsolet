package lamaro.gipsolet.activity;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import lamaro.gipsolet.R;
import lamaro.gipsolet.data.Database;
import lamaro.gipsolet.geolocation.Geolocation;
import lamaro.gipsolet.geolocation.IGeolocation;
import lamaro.gipsolet.geolocation.IGeolocationListener;
import lamaro.gipsolet.model.Building;
import lamaro.gipsolet.model.Campus;
import lamaro.gipsolet.model.ContainerEntity;
import lamaro.gipsolet.model.Room;
import lamaro.gipsolet.model.Service;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MapActivity extends FragmentActivity implements IGeolocationListener, OnMarkerClickListener,
		OnMapLongClickListener {

	private static final int MENU_CONFIG = 0;

	private IGeolocation geolocation = null;
	private Database db = null;
	private GoogleMap map = null;

	private Marker mobile = null;
	private Location currentMobileLocation = null;
	private List<Polygon> buildingsPolygons = null;

	private boolean refreshMap = true;
	private boolean refreshCampus = true;
	private boolean refreshMobile = false;
	private boolean refreshGoogleNavigation = false;
	private boolean refreshFirstCampusFocus = true;
	private boolean refreshMobileCampusFocus = false;
	private boolean refreshMobileFocusTransition = false;
	private boolean refreshMobileFocus = false;
	private boolean refreshBuildings = false;
	private boolean refreshResearchEntity = false;
	private boolean refreshRooms = false;
	private boolean refreshServices = false;

	private boolean onCampus = false;
	private boolean firstMobileCampusFocus = true;
	private boolean mobileFocus = false;
	private List<Boolean> buildingsFocus = null;
	private Integer researchBuildingId = 0;
	private Integer researchRoomId = 0;
	private Integer researchServiceId = 0;

	private boolean setMobileFocus = true;

	private int camera_tilt = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.map);

		if (getIntent().hasExtra("building")) {
			researchBuildingId = getIntent().getIntExtra("building", 0);
		}
		if (getIntent().hasExtra("room")) {
			researchRoomId = getIntent().getIntExtra("room", 0);
		}
		if (getIntent().hasExtra("service")) {
			researchServiceId = getIntent().getIntExtra("service", 0);
		}
		if (getIntent().hasExtra("camera_tilt")) {
			camera_tilt = getIntent().getIntExtra("camera_tilt", camera_tilt);
		}

		geolocation = Geolocation.getInstance();
		if (geolocation != null) {
			geolocation.addListener(this);
		}

		db = new Database(this);

		if (db != null) {
			buildingsFocus = new ArrayList<Boolean>();
			for (int i = 0; i < db.getBuildings().size(); i++) {
				buildingsFocus.add(false);
			}
		}

		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
		if (map != null) {
			map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			map.setOnMarkerClickListener(this);
			map.setOnMapLongClickListener(this);
			map.setOnCameraChangeListener(new OnCameraChangeListener() {

				@Override
				public void onCameraChange(CameraPosition arg0) {
					drawMap();
					map.setOnCameraChangeListener(null);
				}
			});
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		menu.clear();
		menu.add(0, MENU_CONFIG, 0, "Configuration");

		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_CONFIG:
			Intent intent = new Intent(this, ConfigMapActivity.class);
			intent.putExtra("camera_tilt", camera_tilt);

			startActivity(intent);

			return true;
		case R.id.go_home:
			startActivity(new Intent(this, MainActivity.class));
			return true;
		}
		
		return false;
	}

	private void drawMap() {
		if (map != null) {
			if (refreshMap) {
				map.clear();
			}
			if (refreshCampus) {
				drawCampus();
				refreshCampus = !refreshCampus;
			}
			if (refreshMobile) {
				drawMobile();
				refreshMobile = !refreshMobile;
			}
			if (refreshGoogleNavigation) {
				drawGoogleNavigation();
				refreshGoogleNavigation = !refreshGoogleNavigation;
			}
			if (refreshFirstCampusFocus) {
				drawFirstCampusFocus();
				refreshFirstCampusFocus = !refreshFirstCampusFocus;
			}
			if (refreshMobileCampusFocus) {
				drawMobileCampusFocus();
				refreshMobileCampusFocus = !refreshMobileCampusFocus;
			}
			if (refreshMobileFocusTransition) {
				drawMobileFocusTransition();
				refreshMobileFocusTransition = !refreshMobileFocusTransition;
			}
			if (refreshMobileFocus) {
				drawMobileFocus();
				if (!mobileFocus) {
					refreshMobileFocus = !refreshMobileFocus;
				}
			}
			if (refreshBuildings) {
				drawBuildings();
				refreshBuildings = !refreshBuildings;
			}
			if (refreshResearchEntity) {
				drawResearchEntity();
				refreshResearchEntity = !refreshResearchEntity;
			}
			if (refreshRooms) {
				drawRooms();
				refreshRooms = !refreshRooms;
			}
			if (refreshServices) {
				drawServices();
				refreshServices = !refreshServices;
			}

			refreshMap = false;
		}
	}

	private void drawCampus() {
		if (map != null) {
			if (refreshMap) {
				map.addPolygon(Campus.coordinates.fillColor(Color.argb(128, 128, 0, 0))
						.strokeColor(Color.argb(128, 255, 0, 0)).strokeWidth(5.0f));
				map.addMarker(new MarkerOptions().position(Campus.latlng).title("Campus Triolet")
						.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)).anchor(0.5f, 0.5f));
			}
		}
	}

	private void drawMobile() {
		if (map != null) {
			if (mobile == null) {
				mobile = map.addMarker(new MarkerOptions().position(new LatLng(0.0, 0.0)).icon(
						BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
			}

			if (refreshMap) {
				MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(0.0, 0.0));

				if (mobileFocus) {
					markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
				} else {
					markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
				}

				mobile = map.addMarker(markerOptions);
			}

			mobile.setPosition(new LatLng(currentMobileLocation.getLatitude(), currentMobileLocation.getLongitude()));
		}
	}

	private void drawGoogleNavigation() {
		if (map != null) {

		}
	}

	private void drawFirstCampusFocus() {
		if (map != null) {
			Builder builder = new LatLngBounds.Builder();

			for (LatLng latlng : Campus.coordinates.getPoints()) {
				builder.include(latlng);
			}
			map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 20), new CancelableCallback() {

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder(map
							.getCameraPosition()).tilt(camera_tilt).build()));
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub

				}
			});

			map.setOnCameraChangeListener(null);
		}
	}

	private void drawMobileCampusFocus() {
		if (map != null) {
			Builder builder = new LatLngBounds.Builder();

			for (LatLng latlng : Campus.coordinates.getPoints()) {
				builder.include(latlng);
			}

			builder.include(mobile.getPosition());

			map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 20), new CancelableCallback() {

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder(map
							.getCameraPosition()).tilt(camera_tilt).build()));
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub

				}
			});
		}
	}

	private void drawMobileFocusTransition() {
		if (map != null) {
			if (mobile != null) {
				map.getUiSettings().setScrollGesturesEnabled(false);
				map.animateCamera(CameraUpdateFactory.newLatLngZoom(mobile.getPosition(), 18.0f),
						new CancelableCallback() {

							@Override
							public void onFinish() {
								// TODO Auto-generated method stub
								map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder(map
										.getCameraPosition()).tilt(camera_tilt).build()));
							}

							@Override
							public void onCancel() {
								// TODO Auto-generated method stub

							}
						});
			}
		}
	}

	private void drawMobileFocus() {
		if (map != null) {
			if (mobile != null) {
				map.animateCamera(CameraUpdateFactory.newLatLng(mobile.getPosition()), new CancelableCallback() {

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						map.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder(map
								.getCameraPosition()).tilt(camera_tilt).build()));
					}

					@Override
					public void onCancel() {
						// TODO Auto-generated method stub

					}
				});
			}
		}
	}

	private void drawBuildings() {
		if (map != null) {
			if (db != null) {
				if (refreshMap) {
					if (buildingsPolygons == null) {
						buildingsPolygons = new ArrayList<Polygon>();
					}
					buildingsPolygons.clear();

					for (Building building : db.getBuildings()) {
						if (!building.getId().equals(researchBuildingId)) {
							Polygon polygon = map.addPolygon(new PolygonOptions().addAll(building.getShape())
									.fillColor(Color.argb(128, 0, 176, 240)).strokeColor(Color.argb(128, 0, 64, 255))
									.strokeWidth(2.0f));
							map.addMarker(new MarkerOptions().position(building.getLatLng()).title(building.getName())
									.icon(BitmapDescriptorFactory.fromResource(R.drawable.blue_markerb)));

							buildingsPolygons.add(polygon);
						} else {
							Polygon polygon = map.addPolygon(new PolygonOptions().addAll(building.getShape())
									.fillColor(Color.argb(0, 255, 255, 255)).strokeColor(Color.argb(0, 255, 255, 255))
									.strokeWidth(0.0f));

							buildingsPolygons.add(polygon);
						}
					}
				}
			}
		}
	}

	private void drawResearchEntity() {
		if (map != null) {
			if (researchBuildingId != 0) {
				Building building = db.getBuildingById(researchBuildingId);

				if (building != null) {
					map.addPolygon(new PolygonOptions().addAll(building.getShape())
							.fillColor(Color.argb(128, 128, 0, 0)).strokeColor(Color.argb(128, 255, 0, 0))
							.strokeWidth(2.0f));
					map.addMarker(new MarkerOptions().position(building.getLatLng()).title(building.getName())
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.red_markerb)));
				}
			}
			if (researchRoomId != 0) {
				Room room = db.getRoomById(researchRoomId);

				if (room != null) {
					map.addMarker(new MarkerOptions().position(room.getLatLng()).title(room.getName())
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.red_markerc)));
				}
			}
			if (researchServiceId != 0) {
				Service service = db.getServiceById(researchServiceId);

				if (service != null) {
					map.addMarker(new MarkerOptions().position(service.getLatLng()).title(service.getName())
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.red_markers)));
				}
			}
		}
	}

	private void drawRooms() {
		if (map != null) {
			if (db != null) {
				if (refreshMap) {
					for (int i = 0; i < db.getBuildings().size(); i++) {
						if (buildingsFocus.get(i)) {
							List<Room> rooms = db.getRoomsOfBuilding(db.getBuildings().get(i));

							for (Room room : rooms) {
								if (!room.getId().equals(researchRoomId)) {
									map.addMarker(new MarkerOptions().position(room.getLatLng()).title(room.getName())
											.icon(BitmapDescriptorFactory.fromResource(R.drawable.purple_markerc)));
								}
							}
						}
					}
				}
			}
		}
	}

	private void drawServices() {
		if (map != null) {
			if (db != null) {
				if (refreshMap) {
					for (int i = 0; i < db.getBuildings().size(); i++) {
						if (buildingsFocus.get(i)) {
							List<Service> services = db.getServicesOfBuilding(db.getBuildings().get(i));

							for (Service service : services) {
								if (!service.getId().equals(researchServiceId)) {
									map.addMarker(new MarkerOptions().position(service.getLatLng())
											.title(service.getName())
											.icon(BitmapDescriptorFactory.fromResource(R.drawable.yellow_markers)));
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void positionChanged(Location location) {
		if (map != null) {
			currentMobileLocation = location;

			if (firstMobileCampusFocus) {
				firstMobileCampusFocus = !firstMobileCampusFocus;
				refreshMobileCampusFocus = true;
			}

			if (onCampus != Campus.onCampus(location)) {
				onCampus = Campus.onCampus(location);
				if (onCampus) {
					refreshMap = true;
					refreshResearchEntity = true;
					refreshBuildings = true;
					refreshRooms = true;
					refreshServices = true;
					refreshMobile = true;

					refreshMobileFocusTransition = true;
					mobileFocus = true;
				} else {
					if (refreshMap) {
						refreshCampus = true;
						refreshGoogleNavigation = true;
					}
					refreshMobile = true;
				}
			} else {
				if (onCampus) {
					if (refreshMap) {
						refreshResearchEntity = true;
						refreshBuildings = true;
						refreshRooms = true;
						refreshServices = true;
					}
					refreshMobile = true;
				} else {
					if (refreshMap) {
						refreshCampus = true;
						refreshGoogleNavigation = true;
					}
					refreshMobile = true;
				}
			}

			drawMap();
		}
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if (map != null) {
			if (mobile != null) {
				if (marker.equals(mobile)) {
					if (setMobileFocus) {
						refreshMap = true;
						refreshMobileFocus = true;
						mobileFocus = !mobileFocus;
						if (mobileFocus) {
							map.getUiSettings().setScrollGesturesEnabled(false);
						} else {
							map.getUiSettings().setScrollGesturesEnabled(true);
						}
						positionChanged(currentMobileLocation);
					}
				}
			}
		}
		return false;
	}

	@Override
	public void onMapLongClick(LatLng latlng) {
		if (map != null) {
			if (onCampus) {
				if (buildingsPolygons != null) {
					for (int i = 0; i < buildingsPolygons.size(); i++) {
						if (ContainerEntity.contains(latlng, buildingsPolygons.get(i).getPoints())) {
							buildingsFocus.set(i, !buildingsFocus.get(i));
							refreshMap = true;
							positionChanged(currentMobileLocation);
							return;
						}
					}
				}
			}
		}
	}
}