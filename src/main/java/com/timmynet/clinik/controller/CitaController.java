package com.timmynet.clinik.controller;

import com.timmynet.clinik.domain.Cita;
import com.timmynet.clinik.domain.BitacoraCita;
import com.timmynet.clinik.domain.Paciente;
import com.timmynet.clinik.domain.Profesional;
import com.timmynet.clinik.repository.BitacoraCitaRepository;
import com.timmynet.clinik.repository.CitaRepository;
import com.timmynet.clinik.repository.PacienteRepository;
import com.timmynet.clinik.repository.ProfesionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfesionalRepository profesionalRepository;
    private final BitacoraCitaRepository logRepository;

    @GetMapping
    public ResponseEntity<List<Cita>> getAll() {
        return ResponseEntity.ok(citaRepository.findAll());
    }

    @GetMapping("/pacientes/{pacienteId}")
    public ResponseEntity<List<Cita>> findByPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(citaRepository.findByPacienteId(pacienteId));
    }

    @GetMapping("/profesionales/{profesionalId}")
    public ResponseEntity<List<Cita>> findByProfesional(@PathVariable Long profesionalId) {
        return ResponseEntity.ok(citaRepository.findByProfesionalId(profesionalId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cita> getOne(@PathVariable Long id) {
        return citaRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cita> schedule(@RequestBody Cita cita) {
        if (cita.getPaciente() == null || cita.getPaciente().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El paciente es obligatorio");
        }
        if (cita.getProfesional() == null || cita.getProfesional().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El profesional es obligatorio");
        }

        Paciente paciente = pacienteRepository.findById(cita.getPaciente().getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado"));
        Profesional profesional = profesionalRepository.findById(cita.getProfesional().getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesional no encontrado"));
        cita.setPaciente(paciente);
        cita.setProfesional(profesional);
        cita.setCreatedAt(LocalDateTime.now());
        if (cita.getEstadoCita() == null) {
            cita.setEstadoCita(com.timmynet.clinik.domain.EstadoCita.PROGRAMADA);
        }
        Cita saved = citaRepository.save(cita);
        return ResponseEntity.created(URI.create("/api/v1/citas/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Cita> cancel(@PathVariable Long id,
                                       @RequestParam(required = false, defaultValue = "") String reason) {
        return citaRepository.findById(id)
            .map(cita -> {
                if (cita.getEstadoCita() == com.timmynet.clinik.domain.EstadoCita.COMPLETADA
                    || cita.getEstadoCita() == com.timmynet.clinik.domain.EstadoCita.NO_ASISTIO) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "No se puede cancelar una cita finalizada");
                }
                cita.setEstadoCita(com.timmynet.clinik.domain.EstadoCita.CANCELADA);
                cita.setRazonCancelacion(reason.isBlank() ? null : reason.trim());
                cita.setUpdatedAt(LocalDateTime.now());
                return ResponseEntity.ok(citaRepository.save(cita));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/asistencia")
    public ResponseEntity<Cita> registerAttendance(@PathVariable Long id, @RequestParam boolean attended) {
        return citaRepository.findById(id)
            .map(cita -> {
                cita.setAsistio(attended);
                cita.setEstadoCita(attended ? com.timmynet.clinik.domain.EstadoCita.COMPLETADA : com.timmynet.clinik.domain.EstadoCita.NO_ASISTIO);
                return ResponseEntity.ok(citaRepository.save(cita));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/bitacoras")
    public ResponseEntity<BitacoraCita> addLog(@PathVariable Long id, @RequestBody BitacoraCita log) {
        return citaRepository.findById(id)
            .map(cita -> {
                log.setCita(cita);
                log.setCreatedAt(LocalDateTime.now());
                BitacoraCita saved = logRepository.save(log);
                return ResponseEntity.created(URI.create("/api/v1/citas/" + id + "/bitacoras/" + saved.getId())).body(saved);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/bitacoras")
    public ResponseEntity<List<BitacoraCita>> getLogs(@PathVariable Long id) {
        if (!citaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(logRepository.findByCitaId(id));
    }
}
