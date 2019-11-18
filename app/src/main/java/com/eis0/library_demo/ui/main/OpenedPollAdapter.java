package com.eis0.library_demo.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eis0.library_demo.DataProvider;
import com.eis0.library_demo.R;
import com.eis0.storagelibrary.TernaryPoll;

import java.util.Observable;
import java.util.Observer;

/**
 * ListAdapter that adapts data coming from the DataProvider class into the graphical UI elements
 * of the opened poll ListItem view.
 *
 * @author Matteo Carnelos
 */
class OpenedPollAdapter extends BaseAdapter implements Observer {

    private static LayoutInflater inflater = null;

    /**
     * Constructor of the OpenedPollAdapter, it sets the LayoutInflater.
     *
     * @param context The context of the ListFragment.
     * @author Matteo Carnelos
     */
    OpenedPollAdapter(Context context) {
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
     * Get the opened poll unique id associated with the given position. Required by BaseAdapter.
     *
     * @param position The position of the item in the data set.
     * @return The unique id as a long value.
     * @author Matteo Carnelos
     */
    @Override
    public long getItemId(int position) {
        return DataProvider.getOpenedPolls().get(position).getPollId();
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
        return DataProvider.getOpenedPolls().get(position);
    }

    /**
     * How many opened polls are in the list. Required by BaseAdapter.
     *
     * @return The number of opened polls.
     * @author Matteo Carnelos
     */
    @Override
    public int getCount() {
        return DataProvider.getOpenedPolls().size();
    }

    /**
     * Get a View that displays the poll data accordingly to their position in the ListItem view.
     * An opened poll ListItem View has these assignments:
     * - Poll Name TextView         ->  Poll Name
     * - Poll Id TextView           ->  Poll Id
     * - Poll Question TextView     ->  Poll Question
     * - Poll Percentage TextView   ->  Poll completed percentage
     * - Poll ProgressBar           ->  Poll completed percentage
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
            convertView = inflater.inflate(R.layout.listitem_opened_poll, null);

        // Linking UI elements to objects
        TextView pollNameTxt = convertView.findViewById(R.id.pollNameTxt);
        TextView pollIdTxt = convertView.findViewById(R.id.pollIdTxt);
        TextView pollQuestionTxt = convertView.findViewById(R.id.pollQuestionTxt);
        TextView percentageTxt = convertView.findViewById(R.id.percentageTxt);
        ProgressBar pollProgressBar = convertView.findViewById(R.id.pollProgressBar);
        TextView yesNumTxt = convertView.findViewById(R.id.yesNumTxt);
        TextView noNumTxt = convertView.findViewById(R.id.noNumTxt);

        TernaryPoll poll = DataProvider.getOpenedPolls().get(position);

        // Assigning poll display values to UI objects
        pollNameTxt.setText(poll.getPollName());
        pollIdTxt.setText(String.valueOf(getItemId(position)));
        pollQuestionTxt.setText(poll.getPollQuestion());
        int closedPercentage = poll.getClosedPercentage();
        percentageTxt.setText(String.valueOf(closedPercentage));
        pollProgressBar.setProgress(closedPercentage);
        yesNumTxt.setText(String.valueOf(poll.countYes()));
        noNumTxt.setText(String.valueOf(poll.countNo()));

        return convertView;
    }
}