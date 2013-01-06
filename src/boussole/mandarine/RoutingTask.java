package boussole.mandarine;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.PointF;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;

public class RoutingTask extends AsyncTask<PointF, Void, JSONObject> {

	RoutingResultHandler handler;
	
	public RoutingTask(RoutingResultHandler h) {
		super();
		
		handler = h;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();

	}

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected JSONObject doInBackground(PointF... params) {
		String start = params[0].y + "," + params[0].x;
		String end = params[1].y + "," + params[1].x;
		
		String url = Uri.parse("http://boussole.mandarine34.fr/api/7c2xrVH6AhnOqpKd179ioUPxar8IEcvqdtSFD0sJ/getRouting/").buildUpon()
			.appendQueryParameter("domain", "boussole.mandarine34.fr")
			.appendQueryParameter("start", start)
			.appendQueryParameter("end", end)
			.build().toString();
				
		HttpGet request = new HttpGet(url);
		
		JSONObject result = null;
		AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
		try {
			HttpResponse response = client.execute(request);
			result = new JSONObject(EntityUtils.toString(response.getEntity()));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			client.close();
		}
		
		return result;
	}

	@Override
	protected void onPostExecute(JSONObject result) {
		if (result == null) {
			return;
		}
		
		try {
			handler.processGeoJSONResult(result.getJSONObject("tc"));
			handler.processRoutingResult(result.getJSONObject("tc").getJSONArray("routes"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
