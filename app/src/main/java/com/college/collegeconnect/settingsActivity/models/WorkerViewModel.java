package com.college.collegeconnect.settingsActivity.models;

import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.ArrayList;
import java.util.List;

public class WorkerViewModel extends ViewModel {

    private MutableLiveData<ArrayList<String>> workerLiveData;
    private WorkManager workManager;
    private ArrayList<String> workerArrayList;

    public WorkerViewModel() {
        workerLiveData = new MutableLiveData<>();
        workerArrayList = new ArrayList<>();
    }

    public MutableLiveData<ArrayList<String>> getWorkerMutableLiveData() {
        if (workerLiveData == null) {
            loadWorkers();
            workerLiveData.setValue(workerArrayList);
        }
        return workerLiveData;
    }

    public void loadWorkers(){

        Handler myHandler = new Handler();
        myHandler.postDelayed(() -> {
            LiveData<List<WorkInfo>> workInfoLiveData = workManager.getWorkInfosByTagLiveData("cats");

            workerArrayList.add("test1");
            workerArrayList.add("test2");
            workerArrayList.add("test3");


        }, 5000);

    }

}
