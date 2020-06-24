package com.bhsd.bustayo.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.R;

import java.util.ArrayList;

public class ComplaintRecyclerAdapter extends RecyclerView.Adapter<ComplaintRecyclerAdapter.ItemViewHolder>{
    private ArrayList<String[]>    data;
    private Context                context;

    class ItemViewHolder extends RecyclerView.ViewHolder{
        TextView         complaintListItem;
        ConstraintLayout complaintListBody;

        ItemViewHolder(View view){
            super(view);
            complaintListItem = view.findViewById(R.id.complaintListItem);
            complaintListBody = view.findViewById(R.id.complaintListBody);
        }
    }

    public ComplaintRecyclerAdapter(ArrayList<String[]> data){
        this.data = data;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_complaint_item, parent, false);
        context   = parent.getContext();

        return new ComplaintRecyclerAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        final String busNum  = data.get(position)[0];
        final String content = data.get(position)[1];
        String title         = data.get(position)[2];

        holder.complaintListItem.setText(title);
        holder.complaintListBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("버스번호 : " + busNum + "\n" + "접수내용 : " + content);
                dialog.setNegativeButton("닫기", new DialogInterface.OnClickListener() {@Override public void onClick(DialogInterface dialog, int which) {}});
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}