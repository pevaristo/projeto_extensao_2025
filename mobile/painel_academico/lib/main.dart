import 'package:flutter/material.dart';
import 'theme/app_theme.dart';
import 'screens/academic_panel_screen.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Painel Acadêmico',
      theme: AppTheme.theme,
      home: const AcademicPanelScreen(),
      debugShowCheckedModeBanner: false,
    );
  }
}
