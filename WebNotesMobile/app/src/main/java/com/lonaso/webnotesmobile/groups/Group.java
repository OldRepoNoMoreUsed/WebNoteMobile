package com.lonaso.webnotesmobile.groups;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lonaso.webnotesmobile.users.User;

import java.util.ArrayList;
import java.util.List;

/**
 *  Class representing groups in this application
 *
 *  A group has:
 *   - id
 *   - name
 *   - description
 *   - icon
 */
public class Group {
    /*--------------------------------------------------------------------------------------------*\
     *  CONSTRUCTORS
    \*--------------------------------------------------------------------------------------------*/
    public Group(int id, String name, String description, String icon) {
        setId(id);
        setName(name);
        setDescription(description);
        setIcon(icon);
    }

    /*--------------------------------------------------------------------------------------------*\
     *  PROPERTIES
    \*--------------------------------------------------------------------------------------------*/
    /**
     *  The id of this group
     */
    private int id;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     *  The name of this group
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     *  The description of this group
     */
    private String description;
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *  The icon of this group
     */
    private String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     *  The list of user of this group
     */
    private ArrayList<User> users;

    public ArrayList<User> getUsers() { return users; }

    public void setUsers(ArrayList<User> users) { this.users = users; }

    /*--------------------------------------------------------------------------------------------*\
     *  OBJECT METHOD OVERRIDES
    \*--------------------------------------------------------------------------------------------*/
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if(!(obj instanceof Group)) {
            return false;
        }
        Group other = (Group) obj;

        return TextUtils.equals(getName(), other.getName());
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (getName() == null ? 0 : getName().hashCode());

        return result;
    }

    /*--------------------------------------------------------------------------------------------*\
     *  OBJECT METHOD OVERRIDES
    \*--------------------------------------------------------------------------------------------*/
    /**
     *
     * @param response
     * @return
     */
    public static Group parseJSON(String response) {
        Gson gson = new GsonBuilder().create();
        Group group = gson.fromJson(response, Group.class);
        return group;
    }
}
