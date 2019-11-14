package com.eis0.library_demo.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eis0.library_demo.PollListener;
import com.eis0.library_demo.PollManager;
import com.eis0.library_demo.R;
import com.eis0.library_demo.TernaryPoll;

import java.util.ArrayList;

public class ClosedPollAdapter extends BaseAdapter implements PollListener {

    private static LayoutInflater inflater = null;
    private static ArrayList<TernaryPoll> closedPolls = new ArrayList<>();
    PollManager pollManager = PollManager.getInstance();

    public ClosedPollAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pollManager.addPollListener(this);
    }

    public void removeListener() {
        pollManager.removePollListener(this);
    }

    public void onIncomingPoll(TernaryPoll poll) {}

    public void onSentPollUpdate(TernaryPoll poll) {
        if(poll.isClosed()) {
            closedPolls.add(poll);
            notifyDataSetChanged();
        }
    }

    @Override
    public long getItemId(int position) {
        return closedPolls.get(position).getPollID();
    }

    @Override
    public Object getItem(int position) {
        return closedPolls.get(position);
    }

    @Override
    public int getCount() {
        return closedPolls.size();
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

        TernaryPoll poll = closedPolls.get(position);
        pollName.setText("" + poll.getPollName());
        pollID.setText("" + getItemId(position));
        noNum.setText("" + poll.countNo());
        yesNum.setText("" + poll.countYes());
        pollQuestion.setText(poll.getPollQuestion());
        return convertView;
    }
}
