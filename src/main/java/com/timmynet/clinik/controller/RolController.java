package com.timmynet.clinik.controller;

import com.timmynet.clinik.domain.Rol;
import com.timmynet.clinik.dto.DtoMapper;
import com.timmynet.clinik.dto.RolRequest;
import com.timmynet.clinik.dto.RolResponse;
import com.timmynet.clinik.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolRepository rolRepository;

    @GetMapping
    public ResponseEntity<List<RolResponse>> listRols() {
        return ResponseEntity.ok(rolRepository.findAll().stream().map(DtoMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolResponse> getRol(@PathVariable Long id) {
        return rolRepository.findById(id)
            .map(DtoMapper::toResponse)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<RolResponse> createRol(@RequestBody RolRequest request) {
        Rol role = Rol.builder().nombre(request.nombre()).activo(request.activo()).build();
        Rol saved = rolRepository.save(role);
        return ResponseEntity.created(URI.create("/api/v1/roles/" + saved.getId())).body(DtoMapper.toResponse(saved));
    }
}
