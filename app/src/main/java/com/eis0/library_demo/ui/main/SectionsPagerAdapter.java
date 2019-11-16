package com.eis0.library_demo.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.eis0.library_demo.R;

/**
 * A FragmentPagerAdapter that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 * @author Matteo Carnelos.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3};
    private final Context mContext;

    /**
     * Constructor for the SectionsPageAdapter.
     * Calls the FragmentPagerAdapter constructor and sets the context.
     * @param context Context given from the activity.
     * @param fm Fragment manager that manage FragmentPagerAdapter behaviour.
     * @author Matteo Carnelos.
     */
    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    /**
     * Returns the Fragment associated with a specified position.
     * @param position The position of the item.
     * @return The associated Fragment.
     * @author Matteo Carnelos.
     */
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page
        // Return a PlaceholderFragment (defined in PlaceholderFragment.java)
        return PlaceholderFragment.newInstance(position + 1);
    }

    /**
     * Returns the page title associated with a specified position.
     * @param position The position of the page.
     * @return The associated title as a CharSequence.
     * @author Matteo Carnelos.
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    /**
     * Returns the number of tabs. In this case is the TAB_TITLES string length.
     * @return The number of sections/tabs/pages.
     */
    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}