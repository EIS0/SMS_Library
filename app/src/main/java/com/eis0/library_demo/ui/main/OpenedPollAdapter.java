package com.eis0.library_demo.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eis0.library_demo.DataProvider;
import com.eis0.library_demo.PollListener;
import com.eis0.library_demo.PollManager;
import com.eis0.library_demo.R;
import com.eis0.library_demo.TernaryPoll;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class OpenedPollAdapter extends BaseAdapter implements Observer {

    private static LayoutInflater inflater = null;

    public OpenedPollAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void update(Observable o, Object arg) {
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return DataProvider.getOpenedPolls().get(position).getPollID();
    }

    @Override
    public Object getItem(int position) {
        return DataProvider.getOpenedPolls().get(position);
    }

    @Override
    public int getCount() {
        return DataProvider.getOpenedPolls().size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if(convertView == null)
            convertView = inflater.inflate(R.layout.listitem_opened_poll, null);
        TextView pollName = convertView.findViewById(R.id.pollNameTxt);
        TextView pollID = convertView.findViewById(R.id.pollIDTxt);
        ProgressBar pollProgress = convertView.findViewById(R.id.pollProgressBar);
        TextView yesNum = convertView.findViewById(R.id.yesNumTxt);
        TextView noNum = convertView.findViewById(R.id.noNumTxt);
        TextView pollQuestion = convertView.findViewById(R.id.pollQuestionTxt);
        TextView percentage = convertView.findViewById(R.id.percentageTxt);

        TernaryPoll poll = DataProvider.getOpenedPolls().get(position);
        pollName.setText("" + poll.getPollName());
        pollID.setText("" + getItemId(position));
        int closedPercentage = poll.getClosedPercentage();
        percentage.setText("" + closedPercentage);
        pollProgress.setProgress(closedPercentage);
        noNum.setText("" + poll.countNo());
        yesNum.setText("" + poll.countYes());
        pollQuestion.setText(poll.getPollQuestion());
        return convertView;
    }
}
