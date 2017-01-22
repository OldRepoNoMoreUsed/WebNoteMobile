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

public class UpdateUserFragment extends Fragment implements MainActivity.OnBackPressedListener{

    public static final String TAG = "UpateUserFragment";
    private Button saveUserButton;
    private ImageView userAvatarImageView;
    private EditText userNameEditText;
    private EditText userEmailEditText;
    private Bitmap userAvatar;
    private static final int PICK_IMAGE_ID = 234;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_user_update, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("Mon compte");

        ((MainActivity)getActivity()).setOnBackPressedListener(this);
        retrieveViews(getView());
        setUpViews(getActivity());
    }

    private void retrieveViews(View view) {
        saveUserButton = (Button) view.findViewById(R.id.saveUserButton);
        userAvatarImageView = (ImageView) view.findViewById(R.id.userImageView);
        userNameEditText = (EditText) view.findViewById(R.id.userNameEditText);
        userEmailEditText = (EditText) view.findViewById(R.id.userEmailEditText);
    }

    private void setUpViews(final Activity activity) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream in = new URL(UserStore.USER.getAvatar()).openStream();
                    userAvatar = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    // log error
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (userAvatar != null)
                    userAvatarImageView.setImageBitmap(userAvatar);
            }

        }.execute();


        userNameEditText.setText(UserStore.USER.getName());
        userEmailEditText.setText(UserStore.USER.getEmail());

        userAvatarImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPickImage(view);
            }
        });

        saveUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Création d'un objet File depuis l'uri de l'image de l' ImageView
                Bitmap bitmap = ((BitmapDrawable) userAvatarImageView.getDrawable()).getBitmap();
                Uri uri = MainActivity.bitmapToUriConverter(getActivity(), bitmap);
                File file = new File(uri.getPath());

                UserStore.USER.setAvatar(uri.getPath());
                UserStore.USER.setName(userNameEditText.getText().toString());
                UserStore.USER.setEmail(userEmailEditText.getText().toString());

                // Create RequestBody instance from file
                RequestBody requestFile =
                        RequestBody.create(MediaType.parse("multipart/form-data"), file);

                // MultipartBody.Part is used to send also the actual file name
                MultipartBody.Part avatar =
                        MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

                // Add another part within the multipart request
                RequestBody email =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), UserStore.USER.getEmail());
                RequestBody name =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), UserStore.USER.getName());

                // Update Group
                UserStore.updateUser(name, email, avatar);

                Toast.makeText(getContext(), "Enregistré...", Toast.LENGTH_SHORT).show();

                MainActivity.setNavHeader(UserStore.USER.getName(), UserStore.USER.getEmail(), UserStore.USER.getAvatar());
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
                    userAvatarImageView.setImageBitmap(photo);
                    break;
                default:
                    break;
            }
        }
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
