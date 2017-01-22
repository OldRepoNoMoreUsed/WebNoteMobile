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
import android.support.v4.app.FragmentTransaction;
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

public class NewGroupFragment extends Fragment implements MainActivity.OnBackPressedListener{

    public static final String TAG = "NewGroupFragment";
    private Button saveGroupButton;
    private EditText groupNameEditText;
    private EditText groupDescriptionEditText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_new_group, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("Nouveau groupe");

        ((MainActivity)getActivity()).setOnBackPressedListener(this);
        retrieveViews(getView());
        setUpViews(getActivity());
    }

    private void retrieveViews(View view) {
        saveGroupButton = (Button) view.findViewById(R.id.saveGroupButton);
        groupNameEditText = (EditText) view.findViewById(R.id.groupNameEditText);
        groupDescriptionEditText = (EditText) view.findViewById(R.id.groupDescriptionEditText);
    }

    private void setUpViews(final Activity activity) {


        saveGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add another part within the multipart request
                final RequestBody description =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), groupDescriptionEditText.getText().toString());
                final RequestBody name =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), groupNameEditText.getText().toString());


                // Create Group
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        GroupStore.createGroup(name, description, UserStore.USER);
                    }
                };

                thread.start();

                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Toast.makeText(getContext(), "Enregistré...", Toast.LENGTH_SHORT).show();

                getFragmentManager().popBackStack();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void doBack() {
        new AlertDialog.Builder(getContext())
                .setTitle("Quitter la page")
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
