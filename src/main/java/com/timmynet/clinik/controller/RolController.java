package com.timmynet.clinik.controller;

import com.timmynet.clinik.domain.Rol;
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
    public ResponseEntity<List<Rol>> listRols() {
        return ResponseEntity.ok(rolRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rol> getRol(@PathVariable Long id) {
        return rolRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Rol> createRol(@RequestBody Rol role) {
        Rol saved = rolRepository.save(role);
        return ResponseEntity.created(URI.create("/api/v1/roles/" + saved.getId())).body(saved);
    }
}
