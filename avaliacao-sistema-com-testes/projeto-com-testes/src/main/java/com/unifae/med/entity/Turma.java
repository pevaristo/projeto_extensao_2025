package com.unifae.med.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
// <<< MUDANÇA: Removido import java.time.Year >>>

@Entity
@Table(name = "turmas")
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_turma")
    private Integer idTurma;

    @NotBlank(message = "Nome da turma é obrigatório")
    @Size(max = 255, message = "Nome da turma deve ter no máximo 255 caracteres")
    @Column(name = "nome_turma", nullable = false, length = 255)
    private String nomeTurma;

    @Size(max = 50, message = "Código da turma deve ter no máximo 50 caracteres")
    @Column(name = "codigo_turma", unique = true, length = 50)
    private String codigoTurma;

    // <<< MUDANÇA: Voltamos a usar Integer para máxima compatibilidade >>>
    @Column(name = "ano_letivo", nullable = false)
    private Integer anoLetivo;

    @Column(name = "semestre", nullable = false)
    private Integer semestre;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    // Construtores
    public Turma() {
    }

    // Getters e Setters
    public Integer getIdTurma() {
        return idTurma;
    }

    public void setIdTurma(Integer idTurma) {
        this.idTurma = idTurma;
    }

    public String getNomeTurma() {
        return nomeTurma;
    }

    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }

    public String getCodigoTurma() {
        return codigoTurma;
    }

    public void setCodigoTurma(String codigoTurma) {
        this.codigoTurma = codigoTurma;
    }

    // <<< MUDANÇA: Getters e Setters para Integer >>>
    public Integer getAnoLetivo() {
        return anoLetivo;
    }

    public void setAnoLetivo(Integer anoLetivo) {
        this.anoLetivo = anoLetivo;
    }

    public Integer getSemestre() {
        return semestre;
    }

    public void setSemestre(Integer semestre) {
        this.semestre = semestre;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public String toString() {
        return "Turma{"
                + "idTurma=" + idTurma
                + ", nomeTurma='" + nomeTurma + '\''
                + ", anoLetivo=" + anoLetivo
                + ", semestre=" + semestre
                + ", ativo=" + ativo
                + '}';
    }
}
