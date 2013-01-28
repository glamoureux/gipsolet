package lamaro.gipsolet.geolocation.inside;

import android.content.Context;

public interface IInside {
	void setContext(Context context);
	void addListener(IInsideListener listener);
	void removeListener(IInsideListener listener);
}
