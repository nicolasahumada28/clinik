package com.timmynet.clinik.controller;

import com.timmynet.clinik.domain.Deuda;
import com.timmynet.clinik.domain.Paciente;
import com.timmynet.clinik.dto.DeudaRequest;
import com.timmynet.clinik.dto.DeudaResponse;
import com.timmynet.clinik.dto.DtoMapper;
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
    public ResponseEntity<List<DeudaResponse>> getAll() {
        return ResponseEntity.ok(deudaRepository.findAll().stream().map(DtoMapper::toResponse).toList());
    }

    @GetMapping("/pacientes/{pacienteId}")
    public ResponseEntity<List<DeudaResponse>> getByPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(deudaRepository.findByPacienteId(pacienteId).stream().map(DtoMapper::toResponse).toList());
    }

    @PostMapping
    public ResponseEntity<DeudaResponse> create(@RequestBody DeudaRequest request) {
        Deuda deuda = Deuda.builder().montoTotal(request.montoTotal()).balance(request.balance()).build();
        if (request.pacienteId() != null) {
            Paciente paciente = pacienteRepository.findById(request.pacienteId()).orElse(null);
            deuda.setPaciente(paciente);
        }
        deuda.setCreatedAt(LocalDateTime.now());
        if (deuda.getBalance() == null) {
            deuda.setBalance(deuda.getMontoTotal());
        }
        deuda.setEstado("OPEN");
        Deuda saved = deudaRepository.save(deuda);
        return ResponseEntity.created(URI.create("/api/v1/deudas/" + saved.getId())).body(DtoMapper.toResponse(saved));
    }
}
