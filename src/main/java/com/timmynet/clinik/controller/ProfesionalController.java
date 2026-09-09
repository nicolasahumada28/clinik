package com.timmynet.clinik.controller;

import com.timmynet.clinik.domain.Profesional;
import com.timmynet.clinik.dto.DtoMapper;
import com.timmynet.clinik.dto.ProfesionalRequest;
import com.timmynet.clinik.dto.ProfesionalResponse;
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
    public ResponseEntity<List<ProfesionalResponse>> getAll() {
        return ResponseEntity.ok(repository.findAll().stream().map(DtoMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesionalResponse> getOne(@PathVariable Long id) {
        return repository.findById(id)
            .map(DtoMapper::toResponse)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProfesionalResponse> create(@RequestBody ProfesionalRequest request) {
        Profesional professional = Profesional.builder()
            .nombre(request.nombre()).apellido(request.apellido()).especialidad(request.especialidad())
            .numeroLicencia(request.numeroLicencia()).telefono(request.telefono()).email(request.email()).build();
        Profesional saved = repository.save(professional);
        return ResponseEntity.created(URI.create("/api/v1/profesionales/" + saved.getId())).body(DtoMapper.toResponse(saved));
    }
}
