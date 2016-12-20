package com.lonaso.webnotesmobile.groups;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.lonaso.webnotesmobile.ImagePicker;
import com.lonaso.webnotesmobile.MainActivity;
import com.lonaso.webnotesmobile.R;
import com.lonaso.webnotesmobile.users.UserAdapter;

public class GroupDetailsFragment extends Fragment implements MainActivity.OnBackPressedListener{

    public static final String TAG = "GroupDetailsFragment";
    private ListView userListView;
    private SearchView userSearchView;
    private UserAdapter userAdapter;
    private Button saveGroupButton;
    private ImageView groupImageView;
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
//        getActivity()/

        ((MainActivity)getActivity()).setOnBackPressedListener(this);
        retrieveViews(getView());
        setUpViews(getActivity());
    }

    private void retrieveViews(View view) {
        userListView = (ListView) view.findViewById(R.id.userListView);
        userSearchView = (SearchView) view.findViewById(R.id.userSearch);
        saveGroupButton = (Button) view.findViewById(R.id.saveGroupButton);
        groupImageView = (ImageView) view.findViewById(R.id.groupImageView);
    }

    private void setUpViews(final Activity activity) {
        userAdapter = new UserAdapter(activity);
        userListView.setAdapter(userAdapter);
        userListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Supression d'un membre")
                        .setMessage("Etes-vous certain de vouloir supprimer ce membre du groupe ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
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
//                        onDestroy();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        Toast.makeText(getContext(), "Vous n'avez pas quitté l'application", Toast.LENGTH_LONG).show();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    @Override
    public void onDestroy(){
        Toast.makeText(getContext(), "Destroy", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }
}
