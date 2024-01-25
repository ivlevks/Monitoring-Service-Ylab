package org.ivlevks.application.core.usecase;

public interface Registry {

    void registry(String name, String email, String password, Boolean isAdmin);

}
