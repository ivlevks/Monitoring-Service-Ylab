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
import org.ivlevks.domain.entity.Indication;
import org.ivlevks.domain.entity.User;
import org.ivlevks.usecase.UseCaseIndications;
import org.ivlevks.usecase.UseCaseUsers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Сервлет отвечающий за выведение истории показаний пользователя
 */
@WebServlet("/get_history_indications")
public class GetHistoryIndications extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final UseCaseIndications useCaseIndications;
    private final UseCaseUsers useCaseUsers;

    public GetHistoryIndications() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.objectMapper.registerModule(new JavaTimeModule());
        this.useCaseIndications = new UseCaseIndications(new UserRepositoryImpl(), new IndicationRepositoryImpl());
        this.useCaseUsers = new UseCaseUsers(new UserRepositoryImpl(), new IndicationRepositoryImpl());
    }

    /**
     * Получение показаний пользователя
     * если email в теле запроса присутствует - то запрос по определенному пользователю
     * @param req 
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        List<Indication> allIndicationsUser = new ArrayList<>();

        if (email == null) {
            allIndicationsUser = useCaseIndications.getAllIndicationsUser();
        } else {
            if (useCaseUsers.isCurrentUserAdmin()) {
                Optional<User> user = useCaseUsers.findUserByEmail(email);
                allIndicationsUser = useCaseIndications.getAllIndicationsUser(user.get());
                resp.setStatus(HttpServletResponse.SC_OK);
            } else resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        if (allIndicationsUser.isEmpty()) resp.getWriter().write("История показаний отсутствует");

        for (Indication indication : allIndicationsUser) {
            resp.getOutputStream().write(objectMapper.writeValueAsBytes(indication));
        }
        resp.setContentType("application/json");
    }
}
