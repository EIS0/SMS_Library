package com.eis0.easypoll.ui;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.eis0.easypoll.DataProvider;
import com.eis0.easypoll.R;
import com.eis0.easypoll.poll.BinaryPoll;

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
    // Same as primary color
    static final int POSITIVE_COLOR = Color.parseColor("#008577");
    // Same as accent color
    static final int NEGATIVE_COLOR = Color.parseColor("#D81B60");
    // Dark Gray
    static final int NEUTRAL_COLOR = Color.parseColor("#808080");
    // Black
    static final int BLACK_COLOR = Color.parseColor("#000000");

    /**
     * Constructor of the OpenedPollAdapter, it sets the LayoutInflater.
     *
     * @param context The context of the ListFragment.
     * @author Matteo Carnelos
     */
    OpenedPollAdapter(@NonNull Context context) {
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
    public void update(@NonNull Observable o, @Nullable Object arg) {
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
        return DataProvider.getOpenedPolls().get(position).getId();
    }

    /**
     * Get the poll item associated with the given position. Required by BaseAdapter.
     *
     * @param position The position of the item in the data set.
     * @return The object in the given position.
     * @author Matteo Carnelos
     */
    @Override
    @NonNull
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
     * Get a View that displays the poll data accordingly to their position in the ListItem view.<br>
     * An opened poll ListItem View has these assignments:<br>
     * - Poll Name TextView          ->  Poll Name<br>
     * - Poll Author TextView        ->  Poll Author Name<br>
     * - Poll Number TextView        ->  Poll Number<br>
     * - Poll Question TextView      ->  Poll Question<br>
     * - Poll Percentage TextView    ->  Poll completed percentage<br>
     * - Poll Answers Count TextView ->  Poll Answers Count<br>
     * - Poll Users Count TextView   ->  Poll Users Count<br>
     * - Poll ProgressBar            ->  Poll completed percentage<br>
     * - Number of Yes TextView      ->  Number of Yes answers<br>
     * - Number of No TextView       ->  Number of No answers<br>
     *
     * @param position The position in the List.
     * @param convertView The ListItem view on which the UI elements are placed.
     * @param container The ViewGroup containing all the List views.
     * @return A View with all the poll data placed correctly.
     * @author Matteo Carnelos
     */
    @Override
    @NonNull
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup container) {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.listitem_opened_poll, null);

        // Linking UI elements to objects
        TextView pollNameTxt = convertView.findViewById(R.id.pollNameTxt);
        TextView pollAuthorTxt = convertView.findViewById(R.id.pollAuthorTxt);
        TextView pollNumTxt = convertView.findViewById(R.id.pollNumTxt);
        TextView pollQuestionTxt = convertView.findViewById(R.id.pollQuestionTxt);
        TextView percentageTxt = convertView.findViewById(R.id.percentageTxt);
        TextView answersCountTxt = convertView.findViewById(R.id.answersCountTxt);
        TextView usersCountTxt = convertView.findViewById(R.id.usersCountTxt);
        ProgressBar pollProgressBar = convertView.findViewById(R.id.pollProgressBar);
        TextView yesNumTxt = convertView.findViewById(R.id.yesNumTxt);
        TextView yesTxt = convertView.findViewById(R.id.yesTxt);
        TextView noNumTxt = convertView.findViewById(R.id.noNumTxt);
        TextView noTxt = convertView.findViewById(R.id.noTxt);

        BinaryPoll poll = DataProvider.getOpenedPolls().get(position);

        // Assigning poll display values to UI objects
        pollNameTxt.setText(poll.getName());
        pollAuthorTxt.setText(poll.getAuthorName());
        pollNumTxt.setText(String.valueOf(poll.getNumber()));
        pollQuestionTxt.setText(poll.getQuestion());
        int closedPercentage = poll.getClosedPercentage();
        percentageTxt.setText(String.valueOf(closedPercentage));
        answersCountTxt.setText(String.valueOf(poll.getNoCount() + poll.getYesCount()));
        usersCountTxt.setText(String.valueOf(poll.getUsersCount()));
        pollProgressBar.setProgress(closedPercentage);
        int yesCount = poll.getYesCount();
        int noCount = poll.getNoCount();
        yesNumTxt.setText(String.valueOf(yesCount));
        noNumTxt.setText(String.valueOf(noCount));
        if(yesCount == noCount) {
            int color = (yesCount == 0)? NEUTRAL_COLOR : BLACK_COLOR;
            yesNumTxt.setTextColor(color);
            yesTxt.setTextColor(color);
            noNumTxt.setTextColor(color);
            noTxt.setTextColor(color);
        } else {
            int yesColor = (yesCount > noCount)? POSITIVE_COLOR : NEUTRAL_COLOR;
            int noColor = (noCount > yesCount)? NEGATIVE_COLOR : NEUTRAL_COLOR;
            yesNumTxt.setTextColor(yesColor);
            yesTxt.setTextColor(yesColor);
            noNumTxt.setTextColor(noColor);
            noTxt.setTextColor(noColor);
        }

        return convertView;
    }
}
