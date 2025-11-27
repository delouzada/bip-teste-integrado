package com.example.ejb;

import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;

@Stateless
public class BeneficioEjbService {

    @PersistenceContext(unitName = "default")
    private EntityManager em;

    @Transactional
    public void transferir(Long fromId, Long toId, BigDecimal valor) {
        if (fromId == null || toId == null) {
            throw new IllegalArgumentException("IDs nao podem ser nulos");
        }
        if (fromId.equals(toId)) {
            throw new IllegalArgumentException("Nao e permitido transferir para o mesmo beneficio");
        }
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }

        Beneficio origem = em.find(Beneficio.class, fromId, LockModeType.PESSIMISTIC_WRITE);
        Beneficio destino = em.find(Beneficio.class, toId, LockModeType.PESSIMISTIC_WRITE);

        if (origem == null || destino == null) {
            throw new EntityNotFoundException("Beneficio origem/destino nao encontrado");
        }

        if (!Boolean.TRUE.equals(origem.getAtivo()) || !Boolean.TRUE.equals(destino.getAtivo())) {
            throw new IllegalStateException("Ambos os beneficios devem estar ativos");
        }

        origem.debitar(valor);
        destino.creditar(valor);
    }
}

