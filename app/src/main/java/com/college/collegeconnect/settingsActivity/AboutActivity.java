package com.college.collegeconnect.settingsActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.college.collegeconnect.Notification;
import com.college.collegeconnect.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class AboutActivity extends AppCompatActivity {

    private StringBuilder text = new StringBuilder();
    ImageView imageView;
    private static final String LOGTAG = "AboutActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Log.e(LOGTAG, "Creating About Activity");

        // This is just a hack to get a work manager proof of concept
        try {
            Log.e(LOGTAG, "Attempted to create work");
            PeriodicWorkRequest workReq = new PeriodicWorkRequest.Builder(TrialWorker.class, 15, TimeUnit.MINUTES, 5, TimeUnit.MINUTES).build();
            //OneTimeWorkRequest workReq = new OneTimeWorkRequest.Builder(TrialWorker.class).build();
            WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork("arcane_logging", ExistingPeriodicWorkPolicy.KEEP, workReq);
            //WorkManager.getInstance(getApplicationContext()).enqueue(workReq);

        } catch (Exception e) {
            Log.e(LOGTAG, e.toString());
        }

        // This is just a hack to trigger a notification
        //NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //Notification notify = new Notification(android.R.drawable.stat_notify_more,title,System.currentTimeMillis());
        //Notification.displayNotification(getApplicationContext(), "Testing", "Alert");


        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView tv = findViewById(R.id.tvtitle);
        tv.setText("About");
        imageView = findViewById(R.id.imageView13);
        int nightModeFlags =
                getResources().getConfiguration().uiMode &
                        Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO)
            imageView.setImageDrawable(getDrawable(R.drawable.cc2));

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(this.getAssets().open("about_text.txt")));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                text.append(mLine);
                text.append('\n');
            }
        } catch (IOException e) {
            Toast.makeText(AboutActivity.this, "Error reading file!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }

            EditText output = findViewById(R.id.about_text);
            output.setText(text);
            output.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
