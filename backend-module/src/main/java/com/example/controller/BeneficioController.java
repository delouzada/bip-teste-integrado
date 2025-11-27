package com.example.controller;

import com.example.dto.BeneficioRequest;
import com.example.dto.BeneficioResponse;
import com.example.dto.TransferenciaDTO;
import com.example.service.BeneficioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficios")
public class BeneficioController {

    private final BeneficioService service;

    public BeneficioController(BeneficioService service) {
        this.service = service;
    }

    @GetMapping
    public List<BeneficioResponse> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/ativos")
    public List<BeneficioResponse> listarAtivos() {
        return service.listarAtivos();
    }

    @GetMapping("/{id}")
    public BeneficioResponse buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<BeneficioResponse> criar(@RequestBody @Valid BeneficioRequest req) {
        return ResponseEntity.ok(service.criar(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BeneficioResponse> atualizar(@PathVariable Long id,
                                                       @RequestBody @Valid BeneficioRequest req) {
        return ResponseEntity.ok(service.atualizar(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/transferir")
    public ResponseEntity<Void> transferir(@RequestBody @Valid TransferenciaDTO dto) {
        service.transferir(dto);
        return ResponseEntity.ok().build();
    }
}
