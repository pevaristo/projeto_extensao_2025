import 'package:flutter/material.dart';
import 'package:fl_chart/fl_chart.dart';
import 'package:printing/printing.dart';
import 'package:pdf/pdf.dart';
import 'package:pdf/widgets.dart' as pw;
import '../theme/app_colors.dart';
import '../models/historico_escolar.dart';

class HistoricoScreen extends StatefulWidget {
  const HistoricoScreen({super.key});

  @override
  State<HistoricoScreen> createState() => _HistoricoScreenState();
}

class _HistoricoScreenState extends State<HistoricoScreen> {
  final List<HistoricoEscolar> _todoHistorico = HistoricoEscolar.getHistoricoExemplo();
  List<HistoricoEscolar> _historicoFiltrado = [];
  String? _semestreFiltro;
  String? _anoFiltro;
  final List<String> _semestres = ['Todos', '1', '2'];
  final List<String> _anos = ['Todos', '2024', '2023', '2022'];

  @override
  void initState() {
    super.initState();
    _historicoFiltrado = _todoHistorico;
  }

  void _aplicarFiltros() {
    setState(() {
      _historicoFiltrado = _todoHistorico.where((item) {
        bool matchSemestre = _semestreFiltro == null ||
            _semestreFiltro == 'Todos' ||
            item.semestre == _semestreFiltro;
        bool matchAno = _anoFiltro == null ||
            _anoFiltro == 'Todos' ||
            item.ano.toString() == _anoFiltro;
        return matchSemestre && matchAno;
      }).toList();
    });
  }

  Future<void> _exportarPDF() async {
    final pdf = pw.Document();
    
    pdf.addPage(
      pw.Page(
        pageFormat: PdfPageFormat.a4,
        build: (pw.Context context) {
          return pw.Column(
            crossAxisAlignment: pw.CrossAxisAlignment.start,
            children: [
              pw.Text(
                'Histórico Escolar',
                style: pw.TextStyle(fontSize: 24, fontWeight: pw.FontWeight.bold),
              ),
              pw.SizedBox(height: 20),
              pw.Table(
                border: pw.TableBorder.all(),
                children: [
                  pw.TableRow(
                    decoration: const pw.BoxDecoration(color: PdfColors.grey300),
                    children: [
                      pw.Padding(
                        padding: const pw.EdgeInsets.all(8),
                        child: pw.Text('Disciplina', style: pw.TextStyle(fontWeight: pw.FontWeight.bold)),
                      ),
                      pw.Padding(
                        padding: const pw.EdgeInsets.all(8),
                        child: pw.Text('Semestre', style: pw.TextStyle(fontWeight: pw.FontWeight.bold)),
                      ),
                      pw.Padding(
                        padding: const pw.EdgeInsets.all(8),
                        child: pw.Text('Ano', style: pw.TextStyle(fontWeight: pw.FontWeight.bold)),
                      ),
                      pw.Padding(
                        padding: const pw.EdgeInsets.all(8),
                        child: pw.Text('Nota Final', style: pw.TextStyle(fontWeight: pw.FontWeight.bold)),
                      ),
                      pw.Padding(
                        padding: const pw.EdgeInsets.all(8),
                        child: pw.Text('Situação', style: pw.TextStyle(fontWeight: pw.FontWeight.bold)),
                      ),
                    ],
                  ),
                  ..._historicoFiltrado.map((item) {
                    return pw.TableRow(
                      children: [
                        pw.Padding(
                          padding: const pw.EdgeInsets.all(8),
                          child: pw.Text(item.disciplina),
                        ),
                        pw.Padding(
                          padding: const pw.EdgeInsets.all(8),
                          child: pw.Text(item.semestre),
                        ),
                        pw.Padding(
                          padding: const pw.EdgeInsets.all(8),
                          child: pw.Text(item.ano.toString()),
                        ),
                        pw.Padding(
                          padding: const pw.EdgeInsets.all(8),
                          child: pw.Text(item.notaFinal.toStringAsFixed(1)),
                        ),
                        pw.Padding(
                          padding: const pw.EdgeInsets.all(8),
                          child: pw.Text(item.situacao),
                        ),
                      ],
                    );
                  }),
                ],
              ),
            ],
          );
        },
      ),
    );

    await Printing.layoutPdf(
      onLayout: (PdfPageFormat format) async => pdf.save(),
    );
  }

