package org.ivlevks.adapter.repository.in_memory;

import org.ivlevks.domain.entity.Indication;
import org.ivlevks.domain.entity.User;

import java.util.*;

/**
 * Класс хранилища данных в памяти приложения
 * хранение пользователей осуществляется в Liste
 * хранение показаний осуществляется в хэшмапе с доступом по ключу - Юзеру
 */
public class InMemoryData {

    private final ArrayList<User> users = new ArrayList<>();
    private final HashMap<User, LinkedList<Indication>> storageIndicationAllUsers = new HashMap<>();
    private final Set<String> nameIndications;

    public InMemoryData() {
        this.nameIndications = new HashSet<>();
        nameIndications.add("Heat");
        nameIndications.add("Cold Water");
        nameIndications.add("Hot Water");
    }

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

    public Set<String> getListIndications() {
        return nameIndications;
    }

    public void updateListIndications(String newNameIndication) {
        nameIndications.add(newNameIndication);
    }
}
