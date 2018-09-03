package com.appodeal.support.test;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.appodeal.ads.NativeAd;

import java.util.List;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {

    private List<String> simpleData;
    private List<NativeAd> nativeData;

    MyListAdapter(List<String> simpleData, List<NativeAd> nativeData) {
        this.simpleData = simpleData;
        this.nativeData = nativeData;

    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mTextView;
        Button mButton;
        ImageView mImageView;

        ViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.item_textView);
            mButton = v.findViewById(R.id.item_button);
            mImageView = v.findViewById(R.id.item_imageView);
        }
    }

    @NonNull
    @Override
    public MyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View item_view = inflater.inflate(R.layout.list_item, null);
        return new ViewHolder(item_view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < simpleData.size()) {
            holder.mTextView.setText(simpleData.get(position));
        } else {
            position -= simpleData.size();
            NativeAd nativeItem = nativeData.get(position);
            holder.mTextView.setText(nativeItem.getTitle());
            holder.mButton.setText(nativeItem.getCallToAction());
            holder.mImageView.setImageBitmap(nativeItem.getIcon());
        }
    }

    @Override
    public int getItemCount() {
        return simpleData.size() + nativeData.size();
    }
}
