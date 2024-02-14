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
import org.ivlevks.configuration.annotations.Loggable;
import org.ivlevks.domain.entity.Indication;
import org.ivlevks.domain.mappers.IndicationsMapper;
import org.ivlevks.usecase.UseCaseIndications;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * Сервлет отвечающий за получение актуальных показаний
 */
@Loggable
@WebServlet("/get_actual_indications")
public class GetLastActualIndication extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final UseCaseIndications useCaseIndications;

    public GetLastActualIndication() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.objectMapper.registerModule(new JavaTimeModule());
        this.useCaseIndications = new UseCaseIndications(new UserRepositoryImpl(), new IndicationRepositoryImpl());
    }

    /**
     * Получение актуальных показаний пользователя
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Optional<Indication> lastActualIndication = useCaseIndications.getLastActualIndicationUser();

        if (lastActualIndication.isEmpty()) resp.getWriter().write("Актуальные показания отсутствуют");

        resp.getOutputStream().write(objectMapper.writeValueAsBytes(IndicationsMapper.INSTANCE.toIndicationDto(lastActualIndication.get())));
        resp.setContentType("application/json");
    }
}
