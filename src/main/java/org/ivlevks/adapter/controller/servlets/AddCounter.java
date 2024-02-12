package org.ivlevks.adapter.controller.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ivlevks.adapter.repository.jdbc.IndicationRepositoryImpl;
import org.ivlevks.adapter.repository.jdbc.UserRepositoryImpl;
import org.ivlevks.usecase.UseCaseIndications;
import org.ivlevks.usecase.UseCaseUsers;
import java.io.IOException;

/**
 * Сервлет отвечающий за доавление нового счетчика
 */
@WebServlet("/add_counter")
public class AddCounter extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final UseCaseIndications useCaseIndications;
    private final UseCaseUsers useCaseUsers;

    public AddCounter() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.objectMapper.registerModule(new JavaTimeModule());
        this.useCaseIndications = new UseCaseIndications(new UserRepositoryImpl(), new IndicationRepositoryImpl());
        this.useCaseUsers = new UseCaseUsers(new UserRepositoryImpl(), new IndicationRepositoryImpl());
    }

    /**
     * Добаавление нового счетчика
     * @param req 
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String counter = req.getParameter("counter");

        if (useCaseUsers.isCurrentUserAdmin()) {
            useCaseIndications.addNewNameIndication(counter);
            resp.setStatus(HttpServletResponse.SC_OK);
        } else resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        resp.setContentType("application/json");
    }
}
