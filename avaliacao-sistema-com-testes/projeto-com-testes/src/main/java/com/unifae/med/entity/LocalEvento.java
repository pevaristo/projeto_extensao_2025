package com.unifae.med.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "locais_eventos")
public class LocalEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_local_evento")
    private Integer idLocalEvento;

    @NotBlank(message = "Nome do local é obrigatório")
    @Size(max = 255, message = "Nome do local deve ter no máximo 255 caracteres")
    @Column(name = "nome_local", nullable = false, length = 255)
    private String nomeLocal;

    @NotBlank(message = "Tipo do local é obrigatório")
    @Size(max = 100, message = "Tipo do local deve ter no máximo 100 caracteres")
    @Column(name = "tipo_local", nullable = false, length = 100)
    private String tipoLocal;

    @NotBlank(message = "Endereço é obrigatório")
    @Size(max = 200, message = "Endereço deve ter no máximo 200 caracteres")
    @Column(name = "endereco", nullable = false, length = 200)
    private String endereco;

    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
    @Column(name = "cidade", nullable = false, length = 100)
    private String cidade;

    @NotBlank(message = "Estado é obrigatório")
    @Size(min = 2, max = 2, message = "Estado deve ter exatamente 2 caracteres")
    @Column(name = "estado", nullable = false, length = 2)
    private String estado;

    // Construtores
    public LocalEvento() {
    }

    public LocalEvento(String nomeLocal, String tipoLocal, String endereco, String cidade, String estado) {
        this.nomeLocal = nomeLocal;
        this.tipoLocal = tipoLocal;
        this.endereco = endereco;
        this.cidade = cidade;
        this.estado = estado;
    }

    // Getters e Setters
    public Integer getIdLocalEvento() {
        return idLocalEvento;
    }

    public void setIdLocalEvento(Integer idLocalEvento) {
        this.idLocalEvento = idLocalEvento;
    }

    public String getNomeLocal() {
        return nomeLocal;
    }

    public void setNomeLocal(String nomeLocal) {
        this.nomeLocal = nomeLocal;
    }

    public String getTipoLocal() {
        return tipoLocal;
    }

    public void setTipoLocal(String tipoLocal) {
        this.tipoLocal = tipoLocal;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "LocalEvento{"
                + "idLocalEvento=" + idLocalEvento
                + ", nomeLocal='" + nomeLocal + '\''
                + ", tipoLocal='" + tipoLocal + '\''
                + ", cidade='" + cidade + '\''
                + ", estado='" + estado + '\''
                + '}';
    }
}
