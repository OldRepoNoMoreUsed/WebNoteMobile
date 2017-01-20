package com.lonaso.webnotesmobile.groups;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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

import com.lonaso.webnotesmobile.ImagePicker;
import com.lonaso.webnotesmobile.MainActivity;
import com.lonaso.webnotesmobile.R;
import com.lonaso.webnotesmobile.users.UserAdapter;
import com.lonaso.webnotesmobile.users.UserStore;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class GroupDetailsFragment extends Fragment implements MainActivity.OnBackPressedListener{

    public static final String TAG = "GroupDetailsFragment";
    private ListView userListView;
    private SearchView userSearchView;
    private UserAdapter userAdapter;
    private Button saveGroupButton;
    private ImageView groupIconImageView;
    private EditText groupNameEditText;
    private EditText groupDescriptionEditText;
    private int groupID;
    private Bitmap groupIcon;
    private String groupIconPath;
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
        groupIconImageView = (ImageView) view.findViewById(R.id.groupImageView);
        groupNameEditText = (EditText) view.findViewById(R.id.groupNameEditText);
        groupDescriptionEditText = (EditText) view.findViewById(R.id.groupDescriptionEditText);
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
//            System.err.println("HELLO");
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
                    groupIconImageView.setImageBitmap(groupIcon);
            }

        }.execute();

        groupNameEditText.setText(GroupStore.GROUP.getName());
        groupDescriptionEditText.setText(GroupStore.GROUP.getDescription());

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

        groupIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickImage(view);
            }
        });

        saveGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Création d'un objet File depuis l'uri de l'image de l' ImageView
                Bitmap bitmap = ((BitmapDrawable) groupIconImageView.getDrawable()).getBitmap();
                Uri uri = MainActivity.bitmapToUriConverter(getActivity(), bitmap);
                File file = new File(uri.getPath());


                GroupStore.GROUP.setName(groupNameEditText.getText().toString());
                GroupStore.GROUP.setDescription(groupDescriptionEditText.getText().toString());

                // Create RequestBody instance from file
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file);

                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part icon =
                        MultipartBody.Part.createFormData("icon", file.getName(), requestFile);

                // Add another part within the multipart request
                RequestBody description =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), GroupStore.GROUP.getDescription());
                RequestBody name =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), GroupStore.GROUP.getName());

                // Update Group
                GroupStore.updateGroup(name, description, icon, UserStore.USERS);

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
                    groupIconImageView.setImageBitmap(photo);
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
