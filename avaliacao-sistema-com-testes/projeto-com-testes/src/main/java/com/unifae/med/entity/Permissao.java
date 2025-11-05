package com.unifae.med.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "permissoes")
public class Permissao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permissao")
    private Integer idPermissao;

    @NotBlank(message = "Nome da permissão é obrigatório")
    @Size(max = 50, message = "Nome da permissão deve ter no máximo 50 caracteres")
    @Column(name = "nome_permissao", nullable = false, unique = true, length = 50)
    private String nomePermissao;

    @Column(name = "descricao_permissao", columnDefinition = "TEXT")
    private String descricaoPermissao;

    // Construtores
    public Permissao() {
    }

    public Permissao(String nomePermissao, String descricaoPermissao) {
        this.nomePermissao = nomePermissao;
        this.descricaoPermissao = descricaoPermissao;
    }

    // Getters e Setters
    public Integer getIdPermissao() {
        return idPermissao;
    }

    public void setIdPermissao(Integer idPermissao) {
        this.idPermissao = idPermissao;
    }

    public String getNomePermissao() {
        return nomePermissao;
    }

    public void setNomePermissao(String nomePermissao) {
        this.nomePermissao = nomePermissao;
    }

    public String getDescricaoPermissao() {
        return descricaoPermissao;
    }

    public void setDescricaoPermissao(String descricaoPermissao) {
        this.descricaoPermissao = descricaoPermissao;
    }

    @Override
    public String toString() {
        return "Permissao{"
                + "idPermissao=" + idPermissao
                + ", nomePermissao='" + nomePermissao + '\''
                + ", descricaoPermissao='" + descricaoPermissao + '\''
                + '}';
    }
}
