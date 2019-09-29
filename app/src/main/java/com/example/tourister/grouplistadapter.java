package com.example.tourister;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class grouplistadapter extends RecyclerView.Adapter<grouplistadapter.grouplistviewholder> {
    List<groupmodel> grouplist;
    Context context;
    OnItemClickListener onItemClickListener;
    LinearLayout linearLayout;
    public interface  OnItemClickListener {
        void onItemClick(int position,List<groupmodel> grouplist);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener=onItemClickListener;
    }
    public grouplistadapter(List<groupmodel> grouplist, Context context) {
        this.grouplist = grouplist;
        this.context = context;
    }
    @NonNull
    @Override
    public grouplistadapter.grouplistviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.groupnamelayout,parent, false);
        return new grouplistviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull grouplistadapter.grouplistviewholder holder, final int position) {
        holder.groupname.setText(grouplist.get(position).getGroupname());
        //holder.owepayment.setText("You Owe "+grouplist.get(position).getOwepayment());
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null)
                {
                    onItemClickListener.onItemClick(position,grouplist);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return grouplist.size();
    }

    public class grouplistviewholder extends RecyclerView.ViewHolder {
        TextView groupname,owepayment;
        public grouplistviewholder(@NonNull View itemView) {
            super(itemView);
            groupname = itemView.findViewById(R.id.groupname);
//            owepayment = itemView.findViewById(R.id.owepayment);
            linearLayout=itemView.findViewById(R.id.groupnamelayout);
        }
    }
}
