import 'package:flutter/material.dart';
import '../theme/app_colors.dart';
import 'notas_screen.dart';
import 'horarios_screen.dart';
import 'historico_screen.dart';

class AcademicPanelScreen extends StatelessWidget {
  const AcademicPanelScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: 3, // Notas, Horários, Histórico
      child: Scaffold(
        appBar: AppBar(
          title: const Text('PAINEL ACADÊMICO'),
          actions: [
            IconButton(
              icon: const Icon(Icons.menu),
              onPressed: () {
                // Abrir drawer ou menu lateral se necessário
              },
            ),
          ],
          bottom: const TabBar(
            tabs: [
              Tab(text: 'NOTAS'),
              Tab(text: 'HORÁRIOS'),
              Tab(text: 'HISTÓRICO'),
            ],
          ),
        ),
        body: const TabBarView(
          children: [
            NotasScreen(), // Tela completa de Notas
            HorariosScreen(), // Tela completa de Horários
            HistoricoScreen(), // Tela completa de Histórico
          ],
        ),
      ),
    );
  }
}
