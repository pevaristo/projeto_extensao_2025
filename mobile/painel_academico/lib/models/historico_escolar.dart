class HistoricoEscolar {
  final String id;
  final String disciplina;
  final String codigo;
  final String semestre;
  final int ano;
  final double cargaHoraria;
  final double notaFinal;
  final String situacao;
  final int frequencia;

  HistoricoEscolar({
    required this.id,
    required this.disciplina,
    required this.codigo,
    required this.semestre,
    required this.ano,
    required this.cargaHoraria,
    required this.notaFinal,
    required this.situacao,
    required this.frequencia,
  });

  static List<HistoricoEscolar> getHistoricoExemplo() {
    return [
      HistoricoEscolar(
        id: '1',
        disciplina: 'Anatomia I',
        codigo: 'MED101',
        semestre: '1',
        ano: 2024,
        cargaHoraria: 80,
        notaFinal: 8.1,
        situacao: 'Aprovado',
        frequencia: 85,
      ),
      HistoricoEscolar(
        id: '2',
        disciplina: 'Fisiologia',
        codigo: 'MED102',
        semestre: '1',
        ano: 2024,
        cargaHoraria: 80,
        notaFinal: 7.5,
        situacao: 'Aprovado',
        frequencia: 90,
      ),
      HistoricoEscolar(
        id: '3',
        disciplina: 'Bioquímica',
        codigo: 'MED103',
        semestre: '2',
        ano: 2024,
        cargaHoraria: 60,
        notaFinal: 6.0,
        situacao: 'Aprovado',
        frequencia: 75,
      ),
    ];
  }
}
