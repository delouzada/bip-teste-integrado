package com.example.service;

import com.example.dto.BeneficioRequest;
import com.example.dto.TransferenciaDTO;
import com.example.ejb.Beneficio;
import com.example.repository.BeneficioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;

public class BeneficioServiceTest {

    private BeneficioRepository repo;
    private FakeEjbClientService ejbClient;
    private BeneficioService service;

    @BeforeEach
    void setup() {
        repo = mock(BeneficioRepository.class);
        ejbClient = new FakeEjbClientService();
        service = new BeneficioService(repo, ejbClient);
    }

    @Test
    void deveDelegarTransferenciaParaEjbClient() {
        TransferenciaDTO dto = new TransferenciaDTO();
        dto.setFromId(1L);
        dto.setToId(2L);
        dto.setValor(BigDecimal.TEN);

        service.transferir(dto);

        assert ejbClient.called;
        assert ejbClient.lastDto == dto;
    }

    @Test
    void deveListarTodosComLambda() {
        var b1 = new Beneficio();
        b1.setId(1L);
        b1.setNome("A");

        var b2 = new Beneficio();
        b2.setId(2L);
        b2.setNome("B");

        when(repo.findAll()).thenReturn(List.of(b1, b2));

        var result = service.listarTodos();

        Map<Long, String> mapa = result.stream()
                .collect(java.util.stream.Collectors.toMap(
                        r -> r.getId(),
                        r -> r.getNome()
                ));

        assert mapa.get(1L).equals("A");
        assert mapa.get(2L).equals("B");
    }

    @Test
    void deveCriarBeneficio() {
        BeneficioRequest req = new BeneficioRequest();
        req.setNome("Teste");
        req.setValor(BigDecimal.TEN);
        req.setAtivo(true);

        var salvo = new Beneficio();
        salvo.setId(1L);
        salvo.setNome("Teste");
        salvo.setValor(BigDecimal.TEN);
        salvo.setAtivo(true);
        when(repo.save(any())).thenReturn(salvo);

        var resp = service.criar(req);

        assert resp.getId() == 1L;
        assert resp.getNome().equals("Teste");
    }

    private static class FakeEjbClientService extends EjbClientService {
        private boolean called = false;
        private TransferenciaDTO lastDto;

        FakeEjbClientService() {
            super(new RestTemplate());
        }

        @Override
        public void chamarTransferenciaEJB(TransferenciaDTO dto) {
            this.called = true;
            this.lastDto = dto;
        }
    }
}
