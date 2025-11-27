package com.example.service;

import com.example.dto.BeneficioRequest;
import com.example.dto.BeneficioResponse;
import com.example.dto.TransferenciaDTO;
import com.example.ejb.Beneficio;
import com.example.repository.BeneficioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Function;

@Service
public class BeneficioService {

    private final BeneficioRepository repo;
    private final EjbClientService ejbClientService;

    public BeneficioService(BeneficioRepository repo, EjbClientService ejbClientService) {
        this.repo = repo;
        this.ejbClientService = ejbClientService;
    }

    private final Function<Beneficio, BeneficioResponse> mapper =
            b -> new BeneficioResponse(
                    b.getId(),
                    b.getNome(),
                    b.getDescricao(),
                    b.getValor(),
                    b.isAtivo());

    @Transactional(readOnly = true)
    public List<BeneficioResponse> listarTodos() {
        return repo.findAll().stream().map(mapper).toList();
    }

    @Transactional(readOnly = true)
    public List<BeneficioResponse> listarAtivos() {
        return repo.findByAtivoTrue().stream().map(mapper).toList();
    }

    @Transactional(readOnly = true)
    public BeneficioResponse buscarPorId(Long id) {
        Beneficio b = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Benefício " + id + " não encontrado"));
        return mapper.apply(b);
    }

    @Transactional
    public BeneficioResponse criar(BeneficioRequest req) {
        Beneficio b = new Beneficio();
        b.setNome(req.getNome());
        b.setDescricao(req.getDescricao());
        b.setValor(req.getValor());
        b.setAtivo(req.isAtivo());
        return mapper.apply(repo.save(b));
    }

    @Transactional
    public BeneficioResponse atualizar(Long id, BeneficioRequest req) {
        Beneficio b = repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Benefício " + id + " não encontrado"));
        b.setNome(req.getNome());
        b.setDescricao(req.getDescricao());
        b.setValor(req.getValor());
        b.setAtivo(req.isAtivo());
        return mapper.apply(repo.save(b));
    }

    @Transactional
    public void excluir(Long id) {
        if (!repo.existsById(id)) {
            throw new EntityNotFoundException("Benefício " + id + " não encontrado");
        }
        repo.deleteById(id);
    }

    @Transactional
    public void transferir(TransferenciaDTO dto) {
        ejbClientService.chamarTransferenciaEJB(dto);
    }
}
