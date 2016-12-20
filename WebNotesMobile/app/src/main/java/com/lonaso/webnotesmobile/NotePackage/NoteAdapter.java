package com.lonaso.webnotesmobile.NotePackage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.List;

/**
 * Created by nicolas on 29.11.16.
 */

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

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
