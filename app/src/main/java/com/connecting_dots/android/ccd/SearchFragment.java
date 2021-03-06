package com.connecting_dots.android.ccd;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Dell on 04-10-2016.
 */

public class SearchFragment extends Fragment {

    public static String[] imageUrls={"abc","def"};
    private final String Log_Tag = SearchFragment.class.getSimpleName();

    private ProfessionListAdapter professionListAdapter;
    public SearchFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(Log_Tag, "Forecast asa: " );
        View rootView = inflater.inflate(R.layout.fragment_employer, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listView);

        professionListAdapter = new ProfessionListAdapter(getActivity());
        professionListAdapter.clear();

        listView.setAdapter(professionListAdapter);
        return rootView;
    }

}