  @override
  Widget build(BuildContext context) {
    final textTheme = Theme.of(context).textTheme;
    final mediaGeral = _historicoFiltrado.isEmpty
        ? 0.0
        : _historicoFiltrado.map((h) => h.notaFinal).reduce((a, b) => a + b) /
            _historicoFiltrado.length;

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
                      value: _semestreFiltro,
                      items: _semestres.map((sem) {
                        return DropdownMenuItem(
                          value: sem,
                          child: Text(sem == 'Todos' ? 'Todos os Semestres' : '$semº Semestre'),
                        );
                      }).toList(),
                      onChanged: (value) {
                        setState(() {
                          _semestreFiltro = value;
                        });
                        _aplicarFiltros();
                      },
                    ),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: DropdownButtonFormField<String>(
                      decoration: InputDecoration(
                        labelText: 'Ano',
                        filled: true,
                        fillColor: AppColors.branco,
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(8),
                        ),
                      ),
                      value: _anoFiltro,
                      items: _anos.map((ano) {
                        return DropdownMenuItem(
                          value: ano,
                          child: Text(ano == 'Todos' ? 'Todos os Anos' : ano),
                        );
                      }).toList(),
                      onChanged: (value) {
                        setState(() {
                          _anoFiltro = value;
                        });
                        _aplicarFiltros();
                      },
                    ),
                  ),
                  const SizedBox(width: 12),
                  IconButton(
                    icon: const Icon(Icons.picture_as_pdf),
                    onPressed: _exportarPDF,
                    tooltip: 'Exportar PDF',
                    color: AppColors.azulMarinho,
                  ),
                ],
              ),
            ],
          ),
        ),

        // Conteúdo
        Expanded(
          child: SingleChildScrollView(
            padding: const EdgeInsets.all(20),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                // Card de Média Geral e Total
                Card(
                  child: Padding(
                    padding: const EdgeInsets.all(16.0),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceAround,
                      children: [
                        Column(
                          children: [
                            Text(
                              'Média Geral',
                              style: textTheme.bodyMedium,
                            ),
                            Text(
                              mediaGeral.toStringAsFixed(2),
                              style: textTheme.titleLarge?.copyWith(
                                color: AppColors.amareloDourado,
                                fontSize: 24,
                              ),
                            ),
                          ],
                        ),
                        Column(
                          children: [
                            Text(
                              'Total de Disciplinas',
                              style: textTheme.bodyMedium,
                            ),
                            Text(
                              '${_historicoFiltrado.length}',
                              style: textTheme.titleLarge?.copyWith(
                                color: AppColors.azulTurquesa,
                                fontSize: 24,
                              ),
                            ),
                          ],
                        ),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 24),

                // Gráfico de Desempenho
                Card(
                  child: Padding(
                    padding: const EdgeInsets.all(16),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          'Gráfico de Desempenho',
                          style: textTheme.titleLarge?.copyWith(
                            color: AppColors.azulMarinho,
                            fontSize: 12,
                          ),
                        ),
                        const SizedBox(height: 16),
                        SizedBox(
                          height: 200,
                          child: _historicoFiltrado.isEmpty
                              ? Center(
                                  child: Text(
                                    'Nenhum dado para exibir',
                                    style: textTheme.bodyMedium,
                                  ),
                                )
                              : BarChart(
                                  BarChartData(
                                    alignment: BarChartAlignment.spaceAround,
                                    maxY: 10,
                                    barTouchData: BarTouchData(enabled: false),
                                    titlesData: FlTitlesData(
                                      show: true,
                                      bottomTitles: AxisTitles(
                                        sideTitles: SideTitles(
                                          showTitles: true,
                                          getTitlesWidget: (value, meta) {
                                            if (value.toInt() < _historicoFiltrado.length) {
                                              return Padding(
                                                padding: const EdgeInsets.only(top: 8),
                                                child: Text(
                                                  _historicoFiltrado[value.toInt()].disciplina.substring(0, 3),
                                                  style: const TextStyle(
                                                    fontSize: 8,
                                                    color: AppColors.cinzaEscuro,
                                                  ),
                                                ),
                                              );
                                            }
                                            return const Text('');
                                          },
                                        ),
                                      ),
                                      leftTitles: AxisTitles(
                                        sideTitles: SideTitles(
                                          showTitles: true,
                                          reservedSize: 40,
                                          getTitlesWidget: (value, meta) {
                                            return Text(
                                              value.toInt().toString(),
                                              style: const TextStyle(
                                                fontSize: 10,
                                                color: AppColors.cinzaEscuro,
                                              ),
                                            );
                                          },
                                        ),
                                      ),
                                      topTitles: const AxisTitles(
                                        sideTitles: SideTitles(showTitles: false),
                                      ),
                                      rightTitles: const AxisTitles(
                                        sideTitles: SideTitles(showTitles: false),
                                      ),
                                    ),
                                    gridData: FlGridData(
                                      show: true,
                                      drawVerticalLine: false,
                                      getDrawingHorizontalLine: (value) {
                                        return FlLine(
                                          color: AppColors.cinzaEscuro.withOpacity(0.2),
                                          strokeWidth: 1,
                                        );
                                      },
                                    ),
                                    borderData: FlBorderData(
                                      show: true,
                                      border: Border.all(
                                        color: AppColors.cinzaEscuro.withOpacity(0.3),
                                      ),
                                    ),
                                    barGroups: _historicoFiltrado.asMap().entries.map((entry) {
                                      final index = entry.key;
                                      final item = entry.value;
                                      return BarChartGroupData(
                                        x: index,
                                        barRods: [
                                          BarChartRodData(
                                            toY: item.notaFinal,
                                            color: AppColors.amareloDourado,
                                            width: 20,
                                            borderRadius: const BorderRadius.vertical(
                                              top: Radius.circular(4),
                                            ),
                                          ),
                                        ],
                                      );
                                    }).toList(),
                                  ),
                                ),
                        ),
                      ],
                    ),
                  ),
                ),
                const SizedBox(height: 24),

                // Título da Lista
                Text(
                  'Histórico Completo',
                  style: textTheme.titleLarge?.copyWith(
                    color: AppColors.azulMarinho,
                    fontSize: 12,
                  ),
                ),
                const SizedBox(height: 12),

                // Tabela de Histórico
                _historicoFiltrado.isEmpty
                    ? Center(
                        child: Padding(
                          padding: const EdgeInsets.all(40.0),
                          child: Text(
                            'Nenhum histórico encontrado para os filtros selecionados.',
                            style: textTheme.bodyMedium,
                            textAlign: TextAlign.center,
                          ),
                        ),
                      )
                    : Card(
                        clipBehavior: Clip.antiAlias,
                        child: Column(
                          children: _historicoFiltrado.map((item) {
                            return ListTile(
                              contentPadding: const EdgeInsets.all(16),
                              title: Text(
                                item.disciplina,
                                style: const TextStyle(
                                  fontWeight: FontWeight.bold,
                                  color: AppColors.azulMarinho,
                                ),
                              ),
                              subtitle: Column(
                                crossAxisAlignment: CrossAxisAlignment.start,
                                children: [
                                  const SizedBox(height: 8),
                                  Text('Código: ${item.codigo}'),
                                  Text('${item.semestre}º Semestre/${item.ano}'),
                                  Text('Carga Horária: ${item.cargaHoraria}h'),
                                  Text('Frequência: ${item.frequencia}%'),
                                ],
                              ),
                              trailing: Column(
                                mainAxisAlignment: MainAxisAlignment.center,
                                crossAxisAlignment: CrossAxisAlignment.end,
                                children: [
                                  Text(
                                    item.notaFinal.toStringAsFixed(1),
                                    style: const TextStyle(
                                      fontSize: 20,
                                      fontWeight: FontWeight.bold,
                                      color: AppColors.amareloDourado,
                                    ),
                                  ),
                                  Container(
                                    padding: const EdgeInsets.symmetric(
                                      horizontal: 8,
                                      vertical: 4,
                                    ),
                                    decoration: BoxDecoration(
                                      color: item.situacao == 'Aprovado'
                                          ? Colors.green.withOpacity(0.2)
                                          : Colors.red.withOpacity(0.2),
                                      borderRadius: BorderRadius.circular(4),
                                    ),
                                    child: Text(
                                      item.situacao,
                                      style: TextStyle(
                                        fontSize: 10,
                                        color: item.situacao == 'Aprovado'
                                            ? Colors.green
                                            : Colors.red,
                                        fontWeight: FontWeight.bold,
                                      ),
                                    ),
                                  ),
                                ],
                              ),
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
}
