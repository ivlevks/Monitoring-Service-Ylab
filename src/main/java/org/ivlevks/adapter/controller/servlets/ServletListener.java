package org.ivlevks.adapter.controller.servlets;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.ivlevks.configuration.migration.MigrationHelper;

@WebListener
public class ServletListener implements ServletContextListener {
    /**
     * @param sce 
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        MigrationHelper.migrate();
        ServletContextListener.super.contextInitialized(sce);
    }
}
