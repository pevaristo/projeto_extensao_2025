import 'package:flutter/material.dart';
import 'app_colors.dart';

class AppTheme {
  static ThemeData get theme {
    return ThemeData(
      brightness: Brightness.light,
      // Fundo principal branco para look clean
      scaffoldBackgroundColor: AppColors.branco,
      primaryColor: AppColors.azulTurquesa,

      colorScheme: ColorScheme.light(
        primary: AppColors.azulTurquesa,
        secondary: AppColors.amareloDourado,
        surface: AppColors.backgroundCard,
        onPrimary: AppColors.branco,
        onSecondary: AppColors.branco,
        onSurface: AppColors.azulMarinho,
      ),

      // Configuração da AppBar
      appBarTheme: AppBarTheme(
        backgroundColor: AppColors.branco,
        elevation: 0,
        centerTitle: true,
        titleTextStyle: const TextStyle(
          fontSize: 12,
          fontWeight: FontWeight.bold,
          color: AppColors.azulMarinho,
        ),
        iconTheme: const IconThemeData(
          color: AppColors.azulMarinho,
        ),
      ),

      // Configuração dos Cards
      cardTheme: CardThemeData(
        color: AppColors.branco,
        elevation: 3,
        shadowColor: Colors.black.withOpacity(0.1),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(12),
        ),
      ),

      // Configuração da TabBar
      tabBarTheme: const TabBarThemeData(
        indicatorColor: AppColors.azulTurquesa,
        labelColor: AppColors.azulMarinho,
        unselectedLabelColor: AppColors.cinzaEscuro,
        labelStyle: TextStyle(
          fontSize: 12,
          fontWeight: FontWeight.bold,
        ),
      ),

      // Botões
      elevatedButtonTheme: ElevatedButtonThemeData(
        style: ElevatedButton.styleFrom(
          backgroundColor: AppColors.azulTurquesa,
          foregroundColor: AppColors.branco,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(8),
          ),
          padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 12),
        ),
      ),

      // Campos de entrada
      inputDecorationTheme: InputDecorationTheme(
        filled: true,
        fillColor: AppColors.branco,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
          borderSide: BorderSide(color: AppColors.cinzaEscuro.withOpacity(0.3)),
        ),
        contentPadding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
      ),

      // Configuração dos Textos
      textTheme: const TextTheme(
        displayLarge: TextStyle(
          fontSize: 32,
          fontWeight: FontWeight.bold,
          color: AppColors.azulMarinho,
        ),
        displayMedium: TextStyle(
          fontSize: 24,
          fontWeight: FontWeight.bold,
          color: AppColors.azulMarinho,
        ),
        displaySmall: TextStyle(
          fontSize: 20,
          fontWeight: FontWeight.bold,
          color: AppColors.azulMarinho,
        ),
        headlineMedium: TextStyle(
          fontSize: 18,
          fontWeight: FontWeight.bold,
          color: AppColors.azulMarinho,
        ),
        titleLarge: TextStyle(
          fontSize: 16,
          fontWeight: FontWeight.bold,
          color: AppColors.azulMarinho,
        ),
        titleMedium: TextStyle(
          fontSize: 14,
          fontWeight: FontWeight.w500,
          color: AppColors.azulMarinho,
        ),
        bodyLarge: TextStyle(
          fontSize: 12,
          fontWeight: FontWeight.normal,
          color: AppColors.cinzaEscuro,
        ),
        bodyMedium: TextStyle(
          fontSize: 10,
          fontWeight: FontWeight.normal,
          color: AppColors.cinzaEscuro,
        ),
        bodySmall: TextStyle(
          fontSize: 8,
          fontWeight: FontWeight.normal,
          color: AppColors.cinzaEscuro,
        ),
      ),

      // Ícones
      iconTheme: const IconThemeData(
        color: AppColors.azulTurquesa,
      ),
    );
  }
}
