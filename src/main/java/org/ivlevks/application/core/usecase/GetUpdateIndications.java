package org.ivlevks.application.core.usecase;

import org.ivlevks.application.core.entity.Indication;
import org.ivlevks.application.core.entity.User;

import java.util.List;
import java.util.Optional;

public interface GetUpdateIndications {

    void updateIndication(User user, Indication indication);

    Optional<Indication> getLastActualIndication(User user);

    List<Indication> getAllIndications(User user);
    
}
