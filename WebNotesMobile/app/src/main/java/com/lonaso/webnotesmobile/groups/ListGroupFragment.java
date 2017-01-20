package com.lonaso.webnotesmobile.groups;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;

import com.lonaso.webnotesmobile.MainActivity;
import com.lonaso.webnotesmobile.R;
import com.lonaso.webnotesmobile.users.UserStore;

import java.util.List;


public class ListGroupFragment extends Fragment implements MainActivity.OnBackPressedListener {

    public static final String TAG = "ListGroupFragment";
    private Button newGroupButton;
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((MainActivity) getActivity()).setOnBackPressedListener(this);
        getActivity().setTitle("Liste des groupes");
        retrieveViews(getView());
        setUpViews(getActivity());
    }

    private void retrieveViews(View view) {
        newGroupButton = (Button) view.findViewById(R.id.newGroupButton);
        groupListView = (ListView) view.findViewById(R.id.groupListView);
        groupSearchView = (SearchView) view.findViewById(R.id.groupSearch);

    }

    private void setUpViews(final Activity activity) {
        Thread th = new Thread() {
            @Override
            public void run() {
                GroupStore.loadGroups();
            }
        };

        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        groupAdapter = new GroupAdapter(activity);
        groupListView.setAdapter(groupAdapter);
        groupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group group = (Group) groupListView.getItemAtPosition(position);

                Bundle bundle = new Bundle();
                bundle.putInt("id", group.getId());

                GroupDetailsFragment groupDetailsFragment = (GroupDetailsFragment) getFragmentManager().findFragmentById(R.id.groupDetailsFragment);

                if (groupDetailsFragment != null && groupDetailsFragment.isInLayout()) {
                    //groupDetailsFragment.updateDetails(group);
                } else {
                    Fragment fragment = new GroupDetailsFragment();
                    //replacing the fragment
                    if (fragment != null) {
                        fragment.setArguments(bundle);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
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

                if (TextUtils.isEmpty(newText)) {
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
                    ft.addToBackStack(null);
                    ft.commit();

                }
            }
        });
    }

    @Override
    public void doBack() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        groupAdapter.notifyDataSetChanged();
        super.onResume();
    }
}
