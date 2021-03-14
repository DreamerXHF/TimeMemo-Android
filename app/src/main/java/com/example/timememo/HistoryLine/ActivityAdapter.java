package com.example.timememo.HistoryLine;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.timememo.Entity.Activity;
import com.example.timememo.R;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private List<Activity> mActivityList = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView startTime = null;
        private TextView endTime = null;

        private TextView activityTitle = null;
        private TextView activityTime = null;
        private TextView activityState = null;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            startTime = itemView.findViewById(R.id.startTime);
            endTime = itemView.findViewById(R.id.endTime);
            activityTitle = itemView.findViewById(R.id.activityTitle);
            activityTime = itemView.findViewById(R.id.activityTime);
            activityState = itemView.findViewById(R.id.activityState);
        }
    }

    public ActivityAdapter(List<Activity> activitys){
        this.mActivityList = activitys;
    }

    @NonNull
    @Override
    public ActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_item,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityAdapter.ViewHolder viewHolder, int i) {
        Activity activity = mActivityList.get(i);
        float time = (float) (Integer.parseInt(activity.getaTime())/60.0);
        BigDecimal bd = new BigDecimal(time);
        time = bd.setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
        viewHolder.startTime.setText(activity.getaStartTime());
        viewHolder.endTime.setText(activity.getaEndTime());
        viewHolder.activityState.setText(activity.getaState());
        viewHolder.activityTitle.setText(activity.getaTitle());
        viewHolder.activityTime.setText(String.valueOf(time));

    }

    @Override
    public int getItemCount() {
        return mActivityList.size();
    }
}
