package com.lonaso.webnotesmobile;

/**
 * Created by daniel on 02.11.16.
 */

public class Group {

    /*---------------------------------------------------------------------------------------------\
     *
     *
     *
    \*--------------------------------------------------------------------------------------------*/
    public Group(int id, String name, String description, String icon) {
        setId(id);
        setName(name);
        setDescription(description);
        setIcon(icon);
    }

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


}
