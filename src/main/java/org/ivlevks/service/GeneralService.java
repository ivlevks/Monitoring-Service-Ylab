package org.ivlevks.service;

import org.ivlevks.adapter.repository.jdbc.IndicationRepositoryImpl;
import org.ivlevks.adapter.repository.jdbc.UserRepositoryImpl;
import org.ivlevks.configuration.annotations.Loggable;
import org.ivlevks.domain.entity.User;
import org.ivlevks.service.port.IndicationsRepository;
import org.ivlevks.service.port.UsersRepository;
import org.springframework.stereotype.Service;

/**
 * Родительский класс логики действий с пользователями и показаниями
 * хранит в себе ссылку на авторизованного пользователя
 * и перечень изначальных видов счетчиков - 3 шт.
 */
@Loggable
@Service
public class GeneralService {
    protected final UsersRepository usersRepository;
    protected final IndicationsRepository indicationsRepository;
    protected AdminHelper adminHelper;
    protected String regexPatternEmail = "^(.+)@(\\S+)$";

    /**
     * Конструктор, инициализирует начальный перечень видов показаний
     * счетчиков в количестве 3 шт.
     * для jdbc реализации
     * @param usersRepository, getUpdateIndications - реализации подключения к хранилищу данных
     */
    public GeneralService(UsersRepository usersRepository, IndicationsRepository indicationsRepository) {
        this.usersRepository = usersRepository;
        this.indicationsRepository = indicationsRepository;
        adminHelper = new AdminHelper();
    }

    /**
     * Конструктор, инициализирует начальный перечень видов показаний
     */
    public GeneralService() {
        indicationsRepository = new IndicationRepositoryImpl();
        usersRepository = new UserRepositoryImpl();
        adminHelper = new AdminHelper();
    }

    /**
     * Проверка на права доступа пользователя
     * @return true если пользователь авторизован
     * или имеет доступ
     */
    boolean isUserAuthorizeAndHasAccess(User user) {
        return adminHelper.validateIdUser(user.getId());
    }
}
