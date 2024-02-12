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
import org.ivlevks.usecase.UseCaseIndications;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * Сервлет отвечающий за получение показаний за определенный месяц
 */
@WebServlet("/get_month_indications")
public class GetMonthIndications extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final UseCaseIndications useCaseIndications;

    public GetMonthIndications() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.objectMapper.registerModule(new JavaTimeModule());
        this.useCaseIndications = new UseCaseIndications(new UserRepositoryImpl(), new IndicationRepositoryImpl());
    }

    /**
     * Получение показаний за определенынй месяц
     * @param req 
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer year = Integer.valueOf(req.getParameter("year"));
        Integer month = Integer.valueOf(req.getParameter("month"));

        Optional<Indication> indication = useCaseIndications.getMonthIndicationsUser(year, month);
        if (indication.isEmpty()) resp.getWriter().write("Показания отсутствуют");

        for (Map.Entry<String, Double> entry : indication.get().getIndications().entrySet()) {
            resp.getOutputStream().write(objectMapper.writeValueAsBytes(entry));
        }
        resp.setContentType("application/json");
    }
}
