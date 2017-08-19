package com.example.fioni.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.example.fioni.popularmovies.AnObject;
import com.example.fioni.popularmovies.R;

/**
 * Created by fioni on 7/28/2017.
 */

public class AnAdapter extends RecyclerView.Adapter<AnAdapter.AnAdapterViewHolder> {

    private String [] mStringArray;
    private String mType;
    private static final String TRAILER_TYPE = "trailer";
    private static final String REVIEW_TYPE = "review";

    private final AnAdapterOnClickHandler mClickHandler;


    public interface AnAdapterOnClickHandler {
        void onClick(String aString);
    }

    public AnAdapter(AnAdapter.AnAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public AnAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = 0;
        if(mType.equals(TRAILER_TYPE)) {
            layoutIdForListItem = R.layout.trailer_list;
        }
        if(mType.equals(REVIEW_TYPE)) {
            layoutIdForListItem = R.layout.review_list;
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new AnAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnAdapterViewHolder holder, int position) {
        //mTitle = mStringArray[position];
        int n = position + 1;
        if(mType.equals(TRAILER_TYPE)) {
            holder.mTextView.setText("Trailer: " + n);
        }
        if(mType.equals(REVIEW_TYPE)) {
            holder.mTextView.setText(mStringArray[position]);
        }

    }

    @Override
    public int getItemCount() {
        if (null == mStringArray) return 0;
        return mStringArray.length;
    }


    public class AnAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public TextView mTextView;

        public AnAdapterViewHolder(View itemView) {
            super(itemView);
            if(mType.equals(TRAILER_TYPE)){
                mTextView = (TextView) itemView.findViewById(R.id.tv_display_data);
            }
            if(mType.equals(REVIEW_TYPE)){
                mTextView = (TextView) itemView.findViewById(R.id.tv_display_rev);
            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String aText = mStringArray[adapterPosition];
            mClickHandler.onClick(aText);
        }
    }

    public void setStringData(String [] stringData) {
        mStringArray = stringData;
        notifyDataSetChanged();
    }

    public void setObjectData(AnObject object) {
        mStringArray = object.getList();
        mType = object.getType();
        notifyDataSetChanged();
    }
}
