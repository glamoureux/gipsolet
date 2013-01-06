package boussole.mandarine;

import org.json.JSONArray;
import org.json.JSONObject;

public interface RoutingResultHandler {
	void processGeoJSONResult(JSONObject json);
	
	void processRoutingResult(JSONArray routes);
}
