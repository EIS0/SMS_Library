package com.eis0.easypoll.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;

import com.eis0.easypoll.DataProvider;

import java.util.Observer;

/**
 * A placeholder fragment containing one of the three list types depending on the section number.
 *
 * @author Matteo Carnelos
 */
public class PlaceholderFragment extends ListFragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final int INCOMING_SECTION_NUMBER = 1;
    private static final int OPENED_SECTION_NUMBER = 2;
    private static final int CLOSED_SECTION_NUMBER = 3;
    private DataProvider dataProvider = DataProvider.getInstance();

    /**
     * Returns a new PlaceholderFragment instance with the bundle containing the section number
     * specified in the index parameter.
     *
     * @param index The index of the PlaceholderFragment section.
     * @return A PlaceholderFragment instance with the bundle containing the section number.
     * @author Matteo Carnelos
     */
    static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        // Put the section number in the bundle, it will be used in onCreateView(...)
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * Called when the view of the ListFragment is created. It associates one of the
     * three ListAdapters (Incoming, Opened, Closed) accordingly to the section number specified
     * in the bundle of the object and adds the associated ListAdapter to the observers list of
     * the dataProvider object.
     *
     * @param inflater The LayoutInflater on which the ListFragment will be inflated. It contains
     *                 the activity context.
     * @param container The ViewGroup containing all the List views.
     * @param savedInstanceState Instance saved from a previous View destruction.
     * @return The view containing the List connected with its adapter.
     * @author Matteo Carnelos
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        switch(sectionNumber) {
            case INCOMING_SECTION_NUMBER:
                ListAdapter incomingPollAdapter = new IncomingPollAdapter(inflater.getContext());
                setListAdapter(incomingPollAdapter);
                dataProvider.addObserver((Observer)incomingPollAdapter);
                break;
            case OPENED_SECTION_NUMBER:
                ListAdapter openedPollAdapter = new OpenedPollAdapter(inflater.getContext());
                setListAdapter(openedPollAdapter);
                dataProvider.addObserver((Observer)openedPollAdapter);
                break;
            case CLOSED_SECTION_NUMBER:
                ListAdapter closedPollAdapter = new ClosedPollAdapter(inflater.getContext());
                setListAdapter(closedPollAdapter);
                dataProvider.addObserver((Observer)closedPollAdapter);
                break;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     * Called when the view of the ListFragment is destroyed. It removes the ListAdapter associated
     * with the ListFragment from the observers list of the dataProvider object.
     *
     * @author Matteo Carnelos
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dataProvider.deleteObserver((Observer)getListAdapter());
    }
}
