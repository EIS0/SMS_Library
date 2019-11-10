package com.eis0.library_demo.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eis0.library_demo.R;

public class IncomingPollAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;

    public IncomingPollAdapter(Context context) {
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
        return 10;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        if(convertView == null)
            convertView = inflater.inflate(R.layout.incoming_poll_li, null);
        TextView pollName = convertView.findViewById(R.id.pollNameTxt);
        TextView pollID = convertView.findViewById(R.id.pollIDTxt);
        TextView pollQuestion = convertView.findViewById(R.id.pollQuestionTxt);
        pollName.setText("Kebab Poll");
        pollID.setText("00000");
        pollQuestion.setText("Do you want to a deliciuos kebab for launch?");
        return convertView;
    }
}
