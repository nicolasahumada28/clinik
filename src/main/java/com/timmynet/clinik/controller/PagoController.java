package com.timmynet.clinik.controller;

import com.timmynet.clinik.domain.Deuda;
import com.timmynet.clinik.domain.Pago;
import com.timmynet.clinik.repository.DeudaRepository;
import com.timmynet.clinik.repository.PagoRepository;
import com.timmynet.clinik.dto.DtoMapper;
import com.timmynet.clinik.dto.PagoRequest;
import com.timmynet.clinik.dto.PagoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoRepository pagoRepository;
    private final DeudaRepository deudaRepository;

    @GetMapping
    public ResponseEntity<List<PagoResponse>> getAll() {
        return ResponseEntity.ok(pagoRepository.findAll().stream().map(DtoMapper::toResponse).toList());
    }

    @GetMapping("/deudas/{deudaId}")
    public ResponseEntity<List<PagoResponse>> getByDeuda(@PathVariable Long deudaId) {
        return ResponseEntity.ok(pagoRepository.findByDeudaId(deudaId).stream().map(DtoMapper::toResponse).toList());
    }

    @PostMapping
    public ResponseEntity<PagoResponse> create(@RequestBody PagoRequest request) {
        if (request.deudaId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Deuda deuda = deudaRepository.findById(request.deudaId()).orElse(null);
        if (deuda == null) {
            return ResponseEntity.notFound().build();
        }
        Pago pago = Pago.builder().deuda(deuda).monto(request.monto()).metodoPago(request.metodoPago()).build();
        pago.setFechaPago(LocalDateTime.now());
        deuda.setBalance(deuda.getBalance().subtract(pago.getMonto()));
        if (deuda.getBalance().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            deuda.setBalance(java.math.BigDecimal.ZERO);
            deuda.setEstado("PAID");
        }
        deudaRepository.save(deuda);
        Pago saved = pagoRepository.save(pago);
        return ResponseEntity.created(URI.create("/api/v1/pagos/" + saved.getId())).body(DtoMapper.toResponse(saved));
    }
}
