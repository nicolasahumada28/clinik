package com.timmynet.clinik.config;

import com.timmynet.clinik.domain.*;
import com.timmynet.clinik.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class DataInitializer {

    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfesionalRepository profesionalRepository;
    private final CitaRepository citaRepository;
    private final OrdenMedicaRepository ordenMedicaRepository;
    private final DeudaRepository deudaRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner seedData() {
        return args -> {
            if (rolRepository.count() == 0) {
                Rol admin = rolRepository.save(Rol.builder().nombre("ADMIN").build());
                Rol staff = rolRepository.save(Rol.builder().nombre("STAFF").build());
                Rol accountant = rolRepository.save(Rol.builder().nombre("ACCOUNTANT").build());
                usuarioRepository.save(Usuario.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .email("admin@clinik.local")
                    .rol(admin)
                    .build());
                usuarioRepository.save(Usuario.builder()
                    .username("reception")
                    .password(passwordEncoder.encode("recep123"))
                    .email("reception@clinik.local")
                    .rol(staff)
                    .build());
                usuarioRepository.save(Usuario.builder()
                    .username("billing")
                    .password(passwordEncoder.encode("billing123"))
                    .email("billing@clinik.local")
                    .rol(accountant)
                    .build());
            }

            if (pacienteRepository.count() == 0) {
                Paciente paciente = pacienteRepository.save(Paciente.builder()
                    .numeroDocumento("123456789")
                    .nombre("Juan")
                    .apellido("Perez")
                    .fechaNacimiento(LocalDate.of(1985, 4, 12))
                    .telefono("+56912345678")
                    .email("juan.perez@example.com")
                    .direccion("Av. Siempre Viva 123")
                    .build());

                Profesional profesional = profesionalRepository.save(Profesional.builder()
                    .nombre("María")
                    .apellido("González")
                    .especialidad("Medicina General")
                    .numeroLicencia("MED-001")
                    .telefono("+56987654321")
                    .email("maria.gonzalez@clinik.local")
                    .build());

                Cita cita = citaRepository.save(Cita.builder()
                    .tipoCita(TipoCita.CONSULTA)
                    .paciente(paciente)
                    .profesional(profesional)
                    .scheduledAt(LocalDateTime.now().plusDays(1))
                    .estadoCita(EstadoCita.PROGRAMADA)
                    .asistio(false)
                    .notas("Consulta inicial")
                    .createdAt(LocalDateTime.now())
                    .build());

                citaRepository.save(Cita.builder()
                    .tipoCita(TipoCita.INTERCONSULTA)
                    .paciente(paciente)
                    .profesional(profesional)
                    .scheduledAt(LocalDateTime.now().plusDays(3))
                    .estadoCita(EstadoCita.PROGRAMADA)
                    .razonCancelacion("Interconsulta con especialista en cardiología")
                    .createdAt(LocalDateTime.now())
                    .build());

                ordenMedicaRepository.save(OrdenMedica.builder()
                    .tipoOrdenMedica(TipoOrdenMedica.EXAMEN)
                    .paciente(paciente)
                    .profesional(profesional)
                    .cita(cita)
                    .descripcion("Orden de examen de laboratorio general")
                    .createdAt(LocalDateTime.now())
                    .build());

                deudaRepository.save(Deuda.builder()
                    .paciente(paciente)
                    .montoTotal(BigDecimal.valueOf(120_000))
                    .balance(BigDecimal.valueOf(120_000))
                    .createdAt(LocalDateTime.now())
                    .estado("OPEN")
                    .build());
            }
        };
    }
}
