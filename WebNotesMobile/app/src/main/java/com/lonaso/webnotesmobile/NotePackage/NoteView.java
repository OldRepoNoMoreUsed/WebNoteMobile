package com.lonaso.webnotesmobile.NotePackage;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lonaso.webnotesmobile.MainActivity;
import com.lonaso.webnotesmobile.R;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

/**
 * Created by nicolas on 08.11.16.
 */

public class NoteView extends Fragment implements MainActivity.OnBackPressedListener{
    protected Button saveBtn;
    protected ListeNote liste;
    protected Note note;
    protected NoteStore noteStore;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.note_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).setOnBackPressedListener(this);

        noteStore = ((MainActivity)getActivity()).getNoteStore();

        getActivity().setTitle("Edition des notes");

        saveBtn = (Button) getActivity().findViewById(R.id.Save);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Note enregistre", Toast.LENGTH_LONG).show();
                saveNote();
                loadListNote();

            }
        });
    }

    @Override
    public void doBack(){
        new AlertDialog.Builder(getContext())
                .setTitle("Vous allez quitter la note")
                .setMessage("Etes vous sur de ne pas vouloir sauvegarder ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        onDestroy();
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
        loadListNote();
    }

    private void loadListNote() {
        liste = new ListeNote();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, liste).commit();
    }

    private void saveNote(){
        EditText title = (EditText) getActivity().findViewById(R.id.Title);
        EditText description = (EditText) getActivity().findViewById(R.id.Description);
        MultiAutoCompleteTextView content = (MultiAutoCompleteTextView) getActivity().findViewById(R.id.Content);

        note = new Note(title.getText().toString(), description.getText().toString(), content.getText().toString());
    }
}
