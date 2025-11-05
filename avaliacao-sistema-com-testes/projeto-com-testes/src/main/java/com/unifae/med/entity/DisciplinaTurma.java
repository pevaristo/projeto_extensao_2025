package com.unifae.med.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "disciplinas_turmas")
public class DisciplinaTurma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_disciplina_turma")
    private Integer idDisciplinaTurma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_disciplina", nullable = false)
    private Disciplina disciplina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_turma", nullable = false)
    private Turma turma;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_professor")
    private Usuario professor;

    @Column(name = "ativo")
    private Boolean ativo = true;

    // Getters e Setters
    public Integer getIdDisciplinaTurma() {
        return idDisciplinaTurma;
    }

    public void setIdDisciplinaTurma(Integer idDisciplinaTurma) {
        this.idDisciplinaTurma = idDisciplinaTurma;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Usuario getProfessor() {
        return professor;
    }

    public void setProfessor(Usuario professor) {
        this.professor = professor;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
