package com.lonaso.webnotesmobile.NotePackage;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.lonaso.webnotesmobile.R;
import com.lonaso.webnotesmobile.users.UserStore;

public class ListeNote extends Fragment{
    private Button addNewNotebtn;
    private NoteView noteView;
    private ListView noteListView;
    private SearchView noteSearchView;
    private NoteAdapter noteAdapter;
    public static final String TAG = "ListNoteFragment";

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
        getActivity().setTitle("Liste des notes");
        retrieveViews(getView());
        setUpViews(getActivity());
    }

    private void retrieveViews(View view){
        addNewNotebtn = (Button) getActivity().findViewById(R.id.NewNote);
        addNewNotebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Vous avez cree une nouvelle note", Toast.LENGTH_LONG).show();
                loadNewNoteView();

            }
        });
        noteListView = (ListView) view.findViewById(R.id.NoteList);
        noteSearchView = (SearchView) view.findViewById(R.id.NoteSearchView);

    }

    private void setUpViews(final Activity activity){
        Thread thread = new Thread(){
            @Override
            public void run(){
                NoteStore.loadNotesOfUser(UserStore.USER.getId());
                System.out.println(NoteStore.NOTES);
            }
        };

        thread.start();

        try{
            thread.join();
        }catch(InterruptedException e){
            System.out.println("Erreur thread note");
            e.printStackTrace();
            System.err.println("Interrupted Exception Thread setupViewNote");
        }
        noteAdapter = new NoteAdapter(activity);
        noteListView.setAdapter(noteAdapter);
        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Note note =(Note) noteListView.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putInt("id" , note.getId());

                NoteView noteView = (NoteView) getFragmentManager().findFragmentById(R.id.content_frame);

                if(noteView != null && noteView.isInLayout()){
                    Toast.makeText(getContext(), "Affichage du contenu d'un élément", Toast.LENGTH_LONG).show();
                }else{
                    Fragment fragment = new NoteView();
                    if(fragment != null){
                        fragment.setArguments(bundle);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                }
            }
        });

        noteSearchView.setSubmitButtonEnabled(true);
        noteSearchView.setQueryHint("Titre d'une note");
        noteSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){return false;}

            @Override
            public boolean onQueryTextChange(String newText) {
                Filter filter = noteAdapter.getFilter();
                if(TextUtils.isEmpty(newText)){
                    filter.filter(null);
                }else{
                    filter.filter(newText);
                }
                return true;
            }
        });
    }

    private void loadNewNoteView(){
        noteView = new NoteView();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, noteView).commit();
    }

    @Override
    public void onResume(){
        noteAdapter.notifyDataSetChanged();
        super.onResume();
    }
}
