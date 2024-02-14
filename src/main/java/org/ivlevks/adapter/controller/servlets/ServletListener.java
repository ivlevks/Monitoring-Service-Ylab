package org.ivlevks.adapter.controller.servlets;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.ivlevks.configuration.annotations.Loggable;
import org.ivlevks.configuration.migration.MigrationHelper;

/**
 * Сервлет, запускающийся перед запуском приложения
 * Накатываем миграции, перед обращением к какому-либо сервлету
 */
@Loggable
@WebListener
public class ServletListener implements ServletContextListener {
    /**
     * Инициализация контекста сервлетов
     * @param sce 
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        MigrationHelper.migrate();
        ServletContextListener.super.contextInitialized(sce);
    }
}
