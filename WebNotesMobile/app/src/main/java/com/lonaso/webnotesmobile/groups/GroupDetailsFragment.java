package com.lonaso.webnotesmobile.groups;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lonaso.webnotesmobile.IWebNoteAPI;
import com.lonaso.webnotesmobile.ImagePicker;
import com.lonaso.webnotesmobile.MainActivity;
import com.lonaso.webnotesmobile.R;
import com.lonaso.webnotesmobile.users.UserAdapter;
import com.lonaso.webnotesmobile.users.UserStore;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GroupDetailsFragment extends Fragment implements MainActivity.OnBackPressedListener{

    public static final String TAG = "GroupDetailsFragment";
    private ListView userListView;
    private SearchView userSearchView;
    private UserAdapter userAdapter;
    private Button saveGroupButton;
    private ImageView groupImageView;
    private EditText groupNameText;
    private int groupID;
    private Bitmap groupIcon;
    private static final int PICK_IMAGE_ID = 234;


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

        getActivity().setTitle("Détails du groupe");


        Bundle bundle = this.getArguments();
        if(bundle != null) {
            groupID = bundle.getInt("id", 0);
        }

        ((MainActivity)getActivity()).setOnBackPressedListener(this);
        retrieveViews(getView());
        setUpViews(getActivity());
    }

    private void retrieveViews(View view) {
        userListView = (ListView) view.findViewById(R.id.userListView);
        userSearchView = (SearchView) view.findViewById(R.id.userSearch);
        saveGroupButton = (Button) view.findViewById(R.id.saveGroupButton);
        groupImageView = (ImageView) view.findViewById(R.id.groupImageView);
        groupNameText = (EditText) view.findViewById(R.id.groupNameText);
    }

    private void setUpViews(final Activity activity) {
        Thread th = new Thread() {
            @Override
            public void run() {
                UserStore.loadUsersFromGroup(groupID);
                GroupStore.loadGroup(groupID);
            }
        };

        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.err.println("HELLO");
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream in = new URL(GroupStore.GROUP.getIcon()).openStream();
                    groupIcon = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    // log error
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (groupIcon != null)
                    groupImageView.setImageBitmap(groupIcon);
            }

        }.execute();

        groupNameText.setText(GroupStore.GROUP.getName());
//        groupImageView.setImageBitmap(new Bitmap());
        userAdapter = new UserAdapter(activity, groupID);
        userListView.setAdapter(userAdapter);
        userListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Supression d'un membre")
                        .setMessage("Etes-vous certain de vouloir supprimer ce membre du groupe ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                UserStore.removeUser(position);
                                userAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), "Suppression...", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
                return true;
            }
        });

        userSearchView.setSubmitButtonEnabled(true);
        userSearchView.setQueryHint("Nom d'utilisateur ...");

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

        groupImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickImage(view);
            }
        });
    }

    public void onPickImage(View view) {
        Intent chooseImageIntent = ImagePicker.getPickImageIntent(getActivity());
        startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null)
        {
            switch(requestCode) {
                case PICK_IMAGE_ID:
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    groupImageView.setImageBitmap(photo);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        userAdapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    public void doBack() {
        new AlertDialog.Builder(getContext())
                .setTitle("Quitter la page de détails")
                .setMessage("Etes vous sur de ne pas vouloir sauvegarder ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        getFragmentManager().popBackStack();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
