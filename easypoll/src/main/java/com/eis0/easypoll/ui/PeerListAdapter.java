package com.eis0.easypoll.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eis0.easypoll.R;
import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;

public class PeerListAdapter extends RecyclerView.Adapter<PeerListAdapter.PeerListViewHolder> {
    private ArrayList<SMSPeer> peerDataset = new ArrayList<>();

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class PeerListViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView userTxtView;
        public PeerListViewHolder(View v) {
            super(v);
            userTxtView = v.findViewById(R.id.userTxtView);
        }
    }

    public boolean addPeer(SMSPeer peer) {
        if(peerDataset.size() >= 3) return false;
        peerDataset.add(peer);
        notifyItemInserted(peerDataset.size()-1);
        return true;
    }

    public ArrayList<SMSPeer> getPeerDataset() {
        return peerDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public PeerListAdapter.PeerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.peerlist_user, parent, false);
        PeerListViewHolder vh = new PeerListViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(PeerListViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.userTxtView.setText(peerDataset.get(position).getAddress());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return peerDataset.size();
    }
}
