package lamaro.gipsolet.activity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.google.android.gms.maps.model.PolygonOptions;

import lamaro.gipsolet.R;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.View;

public class MapActivity extends FragmentActivity {
	
	private GoogleMap map;
	
	PolygonOptions campus = new PolygonOptions()
		.add(
			new LatLng(43.629614f, 3.867531f),
			new LatLng(43.62963f, 3.869998f),
			new LatLng(43.634755f, 3.86811f),
			new LatLng(43.634056f, 3.863583f),
			new LatLng(43.635578f, 3.861523f),
			new LatLng(43.635066f, 3.859849f),
			new LatLng(43.631043f, 3.860621f))
		.fillColor(Color.argb(128, 255, 0, 0))
		.strokeWidth(1.0f);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        handleIntent();
        
        System.out.println(ConnectionResult.SUCCESS);
        System.out.println(ConnectionResult.SERVICE_MISSING);
        System.out.println(ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED);
        System.out.println(ConnectionResult.SERVICE_DISABLED);
        System.out.println(ConnectionResult.SERVICE_INVALID);
        System.out.println("=> " + GooglePlayServicesUtil.isGooglePlayServicesAvailable(this));
        
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        map.addPolygon(campus);
        map.setOnCameraChangeListener(new OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition arg0) {
            	Builder builder = new LatLngBounds.Builder();
        		for(LatLng latlng : campus.getPoints()) {
        			builder.include(latlng);
        		}
            	map.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 30));
                map.setOnCameraChangeListener(null);
            }
        });
    }
    
    private void handleIntent() {
		Intent intent = getIntent();
    }
}
