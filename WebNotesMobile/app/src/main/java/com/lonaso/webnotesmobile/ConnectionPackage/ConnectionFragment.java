package com.lonaso.webnotesmobile.ConnectionPackage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.lonaso.webnotesmobile.MainActivity;
import com.lonaso.webnotesmobile.NotePackage.ListeNote;
import com.lonaso.webnotesmobile.R;
import com.lonaso.webnotesmobile.users.User;
import com.lonaso.webnotesmobile.users.UserStore;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.support.v4.widget.DrawerLayout.LOCK_MODE_UNLOCKED;

public class ConnectionFragment extends Fragment{

    public static final String TAG = "ConnectionFragment";
    //private ImageView appLogo;
    private EditText userEmail;
    private EditText userName;
    private Button connect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connection, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        retrieveViews(getView());
        setUpViews(getActivity());
        getActivity().setTitle("Connexion");
    }

    private void retrieveViews(View view) {
        userEmail = (EditText) view.findViewById(R.id.user_email);
        userName = (EditText) view.findViewById(R.id.user_name);
        connect = (Button) view.findViewById(R.id.connect);

    }

    private void setUpViews(final Activity activity) {

        // Connection button click
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final RequestBody email =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), userEmail.getText().toString());

                final RequestBody name =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), userName.getText().toString());

                // API Call
                Thread thread = new Thread(){
                    @Override
                    public void run(){
                        UserStore.authUser(email, name);
                    }
                };
                thread.start();
                try{
                    thread.join();
                }catch(InterruptedException e){
                    e.printStackTrace();
                    System.err.println("FUCK");
                }

                // User exist
                if(UserStore.USER.getName() != null) {
                    Fragment fragment = new ListeNote();
                    if(fragment != null) {
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                    MainActivity.setNavHeader(UserStore.USER.getName(), UserStore.USER.getEmail(), UserStore.USER.getAvatar());
                    MainActivity.setDrawerLock(LOCK_MODE_UNLOCKED);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
