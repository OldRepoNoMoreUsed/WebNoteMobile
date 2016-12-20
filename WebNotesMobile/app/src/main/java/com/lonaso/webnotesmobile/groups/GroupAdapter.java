package com.lonaso.webnotesmobile.groups;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.lonaso.webnotesmobile.R;
import com.lonaso.webnotesmobile.groups.Group;
import com.lonaso.webnotesmobile.groups.GroupStore;

import java.util.ArrayList;
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
        filteredGroups = GroupStore.GROUPS;

        groupFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if(constraint == null) {
                    filteredGroups = GroupStore.GROUPS;
                } else {
                    filteredGroups = new ArrayList<>();
                    for(Group group : GroupStore.GROUPS) {
                        if(group.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredGroups.add(group);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredGroups;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getCount() {
        return filteredGroups.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredGroups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GroupHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.group_list_row, parent, false);

            holder = new GroupHolder();

            holder.nameTextView = (TextView) convertView.findViewById(R.id.groupNameTextView);
            holder.descriptionTextView = (TextView) convertView.findViewById(R.id.groupDescriptionTextView);

            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }

        Group group = filteredGroups.get(position);

        holder.nameTextView.setText(group.getName());
        holder.descriptionTextView.setText(group.getDescription());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return groupFilter;
    }

    /**
     *  Wrapper class for our group views
     */
    private static class GroupHolder {
        TextView nameTextView;
        TextView descriptionTextView;
    }
}
