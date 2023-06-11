package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SearchEditAdapter extends RecyclerView.Adapter<SearchEditAdapter.ViewHolder> {
    private ArrayList<ParkingClass> mDataList;

    public SearchEditAdapter(ArrayList<ParkingClass> dataList) {
        this.mDataList = dataList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_edit_view);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_edit_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ParkingClass item = mDataList.get(position);
        holder.textView.setText(item.toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DataManagerEdit2Activity.class);
                intent.putExtra("selectedItem", item.getPrkplceNo());
                view.getContext().startActivity(intent);
                ((Activity) view.getContext()).finish();
                Toast toast = Toast.makeText(view.getContext().getApplicationContext(), "저장된 정보를 불러오는 중입니다.", Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void updateList(ArrayList<ParkingClass> data) {
        mDataList.clear();
        mDataList.addAll(data);
    }
}

