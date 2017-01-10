package com.lonaso.webnotesmobile.users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Switch;
import android.widget.TextView;

import com.lonaso.webnotesmobile.R;
import com.lonaso.webnotesmobile.groups.Group;
import com.lonaso.webnotesmobile.groups.GroupStore;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends BaseAdapter implements Filterable {

    private Context context;

    private List<User> filteredUsers;

    private Filter userFilter;

    public UserAdapter(Context context) {
        super();
        this.context = context;
        construct(0);
    }

    public UserAdapter(Context context, int groupID) {
        super();
        this.context = context;
        construct(groupID);
    }

    private void construct(int groupID) {
        UserStore.USERS = GroupStore.GROUPS.get(groupID).getUsers();
        filteredUsers = UserStore.USERS;

        userFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if(constraint == null) {
                    filteredUsers = UserStore.USERS;
                } else {
                    filteredUsers = new ArrayList<>();
                    for(User user : UserStore.USERS) {
                        if(user.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            filteredUsers.add(user);
                        }
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredUsers;

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
        return filteredUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_list_row, parent, false);

            holder = new UserHolder();

            holder.name = (TextView) convertView.findViewById(R.id.userName);

            convertView.setTag(holder);
        } else {
            holder = (UserHolder) convertView.getTag();
        }

        User user = filteredUsers.get(position);

        holder.name.setText(user.getName());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    /**
     *  Wrapper class for our user views
     */
    private static class UserHolder {
        TextView name;
    }
}
