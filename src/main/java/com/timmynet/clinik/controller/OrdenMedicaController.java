package com.timmynet.clinik.controller;

import com.timmynet.clinik.domain.OrdenMedica;
import com.timmynet.clinik.domain.Paciente;
import com.timmynet.clinik.domain.Profesional;
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
    public ResponseEntity<List<OrdenMedica>> listOrdenes() {
        return ResponseEntity.ok(ordenMedicaRepository.findAll());
    }

    @GetMapping("/pacientes/{pacienteId}")
    public ResponseEntity<List<OrdenMedica>> listByPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(ordenMedicaRepository.findByPacienteId(pacienteId));
    }

    @PostMapping
    public ResponseEntity<OrdenMedica> create(@RequestBody OrdenMedica orden) {
        if (orden.getPaciente() != null && orden.getPaciente().getId() != null) {
            Paciente paciente = pacienteRepository.findById(orden.getPaciente().getId()).orElse(null);
            orden.setPaciente(paciente);
        }
        if (orden.getProfesional() != null && orden.getProfesional().getId() != null) {
            Profesional profesional = profesionalRepository.findById(orden.getProfesional().getId()).orElse(null);
            orden.setProfesional(profesional);
        }
        if (orden.getCita() != null && orden.getCita().getId() != null) {
            citaRepository.findById(orden.getCita().getId()).ifPresent(orden::setCita);
        }
        orden.setCreatedAt(LocalDateTime.now());
        OrdenMedica saved = ordenMedicaRepository.save(orden);
        return ResponseEntity.created(URI.create("/api/v1/ordenes-medicas/" + saved.getId())).body(saved);
    }
}
