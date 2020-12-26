package com.college.collegeconnect.settingsActivity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.college.collegeconnect.R;
import com.college.collegeconnect.adapters.WorkerAdapter;
//import com.college.collegeconnect.models.NotificationModel;

import java.util.Calendar;
import java.util.regex.Pattern;

public class WorkerActivity extends AppCompatActivity {

    private static final String LOGTAG = "WorkerActivity";

    //private NotificationModel notificationModel;
    private WorkerAdapter workerAdapter;

    private DatePickerDialog datePicker;
    private Calendar calendar;
    private int year;
    private int month;
    private int dayOfMonth;
    private TimePickerDialog timePicker;
    private int hour;
    private int minute;

    TextView classView;
    TextView startView;
    TextView endView;
    TextView day0View;
    TextView day1View;
    TextView day2View;
    TextView day3View;
    TextView day4View;
    TextView day5View;
    TextView day6View;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);

        Log.e(LOGTAG, "Creating WorkerActivity");

        // We create a toolbar, then add that to our layout
        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Update the elements in our toolbar to display some info we want
        TextView barTitle = findViewById(R.id.tvtitle);
        barTitle.setText("Schedule Work");

        // Now let's build our TextViews
        classView = findViewById(R.id.row_0_value);
        startView = findViewById(R.id.row_1_value);
        endView = findViewById(R.id.row_2_value);
        day0View = findViewById(R.id.day_0_value);
        day1View = findViewById(R.id.day_1_value);
        day2View = findViewById(R.id.day_2_value);
        day3View = findViewById(R.id.day_3_value);
        day4View = findViewById(R.id.day_4_value);
        day5View = findViewById(R.id.day_5_value);
        day6View = findViewById(R.id.day_6_value);

        // and connect our listeners to the various fields
        startView.setOnClickListener(getDateListener(startView));
        endView.setOnClickListener(getDateListener(endView));
        day0View.setOnClickListener(getTimeListener(day0View));
        day1View.setOnClickListener(getTimeListener(day1View));
        day2View.setOnClickListener(getTimeListener(day2View));
        day3View.setOnClickListener(getTimeListener(day3View));
        day4View.setOnClickListener(getTimeListener(day4View));
        day5View.setOnClickListener(getTimeListener(day5View));
        day6View.setOnClickListener(getTimeListener(day6View));

    }

    // This is a little helper to consume a CharSequence and returns a 2 character string representing a 2 digit number
    private String inflateValue(CharSequence charSeq) {
        if (charSeq.length() == 1) {
            return String.valueOf("0" + charSeq);
        }
        return String.valueOf(charSeq);
    }

    public final void createWork(View view) {
        // Let's just hide the error, in case we get a resubmit
        Log.d(LOGTAG, "start createWork");
        TextView warningText = findViewById(R.id.warning_worker_text);
        warningText.setVisibility(View.GONE);

        CharSequence className = classView.getText();
        CharSequence startDate = startView.getText();
        CharSequence endDate = endView.getText();
        Log.e(LOGTAG, "Got our dates");

        // We need to parse the date fields
        try {
            Pattern pattern = Pattern.compile("/");
            String[] endArray = pattern.split(endDate);
            String[] startArray = pattern.split(startDate);
            String endYear = inflateValue(endArray[2]);
            String endMonth = inflateValue(endArray[0]);
            String endDayOfMonth = inflateValue(endArray[1]);
            String startYear = inflateValue(startArray[2]);
            String startMonth = inflateValue(startArray[0]);
            String startDayOfMonth = inflateValue(startArray[1]);
            Log.e(LOGTAG, "Parsed our stuff");
        } catch (Exception e) {
            Log.e(LOGTAG, e.toString());
        }

        if (className.length() != 2 || startDate.length() != 2 || endDate.length() != 2) {
            Log.d(LOGTAG, "We're missing some data");
            warningText.setText("An error occurred when creating the worker.");
            warningText.setVisibility(View.VISIBLE);
        } else {
            Log.e(LOGTAG, "We got valid data.");
            // We need to account for some type of exception being thrown
            try {
                Log.e(LOGTAG, "Trying stuff");
                int classId = 1;

                /*
                // Create our notification
                notificationModel.addNotification(
                        classId,
                        Integer.parseInt(endYear),
                        Integer.parseInt(endMonth),
                        Integer.parseInt(endDayOfMonth),
                        Integer.parseInt(startYear),
                        Integer.parseInt(startMonth),
                        Integer.parseInt(startDayOfMonth)
                );
                 */

                /*
                // This is here to build periodic requests later
                PeriodicWorkRequest workReq = new PeriodicWorkRequest.Builder(
                        TrialWorker.class,
                        15,
                        TimeUnit.MINUTES,
                        5,
                        TimeUnit.MINUTES
                ).build();
                WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork("arcane_logging", ExistingPeriodicWorkPolicy.KEEP, workReq);
                */

                // This is here so we can build a one time request, for as needed usage
                OneTimeWorkRequest workReq = new OneTimeWorkRequest.Builder(TrialWorker.class).build();
                WorkManager.getInstance(getApplicationContext()).enqueue(workReq);

                workerAdapter.notifyDataSetChanged();

            } catch (Exception e) {
                // Make the error message visible
                Log.e(LOGTAG, e.toString());
                warningText.setText("An error occurred when creating the worker.");
                warningText.setVisibility(View.VISIBLE);

            } finally {
                Log.e(LOGTAG, "Just a test");
            }
        }
    }

    public final void deleteWork(View view) {
        // This is just here as a hack to remove existing jobs
        try {
            Context myContext = getApplicationContext();
            WorkManager.getInstance(myContext).cancelAllWork();
            Log.e(LOGTAG, "Cancelled all work");
        } catch (Exception e) {
            Log.e(LOGTAG, e.toString());
        }
    }

    private View.OnClickListener getTimeListener(TextView textView) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get our objects set
                calendar = Calendar.getInstance();
                hour = calendar.get(Calendar.HOUR_OF_DAY);
                minute = calendar.get(Calendar.MINUTE);
                // Create our picker
                timePicker = new TimePickerDialog(WorkerActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        textView.setText(String.format("%d:%s", hour, minute));
                    }
                }, year, month, true);
                timePicker.show();
            }
        };
    }

    private View.OnClickListener getDateListener(TextView textView) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get our objects set
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                // Create our picker
                datePicker = new DatePickerDialog(WorkerActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker datePicker, int year, int month, int day) {
                        textView.setText(String.format("%d/%d/%d", month + 1, day, year));
                    }
                }, year, month, dayOfMonth);
                datePicker.show();
            }
        };
    }
}