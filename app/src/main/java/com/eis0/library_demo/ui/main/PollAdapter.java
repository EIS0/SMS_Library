package com.eis0.library_demo.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eis0.library_demo.R;

public class PollAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;

    public PollAdapter(Context context) {
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
            convertView = inflater.inflate(R.layout.poll_list_item, null);
        TextView pollName = convertView.findViewById(R.id.pollName);
        TextView pollId = convertView.findViewById(R.id.pollId);
        ProgressBar pollProgress = convertView.findViewById(R.id.pollProgress);
        TextView pollResult = convertView.findViewById(R.id.pollResult);
        pollName.setText("Pizza Poll");
        pollId.setText(": 0001");
        pollProgress.setProgress(50);
        pollResult.setText("5 YES - 2 NO");
        return convertView;
    }
}
