package com.example.myapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private ArrayList<ParkingClass> mDataList;

    public SearchAdapter(ArrayList<ParkingClass> dataList) {
        this.mDataList = dataList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
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
                if(MapsActivity.mMap != null) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    // 해당 위도 경도로 카메라 이동
                    double lat = Double.parseDouble(item.getLatitude());
                    double lng = Double.parseDouble(item.getLongitude());
                    LatLng latlng = new LatLng(lat, lng);
                    //MarkerOptions markerOptions1 = new MarkerOptions();
                    //markerOptions1.position(latlng);
                    //MapsActivity.mMap.addMarker(markerOptions1);
                    MapsActivity.mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 17));
                    ((Activity) view.getContext()).finish();
                }
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

