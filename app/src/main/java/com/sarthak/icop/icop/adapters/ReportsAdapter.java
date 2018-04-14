package com.sarthak.icop.icop.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sarthak.icop.icop.R;
import com.sarthak.icop.icop.models.Report;
import com.sarthak.icop.icop.utils.RecyclerViewOnClickListener;

import java.util.ArrayList;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ReportsViewHolder> {

    private ArrayList<Report> reportList = new ArrayList<>();

    private RecyclerViewOnClickListener mListener;

    public ReportsAdapter(ArrayList<Report> list) {

        reportList = list;
    }

    public void setOnRecyclerViewItemClickListener(RecyclerViewOnClickListener listener) {

        this.mListener = listener;
    }

    @Override
    public ReportsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);

        return new ReportsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ReportsViewHolder holder, int position) {

        holder.bindView(reportList.get(holder.getAdapterPosition()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onItemClicked(view, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    class ReportsViewHolder extends RecyclerView.ViewHolder {

        private TextView mCategoryTv, mIdTv, mContactTv;

        public ReportsViewHolder(View itemView) {
            super(itemView);

            mIdTv = itemView.findViewById(R.id.id);
            mCategoryTv = itemView.findViewById(R.id.category);
            mContactTv = itemView.findViewById(R.id.contact);
        }

        void bindView(Report report) {

            if (report != null) {

                mIdTv.setText("Reporrt ID: " + report.getId());
                mCategoryTv.setText("Category: " + report.getCategory());
                mContactTv.setText("Issued by: " + report.getContact());
            }
        }
    }
}
