package org.ivlevks.adapter.controller;

import org.ivlevks.configuration.annotations.Loggable;
import org.ivlevks.domain.entity.Indication;
import org.ivlevks.domain.entity.User;
import org.ivlevks.domain.mappers.IndicationsMapper;
import org.ivlevks.domain.mappers.IndicationsMapperImpl;
import org.ivlevks.service.IndicationsService;
import org.ivlevks.service.UsersService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Loggable
@RestController
public class IndicationController {
    private final IndicationsMapper indicationsMapper;
    private final IndicationsService useCaseIndications;
    private final UsersService useCaseUsers;

    public IndicationController() {
        indicationsMapper = new IndicationsMapperImpl();
        useCaseIndications = new IndicationsService();
        this.useCaseUsers = new UsersService();
    }

    @GetMapping(value = "/users/{id}/indications", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Indication>> getAllIndications(@PathVariable("id") int id) {
        User user = useCaseUsers.getUserById(id).get();
        List<Indication> indications = useCaseIndications.getAllIndicationsUser(user);
        if (indications.isEmpty()) {
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(indications);
    }

    @PostMapping(value = "/users/{id}/indication", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Indication> addIndications(
            @PathVariable("id") int id,
            @RequestBody Indication indication) {
        User user = useCaseUsers.getUserById(id).get();
        Optional<Indication> resultIndication = useCaseIndications.addIndication(user, indication);
        if (resultIndication.isEmpty()) {
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(resultIndication.get());
    }

    @GetMapping(value = "/users/{id}/indications/actual", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Indication> getLastActualIndications(@PathVariable("id") int id) {
        User user = useCaseUsers.getUserById(id).get();
        Optional<Indication> lastActualIndication = useCaseIndications.getLastActualIndicationUser(user);
        if (lastActualIndication.isEmpty()) {
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(lastActualIndication.get());
    }

    @GetMapping(value = "/users/{id}/indications/month/{year}/{month}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Indication> getIndicationsByMonth(
            @PathVariable("id") int id,
            @PathVariable("year") int year,
            @PathVariable("month") int month) {
        User user = useCaseUsers.getUserById(id).get();
        Optional<Indication> indication = useCaseIndications.getMonthIndicationsUser(user, year, month);
        if (indication.isEmpty()) {
            return new ResponseEntity(null, HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(indication.get());
    }




}
