package org.ivlevks.adapter.controller;

import org.ivlevks.configuration.annotations.Loggable;
import org.ivlevks.domain.mappers.IndicationsMapper;
import org.ivlevks.service.IndicationsService;
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
    private final IndicationsService useCaseIndications;

    public CounterController(IndicationsMapper indicationsMapper, IndicationsService useCaseIndications) {
        this.indicationsMapper = indicationsMapper;
        this.useCaseIndications = useCaseIndications;
    }

    /**
     * Получение списка всех счетчиков
     * @return список счетчиков
     */
    @GetMapping(value = "/counters")
    public ResponseEntity<Set<String>> getAllCounters() {
        Set<String> counters = useCaseIndications.getNamesIndications();
        return ResponseEntity.ok(counters);
    }

    /**
     * Добавление нового счетчика
     * @param counter - новый вид счетчика
     * @return - новый вид счетчика
     */
    @PostMapping(value = "/counter")
    public ResponseEntity<String> updateCounters(@RequestBody String counter) {
        useCaseIndications.addNewNameIndication(counter);
        return ResponseEntity.ok(counter);
    }
}
