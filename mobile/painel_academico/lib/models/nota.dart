class Nota {
  final String id;
  final String disciplina;
  final String tipoAvaliacao;
  final double nota;
  final double peso;
  final DateTime data;
  final String semestre;
  final int ano;

  Nota({
    required this.id,
    required this.disciplina,
    required this.tipoAvaliacao,
    required this.nota,
    required this.peso,
    required this.data,
    required this.semestre,
    required this.ano,
  });

  // Dados de exemplo para demonstração
  static List<Nota> getNotasExemplo() {
    return [
      Nota(
        id: '1',
        disciplina: 'Anatomia I',
        tipoAvaliacao: 'Prova',
        nota: 20.0,
        peso: 0.5,
        data: DateTime(2024, 5, 15),
        semestre: '1',
        ano: 2024,
      ),
      Nota(
        id: '2',
        disciplina: 'Anatomia I',
        tipoAvaliacao: 'Trabalho',
        nota: 18.0,
        peso: 0.3,
        data: DateTime(2024, 4, 20),
        semestre: '1',
        ano: 2024,
      ),
      Nota(
        id: '3',
        disciplina: 'Fisiologia',
        tipoAvaliacao: 'Prova',
        nota: 19.0,
        peso: 0.5,
        data: DateTime(2024, 5, 10),
        semestre: '1',
        ano: 2024,
      ),
    ];
  }

  double calcularMediaFinal(List<Nota> notas) {
    double somaPonderada = 0;
    double somaPesos = 0;
    
    for (var nota in notas) {
      somaPonderada += nota.nota * nota.peso;
      somaPesos += nota.peso;
    }
    
    return somaPesos > 0 ? somaPonderada / somaPesos : 0;
  }
}
