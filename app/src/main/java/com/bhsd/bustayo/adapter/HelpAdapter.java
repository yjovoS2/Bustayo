package com.bhsd.bustayo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bhsd.bustayo.R;
import com.bhsd.bustayo.dto.HelpItem;

import java.util.ArrayList;

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.ItemViewHolder> {

    private ArrayList<HelpItem> items;

    public HelpAdapter() {
        items = new ArrayList<>();
    }

    public HelpAdapter(ArrayList<HelpItem> helpItems) {
        items = helpItems;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_notice, parent, false);
        return new ItemViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.onBind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(HelpItem item) {
        items.add(item);
    }

    public ArrayList<HelpItem> getItems() {
        return items;
    }

    public void clearList() {
        this.items.clear();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView title, date, content;
        ImageView dropdown;

        ItemViewHolder(View itemView, final Context context) {
            super(itemView);

            ConstraintLayout titleLayout = itemView.findViewById(R.id.title_layout);

            title = titleLayout.findViewById(R.id.notice_title);
            date = titleLayout.findViewById(R.id.notice_date);
            dropdown = titleLayout.findViewById(R.id.notice_dropdown);

            content = itemView.findViewById(R.id.notice_content);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (content.getVisibility() == View.GONE) {
                        dropdown.setImageResource(R.drawable.ic_up);
                        content.setVisibility(View.VISIBLE);
                    } else {
                        dropdown.setImageResource(R.drawable.ic_down);
                        content.setVisibility(View.GONE);
                    }
                }
            });
        }

        void onBind(HelpItem item) {
            title.setText(item.getTitle());
            date.setText(item.getDate());
            content.setText(item.getContent());
        }

    }
}
