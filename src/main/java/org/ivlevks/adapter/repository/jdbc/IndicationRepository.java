package org.ivlevks.adapter.repository.jdbc;

import org.ivlevks.domain.entity.Indication;
import org.ivlevks.domain.entity.User;
import org.ivlevks.usecase.port.GetUpdateIndications;

import java.util.List;
import java.util.Optional;

public class IndicationRepository implements GetUpdateIndications {
    /**
     * @param user       - пользователь, которому добавляются показания
     * @param indication - класс показаний
     */
    @Override
    public void addIndication(User user, Indication indication) {

    }

    /**
     * @param user - пользователь, чьи показания выводятся
     * @return
     */
    @Override
    public Optional<Indication> getLastActualIndication(User user) {
        return Optional.empty();
    }

    /**
     * @param user пользователь, чьи показания выводятся
     * @return
     */
    @Override
    public List<Indication> getAllIndications(User user) {
        return null;
    }
}
