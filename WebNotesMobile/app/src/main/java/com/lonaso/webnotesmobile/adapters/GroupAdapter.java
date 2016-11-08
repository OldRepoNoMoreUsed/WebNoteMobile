package com.lonaso.webnotesmobile.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.lonaso.webnotesmobile.Group;

import java.util.List;

public class GroupAdapter extends BaseAdapter implements Filterable {

    private Context context;

    private List<Group> filteredGroups;

    private Filter groupFilter;

    public GroupAdapter(Context context) {
        super();
        this.context = context;
        construct();
    }

    private void construct() {
//        filteredGroups = 
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
