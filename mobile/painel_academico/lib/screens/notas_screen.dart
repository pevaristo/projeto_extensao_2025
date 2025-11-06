import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import '../theme/app_colors.dart';
import '../models/nota.dart';

class NotasScreen extends StatefulWidget {
  const NotasScreen({super.key});

  @override
  State<NotasScreen> createState() => _NotasScreenState();
}

class _NotasScreenState extends State<NotasScreen> {
  final List<Nota> _todasNotas = Nota.getNotasExemplo();
  List<Nota> _notasFiltradas = [];
  String? _semestreSelecionado;
  int? _anoSelecionado;
  final List<String> _semestres = ['1', '2'];
  final List<int> _anos = [2024, 2023, 2022];

  @override
  void initState() {
    super.initState();
    _notasFiltradas = _todasNotas;
  }

  void _aplicarFiltro() {
    setState(() {
      if (_semestreSelecionado == null && _anoSelecionado == null) {
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(
            content: Text('Selecione um período válido para visualizar as notas.'),
            backgroundColor: Colors.red,
          ),
        );
        _notasFiltradas = [];
      } else {
        _notasFiltradas = _todasNotas.where((nota) {
          bool matchSemestre = _semestreSelecionado == null || nota.semestre == _semestreSelecionado;
          bool matchAno = _anoSelecionado == null || nota.ano == _anoSelecionado;
          return matchSemestre && matchAno;
        }).toList();

        if (_notasFiltradas.isEmpty) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(
              content: Text('Nenhuma nota registrada para o período selecionado.'),
            ),
          );
        }
      }
    });
  }

  void _limparFiltros() {
    setState(() {
      _semestreSelecionado = null;
      _anoSelecionado = null;
      _notasFiltradas = _todasNotas;
    });
  }

  @override
  Widget build(BuildContext context) {
    final textTheme = Theme.of(context).textTheme;
    final mediaFinal = Nota(
      id: '',
      disciplina: '',
      tipoAvaliacao: '',
      nota: 0,
      peso: 0,
      data: DateTime.now(),
      semestre: '',
      ano: 2024,
    ).calcularMediaFinal(_notasFiltradas);

    return Column(
      children: [
        // Filtros
        Container(
          padding: const EdgeInsets.all(16),
          decoration: BoxDecoration(
            color: AppColors.azulMarinho.withOpacity(0.05),
            border: Border(
              bottom: BorderSide(color: AppColors.cinzaEscuro.withOpacity(0.2)),
            ),
          ),
          child: Column(
            children: [
              Row(
                children: [
                  Expanded(
                    child: DropdownButtonFormField<String>(
                      decoration: InputDecoration(
                        labelText: 'Semestre',
                        filled: true,
                        fillColor: AppColors.branco,
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(8),
                        ),
                      ),
                      value: _semestreSelecionado,
                      items: _semestres.map((sem) {
                        return DropdownMenuItem(
                          value: sem,
                          child: Text('$semº Semestre'),
                        );
                      }).toList(),
                      onChanged: (value) {
                        setState(() {
                          _semestreSelecionado = value;
                        });
                      },
                    ),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: DropdownButtonFormField<int>(
                      decoration: InputDecoration(
                        labelText: 'Ano',
                        filled: true,
                        fillColor: AppColors.branco,
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(8),
                        ),
                      ),
                      value: _anoSelecionado,
                      items: _anos.map((ano) {
                        return DropdownMenuItem(
                          value: ano,
                          child: Text(ano.toString()),
                        );
                      }).toList(),
                      onChanged: (value) {
                        setState(() {
                          _anoSelecionado = value;
                        });
                      },
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 12),
              Row(
                children: [
                  Expanded(
                    child: ElevatedButton(
                      onPressed: _aplicarFiltro,
                      child: const Text('Pesquisar'),
                    ),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: OutlinedButton(
                      onPressed: _limparFiltros,
                      style: OutlinedButton.styleFrom(
                        foregroundColor: AppColors.azulMarinho,
                        side: const BorderSide(color: AppColors.azulMarinho),
                      ),
                      child: const Text('Limpar'),
                    ),
                  ),
                ],
              ),
            ],
          ),
        ),
        
        // Conteúdo
        Expanded(
          child: SingleChildScrollView(
            padding: const EdgeInsets.all(20.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // Card de Média Final
                Card(
                  child: Padding(
                    padding: const EdgeInsets.all(16.0),
                    child: Row(
                      children: [
                        Container(
                          padding: const EdgeInsets.all(16),
                          decoration: BoxDecoration(
                            color: AppColors.amareloDourado.withOpacity(0.15),
                            shape: BoxShape.circle,
                          ),
                          child: Text(
                            mediaFinal.toStringAsFixed(1),
                            style: textTheme.titleLarge?.copyWith(
                              color: AppColors.amareloDourado,
                              fontSize: 24,
                            ),
                          ),
                        ),
                        const SizedBox(width: 16),
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Text(
                                'Média Final',
                                style: textTheme.titleLarge?.copyWith(
                                  fontSize: 12,
                                ),
                              ),
                              Text(
                                'Seu desempenho está ótimo!',
                                style: textTheme.bodyMedium,
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 24),
                
                // Título da Lista de Notas
                Text(
                  'Notas por Disciplina',
                  style: textTheme.titleLarge?.copyWith(
                    color: AppColors.azulMarinho,
                    fontSize: 12,
                  ),
                ),
                const SizedBox(height: 12),
                
                // Lista de Notas
                _notasFiltradas.isEmpty
                    ? Center(
                        child: Padding(
                          padding: const EdgeInsets.all(40.0),
                          child: Text(
                            _semestreSelecionado == null && _anoSelecionado == null
                                ? 'Selecione um período para visualizar as notas'
                                : 'Nenhuma nota registrada para o período selecionado.',
                            style: textTheme.bodyMedium,
                            textAlign: TextAlign.center,
                          ),
                        ),
                      )
                    : Card(
                        clipBehavior: Clip.antiAlias,
                        child: Column(
                          children: _notasFiltradas.map((nota) {
                            return _buildGradeTile(
                              nota.disciplina,
                              nota.nota.toStringAsFixed(1),
                              Icons.assessment,
                              nota.tipoAvaliacao,
                              nota.data,
                              nota.peso,
                            );
                          }).toList(),
                        ),
                      ),
              ],
            ),
          ),
        ),
      ],
    );
  }

  Widget _buildGradeTile(
    String subject,
    String grade,
    IconData icon,
    String tipoAvaliacao,
    DateTime data,
    double peso,
  ) {
    return ListTile(
      leading: Icon(icon, color: AppColors.azulTurquesa),
      title: Text(
        subject,
        style: const TextStyle(
          color: AppColors.azulMarinho,
          fontSize: 10,
          fontWeight: FontWeight.bold,
        ),
      ),
      subtitle: Text(
        '$tipoAvaliacao - ${DateFormat('dd/MM/yyyy').format(data)} - Peso: $peso',
        style: const TextStyle(
          color: AppColors.cinzaEscuro,
          fontSize: 8,
        ),
      ),
      trailing: Text(
        grade,
        style: const TextStyle(
          color: AppColors.azulMarinho,
          fontSize: 12,
          fontWeight: FontWeight.bold,
        ),
      ),
    );
  }
}
