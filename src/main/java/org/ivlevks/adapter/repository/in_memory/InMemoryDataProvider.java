package org.ivlevks.adapter.repository.in_memory;

import org.ivlevks.domain.entity.Indication;
import org.ivlevks.domain.entity.User;
import org.ivlevks.usecase.port.GetUpdateUsers;
import org.ivlevks.usecase.port.GetUpdateIndications;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Класс с реализацией интерфейсов действий с пользователями и показаниями,
 * определенными на уровне ядра приложения
 */
public class InMemoryDataProvider implements GetUpdateUsers, GetUpdateIndications {

    private final InMemoryData data;

    /**
     * Конструктор
     * @param data хранилище данных
     */
    public InMemoryDataProvider(InMemoryData data) {
        this.data = data;
    }

    /**
     * Добавление пользователя
     * @param user - добавляемый пользователь
     */
    @Override
    public void addUser(User user) {
        data.getUsers().add(user);
        data.getStorageIndicationAllUsers().put(user, new LinkedList<>());
    }

    /**
     * Получение пользователя
     * @param email - email
     * @return возвращает пользователя, обернутого в Optional<>
     */
    @Override
    public Optional<User> getUser(String email) {
        return data.getUsers().stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst();
    }

    /**
     * Получение списка пользователей
     * @return список пользователей
     */
    @Override
    public List<User> getAllUsers() {
        return data.getUsers();
    }

    /** Обновление данных пользователя
     * в in-memory не требует реализации
     * @param user пользователь
     */
    @Override
    public void updateUser(User user) {

    }

    /**
     * Добавление показаний
     * @param user - пользователь, которому добавляются показания
     * @param indication - класс показаний
     */
    @Override
    public void addIndication(User user, Indication indication) {
        data.getStorageIndicationAllUsers().get(user).add(indication);
    }

    /**
     * Получение последних актуальных показаний
     * @param user - пользователь, чьи показания выводятся
     * @return - актуальные показания, обернутые в Optional<>
     */
    @Override
    public Optional<Indication> getLastActualIndication(User user) {
        List<Indication> indicationList = data.getStorageIndicationAllUsers().get(user);
        if (indicationList.isEmpty()) return Optional.empty();
        return Optional.ofNullable(data.getStorageIndicationAllUsers().get(user).getLast());
    }

    /**
     * Получение всех показаний пользователя
     * @param user  пользователь, чьи показания выводятся
     * @return - список всех показаний
     */
    @Override
    public List<Indication> getAllIndications(User user) {
        return data.getStorageIndicationAllUsers().get(user);
    }

    /**
     * @return 
     */
    @Override
    public Set<String> getListIndications() {
        return data.getListIndications();
    }

    /**
     * @param newNameIndication 
     */
    @Override
    public void updateListIndications(String newNameIndication) {
        data.updateListIndications(newNameIndication);
    }

}
