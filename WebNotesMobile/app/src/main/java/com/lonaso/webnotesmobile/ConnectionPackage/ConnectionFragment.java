package com.lonaso.webnotesmobile.ConnectionPackage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.lonaso.webnotesmobile.R;
import com.lonaso.webnotesmobile.groups.ListGroupFragment;

/**
 * Created by steve.nadalin on 27/11/2016.
 */

public class ConnectionFragment extends Fragment{

    public static final String TAG = "ConnectionFragment";
    private ImageView appLogo;
    private Button createAccount;
    private EditText userEmail;
    private EditText userPassword;
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
    }

    private void retrieveViews(View view) {
        createAccount = (Button) view.findViewById(R.id.create_account);
        userEmail = (EditText) view.findViewById(R.id.user_email);
        userPassword = (EditText) view.findViewById(R.id.user_password);
        connect = (Button) view.findViewById(R.id.connect);

    }

    private void setUpViews(final Activity activity) {
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new CreateAccountFragment();
                if (fragment != null) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.commit();
                }
            }
        });

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ListGroupFragment();
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
        super.onResume();
    }
}
