package com.college.collegeconnect.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.college.collegeconnect.R;

import java.util.ArrayList;
import java.util.List;

public class WorkerAdapter extends RecyclerView.Adapter<WorkerAdapter.ViewHolder> {

    private static final String LOGTAG = "WorkerAdapter";
    private ArrayList<String> options;
    private Context context;
    List worker_list;
    int checked_item = 0;

    public WorkerAdapter(ArrayList<String> options, Context context) {
        this.options = options;
        this.context = context;
    }

    @NonNull
    @Override
    public WorkerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.worker_card, parent, false);
        return new WorkerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkerAdapter.ViewHolder holder, int position) {
        checked_item = 0;
        Log.e(LOGTAG, "We binded some data");

        // We need to add the data to the holder object
        holder.workerDetail.setText(options.get(position));
        holder.positionId.setText(String.valueOf(position));
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {

            // This is what we do when the view is clicked
            @Override
            public void onClick(View v) {
                Log.e(LOGTAG, "We got a worker click");
                v.postDelayed(new Runnable() {

                    // This is what we do as our executed action
                    @Override
                    public void run() {
                        // We need to start an activity to edit a worker
                        Log.e(LOGTAG, "We ran something");
                        //context.startActivity(new Intent(context, (Class) worker_list.get(position)));
                    }
                }, 165);

            }
        });

    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView positionId;
        TextView workerDetail;
        View relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            positionId = itemView.findViewById(R.id.worker_position);
            workerDetail = itemView.findViewById(R.id.worker_detail);
            relativeLayout = itemView;
            worker_list = new ArrayList<>();
            worker_list.add(0);
        }
    }
}
