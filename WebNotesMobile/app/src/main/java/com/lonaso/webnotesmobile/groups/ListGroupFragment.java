package com.lonaso.webnotesmobile.groups;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;

import com.lonaso.webnotesmobile.R;


public class ListGroupFragment extends Fragment {

    public static final String TAG = "ListGroupFragment";
    private FloatingActionButton newGroupButton;
    private ListView groupListView;
    private SearchView groupSearchView;
    private GroupAdapter groupAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_group, container, false);
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        //you can set the title for your toolbar here for different fragments different titles
//        getActivity().setTitle("Groupes");
//    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        retrieveViews(getView());
        setUpViews(getActivity());
    }

    private void retrieveViews(View view) {
        newGroupButton = (FloatingActionButton) view.findViewById(R.id.newGroupButton);
        groupListView = (ListView) view.findViewById(R.id.groupListView);
        groupSearchView = (SearchView) view.findViewById(R.id.groupSearch);

    }

    private void setUpViews(final Activity activity) {
        groupAdapter = new GroupAdapter(activity);
        groupListView.setAdapter(groupAdapter);
        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group group = (Group) groupListView.getItemAtPosition(position);

                GroupDetailsFragment groupDetailsFragment = (GroupDetailsFragment) getFragmentManager().findFragmentById(R.id.groupDetailsFragment);

                if(groupDetailsFragment != null && groupDetailsFragment.isInLayout()) {
                    //groupDetailsFragment.updateDetails(group);
                } else {
                    Fragment fragment = new GroupDetailsFragment();
                    //replacing the fragment
                    if (fragment != null) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.commit();
                    }
                }
            }
        });

        groupSearchView.setSubmitButtonEnabled(true);
        groupSearchView.setQueryHint("Nom du groupe ...");

        groupSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Filter filter = groupAdapter.getFilter();

                if(TextUtils.isEmpty(newText)) {
                    filter.filter(null);
                } else {
                    filter.filter(newText);
                }

                return true;
            }
        });

        newGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new GroupDetailsFragment();
                //replacing the fragment
                if (fragment != null) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }
            }
        });
    }

    @Override
    public void onResume() {
        groupAdapter.notifyDataSetChanged();
        super.onResume();
    }
}