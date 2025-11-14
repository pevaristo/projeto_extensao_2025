import 'package:flutter/material.dart';
import '../theme/app_colors.dart';

class TelaEsqueciSenha extends StatefulWidget {
  const TelaEsqueciSenha({super.key});

  @override
  State<TelaEsqueciSenha> createState() => _TelaEsqueciSenhaState();
}

class _TelaEsqueciSenhaState extends State<TelaEsqueciSenha> {
  final _emailController = TextEditingController();

  void _enviarLink() {
    final email = _emailController.text.trim();

    if (email.isEmpty) {
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text('Informe seu e-mail.')));
      return;
    }

    // Simulação de envio de link
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text('Link de redefinição enviado para $email.')),
    );

    Navigator.pop(context); // Volta para o login
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.branco,
      appBar: AppBar(
        title: const Text('Esqueci minha senha'),
        backgroundColor: AppColors.azulMarinho,
        foregroundColor: Colors.white,
      ),
      body: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          children: [
            const Text(
              'Digite o e-mail cadastrado para receber o link de redefinição.',
              style: TextStyle(fontSize: 16),
              textAlign: TextAlign.center,
            ),
            const SizedBox(height: 24),
            TextField(
              controller: _emailController,
              decoration: const InputDecoration(
                labelText: 'E-mail',
                prefixIcon: Icon(Icons.email),
                border: OutlineInputBorder(),
              ),
            ),
            const SizedBox(height: 24),
            ElevatedButton(
              onPressed: _enviarLink,
              style: ElevatedButton.styleFrom(
                backgroundColor: AppColors.azulTurquesa,
                minimumSize: const Size(double.infinity, 48),
              ),
              child: const Text(
                'Enviar link',
                style: TextStyle(color: Colors.white, fontSize: 18),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
