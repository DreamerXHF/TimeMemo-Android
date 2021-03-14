package com.example.timememo.Backlog;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timememo.Entity.Backlog;
import com.example.timememo.Entity.User;
import com.example.timememo.R;

import java.util.List;

public class BacklogAdapter extends RecyclerView.Adapter<BacklogAdapter.ViewHolder> {
    private Context mContext;
    private List<Backlog> mBacklogList;

    private MyDialogFragment myDialogFragment = null;

    private FragmentManager fm = null;

    private User muser = null;

    //创建ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView backlogContent;
        TextView backlogTime;
        TextView Type = null;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.backupCard);
            backlogContent = itemView.findViewById(R.id.backlogContent);
            backlogTime = itemView.findViewById(R.id.backlogTime);
            Type = itemView.findViewById(R.id.backlogType);

        }
    }


    //构造函数
    public BacklogAdapter(Context context, List<Backlog> backlogList,FragmentManager fm,User muser){
        mContext = context;
        mBacklogList = backlogList;
        this.fm = fm;
        this.muser = muser;
    }

    public void remove(int position) {
        mBacklogList.remove(position);
        notifyItemRemoved(position);
    }

    public void add(Backlog backlog, int position) {
        mBacklogList.add(position, backlog);
        notifyItemInserted(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.backlog_item,viewGroup,false);
      //  View view = View.inflate(mContext, R.layout.backlog_item, null);
        final ViewHolder holder = new ViewHolder(view);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Backlog backlog = mBacklogList.get(position);

                myDialogFragment = new MyDialogFragment();
                myDialogFragment.setBacklog(backlog);
                myDialogFragment.show(fm,"Backlog_dialog");
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Backlog backlog = mBacklogList.get(i);
        //设置卡片中的组件
        viewHolder.backlogContent.setText(backlog.getTitle());
        viewHolder.backlogTime.setText(backlog.getTime());
        viewHolder.cardView.setCardBackgroundColor(Color.parseColor(backlog.getRgb()));
        viewHolder.Type.setText(backlog.getBacklogType());
    }

    @Override
    public int getItemCount() {
        return mBacklogList.size();
    }

}
