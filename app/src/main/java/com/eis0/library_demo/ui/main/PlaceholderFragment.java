package com.eis0.library_demo.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;

import com.eis0.library_demo.DataProvider;
import com.eis0.library_demo.PollListener;

import java.util.Observer;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends ListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static DataProvider dataProvider = DataProvider.getInstance();

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        switch(sectionNumber) {
            case 1:
                ListAdapter incomingPollAdapter = new IncomingPollAdapter(inflater.getContext());
                setListAdapter(incomingPollAdapter);
                dataProvider.addObserver((Observer)incomingPollAdapter);
                break;
            case 2:
                ListAdapter openedPollAdapter = new OpenedPollAdapter(inflater.getContext());
                setListAdapter(openedPollAdapter);
                dataProvider.addObserver((Observer)openedPollAdapter);
                break;
            case 3:
                ListAdapter closedPollAdapter = new ClosedPollAdapter(inflater.getContext());
                setListAdapter(closedPollAdapter);
                dataProvider.addObserver((Observer)closedPollAdapter);
                break;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dataProvider.deleteObserver((Observer)getListAdapter());
    }
}