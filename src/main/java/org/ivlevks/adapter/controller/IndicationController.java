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
@RequestMapping("/users")
@RestController
public class IndicationController {
    private final IndicationsMapper indicationsMapper;
    private final IndicationsService useCaseIndications;
    private final UsersService useCaseUsers;

    public IndicationController(IndicationsMapper indicationsMapper, IndicationsService useCaseIndications, UsersService useCaseUsers) {
        this.indicationsMapper = indicationsMapper;
        this.useCaseIndications = useCaseIndications;
        this.useCaseUsers = useCaseUsers;
    }

    @GetMapping(value = "/{id}/indications")
    public ResponseEntity<List<Indication>> getAllIndications(@PathVariable("id") int id) {
        User user = useCaseUsers.getUserById(id).get();
        List<Indication> indications = useCaseIndications.getAllIndicationsUser(user);
        return ResponseEntity.ok(indications);
    }

    @PostMapping(value = "/{id}/indication")
    public ResponseEntity<Indication> addIndications(
            @PathVariable("id") int id,
            @RequestBody Indication indication) {
        User user = useCaseUsers.getUserById(id).get();
        Optional<Indication> resultIndication = useCaseIndications.addIndication(user, indication);
        if (resultIndication.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(resultIndication.get());
    }

    @GetMapping(value = "/{id}/indications/actual")
    public ResponseEntity<Indication> getLastActualIndications(@PathVariable("id") int id) {
        User user = useCaseUsers.getUserById(id).get();
        Optional<Indication> lastActualIndication = useCaseIndications.getLastActualIndicationUser(user);
        if (lastActualIndication.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(lastActualIndication.get());
    }

    @GetMapping(value = "/{id}/indications/month/{year}/{month}")
    public ResponseEntity<Indication> getIndicationsByMonth(
            @PathVariable("id") int id,
            @PathVariable("year") int year,
            @PathVariable("month") int month) {
        User user = useCaseUsers.getUserById(id).get();
        Optional<Indication> indication = useCaseIndications.getMonthIndicationsUser(user, year, month);
        if (indication.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(indication.get());
    }
}
