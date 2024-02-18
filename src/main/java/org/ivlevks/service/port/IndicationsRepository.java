package org.ivlevks.service.port;

import org.ivlevks.domain.entity.Indication;
import org.ivlevks.domain.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Интерфейс действий с показаниями счетчиков
 */
public interface IndicationsRepository {

    /**
     * Добавление показаний
     * @param user - пользователь, которому добавляются показания
     * @param indication - класс показаний
     */
    void addIndication(User user, Indication indication);

    /**
     * Получение последних актуальных показаний
     * @param user - пользователь, чьи показания выводятся
     * @return - актуальные показания, обернутые в Optional<>
     */
    Optional<Indication> getLastActualIndication(User user);

    /**
     * Получение всех показаний пользователя
     * @param user  пользователь, чьи показания выводятся
     * @return - список всех показаний
     */
    List<Indication> getAllIndications(User user);

    Set<String> getListCounters();

    void updateListCounters(String newNameIndication);
}
