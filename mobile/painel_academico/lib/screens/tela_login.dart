import 'package:flutter/material.dart';
import '../theme/app_colors.dart';
import 'tela_cadastro.dart';
import 'tela_esqueci_senha.dart';

class TelaLogin extends StatefulWidget {
  const TelaLogin({super.key});

  @override
  State<TelaLogin> createState() => _TelaLoginState();
}

class _TelaLoginState extends State<TelaLogin> {
  final _emailController = TextEditingController();
  final _senhaController = TextEditingController();
  bool _obscurePassword = true;

  void _fazerLogin() {
    final email = _emailController.text.trim();
    final senha = _senhaController.text.trim();

    if (email.isEmpty || senha.isEmpty) {
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Preencha todos os campos.')),
      );
      return;
    }

    // Simulação de login
    ScaffoldMessenger.of(
      context,
    ).showSnackBar(SnackBar(content: Text('Bem-vindo, $email!')));
  }

  void _abrirTelaCadastro() {
    Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => const TelaCadastro()),
    );
  }

  void _abrirTelaEsqueciSenha() {
    Navigator.push(
      context,
      MaterialPageRoute(builder: (_) => const TelaEsqueciSenha()),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.branco,
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(24),
          child: SingleChildScrollView(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                const SizedBox(height: 32),
                TextField(
                  controller: _emailController,
                  decoration: const InputDecoration(
                    labelText: 'E-mail',
                    prefixIcon: Icon(Icons.email),
                    border: OutlineInputBorder(),
                  ),
                ),
                const SizedBox(height: 16),
                TextField(
                  controller: _senhaController,
                  obscureText: _obscurePassword,
                  decoration: InputDecoration(
                    labelText: 'Senha',
                    prefixIcon: const Icon(Icons.lock),
                    border: const OutlineInputBorder(),
                    suffixIcon: IconButton(
                      icon: Icon(
                        _obscurePassword
                            ? Icons.visibility
                            : Icons.visibility_off,
                      ),
                      onPressed: () {
                        setState(() {
                          _obscurePassword = !_obscurePassword;
                        });
                      },
                    ),
                  ),
                ),
                const SizedBox(height: 24),
                ElevatedButton(
                  onPressed: _fazerLogin,
                  style: ElevatedButton.styleFrom(
                    backgroundColor: AppColors.azulMarinho,
                    minimumSize: const Size(double.infinity, 48),
                  ),
                  child: const Text(
                    'Entrar',
                    style: TextStyle(color: Colors.white, fontSize: 18),
                  ),
                ),
                const SizedBox(height: 12),
                TextButton(
                  onPressed: _abrirTelaEsqueciSenha,
                  child: const Text(
                    'Esqueci minha senha',
                    style: TextStyle(color: AppColors.azulMarinho),
                  ),
                ),
                const SizedBox(height: 12),
                TextButton(
                  onPressed: _abrirTelaCadastro,
                  child: const Text(
                    'Cadastre-se',
                    style: TextStyle(color: AppColors.azulMarinho),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}
