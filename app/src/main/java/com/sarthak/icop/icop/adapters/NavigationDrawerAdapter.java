package com.sarthak.icop.icop.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sarthak.icop.icop.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.NavigationDrawerViewHolder> {

    private ArrayList<String> itemList = new ArrayList<>();
    private ArrayList<Integer> imageList = new ArrayList<>();

    private Context mContext;

    public NavigationDrawerAdapter(Context context, ArrayList<String> itemList, ArrayList<Integer> imageList) {

        mContext = context;
        this.itemList = itemList;
        this.imageList = imageList;
    }

    @Override
    public NavigationDrawerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nav_drawer, parent, false);

        return new NavigationDrawerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NavigationDrawerViewHolder holder, int position) {

        holder.bindView(holder.getAdapterPosition(), itemList.get(holder.getAdapterPosition()), imageList.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class NavigationDrawerViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImage;
        private TextView mLabelTv;
        private ImageButton mToggleBtn;

        NavigationDrawerViewHolder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.nav_drawer_image);
            mLabelTv = itemView.findViewById(R.id.nav_drawer_label);
            mToggleBtn = itemView.findViewById(R.id.on_off_btn);
        }

        void bindView(int pos, String label, int image) {

            mLabelTv.setText(label);
            Picasso.with(mContext)
                    .load(image)
                    .into(mImage);

            if (pos == 0) {
                mToggleBtn.setVisibility(View.VISIBLE);
            } else {
                mToggleBtn.setVisibility(View.GONE);
            }

            if (mContext.getSharedPreferences("SOS", MODE_PRIVATE).getString("status", null) == null) {

                mToggleBtn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.offbutton));
            }

            if (mContext.getSharedPreferences("SOS", MODE_PRIVATE).getString("status", null) != null) {

                if (mContext.getSharedPreferences("SOS", MODE_PRIVATE).getString("status", null).equals("true")) {

                    mToggleBtn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.onbutton));
                } else {

                    mToggleBtn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.offbutton));
                }
            }


            mToggleBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    SharedPreferences.Editor editor = mContext.getSharedPreferences("SOS", MODE_PRIVATE).edit();

                    if (mContext.getSharedPreferences("SOS", MODE_PRIVATE).getString("status", null) == null) {

                        mToggleBtn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.onbutton));
                        editor.putString("status", "true");
                        editor.apply();
                    }

                    if (mContext.getSharedPreferences("SOS", MODE_PRIVATE).getString("status", null) != null) {

                        if (mContext.getSharedPreferences("SOS", MODE_PRIVATE).getString("status", null).equals("true")) {

                            mToggleBtn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.offbutton));
                            editor.putString("status", "false");
                            editor.apply();
                        } else {

                            mToggleBtn.setImageDrawable(mContext.getResources().getDrawable(R.drawable.onbutton));
                            editor.putString("status", "true");
                            editor.apply();
                        }
                    }
                }
            });
        }
    }
}
