package org.ivlevks.service;

import org.ivlevks.starter.annotations.Loggable;
import org.ivlevks.domain.entity.Indication;
import org.ivlevks.domain.entity.User;
import org.ivlevks.service.port.IndicationsRepository;
import org.ivlevks.service.port.UsersRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Класс реализации логики в части показаний
 */
@Loggable
@Service
public class IndicationsService {
    private final UsersRepository usersRepository;
    private final IndicationsRepository indicationsRepository;
    private final AdminHelper adminHelper;

    public IndicationsService(UsersRepository usersRepository, IndicationsRepository indicationsRepository, AdminHelper adminHelper) {
        this.usersRepository = usersRepository;
        this.indicationsRepository = indicationsRepository;
        this.adminHelper = adminHelper;
    }

    /**
     * Добавление новых показателей
     * @param user - пользователь
     * @param indication - новые показания счетчиков
     * @return - новые показания счетчиков
     */
    public Optional<Indication> addIndication(User user, Indication indication) {
        if (!adminHelper.validateIdUser(user.getId())) {
            System.out.println("Ошибка, Вы не авторизованы!");
            return Optional.empty();
        }

        // получение последних актуальных показаний из хранилища и проверка на валидность
        Optional<Indication> lastActualIndications = indicationsRepository.getLastActualIndication(user);
        if (isIndicationValid(indication, lastActualIndications)) {

            indicationsRepository.addIndication(user, new Indication(indication.getIndications()));
            System.out.println("Показания введены");
            Optional<Indication> resultIndication = indicationsRepository.getLastActualIndication(user);
            return resultIndication;
        } else {
            System.out.println("Ошибка, введенные данные не коррректны");
            return Optional.empty();
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
    private boolean isIndicationValid(Indication indications, Optional<Indication> lastActualIndication) {
        if (hasIndicationNegative(indications)) {
            System.out.println("Ошибка, показания содержат отрицательные числа");
            return false;
        }

        if (lastActualIndication.isEmpty()) return true;

        // проверка на наличие показаний в этом месяце
        Indication lastIndication = lastActualIndication.get();
        if (hasCurrentMonthIndication(lastIndication)) {
            System.out.println("Ошибка, в данном месяце показания уже вводились");
            return false;
        }

        // проверка на увеличение величины показаний
        for (Map.Entry<String, Double> entry : lastIndication.getIndications().entrySet()) {
            if (entry.getValue() > indications.getIndications().get(entry.getKey())) return false;
        }
        return true;
    }

    /**
     * Проверка на отрицательные числа показаний
     *
     * @param indications введенные показания
     * @return true если есть отрицательные
     */
    private boolean hasIndicationNegative(Indication indications) {
        for (Map.Entry<String, Double> entry : indications.getIndications().entrySet()) {
            if (entry.getValue() < 0) return true;
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
     * Получение последних актуальных показаний
     *
     * @return показания, обернутые в Optional<>
     */
    public Optional<Indication> getLastActualIndicationUser(User user) {
        if (!adminHelper.validateIdUser(user.getId())) {
            System.out.println("Ошибка, Вы не авторизованы!");
            return Optional.empty();
        }
        return indicationsRepository.getLastActualIndication(user);
    }

    /**
     * Получение всех показаний конкретного пользователя
     *
     * @param user - пользователь, чьи показания выводятся
     * @return - список всех показаний
     */
    public List<Indication> getAllIndicationsUser(User user) {
        if (!adminHelper.validateIdUser(user.getId())) {
            System.out.println("Ошибка, Вы не авторизованы!");
            return new ArrayList<>();
        }
        return indicationsRepository.getAllIndications(user);
    }

    /**
     * Получение перечня видов счетчиков
     *
     * @return перечень видов счетчиков
     */
    public Set<String> getNamesIndications() {
        return indicationsRepository.getListCounters();
    }

    /**
     * Добавление нового вида счетчиков
     *
     * @param newNameIndication наименование нового вида счетчика
     */
    public String addNewNameIndication(String newNameIndication) {
        int id = adminHelper.getIdCurrentUser();
        Optional<User> user = usersRepository.getUserById(id);

        if (user.get().isUserAdmin()) {
            indicationsRepository.updateListCounters(newNameIndication);
            return newNameIndication;
        }
        return "not access";
    }

    /**
     * Получение показаний за определенный месяц
     *
     * @param user  - пользователь
     * @param year  - год
     * @param month - месяц
     * @return - показания
     */
    public Optional<Indication> getMonthIndicationsUser(User user, int year, int month) {
        Optional<Indication> result = null;
        if (adminHelper.validateIdUser(user.getId()) || user.isUserAdmin()) {
            for (Indication indication : getAllIndicationsUser(user)) {
                if (indication.getDateTime().getYear() == year &&
                        indication.getDateTime().getMonthValue() == month) {
                    result = Optional.of(indication);
                } else {
                    System.out.println("Показания на " + month + " месяц " + year + " года отсутствуют");
                }
                System.out.println();
            }
        }
        return result;
    }
}
