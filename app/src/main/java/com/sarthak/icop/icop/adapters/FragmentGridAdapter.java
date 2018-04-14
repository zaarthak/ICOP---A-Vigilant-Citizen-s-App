package com.sarthak.icop.icop.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sarthak.icop.icop.utils.CircleTransform;
import com.sarthak.icop.icop.R;
import com.sarthak.icop.icop.utils.RecyclerViewOnClickListener;
import com.sarthak.icop.icop.utils.SquareImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FragmentGridAdapter extends RecyclerView.Adapter<FragmentGridAdapter.FrgamentGridViewHolder> {

    private Context mContext;

    ArrayList<String> listItems = new ArrayList<>();
    ArrayList<Integer> imageItems = new ArrayList<>();

    private RecyclerViewOnClickListener mListener;

    public FragmentGridAdapter(Context context, ArrayList<String> listItems, ArrayList<Integer> imageItems) {

        this.mContext = context;

        this.listItems = listItems;
        this.imageItems = imageItems;
    }

    public void setOnRecyclerViewItemClickListener(RecyclerViewOnClickListener listener) {

        this.mListener = listener;
    }

    @Override
    public FrgamentGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent, false);

        return new FragmentGridAdapter.FrgamentGridViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FrgamentGridViewHolder holder, int position) {

        holder.bindView(listItems.get(holder.getAdapterPosition()), imageItems.get(holder.getAdapterPosition()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mListener.onItemClicked(view, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class FrgamentGridViewHolder extends RecyclerView.ViewHolder {

        TextView mLabelTv;
        SquareImageView mImage;

        FrgamentGridViewHolder(View itemView) {
            super(itemView);

            mLabelTv = itemView.findViewById(R.id.item_label);
            mImage = itemView.findViewById(R.id.item_image);
        }

        void bindView(String label, int imageId) {

            mLabelTv.setText(label);
            Picasso.with(mContext)
                    .load(imageId)
                    .transform(new CircleTransform())
                    .into(mImage);
        }
    }
}
