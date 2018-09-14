package jobs.background;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.widget.Toast;
import android.content.Context;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;


public class backgroundJobs extends CordovaPlugin {
    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {
    	Context context = cordova.getContext();
    	 String name = data.getString(0);

        Toast.makeText(context,name,Toast.LENGTH_SHORT).show();
        callbackContext.success(name);
        Bundle textParams = new Bundle();
		textParams.putString("textToBeShown", "drink some water");
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));

        Job myJob = dispatcher.newJobBuilder()
		    .setService(basement.class)
		    .setTag("scheduleCallBasement")
//		    .setRecurring(true)
//		    .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
		    .setTrigger(Trigger.executionWindow(10, 10))
//		    .setReplaceCurrent(false)
//		    .setExtras(textParams)
		    .build();



		dispatcher.mustSchedule(myJob);
        return false;
    }
}