package com.timmynet.clinik.controller;

import com.timmynet.clinik.domain.Cita;
import com.timmynet.clinik.domain.BitacoraCita;
import com.timmynet.clinik.domain.Paciente;
import com.timmynet.clinik.domain.Profesional;
import com.timmynet.clinik.dto.BitacoraRequest;
import com.timmynet.clinik.dto.BitacoraResponse;
import com.timmynet.clinik.dto.CitaRequest;
import com.timmynet.clinik.dto.CitaResponse;
import com.timmynet.clinik.dto.DtoMapper;
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
    public ResponseEntity<List<CitaResponse>> getAll() {
        return ResponseEntity.ok(citaRepository.findAll().stream().map(DtoMapper::toResponse).toList());
    }

    @GetMapping("/pacientes/{pacienteId}")
    public ResponseEntity<List<CitaResponse>> findByPaciente(@PathVariable Long pacienteId) {
        return ResponseEntity.ok(citaRepository.findByPacienteId(pacienteId).stream().map(DtoMapper::toResponse).toList());
    }

    @GetMapping("/profesionales/{profesionalId}")
    public ResponseEntity<List<CitaResponse>> findByProfesional(@PathVariable Long profesionalId) {
        return ResponseEntity.ok(citaRepository.findByProfesionalId(profesionalId).stream().map(DtoMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitaResponse> getOne(@PathVariable Long id) {
        return citaRepository.findById(id)
            .map(DtoMapper::toResponse)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CitaResponse> schedule(@RequestBody CitaRequest request) {
        if (request.pacienteId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El paciente es obligatorio");
        }
        if (request.profesionalId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El profesional es obligatorio");
        }

        Paciente paciente = pacienteRepository.findById(request.pacienteId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente no encontrado"));
        Profesional profesional = profesionalRepository.findById(request.profesionalId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profesional no encontrado"));
        Cita cita = Cita.builder().tipoCita(request.tipoCita()).paciente(paciente).profesional(profesional)
            .scheduledAt(request.scheduledAt()).estadoCita(request.estadoCita()).asistio(request.asistio())
            .razonCancelacion(request.razonCancelacion()).notas(request.notas()).build();
        cita.setCreatedAt(LocalDateTime.now());
        if (cita.getEstadoCita() == null) {
            cita.setEstadoCita(com.timmynet.clinik.domain.EstadoCita.PROGRAMADA);
        }
        Cita saved = citaRepository.save(cita);
        return ResponseEntity.created(URI.create("/api/v1/citas/" + saved.getId())).body(DtoMapper.toResponse(saved));
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<CitaResponse> cancel(@PathVariable Long id,
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
                return ResponseEntity.ok(DtoMapper.toResponse(citaRepository.save(cita)));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/asistencia")
    public ResponseEntity<CitaResponse> registerAttendance(@PathVariable Long id, @RequestParam boolean attended) {
        return citaRepository.findById(id)
            .map(cita -> {
                cita.setAsistio(attended);
                cita.setEstadoCita(attended ? com.timmynet.clinik.domain.EstadoCita.COMPLETADA : com.timmynet.clinik.domain.EstadoCita.NO_ASISTIO);
                return ResponseEntity.ok(DtoMapper.toResponse(citaRepository.save(cita)));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/bitacoras")
    public ResponseEntity<BitacoraResponse> addLog(@PathVariable Long id, @RequestBody BitacoraRequest request) {
        return citaRepository.findById(id)
            .map(cita -> {
                BitacoraCita log = BitacoraCita.builder().note(request.note()).build();
                log.setCita(cita);
                log.setCreatedAt(LocalDateTime.now());
                BitacoraCita saved = logRepository.save(log);
                return ResponseEntity.created(URI.create("/api/v1/citas/" + id + "/bitacoras/" + saved.getId())).body(DtoMapper.toResponse(saved));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/bitacoras")
    public ResponseEntity<List<BitacoraResponse>> getLogs(@PathVariable Long id) {
        if (!citaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(logRepository.findByCitaId(id).stream().map(DtoMapper::toResponse).toList());
    }
}
