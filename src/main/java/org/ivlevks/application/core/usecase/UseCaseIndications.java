package org.ivlevks.application.core.usecase;

import org.ivlevks.application.configuration.Audit;
import org.ivlevks.application.core.entity.Indication;
import org.ivlevks.application.core.entity.User;
import org.ivlevks.application.dataproviders.repositories.InMemoryDataProvider;
import java.time.LocalDateTime;
import java.util.*;

/**
 *  Подкласс реализации логики в части показаний
 */
public class UseCaseIndications extends UseCase {

    /**
     * Конструктор
     * @param dataProvider - реализация подключения к хранилищу данных
     */
    public UseCaseIndications(InMemoryDataProvider dataProvider) {
        super(dataProvider);
    }

    /**
     * Добавление показаний счетчиков
     * @param indications - хэшмап с перечнем показаний их их значениями
     */
    public void addIndication(HashMap<String, Double> indications) {
        if (!isUserAuthorize()) {
            System.out.println("Ошибка, Вы не авторизованы!");
            Audit.addInfoInAudit("Failure insert indication");
            return;
        }

        // получение последних актуальных показаний из хранилища и проверка на валидность
        Optional<Indication> lastActualIndications = getUpdateIndications.getLastActualIndication(currentUser);
        if (isIndicationValid(indications, lastActualIndications)) {
            getUpdateIndications.addIndication(currentUser, new Indication(indications));
            System.out.println("Показания введены");
            Audit.addInfoInAudit("Success insert indication " + currentUser.getEmail());
        } else {
            System.out.println("Ошибка, введенные данные не коррректны");
            Audit.addInfoInAudit("Incorrect insert indication");
        }
    }

    /**
     * Проверка на валидность показаний
     * в части подачи показаний только 1 раз в месяц и увеличения их значений
     * @param indications - хэшмап с перечнем показаний их их значениями
     * @param lastActualIndication - последние актуальные показания
     * @return true если показания валидны
     */
    private boolean isIndicationValid(HashMap<String, Double> indications, Optional<Indication> lastActualIndication) {
        if (lastActualIndication.isEmpty()) return true;

        // проверка на наличие показаний в этом месяце
        Indication lastIndication = lastActualIndication.get();
        if (hasCurrentMonthIndication(lastIndication)) {
            System.out.println("Ошибка, в данном месяце показания уже вводились");
            Audit.addInfoInAudit("Failure insert indication - this month already has indications");
            return false;
        }

        // проверка на увеличение величины показаний
        for (Map.Entry<String, Double> entry : lastIndication.getIndications().entrySet()) {
            if (entry.getValue() > indications.get(entry.getKey())) return false;
        }
        return true;
    }

    /**
     * Проверка на наличие показаний в этом месяце
     * @param lastIndication - последние актуальные показания
     * @return true если показаний не было
     */
    private boolean hasCurrentMonthIndication(Indication lastIndication) {
        LocalDateTime lastIndicationDateTime = lastIndication.getDateTime();
        if (lastIndicationDateTime.getYear() < LocalDateTime.now().getYear()) return false;
        if (lastIndicationDateTime.getMonthValue() < LocalDateTime.now().getMonthValue()) return false;
        return true;
    }

    /**
     * Получение последних актуальных показаний
     * @return показания, обернутые в Optional<>
     */
    public Optional<Indication> getLastActualIndicationUser() {
        return getUpdateIndications.getLastActualIndication(currentUser);
    }

    /**
     * Получение всех показаний текущего пользователя
     * @return - список всех показаний
     */
    public List<Indication> getAllIndicationsUser () {
        return getUpdateIndications.getAllIndications(currentUser);
    }

    /**
     * Получение всех показаний конкретного пользователя
     * @param user - пользователь, чьи показания выводятся
     * @return - список всех показаний
     */
    public List<Indication> getAllIndicationsUser (User user) {
        return getUpdateIndications.getAllIndications(user);
    }

    /**
     * Получение перечня видов счетчиков
     * @return перечень видов счетчиков
     */
    public Set<String> getNamesIndications() {
        return nameIndications;
    }

    /**
     * Добавление нового вида счетчиков
     * @param newNameIndication наименование нового вида счетчика
     */
    public void addNewNameIndication(String newNameIndication) {
        getNamesIndications().add(newNameIndication);
    }
}