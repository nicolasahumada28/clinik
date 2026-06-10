package com.timmynet.clinik.controller;

import com.timmynet.clinik.domain.Deuda;
import com.timmynet.clinik.domain.Paciente;
import com.timmynet.clinik.repository.DeudaRepository;
import com.timmynet.clinik.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/deudas")
@RequiredArgsConstructor
public class DeudaController {

    private final DeudaRepository deudaRepository;
    private final PacienteRepository pacienteRepository;

    @GetMapping
    public ResponseEntity<List<Deuda>> getAll() {
        return ResponseEntity.ok(deudaRepository.findAll());
    }

    @GetMapping("/pacientes/{pacienteId}")
    public ResponseEntity<List<Deuda>> getByPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(deudaRepository.findByPacienteId(pacienteId));
    }

    @PostMapping
    public ResponseEntity<Deuda> create(@RequestBody Deuda deuda) {
        if (deuda.getPaciente() != null && deuda.getPaciente().getId() != null) {
            Paciente paciente = pacienteRepository.findById(deuda.getPaciente().getId()).orElse(null);
            deuda.setPaciente(paciente);
        }
        deuda.setCreatedAt(LocalDateTime.now());
        if (deuda.getBalance() == null) {
            deuda.setBalance(deuda.getMontoTotal());
        }
        deuda.setEstado("OPEN");
        Deuda saved = deudaRepository.save(deuda);
        return ResponseEntity.created(URI.create("/api/v1/deudas/" + saved.getId())).body(saved);
    }
}
