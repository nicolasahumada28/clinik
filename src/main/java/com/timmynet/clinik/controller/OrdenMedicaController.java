package com.timmynet.clinik.controller;

import com.timmynet.clinik.domain.OrdenMedica;
import com.timmynet.clinik.domain.Paciente;
import com.timmynet.clinik.domain.Profesional;
import com.timmynet.clinik.dto.DtoMapper;
import com.timmynet.clinik.dto.OrdenMedicaRequest;
import com.timmynet.clinik.dto.OrdenMedicaResponse;
import com.timmynet.clinik.repository.CitaRepository;
import com.timmynet.clinik.repository.OrdenMedicaRepository;
import com.timmynet.clinik.repository.PacienteRepository;
import com.timmynet.clinik.repository.ProfesionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes-medicas")
@RequiredArgsConstructor
public class OrdenMedicaController {

    private final OrdenMedicaRepository ordenMedicaRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfesionalRepository profesionalRepository;
    private final CitaRepository citaRepository;

    @GetMapping
    public ResponseEntity<List<OrdenMedicaResponse>> listOrdenes() {
        return ResponseEntity.ok(ordenMedicaRepository.findAll().stream().map(DtoMapper::toResponse).toList());
    }

    @GetMapping("/pacientes/{pacienteId}")
    public ResponseEntity<List<OrdenMedicaResponse>> listByPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(ordenMedicaRepository.findByPacienteId(pacienteId).stream().map(DtoMapper::toResponse).toList());
    }

    @PostMapping
    public ResponseEntity<OrdenMedicaResponse> create(@RequestBody OrdenMedicaRequest request) {
        OrdenMedica orden = OrdenMedica.builder().tipoOrdenMedica(request.tipoOrdenMedica()).descripcion(request.descripcion()).build();
        if (request.pacienteId() != null) {
            Paciente paciente = pacienteRepository.findById(request.pacienteId()).orElse(null);
            orden.setPaciente(paciente);
        }
        if (request.profesionalId() != null) {
            Profesional profesional = profesionalRepository.findById(request.profesionalId()).orElse(null);
            orden.setProfesional(profesional);
        }
        if (request.citaId() != null) {
            citaRepository.findById(request.citaId()).ifPresent(orden::setCita);
        }
        orden.setCreatedAt(LocalDateTime.now());
        OrdenMedica saved = ordenMedicaRepository.save(orden);
        return ResponseEntity.created(URI.create("/api/v1/ordenes-medicas/" + saved.getId())).body(DtoMapper.toResponse(saved));
    }
}
