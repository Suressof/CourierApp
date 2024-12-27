package com.example.courierapp.ui.order;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.courierapp.R;
public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {
    private List<String> data;
    private List<String> data1;
    private List<String> data2;
    private OnItemClickListener mListener;
    private int selectedPosition = -1;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text1;
        public TextView text2;
        public TextView text3;


        public ViewHolder(View itemView) {
            super(itemView);
            text1 = itemView.findViewById(R.id.text1);
            text2 = itemView.findViewById(R.id.text2);
            text3 = itemView.findViewById(R.id.text3);
        }
    }

    public TableAdapter(List<String> data, List<String> data1, List<String> data2) {
        this.data = data;
        this.data1 = data1;
        this.data2 = data2;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        return new ViewHolder(view);
    }

    public interface OnItemClickListener {
        void onItemClick(String rowData, String rowData2, String rowData1, int index);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String rowData = data.get(position);
        String rowData1 = data1.get(position);
        String rowData2 = data2.get(position);

        int index = position;
        holder.text1.setText(rowData);
        holder.text2.setText(rowData2);
        holder.text3.setText(rowData1);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(rowData, rowData2, rowData1, index);
                }
            }
        });


        if (position == selectedPosition) {
            holder.itemView.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_gradient_table_focus));
        }
        else {
            holder.itemView.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_gradient_table2));
            if (position % 2 == 0){
                holder.itemView.setBackground(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.bg_gradient_table));
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
