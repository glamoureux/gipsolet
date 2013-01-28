package lamaro.gipsolet.activity;

import lamaro.gipsolet.R;
import lamaro.gipsolet.R.layout;
import lamaro.gipsolet.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class ConfigMapActivity extends Activity {
	
	private static final int MENU_MAP = 0;
	
	private int camera_tilt = 0;
	
	private EditText editText1 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config_map);
		
		camera_tilt = getIntent().getIntExtra("camera_tilt", camera_tilt);
		
		editText1 = (EditText)findViewById(R.id.editText1);
		editText1.setText("" + camera_tilt, EditText.BufferType.EDITABLE);
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
        	
        	startActivity(intent);
        	
            return true;
        }
        return false;
    }

}
