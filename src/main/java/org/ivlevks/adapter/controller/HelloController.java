package org.ivlevks.adapter.controller;

import jakarta.annotation.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.Objects;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public ResponseEntity<String> hello(@Nullable @RequestParam String name) {
        String response = Objects.isNull(name)
                ? "Spring boot"
                : name;
        return ResponseEntity.ok("Hello" + response);
    }
}
