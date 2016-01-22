package com.example.kartishe.test2;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * Created by kartishe on 10/14/15.
 */
// Instances of this class are fragments representing a single
// object in our collection.
public class DemoObjectFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    private FragmentTabHost mTabHost;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // The last two arguments ensure LayoutParams are inflated
        // properly.
/*
        View rootView = inflater.inflate(
                R.layout.fragment_layout, container, false);
        Bundle args = getArguments();

        ((TextView) rootView.findViewById(android.R.id.text1)).setText(
                Integer.toString(args.getInt(ARG_OBJECT)));
        return rootView;
*/

        mTabHost = new FragmentTabHost(getActivity());
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.layout.not_working_tab);

        Bundle args = getArguments();
        mTabHost.addTab(mTabHost.newTabSpec("Tab1").setIndicator("CSR"+args.getInt(ARG_OBJECT)),
                Tab1Fragment.class,args);
        mTabHost.addTab(mTabHost.newTabSpec("Tab2").setIndicator("Intf"),
                Tab2Fragment.class,args);
        mTabHost.addTab(mTabHost.newTabSpec("Tab3").setIndicator("Memory"),
                Tab3Fragment.class,args);
/*
        for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {

            final TextView tv = (TextView) mTabHost.getTabWidget().getChildAt(i)
                    .findViewById(android.R.id.title);
            mTabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.RED);

            if (tv == null)
                continue;
            else
                tv.setTextColor(0xFFFFFFFF);

        }
*/
        return mTabHost;
    }

}
