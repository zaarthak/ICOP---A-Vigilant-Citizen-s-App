package com.sarthak.icop.icop.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sarthak.icop.icop.models.Police;
import com.sarthak.icop.icop.R;

import java.util.ArrayList;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.CallViewHolder> {

    int type;

    private ArrayList<Police> mCallList = new ArrayList<>();

    private Context mContext;

    public CallAdapter(Context context, ArrayList<Police> list, int type) {

        mContext = context;
        mCallList = list;
        this.type = type;
    }

    @Override
    public CallViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_call, parent, false);

        return new CallViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CallViewHolder holder, int position) {

        Police police = mCallList.get(holder.getAdapterPosition());

        holder.bindView(police);
    }

    @Override
    public int getItemCount() {
        return mCallList.size();
    }

    class CallViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        String phone = "";

        private TextView mNameTv, mPostTv, mPhoneTv;
        private ImageButton mCallBtn, mMessageBtn;

        public CallViewHolder(View itemView) {
            super(itemView);

            mNameTv = itemView.findViewById(R.id.name);
            mPostTv = itemView.findViewById(R.id.post);
            mPhoneTv = itemView.findViewById(R.id.phone);

            mCallBtn = itemView.findViewById(R.id.call);
            mMessageBtn = itemView.findViewById(R.id.message);

            mCallBtn.setOnClickListener(this);
            mMessageBtn.setOnClickListener(this);
        }

        void bindView(Police police) {

            if (type == 3) {

                mPostTv.setVisibility(View.GONE);
                mMessageBtn.setVisibility(View.GONE);
            }

            mNameTv.setText(police.getName());
            mPostTv.setText(police.getPost());
            mPhoneTv.setText(police.getPhone());

            phone = police.getPhone();
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.call:

                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(mContext, "Enable Phone permission.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mContext.startActivity(intent);
                    break;

                case R.id.message:

                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                    smsIntent.setData(Uri.parse("sms:"));
                    smsIntent.putExtra("address", phone);
                    mContext.startActivity(smsIntent);
                    break;
            }
        }
    }
}
