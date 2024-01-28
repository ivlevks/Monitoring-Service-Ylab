package org.ivlevks.application.core.usecase;

import org.ivlevks.application.core.entity.Indication;
import org.ivlevks.application.core.entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс действий с показаниями счетчиков
 */
public interface GetUpdateIndications {

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
    
}
