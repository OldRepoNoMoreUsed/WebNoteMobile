package com.lonaso.webnotesmobile.notes;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lonaso.webnotesmobile.MainActivity;
import com.lonaso.webnotesmobile.R;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class NoteViewFragment extends Fragment implements MainActivity.OnBackPressedListener{


    public static final String TAG = "NoteViewFragment";
    private EditText noteTitleTextEdit;
    private EditText noteDescriptionEditText;
    private EditText noteContentEditText;
    private Button saveNoteBtn;
    private int noteID;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.note_layout, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setTitle("Edition de la note");

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            noteID = bundle.getInt("id", 0);
        }

        ((MainActivity)getActivity()).setOnBackPressedListener(this);
        retrieveViews(getView());
        setUpViews(getActivity());
    }

    private void retrieveViews(View view) {
        noteTitleTextEdit = (EditText) view.findViewById(R.id.noteTitleEditText);
        noteDescriptionEditText = (EditText) view.findViewById(R.id.noteDescriptionEditText);
        noteContentEditText = (EditText) view.findViewById(R.id.noteContentEditText);
        saveNoteBtn = (Button) view.findViewById(R.id.saveNoteBtn);
    }

    private void setUpViews(final Activity activity) {
        System.out.println("Line 69 : " + noteID);
        if(noteID != 0) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    NoteStore.loadNote(noteID);
                }
            };
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            noteTitleTextEdit.setText(NoteStore.NOTE.getTitle());
            noteTitleTextEdit.setEnabled(false);
            noteDescriptionEditText.setText(NoteStore.NOTE.getDescription());
            noteDescriptionEditText.setEnabled(false);
            noteContentEditText.setText(NoteStore.NOTE.getContent());
        }

        saveNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestBody title =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), noteTitleTextEdit.getText().toString());
                RequestBody description =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), noteDescriptionEditText.getText().toString());
                RequestBody content =
                        RequestBody.create(
                                MediaType.parse("multipart/form-data"), noteContentEditText.getText().toString());

                if(noteID != 0) {
                    NoteStore.NOTE.setContent(noteContentEditText.getText().toString());

                    NoteStore.updateNote(content);
                } else {
                    NoteStore.createNote(title, description, content);
                }
                Toast.makeText(getContext(), "Enregistrée...", Toast.LENGTH_SHORT).show();
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
    }
}
