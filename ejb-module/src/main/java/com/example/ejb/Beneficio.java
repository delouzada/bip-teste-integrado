package com.example.ejb;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "beneficio")
public class Beneficio implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column
    private boolean ativo;

    @Version
    @Column
    private Long versao;

    public Beneficio() {
    }

    // --- GETTERS / SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public void debitar(BigDecimal valor) {
        validarValorPositivo(valor);
        BigDecimal saldoAtual = this.valor == null ? BigDecimal.ZERO : this.valor;
        if (saldoAtual.compareTo(valor) < 0) {
            throw new IllegalStateException("Saldo insuficiente para debito");
        }
        this.valor = saldoAtual.subtract(valor);
    }

    public void creditar(BigDecimal valor) {
        validarValorPositivo(valor);
        BigDecimal saldoAtual = this.valor == null ? BigDecimal.ZERO : this.valor;
        this.valor = saldoAtual.add(valor);
    }

    private void validarValorPositivo(BigDecimal valor) {
        if (valor == null || valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser positivo");
        }
    }

    // boolean padrao JavaBean
    public boolean isAtivo() {
        return ativo;
    }

    // getter "normal" para quem chamar getAtivo()
    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Long getVersao() {
        return versao;
    }

    public void setVersao(Long versao) {
        this.versao = versao;
    }
}

