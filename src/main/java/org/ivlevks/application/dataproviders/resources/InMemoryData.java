package org.ivlevks.application.dataproviders.resources;

import org.ivlevks.application.core.entity.Indication;
import org.ivlevks.application.core.entity.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Класс хранилища данных в памяти приложения
 * хранение пользователей осуществляется в Liste
 * хранение показаний осуществляется в хэшмапе с доступом по ключу - Юзеру
 */
public class InMemoryData {

    private final ArrayList<User> users = new ArrayList<>();
    private final HashMap<User, LinkedList<Indication>> storageIndicationAllUsers = new HashMap<>();

    /**
     * Получение списка всех пользователей
     * @return список пользователей
     */
    public ArrayList<User> getUsers() {
        return users;
    }

    /**
     * Получение хранилища показаний
     * @return хэшмап с показаниями
     */
    public HashMap<User, LinkedList<Indication>> getStorageIndicationAllUsers() {
        return storageIndicationAllUsers;
    }
}
