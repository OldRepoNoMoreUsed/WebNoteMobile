package com.lonaso.webnotesmobile.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.lonaso.webnotesmobile.R;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends BaseAdapter implements Filterable{

    private Context context;
    private List<Note> filteredNotes;
    private Filter noteFilter;

    public NoteAdapter(Context context) {
        super();
        this.context = context;
        construct();
    }

    public void construct(){
        filteredNotes = NoteStore.NOTES;

        noteFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint == null) {
                    filteredNotes = NoteStore.NOTES;
                } else {
                    filteredNotes = new ArrayList<>();
                    for (Note note : NoteStore.NOTES) {
                        if (note.getTitle().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredNotes.add(note);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredNotes;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                notifyDataSetChanged();
            }
        };

    }

    @Override
    public int getCount() {
        return filteredNotes.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredNotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final NoteHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.note_list_row, parent, false);
            holder = new NoteHolder();
            holder.titleTextView = (TextView) convertView.findViewById(R.id.noteNameTextView);
            holder.descriptionTextView = (TextView) convertView.findViewById(R.id.noteDescriptionTextView);
            convertView.setTag(holder);
        }
        else{
            holder = (NoteHolder) convertView.getTag();
        }
        final Note note = filteredNotes.get(position);
        holder.titleTextView.setText(note.getTitle());
        holder.descriptionTextView.setText(note.getDescription());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        return noteFilter;
    }

    private static class NoteHolder{
        TextView titleTextView;
        TextView descriptionTextView;
    }
}
