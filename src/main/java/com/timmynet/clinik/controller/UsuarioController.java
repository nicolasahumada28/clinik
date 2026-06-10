package com.timmynet.clinik.controller;

import com.timmynet.clinik.domain.Rol;
import com.timmynet.clinik.domain.Usuario;
import com.timmynet.clinik.repository.RolRepository;
import com.timmynet.clinik.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    @GetMapping
    public ResponseEntity<List<Usuario>> listUsers() {
        return ResponseEntity.ok(usuarioRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUser(@PathVariable Long id) {
        return usuarioRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> createUser(@RequestBody Usuario user) {
        if (user.getRol() != null && user.getRol().getId() != null) {
            Rol role = rolRepository.findById(user.getRol().getId()).orElse(null);
            user.setRol(role);
        }
        Usuario saved = usuarioRepository.save(user);
        return ResponseEntity.created(URI.create("/api/v1/usuarios/" + saved.getId())).body(saved);
    }
}
