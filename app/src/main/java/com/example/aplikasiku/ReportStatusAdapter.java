package com.example.aplikasiku;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class ReportStatusAdapter extends ArrayAdapter<String> {
    private ArrayList<String> itemData;
    private ArrayList<String> itemGps;
    private ArrayList<String> itemTime;
    private ArrayList<String> itemStatus;
    private Context mContext;

    public ReportStatusAdapter(Context context, ArrayList<String> itemTime, ArrayList<String> itemGps, ArrayList<String> itemStatus) {
        super(context, R.layout.listview_items);
        this.itemGps = itemGps;
        this.itemTime = itemTime;
        this.itemStatus = itemStatus;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return itemTime.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            convertView = inflater.inflate(R.layout.listview_items, parent, false);

            viewHolder.tv_Time =  convertView.findViewById(R.id.tvTime);
            viewHolder.tv_GPS =  convertView.findViewById(R.id.tvGPS);
//            viewHolder.count = convertView.findViewById(R.id.count);
//            viewHolder.dimensions = convertView.findViewById(R.id.dimensions);
            viewHolder.tv_Status = convertView.findViewById(R.id.tvStatus);
//            viewHolder.progressBar = convertView.findViewById(R.id.progressBar);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String status_text = null;
        switch (itemStatus.get(position)) {
            case "0":
                status_text = "Laporan Ditolak";
                break;
            case "10":
                status_text = "Laporan Dikirim";
                break;
            case "50":
                status_text = "Lubang Jalan Terdeteksi dan Laporan Diterima";
                break;
            case "70":
                status_text = "Dalam Proses";
                break;
//            case "70":
//                status_text = "Engineer Assigned for fixing the issue";
//                break;
            case "80":
                status_text = "Telah Teratasi";
                break;
        }

        viewHolder.tv_Time.setText(itemTime.get(position));
        viewHolder.tv_GPS.setText(itemGps.get(position));
//        viewHolder.dimensions.setText(String.format("%s*%s", itemWidth.get(position), itemHeight.get(position)));
//        viewHolder.count.setText(itemCount.get(position));
        viewHolder.tv_Status.setText(status_text);
//        viewHolder.progressBar.setProgress(Integer.parseInt(itemStatus.get(position)));

        return convertView;
    }

    static class ViewHolder{
        TextView tv_Time;
        TextView tv_GPS;
//        TextView count;
//        TextView dimensions;
        TextView tv_Status;
//        ProgressBar progressBar;
    }
}
