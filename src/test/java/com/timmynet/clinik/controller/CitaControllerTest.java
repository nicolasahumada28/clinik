package com.timmynet.clinik.controller;

import com.timmynet.clinik.domain.Cita;
import com.timmynet.clinik.domain.EstadoCita;
import com.timmynet.clinik.domain.Paciente;
import com.timmynet.clinik.domain.Profesional;
import com.timmynet.clinik.domain.TipoCita;
import com.timmynet.clinik.dto.CitaRequest;
import com.timmynet.clinik.dto.CitaResponse;
import com.timmynet.clinik.repository.BitacoraCitaRepository;
import com.timmynet.clinik.repository.CitaRepository;
import com.timmynet.clinik.repository.PacienteRepository;
import com.timmynet.clinik.repository.ProfesionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.springframework.web.server.ResponseStatusException;

class CitaControllerTest {

    private CitaRepository citaRepository;
    private PacienteRepository pacienteRepository;
    private ProfesionalRepository profesionalRepository;
    private BitacoraCitaRepository logRepository;
    private CitaController controller;

    @BeforeEach
    void setUp() {
        citaRepository = Mockito.mock(CitaRepository.class);
        pacienteRepository = Mockito.mock(PacienteRepository.class);
        profesionalRepository = Mockito.mock(ProfesionalRepository.class);
        logRepository = Mockito.mock(BitacoraCitaRepository.class);
        controller = new CitaController(citaRepository, pacienteRepository, profesionalRepository, logRepository);
    }

    @Test
    void shouldReturnAllCitas() {
        Cita cita = Cita.builder()
            .id(1L)
            .tipoCita(TipoCita.CONSULTA)
            .estadoCita(EstadoCita.PROGRAMADA)
            .asistio(false)
            .createdAt(LocalDateTime.now())
            .build();

        Mockito.when(citaRepository.findAll()).thenReturn(Collections.singletonList(cita));

        ResponseEntity<java.util.List<CitaResponse>> response = controller.getAll();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).tipoCita()).isEqualTo(TipoCita.CONSULTA);
    }

    @Test
    void shouldScheduleCita() {
        Paciente paciente = Paciente.builder().id(1L).build();
        Profesional profesional = Profesional.builder().id(2L).build();

        CitaRequest citaToSave = new CitaRequest(
            TipoCita.CONSULTA, 1L, 2L, LocalDateTime.of(2026, 6, 10, 10, 30),
            null, null, null, "Consulta inicial");

        Cita savedCita = Cita.builder()
            .id(1L)
            .tipoCita(TipoCita.CONSULTA)
            .paciente(paciente)
            .profesional(profesional)
            .scheduledAt(LocalDateTime.of(2026, 6, 10, 10, 30))
            .estadoCita(EstadoCita.PROGRAMADA)
            .notas("Consulta inicial")
            .createdAt(LocalDateTime.now())
            .build();

        Mockito.when(pacienteRepository.findById(eq(1L))).thenReturn(Optional.of(paciente));
        Mockito.when(profesionalRepository.findById(eq(2L))).thenReturn(Optional.of(profesional));
        Mockito.when(citaRepository.save(any(Cita.class))).thenReturn(savedCita);

        ResponseEntity<CitaResponse> response = controller.schedule(citaToSave);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(1L);
        assertThat(response.getBody().estadoCita()).isEqualTo(EstadoCita.PROGRAMADA);
    }

    @Test
    void shouldRegisterAttendance() {
        Cita cita = Cita.builder()
            .id(1L)
            .tipoCita(TipoCita.CONSULTA)
            .estadoCita(EstadoCita.PROGRAMADA)
            .asistio(false)
            .build();

        Cita updatedCita = Cita.builder()
            .id(1L)
            .tipoCita(TipoCita.CONSULTA)
            .estadoCita(EstadoCita.COMPLETADA)
            .asistio(true)
            .build();

        Mockito.when(citaRepository.findById(eq(1L))).thenReturn(Optional.of(cita));
        Mockito.when(citaRepository.save(any(Cita.class))).thenReturn(updatedCita);

        ResponseEntity<CitaResponse> response = controller.registerAttendance(1L, true);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().asistio()).isTrue();
        assertThat(response.getBody().estadoCita()).isEqualTo(EstadoCita.COMPLETADA);
    }

    @Test
    void shouldRejectScheduleWhenPacienteDoesNotExist() {
        CitaRequest cita = new CitaRequest(TipoCita.CONSULTA, 99L, 2L, null, null, null, null, null);

        Mockito.when(pacienteRepository.findById(eq(99L))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> controller.schedule(cita))
            .isInstanceOf(ResponseStatusException.class)
            .extracting("statusCode.value")
            .isEqualTo(404);
        Mockito.verify(citaRepository, Mockito.never()).save(any(Cita.class));
    }

    @Test
    void shouldCancelScheduledCitaWithReason() {
        Cita cita = Cita.builder()
            .id(1L)
            .tipoCita(TipoCita.CONSULTA)
            .estadoCita(EstadoCita.PROGRAMADA)
            .build();

        Mockito.when(citaRepository.findById(eq(1L))).thenReturn(Optional.of(cita));
        Mockito.when(citaRepository.save(any(Cita.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ResponseEntity<CitaResponse> response = controller.cancel(1L, "  Paciente solicito cambio  ");

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody().estadoCita()).isEqualTo(EstadoCita.CANCELADA);
        assertThat(response.getBody().razonCancelacion()).isEqualTo("Paciente solicito cambio");
        assertThat(response.getBody().updatedAt()).isNotNull();
    }
}
