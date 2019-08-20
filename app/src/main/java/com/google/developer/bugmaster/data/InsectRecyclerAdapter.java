package com.google.developer.bugmaster.data;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.developer.bugmaster.views.DangerLevelView;

import com.google.developer.bugmaster.InsectDetailsActivity;
import com.google.developer.bugmaster.MainActivity.ItemClickListener;
import com.google.developer.bugmaster.R;

/**
 * RecyclerView adapter extended with project-specific required methods.
 */

public class InsectRecyclerAdapter extends
        RecyclerView.Adapter<InsectRecyclerAdapter.InsectHolder> {

    Insect insect;
    DangerLevelView dangerLevelView;

    public InsectRecyclerAdapter(Insect insect) {
        this.insect = insect;
    }

    /* ViewHolder for each insect item */
    public class InsectHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView dangerLevel, bugName, bugSciName;
        RelativeLayout container;
        ItemClickListener itemClickListener;
        public InsectHolder(View itemView) {
            super(itemView);
            dangerLevel = (TextView) itemView.findViewById(R.id.danger_level);
            bugName = (TextView) itemView.findViewById(R.id.bug_name);
            bugSciName = (TextView) itemView.findViewById(R.id.bug_sci_name);
            container = (RelativeLayout) itemView.findViewById(R.id.rel_layout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClicked(v, insect, getAdapterPosition());
        }

        public void setItemClickListener(ItemClickListener ic){
            this.itemClickListener = ic;
        }
    }

    private Cursor mCursor;
    private Context mContext;

    public InsectRecyclerAdapter(Cursor mCursor, Context mContext) {
        this.mCursor = mCursor;
        this.mContext = mContext;
    }

    @Override
    public InsectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the custom layout to the view
        return new InsectHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(InsectHolder holder, int position) {
        if (!mCursor.moveToPosition(position))
            return; // bail if returned null

        // set data to the view holder
        String friendlyName = mCursor.getString(mCursor.getColumnIndex(DatabaseManager
                .Contract.Columns.FRIENDLY_NAME));
        String scientificName = mCursor.getString(mCursor.getColumnIndex(DatabaseManager
                .Contract.Columns.SCIENTIFIC_NAME));
        int wildLevel = mCursor.getInt(mCursor.getColumnIndex(DatabaseManager
                .Contract.Columns.DANGER_LEVEL));
        holder.bugName.setText(friendlyName);
        holder.bugSciName.setText(scientificName);
        holder.dangerLevel.setText(Integer.toString(wildLevel));
        dangerLevelView = new DangerLevelView(mContext);
        dangerLevelView.setDangerLevel(wildLevel);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClicked(View v, Insect insecta, int position) {
                insecta = getItem(position);
                Intent intent = new Intent(mContext, InsectDetailsActivity.class);
                // store data
                intent.putExtra("name", insecta.getName());
                intent.putExtra("sciName", insecta.getScientificName());
                intent.putExtra("classification", insecta.getClassification());
                intent.putExtra("imageAsset", insecta.getImageAsset());
                intent.putExtra("wildLevel", insecta.getDangerLevel());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    /**
     * Return the {@link Insect} represented by this item in the adapter.
     *
     * @param position Adapter item position.
     *
     * @return A new {@link Insect} filled with this position's attributes
     *
     * @throws IllegalArgumentException if position is out of the adapter's bounds.
     */
    public Insect getItem(int position) {
        if (position < 0 || position >= getItemCount()) {
            throw new IllegalArgumentException("Item position is out of adapter's range");
        } else if (mCursor.moveToPosition(position)) {
            return new Insect(mCursor);
        }
        return null;
    }
}
