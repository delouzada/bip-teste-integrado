package com.example.ejb;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.LockModeType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BeneficioEjbServiceTest {

    @Mock
    private EntityManager em;

    @InjectMocks
    private BeneficioEjbService service;

    @Test
    void transferir_Sucesso() {
        // Arrange
        Long fromId = 1L;
        Long toId = 2L;
        BigDecimal valor = new BigDecimal("100.00");

        Beneficio origem = new Beneficio();
        origem.setId(fromId);
        origem.setValor(new BigDecimal("200.00"));
        origem.setAtivo(true);

        Beneficio destino = new Beneficio();
        destino.setId(toId);
        destino.setValor(new BigDecimal("50.00"));
        destino.setAtivo(true);

        when(em.find(eq(Beneficio.class), eq(fromId), any(LockModeType.class))).thenReturn(origem);
        when(em.find(eq(Beneficio.class), eq(toId), any(LockModeType.class))).thenReturn(destino);

        // Act
        service.transferir(fromId, toId, valor);

        // Assert
        assertEquals(new BigDecimal("100.00"), origem.getValor());
        assertEquals(new BigDecimal("150.00"), destino.getValor());
    }

    @Test
    void transferir_DeveFalhar_QuandoIdsNulos() {
        assertThrows(IllegalArgumentException.class, () -> service.transferir(null, 2L, BigDecimal.TEN));
        assertThrows(IllegalArgumentException.class, () -> service.transferir(1L, null, BigDecimal.TEN));
    }

    @Test
    void transferir_DeveFalhar_QuandoMesmoId() {
        assertThrows(IllegalArgumentException.class, () -> service.transferir(1L, 1L, BigDecimal.TEN));
    }

    @Test
    void transferir_DeveFalhar_QuandoValorInvalido() {
        assertThrows(IllegalArgumentException.class, () -> service.transferir(1L, 2L, null));
        assertThrows(IllegalArgumentException.class, () -> service.transferir(1L, 2L, BigDecimal.ZERO));
        assertThrows(IllegalArgumentException.class, () -> service.transferir(1L, 2L, new BigDecimal("-10")));
    }

    @Test
    void transferir_DeveFalhar_QuandoBeneficioNaoEncontrado() {
        when(em.find(eq(Beneficio.class), eq(1L), any(LockModeType.class))).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> service.transferir(1L, 2L, BigDecimal.TEN));
    }

    @Test
    void transferir_DeveFalhar_QuandoBeneficioInativo() {
        Beneficio origem = new Beneficio();
        origem.setAtivo(false);

        Beneficio destino = new Beneficio();
        destino.setAtivo(true);

        when(em.find(eq(Beneficio.class), eq(1L), any(LockModeType.class))).thenReturn(origem);
        when(em.find(eq(Beneficio.class), eq(2L), any(LockModeType.class))).thenReturn(destino);

        assertThrows(IllegalStateException.class, () -> service.transferir(1L, 2L, BigDecimal.TEN));
    }
}
