package com.timmynet.clinik.controller;

import com.timmynet.clinik.domain.Paciente;
import com.timmynet.clinik.dto.DtoMapper;
import com.timmynet.clinik.dto.PacienteRequest;
import com.timmynet.clinik.dto.PacienteResponse;
import com.timmynet.clinik.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteRepository repository;

    @GetMapping
    public ResponseEntity<List<PacienteResponse>> getAll() {
        return ResponseEntity.ok(repository.findAll().stream().map(DtoMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> getOne(@PathVariable Long id) {
        return repository.findById(id)
            .map(DtoMapper::toResponse)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PacienteResponse> create(@RequestBody PacienteRequest request) {
        Paciente patient = Paciente.builder()
            .numeroDocumento(request.numeroDocumento()).nombre(request.nombre()).apellido(request.apellido())
            .fechaNacimiento(request.fechaNacimiento()).telefono(request.telefono()).email(request.email())
            .direccion(request.direccion()).build();
        Paciente saved = repository.save(patient);
        return ResponseEntity.created(URI.create("/api/v1/pacientes/" + saved.getId())).body(DtoMapper.toResponse(saved));
    }
}
