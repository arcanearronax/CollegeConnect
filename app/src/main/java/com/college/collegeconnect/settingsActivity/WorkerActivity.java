package com.college.collegeconnect.settingsActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.college.collegeconnect.DividerItemDecoration;
import com.college.collegeconnect.R;
import com.college.collegeconnect.adapters.WorkerAdapter;

import java.util.ArrayList;

public class WorkerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WorkerAdapter workerAdapter;

    private static final String LOGTAG = "AboutActivity";

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
        barTitle.setText("About");
        TextView viewText = findViewById(R.id.textView_worker);
        viewText.setText("Hello Friend, this is just a test");

        // Now we bind our WorkerRecycler
        recyclerView = findViewById(R.id.recycler_worker);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Put the items to go in the recycler in a list
        ArrayList<String> menu_options = new ArrayList<>();
        menu_options.add("Test0");
        menu_options.add("Test1");
        menu_options.add("Test2");

        // Create and connect our WorkerAdapter
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
}