package com.eis0.library_demo.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.eis0.library_demo.PollListener;
import com.eis0.library_demo.PollManager;
import com.eis0.library_demo.R;
import com.eis0.library_demo.TernaryPoll;

import java.util.ArrayList;

public class IncomingPollAdapter extends BaseAdapter implements PollListener {

    private static LayoutInflater inflater = null;
    private static ArrayList<TernaryPoll> incomingPolls = new ArrayList<>();
    PollManager pollManager = PollManager.getInstance();

    public IncomingPollAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pollManager.addPollListener(this);
    }

    public void onIncomingPoll(TernaryPoll poll) {
        incomingPolls.add(poll);
        notifyDataSetChanged();
    }

    public void onSentPollUpdate(TernaryPoll poll) {}

    @Override
    public long getItemId(int position) {
        return incomingPolls.get(position).getPollID();
    }

    @Override
    public Object getItem(int position) {
        return incomingPolls.get(position);
    }

    @Override
    public int getCount() {
        return incomingPolls.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if(convertView == null)
            convertView = inflater.inflate(R.layout.listitem_incoming_poll, null);
        TextView pollName = convertView.findViewById(R.id.pollNameTxt);
        TextView pollID = convertView.findViewById(R.id.pollIDTxt);
        TextView pollQuestion = convertView.findViewById(R.id.pollQuestionTxt);
        Button yesBtn = convertView.findViewById(R.id.yesBtn);
        Button noBtn = convertView.findViewById(R.id.noBtn);

        final TernaryPoll poll = incomingPolls.get(position);

        yesBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pollManager.answerPoll(poll, true);
                incomingPolls.remove(poll);
                notifyDataSetChanged();
            }
        });

        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pollManager.answerPoll(poll, false);
                incomingPolls.remove(poll);
                notifyDataSetChanged();
            }
        });

        pollName.setText("" + poll.getPollName());
        pollID.setText("" + poll.getPollID());
        pollQuestion.setText(poll.getPollQuestion());
        return convertView;
    }
}