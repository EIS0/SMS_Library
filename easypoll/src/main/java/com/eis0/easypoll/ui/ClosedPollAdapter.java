package com.eis0.easypoll.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eis0.easypoll.DataProvider;
import com.eis0.easypoll.R;
import com.eis0.easypoll.poll.BinaryPoll;

import java.util.Observable;
import java.util.Observer;

/**
 * ListAdapter that adapts data coming from the DataProvider class into the graphical UI elements
 * of the closed poll ListItem view.
 *
 * @author Matteo Carnelos
 */
public class ClosedPollAdapter extends BaseAdapter implements Observer {

    private static LayoutInflater inflater = null;

    /**
     * Constructor of the ClosedPollAdapter, it sets the LayoutInflater.
     *
     * @param context The context of the ListFragment.
     * @author Matteo Carnelos
     */
    ClosedPollAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Called whenever there is an update in the DataProvider class. It refreshes the views
     * accordingly to the new data.
     *
     * @param o   The object that called the update, in this case is a DataProvider object.
     * @param arg The object that is being added/removed/updated, in this case is the Poll
     *            object coming from the PollManager.
     * @author Matteo Carnelos
     */
    public void update(Observable o, Object arg) {
        notifyDataSetChanged();
    }

    /**
     * Get the closed poll unique id associated with the given position. Required by BaseAdapter.
     *
     * @param position The position of the item in the data set.
     * @return The unique id as a long value.
     * @author Matteo Carnelos
     */
    @Override
    public long getItemId(int position) {
        return DataProvider.getClosedPolls().get(position).getId();
    }

    /**
     * Get the poll item associated with the given position. Required by BaseAdapter.
     *
     * @param position The position of the item in the data set.
     * @return The object in the given position.
     * @author Matteo Carnelos
     */
    @Override
    public Object getItem(int position) {
        return DataProvider.getClosedPolls().get(position);
    }

    /**
     * How many closed polls are in the list. Required by BaseAdapter.
     *
     * @return The number of closed polls.
     * @author Matteo Carnelos
     */
    @Override
    public int getCount() {
        return DataProvider.getClosedPolls().size();
    }

    /**
     * Get a View that displays the poll data accordingly to their position in the ListItem view.
     * A closed poll ListItem View has these assignements:
     * - Poll Name TextView         ->  Poll Name
     * - Poll Id TextView           ->  Poll Id
     * - Poll Question TextView     ->  Poll Question
     * - Number of Yes TextView     ->  Number of Yes answers
     * - Number of No TextView      ->  Number of No answers
     *
     * @param position    The position in the List.
     * @param convertView The ListItem view on which the UI elements are placed.
     * @param container   The ViewGroup containing all the List views.
     * @return A View with all the poll data placed correctly.
     * @author Matteo Carnelos
     */
    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.listitem_closed_poll, null);

        // Linking UI elements to objects
        TextView pollNameTxt = convertView.findViewById(R.id.pollNameTxt);
        TextView pollAuthorTxt = convertView.findViewById(R.id.pollAuthorTxt);
        TextView pollIdTxt = convertView.findViewById(R.id.pollIdTxt);
        TextView pollQuestionTxt = convertView.findViewById(R.id.pollQuestionTxt);
        TextView yesNumTxt = convertView.findViewById(R.id.yesNumTxt);
        TextView noNumTxt = convertView.findViewById(R.id.noNumTxt);

        BinaryPoll poll = DataProvider.getClosedPolls().get(position);

        // Assigning poll display values to UI objects
        pollNameTxt.setText(poll.getName());
        pollAuthorTxt.setText(poll.getOwnerName());
        pollIdTxt.setText(String.valueOf(poll.getLocalId()));
        pollQuestionTxt.setText(poll.getQuestion());
        yesNumTxt.setText(String.valueOf(poll.getYesCount()));
        noNumTxt.setText(String.valueOf(poll.getNoCount()));

        return convertView;
    }
}
