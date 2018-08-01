package com.dicoding.hendro.mynotepadwithroom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {
    Context context;
    List<Note> list;

    public NotesAdapter(Context context) {
        this.context = context;
    }

    public NotesAdapter(Context context, List<Note> list) {
        this.context = context;
        this.list = list;
    }

    public void refreshData(List<Note> list){
        this.list = list;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String  t, c;
        final int i;

        i = list.get(position).getNote_id();
        t = list.get(position).getTitle();
        c = list.get(position).getContent();

        holder.tvTitle.setText(t);
        holder.tvContent.setText(c);
        holder.layout.setOnClickListener(new CustomOnItemClickListener(position, new CustomOnItemClickListener.OnItemClickCallback() {
            @Override
            public void onItemClicked(View view, int position) {
                Intent intent = new Intent(context, NoteActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("id", i);
                intent.putExtra("title", t);
                intent.putExtra("content", c);
                ((Activity) context).startActivityForResult(intent, MainActivity.REQUEST_CODE);
            }
        }));
    }

    @Override
    public int getItemCount() {
        if (list == null) {
            return 0;
        } else{
            return list.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.tv_title);
            tvContent = (TextView)itemView.findViewById(R.id.tv_content);
            layout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
        }
    }
}
