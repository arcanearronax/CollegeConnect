package com.college.collegeconnect.settingsActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.college.collegeconnect.DividerItemDecoration;
import com.college.collegeconnect.R;
import com.college.collegeconnect.adapters.WorkerAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class WorkManagerActivity extends AppCompatActivity {

    private static final String LOGTAG = "WorkerManagerActivity";
    private RecyclerView recyclerView;
    private WorkerAdapter workerAdapter;
    ArrayList<String> menu_options;
    private static final int REQUEST_MANAGER = 0;
    private WorkManager workManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(LOGTAG, "Creating WorkManagerActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_manager);

        // We create a toolbar, then add that to our layout
        Toolbar toolbar = findViewById(R.id.toolbarcom);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Update the elements in our toolbar to display some info we want
        TextView barTitle = findViewById(R.id.tvtitle);
        barTitle.setText("Work Manager");

        // Here we just set the worker TextView
        TextView viewText = findViewById(R.id.textView_worker);
        viewText.setText("Tap here to create a new notification worker");

        viewText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Just launch a new activity
                    startActivityForResult(new Intent(WorkManagerActivity.this, WorkerActivity.class), REQUEST_MANAGER);
                }
            }
        );


        // Now we create our Recycler
        recyclerView = findViewById(R.id.recycler_worker);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Build the data we provide to our Adapter
        // TODO: We need to get a list of currently active notification workers for the user
        menu_options = new ArrayList<>();
        menu_options.add("Test0");
        menu_options.add("Test1");
        menu_options.add("Test2");
        workManager = WorkManager.getInstance();

        // Create and connect our Adapter
        workerAdapter = new WorkerAdapter(menu_options, this);
        recyclerView.setAdapter(workerAdapter);

        // Decorate our recycler
        DividerItemDecoration decoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST, 80, 0);
        recyclerView.addItemDecoration(decoration);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_MANAGER) {
            Log.e(LOGTAG, "Got a manager request");
            if (resultCode == RESULT_OK) {
                Log.e(LOGTAG, "Got a valid request");
                menu_options.add("Test3");
                workerAdapter.notifyDataSetChanged();
            }
        }
    }

    private void getMenuOptions(String tag) {
        workManager.getWorkInfosByTag(tag);
    }
}