package com.lonaso.webnotesmobile.groups;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.lonaso.webnotesmobile.R;
import com.lonaso.webnotesmobile.groups.Group;
import com.lonaso.webnotesmobile.groups.GroupStore;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class GroupAdapter extends BaseAdapter implements Filterable {

    private Context context;

    private List<Group> filteredGroups;

    private Filter groupFilter;

    private Bitmap groupIcon;

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
        final GroupHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.group_list_row, parent, false);

            holder = new GroupHolder();

            holder.nameTextView = (TextView) convertView.findViewById(R.id.groupNameTextView);
            holder.descriptionTextView = (TextView) convertView.findViewById(R.id.groupDescriptionTextView);
            holder.icon = (ImageView) convertView.findViewById(R.id.groupImageView);

            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }

        final Group group = filteredGroups.get(position);

        holder.nameTextView.setText(group.getName());
        holder.descriptionTextView.setText(group.getDescription());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream in = new URL(group.getIcon()).openStream();
                    groupIcon = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    // log error
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (groupIcon != null)
                    holder.icon.setImageBitmap(groupIcon);
            }

        }.execute();

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
        ImageView icon;
    }
}
