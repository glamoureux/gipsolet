package lamaro.gipsolet.data;

import lamaro.gipsolet.R;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.ResourceCursorAdapter;
import android.view.View;
import android.widget.TextView;

public class CEAdapter extends ResourceCursorAdapter {

	public CEAdapter(Context context, int layout, Cursor c, int flags) {
		super(context, layout, c, flags);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		TextView label = (TextView) view.findViewById(R.id.label);
		label.setText(cursor.getString(cursor.getColumnIndexOrThrow(Database.KEYWORDS_NAME)));

		Drawable icon = context.getResources().getDrawable(
				cursor.getInt(cursor.getColumnIndexOrThrow(Database.KEYWORDS_ICON)));
		icon.setBounds(0, 0, 48, 48);
		label.setCompoundDrawables(icon, null, null, null);
		
		TextView hideLabel = (TextView) view.findViewById(R.id.hideLabel);
		String hideStr = cursor.getString(cursor.getColumnIndexOrThrow(Database.KEYWORDS_TYPE));
		hideStr += "/" + cursor.getString(cursor.getColumnIndexOrThrow(Database.KEYWORDS_ID));		
		hideLabel.setText(hideStr);
	}

}
