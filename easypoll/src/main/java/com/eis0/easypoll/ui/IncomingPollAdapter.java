package com.eis0.easypoll.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.eis0.easypoll.DataProvider;
import com.eis0.easypoll.poll.PollManager;
import com.eis0.easypoll.R;
import com.eis0.easypoll.poll.TernaryPoll;

import java.util.Observable;
import java.util.Observer;

/**
 * ListAdapter that adapts data coming from the DataProvider class into the graphical UI elements
 * of the incoming poll ListItem view.
 *
 * @author Matteo Carnelos
 */
public class IncomingPollAdapter extends BaseAdapter implements Observer {

    private static LayoutInflater inflater = null;
    private PollManager pollManager = PollManager.getInstance();

    /**
     * Constructor of the IncomingPollAdapter, it sets the LayoutInflater.
     *
     * @param context The context of the ListFragment.
     * @author Matteo Carnelos
     */
    IncomingPollAdapter(Context context) {
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
     * Get the incmoing poll unique id associated with the given position. Required by BaseAdapter.
     *
     * @param position The position of the item in the data set.
     * @return The unique id as a long value.
     * @author Matteo Carnelos
     */
    @Override
    public long getItemId(int position) {
        return DataProvider.getIncomingPolls().get(position).getPollId();
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
        return DataProvider.getIncomingPolls().get(position);
    }

    /**
     * How many incoming polls are in the list. Required by BaseAdapter.
     *
     * @return The number of incoming polls.
     * @author Matteo Carnelos
     */
    @Override
    public int getCount() {
        return DataProvider.getIncomingPolls().size();
    }

    /**
     * Get a View that displays the poll data in the LitItem View accordingly to their position.
     * An incoming poll ListItem View has a:
     * - Poll Name TextView         ->  Poll Name
     * - Poll Id TextView           ->  Poll Id
     * - Poll Question TextView     ->  Poll Question
     * - Yes Button                 ->  Send answer 'Yes' action
     * - No Button                  ->  Send answer 'No' action
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
            convertView = inflater.inflate(R.layout.listitem_incoming_poll, null);

        // Linking UI elements to objects
        TextView pollNameTxt = convertView.findViewById(R.id.pollNameTxt);
        TextView pollIdTxt = convertView.findViewById(R.id.pollIdTxt);
        TextView pollQuestionTxt = convertView.findViewById(R.id.pollQuestionTxt);
        Button yesBtn = convertView.findViewById(R.id.yesBtn);
        Button noBtn = convertView.findViewById(R.id.noBtn);

        final TernaryPoll poll = DataProvider.getIncomingPolls().get(position);

        // Assigning poll display values and actions to UI objects
        pollNameTxt.setText(poll.getPollName());
        pollIdTxt.setText(String.valueOf(getItemId(position)));
        pollQuestionTxt.setText(poll.getPollQuestion());
        yesBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pollManager.answerPoll(poll, true);
                DataProvider.getIncomingPolls().remove(poll);
                notifyDataSetChanged();
            }
        });
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pollManager.answerPoll(poll, false);
                DataProvider.getIncomingPolls().remove(poll);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
