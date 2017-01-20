package com.lonaso.webnotesmobile.groups;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.lonaso.webnotesmobile.MainActivity;
import com.lonaso.webnotesmobile.R;
import com.lonaso.webnotesmobile.users.User;
import com.lonaso.webnotesmobile.users.UserAdapter;
import com.lonaso.webnotesmobile.users.UserStore;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class GroupAddUsersFragment extends Fragment implements MainActivity.OnBackPressedListener {

    public static final String TAG = "GroupAddUsersFragment";
    private ListView addUserListView;
    private SearchView addUserSearchView;
    private UserAdapter userAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_list_add_users, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("Ajouter des membres");


        ((MainActivity)getActivity()).setOnBackPressedListener(this);
        retrieveViews(getView());
        setUpViews(getActivity());
    }

    private void retrieveViews(View view) {
        addUserListView = (ListView) view.findViewById(R.id.addUserListView);
        addUserSearchView = (SearchView) view.findViewById(R.id.addUserSearchView);
    }

    private void setUpViews(final Activity activity) {
        Thread th = new Thread() {
            @Override
            public void run() {
                UserStore.loadUsers();
            }
        };
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        userAdapter = new UserAdapter(activity);
        addUserListView.setAdapter(userAdapter);
        addUserListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                User user = (User) addUserListView.getItemAtPosition(position);

                GroupStore.addUser(user.getId());

                Toast.makeText(getContext(), "Ajouté", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        addUserSearchView.setSubmitButtonEnabled(true);
        addUserSearchView.setQueryHint("Nom de l'utilisateur ...");

        addUserSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
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

    @Override
    public void doBack() {
        getFragmentManager().popBackStack();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
