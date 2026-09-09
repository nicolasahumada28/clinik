package com.timmynet.clinik.controller;

import com.timmynet.clinik.domain.Rol;
import com.timmynet.clinik.domain.Usuario;
import com.timmynet.clinik.dto.DtoMapper;
import com.timmynet.clinik.dto.UsuarioRequest;
import com.timmynet.clinik.dto.UsuarioResponse;
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
    public ResponseEntity<List<UsuarioResponse>> listUsers() {
        return ResponseEntity.ok(usuarioRepository.findAll().stream().map(DtoMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> getUser(@PathVariable Long id) {
        return usuarioRepository.findById(id)
            .map(DtoMapper::toResponse)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> createUser(@RequestBody UsuarioRequest request) {
        Usuario user = Usuario.builder().username(request.username()).password(request.password()).email(request.email()).build();
        if (request.rolId() != null) {
            Rol role = rolRepository.findById(request.rolId()).orElse(null);
            user.setRol(role);
        }
        Usuario saved = usuarioRepository.save(user);
        return ResponseEntity.created(URI.create("/api/v1/usuarios/" + saved.getId())).body(DtoMapper.toResponse(saved));
    }
}
