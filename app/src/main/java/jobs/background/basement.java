package jobs.background;

import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Trigger;

import java.text.DateFormat;
import java.util.Date;
import java.util.Random;
import android.content.Context;
import android.widget.Toast;
import fitcall.org.R;

public class basement extends JobService{
	private static final String TAG = "basement";

	@Override
	public boolean onStartJob(JobParameters jobParameters) {
		Context context = getApplicationContext();
		String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
	    .setSmallIcon(R.mipmap.icon)
	    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.icon))
        .setContentTitle(currentDateTimeString)
        .setContentText(currentDateTimeString)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        Random rand = new Random();
        int  notificationId = rand.nextInt(50) + 1;

        notificationManager.notify(notificationId, mBuilder.build());

		Log.d("basement","Invoked basement class");
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));

        Job myJob = dispatcher.newJobBuilder()
                .setService(basement.class)
                .setTag("scheduleCallBasement")
				.setTrigger(Trigger.executionWindow(10, 10))
                .build();

        dispatcher.mustSchedule(myJob);
		return true;
	}

	@Override
	public boolean onStopJob(JobParameters jobParameters) {
		Log.d(TAG, "Job cancelled!");

		return false;
	}
}