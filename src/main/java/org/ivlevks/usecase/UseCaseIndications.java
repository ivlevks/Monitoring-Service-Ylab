package org.ivlevks.usecase;

import org.ivlevks.configuration.Audit;
import org.ivlevks.domain.entity.Indication;
import org.ivlevks.domain.entity.User;
import org.ivlevks.adapter.repository.in_memory.InMemoryDataProvider;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Подкласс реализации логики в части показаний
 */
public class UseCaseIndications extends UseCase {

    /**
     * Конструктор
     *
     * @param dataProvider - реализация подключения к хранилищу данных
     */
    public UseCaseIndications(InMemoryDataProvider dataProvider) {
        super(dataProvider);
    }

    /**
     * Добавление показаний счетчиков
     *
     * @param indications - хэшмап с перечнем показаний их их значениями
     */
    public void addIndication(HashMap<String, String> indications) {
        if (!isUserAuthorize()) {
            System.out.println("Ошибка, Вы не авторизованы!");
            Audit.addInfoInAudit("Failure insert indication");
            return;
        }

        // получение последних актуальных показаний из хранилища и проверка на валидность
        Optional<Indication> lastActualIndications = getUpdateIndications.getLastActualIndication(currentUser);
        if (isIndicationValid(indications, lastActualIndications)) {
            HashMap<String, Double> resultIndications = getResultIndications(indications);
            getUpdateIndications.addIndication(currentUser, new Indication(resultIndications));
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
     *
     * @param indications          - хэшмап с перечнем показаний их их значениями
     * @param lastActualIndication - последние актуальные показания
     * @return true если показания валидны
     */
    private boolean isIndicationValid(HashMap<String, String> indications, Optional<Indication> lastActualIndication) {
        if (hasIndicationString(indications)) {
            System.out.println("Ошибка, показания содержат символы");
            return false;
        }

        if (hasIndicationNegative(indications)) {
            System.out.println("Ошибка, показания содержат отрицательные числа");
            return false;
        }

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
            if (entry.getValue() > Double.parseDouble(indications.get(entry.getKey()))) return false;
        }
        return true;
    }


    /**
     * Проверка на отрицательные числа показаний
     *
     * @param indications введенные показания
     * @return true если есть отрицательные
     */
    private boolean hasIndicationNegative(HashMap<String, String> indications) {
        for (Map.Entry<String, String> entry : indications.entrySet()) {
            if (Double.parseDouble(entry.getValue()) < 0) return false;
        }
        return true;
    }

    /**
     * Проверка на наличие символов
     *
     * @param indications введенные показания
     * @return true если есть символы
     */
    private boolean hasIndicationString(HashMap<String, String> indications) {
        for (Map.Entry<String, String> entry : indications.entrySet()) {
            try {
                double value = Double.parseDouble(entry.getValue());
            } catch (NumberFormatException nfe) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверка на наличие показаний в этом месяце
     *
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
     * Конвертирование показаний из строк в Double
     *
     * @param indications введенные показания
     * @return - итоговая хэшмап с перечнем показаний их их значениями
     */
    private HashMap<String, Double> getResultIndications(HashMap<String, String> indications) {
        HashMap<String, Double> result = new HashMap<>();
        for (Map.Entry<String, String> entry : indications.entrySet()) {
            result.put(entry.getKey(), Double.valueOf(entry.getValue()));
        }
        return result;
    }

    /**
     * Получение последних актуальных показаний
     *
     * @return показания, обернутые в Optional<>
     */
    public Optional<Indication> getLastActualIndicationUser() {
        return getUpdateIndications.getLastActualIndication(currentUser);
    }

    /**
     * Получение всех показаний текущего пользователя
     *
     * @return - список всех показаний
     */
    public List<Indication> getAllIndicationsUser() {
        return getUpdateIndications.getAllIndications(currentUser);
    }

    /**
     * Получение всех показаний конкретного пользователя
     *
     * @param user - пользователь, чьи показания выводятся
     * @return - список всех показаний
     */
    public List<Indication> getAllIndicationsUser(User user) {
        return getUpdateIndications.getAllIndications(user);
    }

    /**
     * Получение перечня видов счетчиков
     *
     * @return перечень видов счетчиков
     */
    public Set<String> getNamesIndications() {
        return nameIndications;
    }

    /**
     * Добавление нового вида счетчиков
     *
     * @param newNameIndication наименование нового вида счетчика
     */
    public void addNewNameIndication(String newNameIndication) {
        getNamesIndications().add(newNameIndication);
    }
}
