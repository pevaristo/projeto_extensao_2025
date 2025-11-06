class Horario {
  final String id;
  final String disciplina;
  final String sigla;
  final String professor;
  final String diaSemana;
  final String horarioInicio;
  final String horarioFim;
  final String sala;
  final DateTime? dataEspecifica;
  final bool alterado;
  final String? detalhesAlteracao;

  Horario({
    required this.id,
    required this.disciplina,
    required this.sigla,
    required this.professor,
    required this.diaSemana,
    required this.horarioInicio,
    required this.horarioFim,
    required this.sala,
    this.dataEspecifica,
    this.alterado = false,
    this.detalhesAlteracao,
  });

  // Dados de exemplo
  static List<Horario> getHorariosExemplo() {
    return [
      Horario(
        id: '1',
        disciplina: 'Anatomia',
        sigla: 'ANA',
        professor: 'Dr. João Silva',
        diaSemana: 'Segunda',
        horarioInicio: '08:00',
        horarioFim: '10:00',
        sala: 'A101',
        alterado: true,
        detalhesAlteracao: 'Sala alterada de A102 para A101',
      ),
      Horario(
        id: '2',
        disciplina: 'Fisiologia',
        sigla: 'FIS',
        professor: 'Dra. Maria Santos',
        diaSemana: 'Terça',
        horarioInicio: '10:00',
        horarioFim: '12:00',
        sala: 'B205',
      ),
      Horario(
        id: '3',
        disciplina: 'Bioquímica',
        sigla: 'BIO',
        professor: 'Dr. Pedro Costa',
        diaSemana: 'Quarta',
        horarioInicio: '14:00',
        horarioFim: '16:00',
        sala: 'C301',
      ),
    ];
  }

  static List<Evento> getEventosExemplo() {
    return [
      Evento(
        id: '1',
        titulo: 'Aula de Anatomia',
        data: DateTime(2024, 6, 15),
        horario: '14:00',
        tipo: 'Aula',
      ),
      Evento(
        id: '2',
        titulo: 'Prova de Fisiologia',
        data: DateTime(2024, 6, 20),
        horario: '08:00',
        tipo: 'Avaliação',
      ),
    ];
  }
}

class Evento {
  final String id;
  final String titulo;
  final DateTime data;
  final String horario;
  final String tipo;

  Evento({
    required this.id,
    required this.titulo,
    required this.data,
    required this.horario,
    required this.tipo,
  });
}
