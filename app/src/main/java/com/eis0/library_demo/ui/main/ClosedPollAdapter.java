package com.eis0.library_demo.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eis0.library_demo.DataProvider;
import com.eis0.library_demo.PollListener;
import com.eis0.library_demo.PollManager;
import com.eis0.library_demo.R;
import com.eis0.library_demo.TernaryPoll;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ClosedPollAdapter extends BaseAdapter implements Observer {

    private static LayoutInflater inflater = null;

    public ClosedPollAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void update(Observable o, Object arg) {
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return DataProvider.getClosedPolls().get(position).getPollId();
    }

    @Override
    public Object getItem(int position) {
        return DataProvider.getClosedPolls().get(position);
    }

    @Override
    public int getCount() {
        return DataProvider.getClosedPolls().size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if(convertView == null)
            convertView = inflater.inflate(R.layout.listitem_closed_poll, null);
        TextView pollName = convertView.findViewById(R.id.pollNameTxt);
        TextView pollID = convertView.findViewById(R.id.pollIDTxt);
        TextView yesNum = convertView.findViewById(R.id.yesNumTxt);
        TextView noNum = convertView.findViewById(R.id.noNumTxt);
        TextView pollQuestion = convertView.findViewById(R.id.pollQuestionTxt);

        TernaryPoll poll = DataProvider.getClosedPolls().get(position);
        pollName.setText("" + poll.getPollName());
        pollID.setText("" + getItemId(position));
        noNum.setText("" + poll.countNo());
        yesNum.setText("" + poll.countYes());
        pollQuestion.setText(poll.getPollQuestion());
        return convertView;
    }
}