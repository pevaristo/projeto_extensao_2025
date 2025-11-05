package com.unifae.med.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "disciplinas")
public class Disciplina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_disciplina")
    private Integer idDisciplina;

    @NotBlank(message = "Nome da disciplina é obrigatório")
    @Size(max = 255, message = "Nome da disciplina deve ter no máximo 255 caracteres")
    @Column(name = "nome_disciplina", nullable = false, length = 255)
    private String nomeDisciplina;

    // <<< CAMPO ADICIONADO PARA CORRIGIR O ERRO NO FORMULÁRIO DE NOTAS >>>
    @NotBlank(message = "Código da disciplina é obrigatório")
    @Size(max = 20, message = "Código da disciplina deve ter no máximo 20 caracteres")
    @Column(name = "codigo_disciplina", unique = true, nullable = false, length = 20)
    private String codigoDisciplina;

    @Size(max = 10, message = "Sigla da disciplina deve ter no máximo 10 caracteres")
    @Column(name = "sigla_disciplina", unique = true, length = 10)
    private String siglaDisciplina;

    // <<< CAMPO E MÉTODO RESTAURADOS PARA CORRIGIR O ERRO NO SERVLET >>>
    // A anotação @Column garante o mapeamento correto com o banco de dados.
    @Column(name = "ativo", nullable = false)
    private Boolean ativa = true;

    // Construtores
    public Disciplina() {
    }

    // Getters e Setters
    public Integer getIdDisciplina() {
        return idDisciplina;
    }

    public void setIdDisciplina(Integer idDisciplina) {
        this.idDisciplina = idDisciplina;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public String getCodigoDisciplina() {
        return codigoDisciplina;
    }

    public void setCodigoDisciplina(String codigoDisciplina) {
        this.codigoDisciplina = codigoDisciplina;
    }

    public String getSiglaDisciplina() {
        return siglaDisciplina;
    }

    public void setSiglaDisciplina(String siglaDisciplina) {
        this.siglaDisciplina = siglaDisciplina;
    }

    // <<< MÉTODO getAtiva() RESTAURADO >>>
    public Boolean getAtiva() {
        return ativa;
    }

    public void setAtiva(Boolean ativa) {
        this.ativa = ativa;
    }

    @Override
    public String toString() {
        return "Disciplina{"
                + "idDisciplina=" + idDisciplina
                + ", nomeDisciplina='" + nomeDisciplina + '\''
                + ", codigoDisciplina='" + codigoDisciplina + '\''
                + ", siglaDisciplina='" + siglaDisciplina + '\''
                + ", ativa=" + ativa
                + '}';
    }
}
