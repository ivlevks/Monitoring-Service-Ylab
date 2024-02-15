package org.ivlevks.adapter.controller.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ivlevks.adapter.repository.jdbc.IndicationRepositoryImpl;
import org.ivlevks.adapter.repository.jdbc.UserRepositoryImpl;
import org.ivlevks.configuration.annotations.Loggable;
import org.ivlevks.usecase.UseCaseUsers;
import org.springframework.stereotype.Controller;

import java.io.IOException;

/**
 * Сервлет отвечающий за авторизацию пользователя
 */
@Loggable
@WebServlet("/login")
@Controller
public class LoginServlet extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final UseCaseUsers useCaseUsers;

    public LoginServlet() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.useCaseUsers = new UseCaseUsers(new UserRepositoryImpl(), new IndicationRepositoryImpl());
    }

    /**
     * Авторизация пользователя
     * @param req 
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        boolean result = useCaseUsers.auth(email, password);

        if (result) {
            resp.setStatus(HttpServletResponse.SC_OK);
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        resp.setContentType("application/json");
    }
}
