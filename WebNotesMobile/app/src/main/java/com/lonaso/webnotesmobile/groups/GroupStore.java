package com.lonaso.webnotesmobile.groups;

import java.util.ArrayList;
import java.util.List;

public class GroupStore {

    public static final List<Group> GROUPS = new ArrayList<>();

    static {
        for(int i = 0; i < 10; i++) {
            GROUPS.add(new Group(i, "Group name " + i, "Description " + i, "icon" + 1 + ".png"));
        }
    }

    public static Group findGroupById(int id) {
        Group result = null;

        for(Group group : GROUPS) {
            if(group.getId() == id) {
                result = group;
            }
        }

        return result;
    }

    public static Group findGroupByName(int name) {
        Group result = null;

        for(Group group : GROUPS) {
            if(group.getName().equals(name)) {
                result = group;
            }
        }

        return result;
    }
}
