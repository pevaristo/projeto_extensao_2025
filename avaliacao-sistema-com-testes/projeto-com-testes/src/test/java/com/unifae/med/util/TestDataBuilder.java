package com.unifae.med.util;

import com.unifae.med.entity.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Classe utilitária para construção de objetos de teste.
 *
 * Fornece métodos factory para criar instâncias de entidades com dados válidos
 * para uso em testes.
 *
 * Padrão Test Data Builder para facilitar criação de objetos de teste.
 * 
 * CORREÇÃO APLICADA: Gerador de emails únicos para evitar violação de constraint UNIQUE.
 * CORREÇÃO APLICADA: Matrícula com máximo 6 caracteres conforme validação da entidade.
 */
public class TestDataBuilder {

    private static final AtomicInteger counter = new AtomicInteger(0);

    /**
     * Gera um sufixo único para emails e códigos baseado em timestamp e contador.
     * Thread-safe usando AtomicInteger.
     */
    private static String getUniqueSuffix() {
        return System.currentTimeMillis() + "_" + counter.incrementAndGet();
    }

    /**
     * Gera uma matrícula única com no máximo 6 caracteres.
     * Formato: Usa apenas os últimos 5 dígitos do contador + prefixo de 1 caractere.
     * Exemplo: E00001, E00002, etc.
     */
    private static String getUniqueMatricula() {
        int num = counter.incrementAndGet();
        return String.format("E%05d", num % 100000); // E + 5 dígitos = 6 caracteres
    }

    /**
     * Gera um código de disciplina único com no máximo 20 caracteres.
     * Formato: MED + 5 dígitos
     */
    private static String getUniqueCodDisciplina() {
        int num = counter.incrementAndGet();
        return String.format("MED%05d", num % 100000);
    }

    /**
     * Cria um usuário estudante válido para testes.
     * CORREÇÃO: Email único gerado dinamicamente para evitar violação de constraint.
     * CORREÇÃO: Matrícula com máximo 6 caracteres (formato E00001).
     */
    public static Usuario criarUsuarioEstudante() {
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto("João da Silva");
        usuario.setEmail("joao.silva." + getUniqueSuffix() + "@unifae.edu.br");
        usuario.setSenhaHash("$2a$10$abcdefghijklmnopqrstuvwxyz");
        usuario.setTelefone("43999887766");
        usuario.setMatriculaRA(getUniqueMatricula());
        usuario.setTipoUsuario(TipoUsuario.ESTUDANTE);
        usuario.setAtivo(true);
        usuario.setPeriodoAtualAluno("5");
        return usuario;
    }

    /**
     * Cria um usuário professor válido para testes.
     * CORREÇÃO: Email único gerado dinamicamente para evitar violação de constraint.
     */
    public static Usuario criarUsuarioProfessor() {
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto("Maria Santos");
        usuario.setEmail("maria.santos." + getUniqueSuffix() + "@unifae.edu.br");
        usuario.setSenhaHash("$2a$10$abcdefghijklmnopqrstuvwxyz");
        usuario.setTelefone("43988776655");
        usuario.setTipoUsuario(TipoUsuario.PROFESSOR);
        usuario.setAtivo(true);
        return usuario;
    }

    /**
     * Cria um usuário coordenador válido para testes.
     * CORREÇÃO: Email único gerado dinamicamente para evitar violação de constraint.
     */
    public static Usuario criarUsuarioCoordenador() {
        Usuario usuario = new Usuario();
        usuario.setNomeCompleto("Pedro Oliveira");
        usuario.setEmail("pedro.oliveira." + getUniqueSuffix() + "@unifae.edu.br");
        usuario.setSenhaHash("$2a$10$abcdefghijklmnopqrstuvwxyz");
        usuario.setTelefone("43977665544");
        usuario.setTipoUsuario(TipoUsuario.COORDENADOR);
        usuario.setAtivo(true);
        return usuario;
    }

    /**
     * Cria um questionário válido para testes.
     */
    public static Questionario criarQuestionario() {
        Questionario questionario = new Questionario();
        questionario.setNomeModelo("Mini CEX");
        questionario.setDescricao("Avaliação de competências clínicas");
        return questionario;
    }

