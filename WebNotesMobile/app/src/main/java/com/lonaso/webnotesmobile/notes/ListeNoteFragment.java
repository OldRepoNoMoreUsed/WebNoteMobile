package com.lonaso.webnotesmobile.notes;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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
import com.lonaso.webnotesmobile.groups.NewGroupFragment;
import com.lonaso.webnotesmobile.users.UserStore;

public class ListeNoteFragment extends Fragment{
    private Button addNewNotebtn;
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
        noteListView = (ListView) view.findViewById(R.id.NoteList);
        noteSearchView = (SearchView) view.findViewById(R.id.NoteSearchView);

    }

    private void setUpViews(final Activity activity){
        Thread thread = new Thread(){
            @Override
            public void run(){
                NoteStore.loadNotesOfUser(UserStore.USER.getId());
            }
        };

        thread.start();

        try{
            thread.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        noteAdapter = new NoteAdapter(activity);
        noteListView.setAdapter(noteAdapter);
        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Note note =(Note) noteListView.getItemAtPosition(position);

                Bundle bundle = new Bundle();
                bundle.putInt("id" , note.getId());

                NoteViewFragment noteViewFragment = (NoteViewFragment) getFragmentManager().findFragmentById(R.id.noteViewFragment);

                if(noteViewFragment != null && noteViewFragment.isInLayout()){
//                    Toast.makeText(getContext(), "Affichage du contenu d'un élément", Toast.LENGTH_LONG).show();
                }else{
                    Fragment fragment = new NoteViewFragment();
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
        noteSearchView.setQueryHint("Titre d'une note...");
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

        addNewNotebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", 0);
                Fragment fragment = new NoteViewFragment();
                //replacing the fragment
                if (fragment != null) {
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.addToBackStack(null);
                    ft.commit();

                }
            }
        });
    }

    @Override
    public void onResume(){
        noteAdapter.notifyDataSetChanged();
        super.onResume();
    }
}
