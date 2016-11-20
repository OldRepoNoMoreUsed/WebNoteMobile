package com.lonaso.webnotesmobile.groups;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;

import com.lonaso.webnotesmobile.R;
import com.lonaso.webnotesmobile.users.UserAdapter;

public class GroupDetailsFragment extends Fragment {

    public static final String TAG = "GroupDetailsFragment";
    private ListView userListView;
    private SearchView userSearchView;
    private UserAdapter userAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_details, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        retrieveViews(getView());
        setUpViews(getActivity());
    }

    private void retrieveViews(View view) {
        userListView = (ListView) view.findViewById(R.id.userListView);
        userSearchView = (SearchView) view.findViewById(R.id.userSearch);
    }

    private void setUpViews(final Activity activity) {
        userAdapter = new UserAdapter(activity);
        userListView.setAdapter(userAdapter);
//        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Group group = (Group) groupListView.getItemAtPosition(position);
//
//                GroupDetailsFragment groupDetailsFragment = (GroupDetailsFragment) getFragmentManager().findFragmentById(R.id.groupDetailsFragment);
//
//                if(groupDetailsFragment != null && groupDetailsFragment.isInLayout()) {
//                    //groupDetailsFragment.updateDetails(group);
//                } else {
//                    Fragment fragment = new GroupDetailsFragment();
//                    //replacing the fragment
//                    if (fragment != null) {
//                        FragmentTransaction ft = getFragmentManager().beginTransaction();
//                        ft.replace(R.id.content_frame, fragment);
//                        ft.commit();
//                    }
//                }
//            }
//        });

        userSearchView.setSubmitButtonEnabled(true);
        userSearchView.setQueryHint("Nom du groupe ...");

        userSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Filter filter = userAdapter.getFilter();

                if(TextUtils.isEmpty(newText)) {
                    filter.filter(null);
                } else {
                    filter.filter(newText);
                }

                return true;
            }
        });
    }

    @Override
    public void onResume() {
        userAdapter.notifyDataSetChanged();
        super.onResume();
    }
}
