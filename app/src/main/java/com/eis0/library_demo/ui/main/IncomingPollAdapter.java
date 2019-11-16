package com.eis0.library_demo.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.eis0.library_demo.DataProvider;
import com.eis0.library_demo.PollListener;
import com.eis0.library_demo.PollManager;
import com.eis0.library_demo.R;
import com.eis0.library_demo.TernaryPoll;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class IncomingPollAdapter extends BaseAdapter implements Observer {

    private static LayoutInflater inflater = null;
    PollManager pollManager = PollManager.getInstance();

    public IncomingPollAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void update(Observable o, Object arg) {
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return DataProvider.getIncomingPolls().get(position).getPollId();
    }

    @Override
    public Object getItem(int position) {
        return DataProvider.getIncomingPolls().get(position);
    }

    @Override
    public int getCount() {
        return DataProvider.getIncomingPolls().size();
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

        final TernaryPoll poll = DataProvider.getIncomingPolls().get(position);

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

        pollName.setText("" + poll.getPollName());
        pollID.setText("" + poll.getPollId());
        pollQuestion.setText(poll.getPollQuestion());
        return convertView;
    }
}