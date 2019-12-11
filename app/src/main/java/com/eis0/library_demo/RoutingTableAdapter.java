package com.eis0.library_demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.eis0.kademlia.Contact;

import java.util.List;

/**
 * Adapter class that handles data from a dataset and places them in the corresponding UI elements
 * of a View.
 *
 * @author Matteo Carnelos
 */
public class RoutingTableAdapter extends RecyclerView.Adapter<RoutingTableAdapter.ViewHolder> {

    private List<Contact> contactsList;

    /**
     * Provide a reference to the views for each data item.
     * Complex data items may need more than one view per item, and you provide access to
     * all the views for a data item in a view holder.
     *
     * @author Matteo Carnelos
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nodePeerLbl;
        public TextView nodeIdLbl;
        public ViewHolder(View view) {
            super(view);
            nodePeerLbl = view.findViewById(R.id.nodePeerLbl);
            nodeIdLbl = view.findViewById(R.id.nodeIdLbl);
        }
    }

    /**
     * Provide a suitable constructor (depends on the kind of dataset).
     *
     * @param contactsList A List of Contact representing all the contacts in a routing table.
     * @author Matteo Carnelos
     */
    public RoutingTableAdapter(List<Contact> contactsList) {
        this.contactsList = contactsList;
    }

    /**
     * Add a contact to the contacts list.
     *
     * @param contact The Contact to add.
     * @author Matteo Carnelos
     */
    public void addContact(Contact contact) {
        contactsList.add(contact);
        notifyItemInserted(getItemCount()-1);
    }

    /**
     * Create new views. Invoked by the layout manager.
     *
     * @param parent The parent ViewGroup where the View will be inflated.
     * @param viewType The type of the recycler view list item.
     * @return An instance of the ViewHolder inner class.
     * @author Matteo Carnelos
     */
    @Override
    public RoutingTableAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rclviewitem_contact, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * Replace the contents of a view. Invoked by the layout manager.
     *
     * @param holder The ViewHolder that contains all the UI objects.
     * @param position The position of the item in the list.
     * @author Matteo Carnelos
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Contact contact = contactsList.get(position);
        holder.nodePeerLbl.setText(contact.getNode().getPeer().toString());
        holder.nodeIdLbl.setText(contact.getNode().getId().toString());
    }

    /**
     * Return the size of your dataset. Invoked by the layout manager.
     *
     * @return An integer representing the size of the dataset.
     * @author Matteo Carnelos
     */
    @Override
    public int getItemCount() {
        return contactsList.size();
    }
}
