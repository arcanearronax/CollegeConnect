package com.college.collegeconnect.settingsActivity;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.college.collegeconnect.Notification;

import org.jetbrains.annotations.NotNull;

import java.sql.Date;
import java.sql.Time;

public class TrialWorker extends Worker {

    private static final String LOGTAG = "UploadWorker";
    private static final int MAXRETRIES = 1;
    private int retryCount = 0;

    public TrialWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public void onStopped() {

        Log.e(LOGTAG, "Stopping worker - " + getId());

    }

    @NotNull
    @Override
    public Result doWork() {

        // This is an old way to get notifications to appear
        //NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //Notification notify = new Notification(android.R.drawable.stat_notify_more,title,System.currentTimeMillis());

        // Assume we have a failure, unless told otherwise
        Result work_result = Result.failure();

        // Try to complete the work
        try {

            // This is the part that might throw an error
            Log.e(LOGTAG, "TrialWorker Run ");
            Notification.displayNotification(getApplicationContext(), "TrialWorker", "Message body");

            // Set the retry counter to zero and create a success
            work_result = Result.success();
        }

        // If we encounter an error, clean up and respond with a retry
        catch (Exception e) {

            // Clean up the attempt
            Log.wtf(LOGTAG, "Worker Run Failure");

            // Increment the counter and create a retry
            retryCount++;
            work_result = Result.retry();
        }

        // Regardless of what happens, we need to see what to do
        finally {

            // We return the result, whatever it may be
            return work_result;
        }

    }
}

