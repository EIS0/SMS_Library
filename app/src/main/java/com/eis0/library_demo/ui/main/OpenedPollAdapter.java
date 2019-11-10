package com.eis0.library_demo.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eis0.library_demo.R;

public class OpenedPollAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;

    public OpenedPollAdapter(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 50;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if(convertView == null)
            convertView = inflater.inflate(R.layout.opened_poll_li, null);
        TextView pollName = convertView.findViewById(R.id.pollNameTxt);
        TextView pollID = convertView.findViewById(R.id.pollIDTxt);
        ProgressBar pollProgress = convertView.findViewById(R.id.pollProgressBar);
        TextView yesNum = convertView.findViewById(R.id.yesNumTxt);
        TextView noNum = convertView.findViewById(R.id.noNumTxt);
        TextView pollQuestion = convertView.findViewById(R.id.pollQuestionTxt);
        TextView percentage = convertView.findViewById(R.id.percentageTxt);
        pollName.setText("Pizza Poll");
        pollID.setText("45378");
        percentage.setText("66");
        pollProgress.setProgress(Integer.parseInt(percentage.getText().toString()));
        noNum.setText("2");
        yesNum.setText("4");
        pollQuestion.setText("Do you want to eat pizza tonight?");
        return convertView;
    }
}
