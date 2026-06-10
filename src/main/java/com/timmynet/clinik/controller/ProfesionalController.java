package com.timmynet.clinik.controller;

import com.timmynet.clinik.domain.Profesional;
import com.timmynet.clinik.repository.ProfesionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/profesionales")
@RequiredArgsConstructor
public class ProfesionalController {

    private final ProfesionalRepository repository;

    @GetMapping
    public ResponseEntity<List<Profesional>> getAll() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profesional> getOne(@PathVariable Long id) {
        return repository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Profesional> create(@RequestBody Profesional professional) {
        Profesional saved = repository.save(professional);
        return ResponseEntity.created(URI.create("/api/v1/profesionales/" + saved.getId())).body(saved);
    }
}
