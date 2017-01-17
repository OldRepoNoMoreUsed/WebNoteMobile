package com.lonaso.webnotesmobile.users;

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

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends BaseAdapter implements Filterable {

    private Context context;

    private List<User> filteredUsers;

    private Filter userFilter;
    private Bitmap userAvatar;
    private User user;

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

    private void construct(final int groupID) {
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
        final UserHolder holder;

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.user_list_row, parent, false);

            holder = new UserHolder();

            holder.nameTextView = (TextView) convertView.findViewById(R.id.userNameTextView);
            holder.avatarImageView = (ImageView) convertView.findViewById(R.id.userAvatarImageView);

            convertView.setTag(holder);
        } else {
            holder = (UserHolder) convertView.getTag();
        }

        user = filteredUsers.get(position);

        holder.nameTextView.setText(user.getName());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    InputStream in = new URL(user.getAvatar()).openStream();
                    userAvatar = BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    // log error
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (userAvatar != null)
                    holder.avatarImageView.setImageBitmap(userAvatar);
            }

        }.execute();

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
        TextView nameTextView;
        ImageView avatarImageView;
    }
}
