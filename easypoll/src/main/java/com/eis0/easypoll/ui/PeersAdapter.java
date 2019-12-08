package com.eis0.easypoll.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eis0.easypoll.R;
import com.eis0.smslibrary.SMSPeer;

import java.util.ArrayList;

/**
 * Adapter for the users recycler view in the Create Poll Activity.
 * It manages the memorization and the displaying of the peers added to the poll.
 *
 * @author Matteo Carnelos
 */
public class PeersAdapter extends RecyclerView.Adapter<PeersAdapter.PeerListViewHolder> {

    private ArrayList<SMSPeer> peersDataset = new ArrayList<>();

    /**
     * Provide a reference to the views for each data item.
     * Complex data items may need more than one view per item, and
     * you provide access to all the views for a data item in a view holder.
     *
     * @author Matteo Carnelos
     */
    static class PeerListViewHolder extends RecyclerView.ViewHolder {

        TextView userTxtView;
        Button removeBtn;
        PeerListViewHolder(View v) {
            super(v);
            userTxtView = v.findViewById(R.id.userTxtView);
            removeBtn = v.findViewById(R.id.removeBtn);
        }
    }

    /**
     * Add a valid peer to the dataset, if it is not already present.
     *
     * @param peer The SMSPeer to add.
     * @return True if the item is not already present and it has been added, false otherwise.
     * @author Matteo Carnelos
     */
    public boolean addPeer(SMSPeer peer) {
        if(peersDataset.contains(peer)) return false;
        peersDataset.add(peer);
        notifyItemInserted(peersDataset.size()-1);
        return true;
    }

    /**
     * Remove a peer from the dataset in the specified index.
     *
     * @param index The index of the SMSPeer to remove.
     * @author Matteo Carnelos
     */
    public void removePeer(int index) {
        peersDataset.remove(index);
        notifyItemRemoved(index);
        for(int i = index; i < getItemCount(); i++)
            notifyItemChanged(i);
    }

    /**
     * Return an list containing all the SMSPeer in the dataset.
     *
     * @return The ArrayList of SMSPeer.
     * @author Matteo Carnelos
     */
    public ArrayList<SMSPeer> getPeersDataset() {
        return peersDataset;
    }

    /**
     * Create new views (invoked by the layout manager).
     *
     * @param parent The parent view in which the recycler view item will be inflated.
     * @param viewType The view type of the new view.
     * @return A new ViewHolder that holds a View of the given view type.
     * @author Matteo Carnelos
     */
    @NonNull
    @Override
    public PeersAdapter.PeerListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rclviewitem_user, parent, false);
        return new PeerListViewHolder(v);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager).
     *
     * @param holder The ViewHolder which should be updated to represent the contents of the
     *               item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     * @author Matteo Carnelos
     */
    @Override
    public void onBindViewHolder(PeerListViewHolder holder, final int position) {
        // - Get element from your dataset at this position
        // - Replace the contents of the view with that element
        holder.userTxtView.setText(peersDataset.get(position).getAddress());
        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removePeer(position);
            }
        });
    }

    /**
     * Return the size of your dataset (invoked by the layout manager).
     *
     * @return An integer representing the size of the dataset.
     * @author Matteo Carnelos
     */
    @Override
    public int getItemCount() {
        return peersDataset.size();
    }
}
