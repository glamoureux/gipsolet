package lamaro.gipsolet.activity;

import lamaro.gipsolet.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class ConfigMapActivity extends Activity implements OnItemSelectedListener {
	
	private static final int MENU_MAP = 0;
	
	private int camera_tilt = 0;
	
	private EditText editText1 = null;
	
	public enum MAP_TYPE {Normal, Hybrid, Satellite, Terrain, None};
	
	private MAP_TYPE map_type = MAP_TYPE.Normal;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config_map);
		
		camera_tilt = getIntent().getIntExtra("camera_tilt", camera_tilt);
		
		editText1 = (EditText)findViewById(R.id.editText1);
		editText1.setText("" + camera_tilt, EditText.BufferType.EDITABLE);
		
		Spinner choix = (Spinner) findViewById(R.id.choix);
		ArrayAdapter<MAP_TYPE> semaine = new ArrayAdapter<MAP_TYPE>(this, android.R.layout.simple_spinner_item, MAP_TYPE.values());
		semaine.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		choix.setOnItemSelectedListener(this);
		choix.setAdapter(semaine);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
    	menu.clear();
        menu.add(0, MENU_MAP, 0, "Map");
        
        getMenuInflater().inflate(R.menu.activity_config_map, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_MAP:
        	Intent intent = new Intent(this, MapActivity.class);
        	
        	camera_tilt = Integer.parseInt(editText1.getText().toString());
        	
        	intent.putExtra("camera_tilt", camera_tilt);
        	intent.putExtra("map_type", map_type);
        	
        	startActivity(intent);
        	
            return true;
        }
        return false;
    }

	@Override
	public void onItemSelected(AdapterView<?> parent, View vue, int position, long id) {
		map_type = MAP_TYPE.values()[position];
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
