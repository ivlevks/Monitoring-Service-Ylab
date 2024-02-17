package org.ivlevks.adapter.controller;

import org.ivlevks.configuration.annotations.Loggable;
import org.ivlevks.domain.mappers.IndicationsMapper;
import org.ivlevks.domain.mappers.IndicationsMapperImpl;
import org.ivlevks.usecase.UseCaseIndications;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Set;

/**
 * Рест контроллер для действий со списком счетчиков
 */
@Loggable
@RestController
public class CounterController {
    private final IndicationsMapper indicationsMapper;
    private final UseCaseIndications useCaseIndications;

    public CounterController() {
        indicationsMapper = new IndicationsMapperImpl();
        useCaseIndications = new UseCaseIndications();
    }

    /**
     * Получение списка всех счетчиков
     * @return список счетчиков
     */
    @GetMapping(value = "/counters", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<String>> getAllCounters() {
        Set<String> counters = useCaseIndications.getNamesIndications();
        return ResponseEntity.ok(counters);
    }

    /**
     * Добавление нового счетчика
     * @param counter - новый вид счетчика
     * @return - новый вид счетчика
     */
    @PostMapping(value = "/counter", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateCounters(@RequestBody String counter) {
        useCaseIndications.addNewNameIndication(counter);
        return ResponseEntity.ok(counter);
    }
}
