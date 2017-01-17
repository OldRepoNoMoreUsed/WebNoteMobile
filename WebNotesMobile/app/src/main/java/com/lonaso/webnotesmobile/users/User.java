package com.lonaso.webnotesmobile.users;

import android.text.TextUtils;

import com.lonaso.webnotesmobile.IWebNoteAPI;
import com.lonaso.webnotesmobile.groups.Group;

/**
 *  Class representing users in this application
 *
 *  A user has:
 *   - id
 *   - name
 *   - email
 *   - password
 *   - avatar
 *   - create_at
 *   - updated_at
 */
public class User {

    /*--------------------------------------------------------------------------------------------*\
     *  CONSTRUCTORS
    \*--------------------------------------------------------------------------------------------*/
    public User(int id, String name, String email, String password, String avatar) {
        setId(id);
        setName(name);
        setEmail(email);
        setPassword(password);
        setAvatar(avatar);
    }
    /*--------------------------------------------------------------------------------------------*\
     *  PROPERTIES
    \*--------------------------------------------------------------------------------------------*/
    /**
     *  The id of this user
     */
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * The name of this user
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The email of this user
     */
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * The password of this user
     */
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * The avatar of this user
     */
    private String avatar;

    public String getAvatar() {
        return IWebNoteAPI.STORAGE + avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

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
        User other = (User) obj;

        return TextUtils.equals(getName(), other.getName());
    }

    @Override
    public int hashCode() {
        int result = 1;

        result = 31 * result + (getName() == null ? 0 : getName().hashCode());

        return result;
    }
}
