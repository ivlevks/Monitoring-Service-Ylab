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
import java.util.List;

@WebServlet("/get_history_indications")
public class GetHistoryIndications extends HttpServlet {
    private final ObjectMapper objectMapper;
    private final UseCaseIndications useCaseIndications;

    public GetHistoryIndications() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.objectMapper.registerModule(new JavaTimeModule());
        this.useCaseIndications = new UseCaseIndications(new UserRepositoryImpl(), new IndicationRepositoryImpl());
    }

    /**
     * @param req 
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Indication> allIndicationsUser = useCaseIndications.getAllIndicationsUser();

        if (allIndicationsUser.isEmpty()) resp.getWriter().write("История показаний отсутствует");

        for (Indication indication : allIndicationsUser) {
            resp.getOutputStream().write(objectMapper.writeValueAsBytes(indication));
        }
        resp.setContentType("application/json");
    }
}
