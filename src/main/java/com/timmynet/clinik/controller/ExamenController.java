package com.timmynet.clinik.controller;

import com.timmynet.clinik.domain.Examen;
import com.timmynet.clinik.domain.DocumentoExamen;
import com.timmynet.clinik.domain.Cita;
import com.timmynet.clinik.repository.CitaRepository;
import com.timmynet.clinik.repository.DocumentoExamenRepository;
import com.timmynet.clinik.repository.ExamenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/examenes")
@RequiredArgsConstructor
public class ExamenController {

    private final ExamenRepository examRepository;
    private final CitaRepository citaRepository;
    private final DocumentoExamenRepository documentRepository;

    @GetMapping
    public ResponseEntity<List<Examen>> getAll() {
        return ResponseEntity.ok(examRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Examen> create(@RequestBody Examen exam) {
        if (exam.getCita() != null && exam.getCita().getId() != null) {
            Cita appointment = citaRepository.findById(exam.getCita().getId()).orElse(null);
            exam.setCita(appointment);
        }
        exam.setCreatedAt(LocalDateTime.now());
        Examen saved = examRepository.save(exam);
        return ResponseEntity.created(URI.create("/api/v1/examenes/" + saved.getId())).body(saved);
    }

    @PostMapping("/{id}/documentos")
    public ResponseEntity<DocumentoExamen> uploadDocument(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws Exception {
        return examRepository.findById(id)
            .map(exam -> {
                try {
                    DocumentoExamen document = DocumentoExamen.builder()
                        .filename(file.getOriginalFilename())
                        .contentType(file.getContentType())
                        .fileSize(file.getSize())
                        .uploadedAt(LocalDateTime.now())
                        .data(file.getBytes())
                        .examen(exam)
                        .build();
                    DocumentoExamen saved = documentRepository.save(document);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setLocation(URI.create("/api/v1/examenes/" + id + "/documentos/" + saved.getId()));
                    return new ResponseEntity<DocumentoExamen>(saved, headers, HttpStatus.CREATED);
                } catch (Exception ex) {
                    return new ResponseEntity<DocumentoExamen>(HttpStatus.BAD_REQUEST);
                }
            })
            .orElse(new ResponseEntity<DocumentoExamen>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/documentos")
    public ResponseEntity<List<DocumentoExamen>> listDocuments(@PathVariable Long id) {
        if (!examRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(documentRepository.findByExamenId(id));
    }

    @GetMapping("/{examenId}/documentos/{documentoId}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long examenId, @PathVariable Long documentoId) {
        return documentRepository.findById(documentoId)
            .filter(doc -> doc.getExamen() != null && doc.getExamen().getId().equals(examenId))
            .map(document -> ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getContentType() == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : document.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFilename() + "\"")
                .body(document.getData()))
            .orElse(ResponseEntity.notFound().build());
    }
}
