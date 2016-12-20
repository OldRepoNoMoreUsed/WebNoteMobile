package com.lonaso.webnotesmobile.NotePackage;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.lonaso.webnotesmobile.R;

/**
 * Created by nicolas on 08.11.16.
 */

public class ListeNote extends Fragment{
    private Button addNewNotebtn = null;
    private NoteView noteView = null;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        return inflater.inflate(R.layout.note_liste_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Liste des notes");
        addNewNotebtn = (Button) getActivity().findViewById(R.id.NewNote);
        addNewNotebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Vous avez cree une nouvelle note", Toast.LENGTH_LONG).show();
                loadNewNoteView();

            }
        });
    }

    private void loadNewNoteView(){
        noteView = new NoteView();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, noteView).commit();
    }
}
