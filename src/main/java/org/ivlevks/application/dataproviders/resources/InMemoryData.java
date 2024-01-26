package org.ivlevks.application.dataproviders.resources;

import org.ivlevks.application.core.entity.Indication;
import org.ivlevks.application.core.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class InMemoryData {

    private ArrayList<User> users = new ArrayList<>();
    private final HashMap<User, LinkedList<Indication>> storageIndicationAllUsers = new HashMap<>();

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }

    public HashMap<User, LinkedList<Indication>> getStorageIndicationAllUsers() {
        return storageIndicationAllUsers;
    }
}
