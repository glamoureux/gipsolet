package lamaro.gipsolet.sqlite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lamaro.gipsolet.R;
import lamaro.gipsolet.model.CampusEntity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CampusEntitiesAdapter extends BaseAdapter {
	private List<CampusEntity> source;
	private Context context;
	private int layoutId;
	
	public CampusEntitiesAdapter(Context context, int layoutId) {
		this.source = new ArrayList<CampusEntity>();
		this.context = context;
		this.layoutId = layoutId;
	}
	
	public void add(CampusEntity ce) {
		source.add(ce);
		notifyDataSetChanged();
	}
	
	public void addAll(Collection<CampusEntity> c) {
		source.addAll(c);
		notifyDataSetChanged();
	}
	
	public void set(Collection<CampusEntity> c) {
		source.clear();
		source.addAll(c);
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CampusEntityHolder holder = null;

		if (convertView == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			convertView = inflater.inflate(layoutId, parent, false);

			holder = new CampusEntityHolder();
			holder.label = (TextView) convertView.findViewById(R.id.label);

			convertView.setTag(holder);
		} else {
			holder = (CampusEntityHolder) convertView.getTag();
		}

		CampusEntity entity = getItem(position);
		holder.label.setText(entity.getName());

		return convertView;
	}

	@Override
	public int getCount() {
		return (source != null) ? source.size() : 0;
	}

	@Override
	public CampusEntity getItem(int position) {
		return (source != null) ? source.get(position) : null;
	}

	@Override
	public long getItemId(int position) {
		return position;

	}

//	@Override
//	public boolean hasStableIds() {
//		return false;
//	}

//	@Override
//	public int getItemViewType(int position) {
//		return IGNORE_ITEM_VIEW_TYPE;
//	}
//
//	@Override
//	public int getViewTypeCount() {
//		return 1;
//	}
	public static class CampusEntityHolder {
		public TextView label;
	}
}
