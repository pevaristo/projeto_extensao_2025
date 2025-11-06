import 'package:flutter/material.dart';
import '../theme/app_colors.dart';
import '../models/horario.dart';

class HorariosScreen extends StatefulWidget {
  const HorariosScreen({super.key});

  @override
  State<HorariosScreen> createState() => _HorariosScreenState();
}

class _HorariosScreenState extends State<HorariosScreen> {
  final List<Horario> _horarios = Horario.getHorariosExemplo();
  final List<Evento> _eventos = Horario.getEventosExemplo();
  
  final Map<String, List<Horario>> _horariosPorDia = {};

  @override
  void initState() {
    super.initState();
    _organizarHorarios();
  }

  void _organizarHorarios() {
    _horariosPorDia.clear();
    for (var horario in _horarios) {
      if (!_horariosPorDia.containsKey(horario.diaSemana)) {
        _horariosPorDia[horario.diaSemana] = [];
      }
      _horariosPorDia[horario.diaSemana]!.add(horario);
    }
  }

  @override
  Widget build(BuildContext context) {
    final textTheme = Theme.of(context).textTheme;
    final horariosAlterados = _horarios.where((h) => h.alterado).toList();

    return SingleChildScrollView(
      padding: const EdgeInsets.all(20),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          // Notificações de Alterações
          if (horariosAlterados.isNotEmpty) ...[
            Container(
              padding: const EdgeInsets.all(12),
              margin: const EdgeInsets.only(bottom: 16),
              decoration: BoxDecoration(
                color: AppColors.amareloDourado.withOpacity(0.1),
                borderRadius: BorderRadius.circular(8),
                border: Border.all(color: AppColors.amareloDourado),
              ),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  const Row(
                    children: [
                      Icon(Icons.notifications, color: AppColors.amareloDourado),
                      SizedBox(width: 8),
                      Text(
                        'Alterações nos Horários',
                        style: TextStyle(
                          fontWeight: FontWeight.bold,
                          fontSize: 12,
                          color: AppColors.azulMarinho,
                        ),
                      ),
                    ],
                  ),
                  const SizedBox(height: 8),
                  ...horariosAlterados.map((horario) => Padding(
                        padding: const EdgeInsets.only(bottom: 8),
                        child: Row(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            const Icon(Icons.info_outline, size: 16, color: AppColors.amareloDourado),
                            const SizedBox(width: 8),
                            Expanded(
                              child: Text(
                                '${horario.disciplina}: ${horario.detalhesAlteracao ?? "Alteração detectada"}',
                                style: textTheme.bodyMedium,
                              ),
                            ),
                          ],
                        ),
                      )),
                ],
              ),
            ),
          ],

          // Próximos Eventos
          Text(
            'Próximos Eventos',
            style: textTheme.titleLarge?.copyWith(
              color: AppColors.azulMarinho,
              fontSize: 12,
            ),
          ),
          const SizedBox(height: 12),
          ..._eventos.map((evento) => _buildScheduleCard(
                context,
                evento.horario,
                evento.titulo,
                evento.tipo,
                AppColors.azulTurquesa,
                evento.data,
              )),
          const SizedBox(height: 24),

          // Grade Semanal
          Text(
            'Grade Semanal',
            style: textTheme.titleLarge?.copyWith(
              color: AppColors.azulMarinho,
              fontSize: 12,
            ),
          ),
          const SizedBox(height: 12),
          ..._horariosPorDia.keys.map((dia) => _DiaCard(
                dia: dia,
                horarios: _horariosPorDia[dia]!,
              )),
        ],
      ),
    );
  }

  Widget _buildScheduleCard(
    BuildContext context,
    String time,
    String subject,
    String tipo,
    Color highlightColor,
    DateTime data,
  ) {
    return Card(
      margin: const EdgeInsets.only(bottom: 16),
      child: IntrinsicHeight(
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            Container(
              width: 8,
              decoration: BoxDecoration(
                color: highlightColor,
                borderRadius: const BorderRadius.only(
                  topLeft: Radius.circular(12),
                  bottomLeft: Radius.circular(12),
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    time,
                    style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                          color: AppColors.cinzaEscuro,
                          fontSize: 10,
                        ),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    subject,
                    style: Theme.of(context).textTheme.titleLarge?.copyWith(
                          fontSize: 12,
                        ),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    tipo,
                    style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                          fontSize: 8,
                          color: AppColors.azulTurquesa,
                        ),
                  ),
                  const SizedBox(height: 4),
                  Text(
                    '${data.day}/${data.month}/${data.year}',
                    style: Theme.of(context).textTheme.bodyMedium?.copyWith(
                          fontSize: 8,
                        ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _DiaCard extends StatelessWidget {
  final String dia;
  final List<Horario> horarios;

  const _DiaCard({required this.dia, required this.horarios});

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.only(bottom: 16),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              dia,
              style: const TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.bold,
                color: AppColors.azulTurquesa,
              ),
            ),
            const SizedBox(height: 12),
            ...horarios.map((horario) => _HorarioItem(horario: horario)),
          ],
        ),
      ),
    );
  }
}

class _HorarioItem extends StatelessWidget {
  final Horario horario;

  const _HorarioItem({required this.horario});

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.only(bottom: 12),
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: horario.alterado
            ? AppColors.amareloDourado.withOpacity(0.1)
            : AppColors.azulTurquesa.withOpacity(0.05),
        borderRadius: BorderRadius.circular(8),
        border: horario.alterado
            ? Border.all(color: AppColors.amareloDourado)
            : Border.all(color: AppColors.azulTurquesa.withOpacity(0.2)),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      horario.disciplina,
                      style: const TextStyle(
                        fontWeight: FontWeight.bold,
                        fontSize: 14,
                        color: AppColors.azulMarinho,
                      ),
                    ),
                    Text(
                      '${horario.sigla} - ${horario.professor}',
                      style: const TextStyle(
                        fontSize: 11,
                        color: AppColors.cinzaEscuro,
                      ),
                    ),
                  ],
                ),
              ),
              if (horario.alterado)
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                  decoration: BoxDecoration(
                    color: AppColors.amareloDourado,
                    borderRadius: BorderRadius.circular(4),
                  ),
                  child: const Text(
                    'ALTERADO',
                    style: TextStyle(
                      fontSize: 9,
                      color: AppColors.branco,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
            ],
          ),
          const SizedBox(height: 8),
          Row(
            children: [
              const Icon(Icons.access_time, size: 14, color: AppColors.cinzaEscuro),
              const SizedBox(width: 4),
              Text(
                '${horario.horarioInicio} - ${horario.horarioFim}',
                style: const TextStyle(fontSize: 11, color: AppColors.cinzaEscuro),
              ),
              const SizedBox(width: 16),
              const Icon(Icons.room, size: 14, color: AppColors.cinzaEscuro),
              const SizedBox(width: 4),
              Text(
                horario.sala,
                style: const TextStyle(fontSize: 11, color: AppColors.cinzaEscuro),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