    /**
     * Cria uma competência de questionário válida para testes.
     */
    public static CompetenciaQuestionario criarCompetenciaQuestionario(Questionario questionario) {
        CompetenciaQuestionario competencia = new CompetenciaQuestionario();
        competencia.setQuestionario(questionario);
        competencia.setNomeCompetencia("Habilidades de comunicação");
        competencia.setTipoItem(CompetenciaQuestionario.TipoItem.escala_numerica);
        competencia.setOrdemExibicao(1);
        competencia.setObrigatorio(true);
        competencia.setAtivo(true);
        return competencia;
    }

    /**
     * Cria uma turma válida para testes.
     */
    public static Turma criarTurma() {
        Turma turma = new Turma();
        turma.setNomeTurma("Medicina 2024");
        turma.setAnoLetivo(2024);
        turma.setSemestre(1);
        return turma;
    }

    /**
     * Cria uma disciplina válida para testes.
     * CORREÇÃO: Código único gerado dinamicamente para evitar violação de constraint.
     */
    public static Disciplina criarDisciplina() {
        Disciplina disciplina = new Disciplina();
        disciplina.setNomeDisciplina("Clínica Médica");
        disciplina.setCodigoDisciplina(getUniqueCodDisciplina());
        disciplina.setSiglaDisciplina("CLINMED");
        disciplina.setAtiva(true);
        return disciplina;
    }

    /**
     * Cria um local de evento válido para testes.
     */
    public static LocalEvento criarLocalEvento() {
        LocalEvento local = new LocalEvento();
        local.setNomeLocal("Hospital Universitário");
        local.setTipoLocal("Hospital");
        local.setEndereco("Rua das Flores, 123");
        local.setCidade("Londrina");
        local.setEstado("PR");
        return local;
    }

    /**
     * Cria uma permissão válida para testes.
     * CORREÇÃO: Nome único gerado dinamicamente para evitar violação de constraint.
     */
    public static Permissao criarPermissao() {
        Permissao permissao = new Permissao();
        permissao.setNomePermissao("Gerenciar Avaliações " + getUniqueSuffix());
        permissao.setDescricaoPermissao("Permite criar e editar avaliações");
        return permissao;
    }

    /**
     * Cria uma avaliação preenchida válida para testes.
     */
    public static AvaliacaoPreenchida criarAvaliacaoPreenchida(
            Questionario questionario,
            Usuario avaliador,
            Usuario avaliado) {
        AvaliacaoPreenchida avaliacao = new AvaliacaoPreenchida();
        avaliacao.setQuestionario(questionario);
        avaliacao.setAvaliador(avaliador);
        avaliacao.setAlunoAvaliado(avaliado);
        avaliacao.setDataRealizacao(LocalDate.now());
        avaliacao.setHorarioInicio(LocalTime.of(9, 0));
        avaliacao.setHorarioFim(LocalTime.of(10, 0));
        avaliacao.setLocalRealizacao("Hospital Universitário");
        return avaliacao;
    }

    /**
     * Cria um evento de agenda válido para testes.
     */
    public static EventoAgenda criarEventoAgenda(Usuario usuario, LocalEvento local) {
        EventoAgenda evento = new EventoAgenda();
        evento.setResponsavel(usuario);
        evento.setLocalEvento(local);
        evento.setTitulo("Avaliação Clínica");
        evento.setDescricao("Avaliação de habilidades práticas");
        evento.setDataInicio(LocalDateTime.now().plusDays(1));
        evento.setDataFim(LocalDateTime.now().plusDays(1).plusHours(2));
        evento.setTipoEvento(TipoEvento.AVALIACAO);
        evento.setStatusEvento(StatusEvento.AGENDADO);
        return evento;
    }

    /**
     * Cria uma nota válida para testes.
     */
    public static Nota criarNota(Usuario aluno, Disciplina disciplina) {
        Nota nota = new Nota();
        nota.setAluno(aluno);
        nota.setDisciplina(disciplina);
        nota.setValorNota(new BigDecimal("8.5"));
        nota.setDataAvaliacao(LocalDate.now());
        nota.setDataLancamento(LocalDate.now());
        nota.setTipoAvaliacao(TipoAvaliacao.PROVA);
        return nota;
    }
}
