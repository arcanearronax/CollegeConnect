package com.college.collegeconnect.settingsActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.college.collegeconnect.R;

import java.util.Calendar;

public class WorkerActivity extends AppCompatActivity {

    private DatePickerDialog datePicker;
    private Calendar calendar;
    private int year;
    private int month;
    private int dayOfMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker);

        // Update the elements in our toolbar to display some info we want
        TextView barTitle = findViewById(R.id.tvtitle);
        barTitle.setText("Create Work");

        /*
         * TODO: we'll need to capture...
         *   *Class schedule pattern, assuming weekly recurrence
         *   *End time of the class for each day
         *   *Start date of the class schedule pattern
         *   *End date of the class schedule pattern
         *   *Name of the class
         */

        // Now here we build a listener to add a class name
        EditText classView = findViewById(R.id.row_0_value);

        // And take care of the start date selector
        TextView startView = findViewById(R.id.row_1_value);

        startView.setOnClickListener( new View.OnClickListener() {
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
                        startView.setText((month + 1) + "/" + day + "/" + year);
                    }
                }, year, month, dayOfMonth);
                datePicker.show();
            }
        });

    }
}