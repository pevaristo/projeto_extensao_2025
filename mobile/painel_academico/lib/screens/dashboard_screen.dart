import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import '../theme/app_colors.dart';
import '../models/nota.dart';
import '../models/horario.dart';
import 'notas_screen.dart';
import 'horarios_screen.dart';
import 'historico_screen.dart';

class DashboardScreen extends StatelessWidget {
  const DashboardScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final notas = Nota.getNotasExemplo();
    final eventos = Horario.getEventosExemplo();
    final mediaFinal = Nota(
      id: '',
      disciplina: '',
      tipoAvaliacao: '',
      nota: 0,
      peso: 0,
      data: DateTime.now(),
      semestre: '',
      ano: 2024,
    ).calcularMediaFinal(notas);

    return Scaffold(
      backgroundColor: AppColors.backgroundEscuro,
      drawer: const AppDrawer(),
      appBar: AppBar(
        title: const Text('Dashboard'),
        leading: Builder(
          builder: (context) => IconButton(
            icon: const Icon(Icons.menu),
            onPressed: () => Scaffold.of(context).openDrawer(),
          ),
        ),
        flexibleSpace: Container(
          decoration: const BoxDecoration(
            color: AppColors.azulTurquesa,
          ),
          child: const Center(
            child: Text(
              'UNIVIRTUAL - MEDICINA',
              style: TextStyle(
                color: AppColors.branco,
                fontSize: 16,
                fontWeight: FontWeight.bold,
              ),
            ),
          ),
        ),
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Botões de acesso rápido
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceEvenly,
              children: [
                _QuickAccessButton(
                  icon: Icons.refresh,
                  label: 'Notas',
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => const NotasScreen()),
                    );
                  },
                ),
                _QuickAccessButton(
                  icon: Icons.book,
                  label: 'Matérias',
                  onTap: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => const HorariosScreen()),
                    );
                  },
                ),
                _QuickAccessButton(
                  icon: Icons.credit_card,
                  label: 'Financeiro',
                  onTap: () {
                    // Navegar para tela financeira quando criada
                  },
                ),
              ],
            ),
            const SizedBox(height: 24),
            
            // Próximos Eventos
            const Text(
              'Próximos Eventos',
              style: TextStyle(
                color: AppColors.branco,
                fontSize: 18,
                fontWeight: FontWeight.bold,
              ),
            ),
            const SizedBox(height: 12),
            ...eventos.take(3).map((evento) => _EventoCard(evento: evento)),
            const SizedBox(height: 24),
            
            // Card de Desempenho
            Card(
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              mediaFinal.toStringAsFixed(1),
                              style: const TextStyle(
                                fontSize: 48,
                                fontWeight: FontWeight.bold,
                                color: AppColors.preto,
                              ),
                            ),
                            const Text(
                              'Média Final',
                              style: TextStyle(
                                fontSize: 12,
                                color: AppColors.cinzaEscuro,
                              ),
                            ),
                          ],
                        ),
                        Expanded(
                          child: Column(
                            children: notas.take(1).map((nota) => _NotaItem(nota: nota)).toList(),
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 16),
                    SizedBox(
                      width: double.infinity,
                      child: ElevatedButton(
                        onPressed: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(builder: (context) => const HistoricoScreen()),
                          );
                        },
                        child: const Text('Ver Gráfico de Desempenho'),
                      ),
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _QuickAccessButton extends StatelessWidget {
  final IconData icon;
  final String label;
  final VoidCallback onTap;

  const _QuickAccessButton({
    required this.icon,
    required this.label,
    required this.onTap,
  });

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: onTap,
      child: Column(
        children: [
          Container(
            width: 60,
            height: 60,
            decoration: const BoxDecoration(
              color: AppColors.azulTurquesa,
              shape: BoxShape.circle,
            ),
            child: Icon(icon, color: AppColors.branco, size: 28),
          ),
          const SizedBox(height: 8),
          Text(
            label,
            style: const TextStyle(
              color: AppColors.branco,
              fontSize: 12,
            ),
          ),
        ],
      ),
    );
  }
}

class _EventoCard extends StatelessWidget {
  final Evento evento;

  const _EventoCard({required this.evento});

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: const EdgeInsets.only(bottom: 8),
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: AppColors.backgroundCard,
        borderRadius: BorderRadius.circular(8),
      ),
      child: Row(
        children: [
          const Icon(Icons.event, color: AppColors.azulTurquesa),
          const SizedBox(width: 12),
          Expanded(
            child: Text(
              '${evento.titulo} - ${DateFormat('dd/MM/yyyy').format(evento.data)} - ${evento.horario}',
              style: const TextStyle(
                fontSize: 12,
                color: AppColors.preto,
              ),
            ),
          ),
        ],
      ),
    );
  }
}

class _NotaItem extends StatelessWidget {
  final Nota nota;

  const _NotaItem({required this.nota});

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(8),
      decoration: BoxDecoration(
        border: Border.all(color: AppColors.cinzaEscuro.withOpacity(0.3)),
        borderRadius: BorderRadius.circular(4),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text('Prova: ${nota.disciplina}', style: const TextStyle(fontSize: 10)),
          const SizedBox(height: 4),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text('Peso: ${nota.peso}', style: const TextStyle(fontSize: 9)),
              Text('Nota: ${nota.nota}', style: const TextStyle(fontSize: 9, fontWeight: FontWeight.bold)),
            ],
          ),
        ],
      ),
    );
  }
}

class AppDrawer extends StatelessWidget {
  const AppDrawer({super.key});

  @override
  Widget build(BuildContext context) {
    return Drawer(
      child: ListView(
        padding: EdgeInsets.zero,
        children: [
          const DrawerHeader(
            decoration: BoxDecoration(color: AppColors.azulTurquesa),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              mainAxisAlignment: MainAxisAlignment.end,
              children: [
                Text(
                  'Painel Acadêmico',
                  style: TextStyle(
                    color: AppColors.branco,
                    fontSize: 24,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ],
            ),
          ),
          ListTile(
            leading: const Icon(Icons.dashboard),
            title: const Text('Dashboard'),
            onTap: () {
              Navigator.pop(context);
              Navigator.pushReplacement(
                context,
                MaterialPageRoute(builder: (context) => const DashboardScreen()),
              );
            },
          ),
          ListTile(
            leading: const Icon(Icons.assessment),
            title: const Text('Notas'),
            onTap: () {
              Navigator.pop(context);
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => const NotasScreen()),
              );
            },
          ),
          ListTile(
            leading: const Icon(Icons.schedule),
            title: const Text('Horários'),
            onTap: () {
              Navigator.pop(context);
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => const HorariosScreen()),
              );
            },
          ),
          ListTile(
            leading: const Icon(Icons.history),
            title: const Text('Histórico Escolar'),
            onTap: () {
              Navigator.pop(context);
              Navigator.push(
                context,
                MaterialPageRoute(builder: (context) => const HistoricoScreen()),
              );
            },
          ),
        ],
      ),
    );
  }
}
