package org.ivlevks.service;

import org.starter.annotations.Loggable;
import org.springframework.stereotype.Service;

@Loggable
@Service
public class AdminHelper {
    private int idCurrentUser = 0;

    public AdminHelper() {
    }

    public boolean validateIdUser(int id) {
        return idCurrentUser == id;
    }

    public int getIdCurrentUser() {
        return idCurrentUser;
    }

    public void setIdCurrentUser(int idCurrentUser) {
        this.idCurrentUser = idCurrentUser;
    }
}
