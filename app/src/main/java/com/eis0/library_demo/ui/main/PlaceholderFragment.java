package com.eis0.library_demo.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends ListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

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
                setListAdapter(new IncomingPollAdapter(inflater.getContext()));
                break;
            case 2:
                setListAdapter(new OpenedPollAdapter(inflater.getContext()));
                break;
            case 3:
                setListAdapter(new ClosedPollAdapter(inflater.getContext()));
                break;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}