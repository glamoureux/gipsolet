package lamaro.gipsolet.data;

import java.util.List;

import lamaro.gipsolet.R;
import lamaro.gipsolet.model.CampusEntity;
import lamaro.gipsolet.model.Room;
import lamaro.gipsolet.model.Service;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CampusEntityAdapter extends BaseAdapter {
	private List<? extends CampusEntity> CEs;
	    	
	private Context context;
	    	
	private LayoutInflater inflater;
	
	public CampusEntityAdapter(Context context, List<? extends CampusEntity> list) {
		CEs = list;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return CEs.size();
	}

	@Override
	public CampusEntity getItem(int pos) {
		return CEs.get(pos);
	}

	@Override
	public long getItemId(int pos) {
		return getItem(pos).getId();
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		View rowView = inflater.inflate(R.layout.ce_search_item, viewGroup, false);
		TextView label = (TextView) rowView.findViewById(R.id.label);
		CampusEntity ce = (CampusEntity) getItem(position);
		System.out.println("CLASS : " + ce.getClass());
		if (ce.getClass().getName().contains("Building"))
			label.setText(context.getString(R.string.building) + " " + ce.getName());
		else if (ce.getClass().getName().contains("Service"))
			label.setText(((Service) ce).description);
		else
			label.setText(ce.getName());
		
//		Drawable icon = context.getResources().getDrawable(
//				cursor.getInt(cursor.getColumnIndexOrThrow(Database.KEYWORDS_ICON)));
//		icon.setBounds(0, 0, 48, 48);
//		label.setCompoundDrawables(icon, null, null, null);
		
		TextView hideLabel = (TextView) rowView.findViewById(R.id.hideLabel);
		String hideStr = ce.getClass().getSimpleName().toLowerCase();
		hideStr += "/" + ce.getId();		
		hideLabel.setText(hideStr);
		
		return rowView;
	}

}
