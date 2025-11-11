import 'package:flutter/material.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Configuração de Avaliações',
      theme: ThemeData(
        
        primaryColor: AppColors.azulMarinho,
        colorScheme: const ColorScheme.light(
          primary: AppColors.azulMarinho,
          secondary: AppColors.azulTurquesa,
        ),
        fontFamily: 'Roboto', 
      ),
      home: const ConfigAvaliacoesScreen(),
    );
  }
}

class AppColors {
  static const azulMarinho = Color(0xFF003399);
  static const azulTurquesa = Color(0xFF3ABDB2);
  static const cinzaEscuro = Color(0xFF808080);
  static const amareloDourado = Color(0xFFFEB100);
  static const branco = Color(0xFFFFFFFF);
}

class Usuario {
  final String id;
  final String nome;
  final String email;
  final String tipo;
  final String? matricula;

  Usuario({
    required this.id,
    required this.nome,
    required this.email,
    required this.tipo,
    this.matricula,
  });

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is Usuario && runtimeType == other.runtimeType && id == other.id;

  @override
  int get hashCode => id.hashCode;
}

class Questionario {
  final String id;
  final String nome;
  final String tipo;
  final String descricao;

  Questionario({
    required this.id,
    required this.nome,
    required this.tipo,
    required this.descricao,
  });

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is Questionario && runtimeType == other.runtimeType && id == other.id;

  @override
  int get hashCode => id.hashCode;
}

class ConfigAvaliacoesScreen extends StatefulWidget {
  const ConfigAvaliacoesScreen({super.key});

  @override
  State<ConfigAvaliacoesScreen> createState() => _ConfigAvaliacoesScreenState();
}

class _ConfigAvaliacoesScreenState extends State<ConfigAvaliacoesScreen> {
  List<Usuario> _usuarios = [];
  List<Usuario> _usuariosSelecionados = [];
  List<Questionario> _questionariosSelecionados = [];
  
  
  final List<Questionario> _questionariosDisponiveis = [
    Questionario(
      id: '1', 
      nome: 'Mini Clinical Evaluation Exercise (Mini-CEX)', 
      tipo: 'MINI_CEX',
      descricao: 'Avaliação clínica estruturada para competências médicas básicas'
    ),
    Questionario(
      id: '2', 
      nome: 'Avaliação 360° - Professor/Preceptor', 
      tipo: 'AVALIACAO_360_PROFESSOR',
      descricao: 'Avaliação formativa realizada por professores e preceptores'
    ),
  ];

  @override
  void initState() {
    super.initState();
    _carregarUsuarios();
  }

  void _carregarUsuarios() {
    
    setState(() {
      _usuarios = [
        Usuario(id: '1', nome: 'Dr. Carlos Eduardo Silva', email: 'admin@unifae.br', tipo: 'ADMINISTRADOR', matricula: 'ADM001'),
        Usuario(id: '2', nome: 'Dra. Maria Fernanda Costa', email: 'admin2@unifae.br', tipo: 'ADMINISTRADOR', matricula: 'ADM002'),
        Usuario(id: '3', nome: 'Dr. Roberto Mendes', email: 'coordenador@unifae.br', tipo: 'COORDENADOR', matricula: 'COORD001'),
        Usuario(id: '5', nome: 'Dr. João Carlos Oliveira', email: 'joao.oliveira@unifae.br', tipo: 'PROFESSOR', matricula: 'PROF001'),
        Usuario(id: '11', nome: 'Lucas Gabriel Santos', email: 'lucas.santos@aluno.unifae.br', tipo: 'ESTUDANTE', matricula: 'RA001001'),
        Usuario(id: '12', nome: 'Ana Carolina Silva', email: 'ana.silva@aluno.unifae.br', tipo: 'ESTUDANTE', matricula: 'RA001002'),
        Usuario(id: '13', nome: 'Felipe Augusto Costa', email: 'felipe.costa@aluno.unifae.br', tipo: 'ESTUDANTE', matricula: 'RA001003'),
      ];
    });
  }

  void _alternarSelecaoUsuario(Usuario usuario) {
    setState(() {
      if (_usuariosSelecionados.contains(usuario)) {
        _usuariosSelecionados.remove(usuario);
      } else {
        _usuariosSelecionados.add(usuario);
      }
    });
  }

  void _alternarSelecaoQuestionario(Questionario questionario) {
    setState(() {
      if (_questionariosSelecionados.contains(questionario)) {
        _questionariosSelecionados.remove(questionario);
      } else {
        _questionariosSelecionados.add(questionario);
      }
    });
  }

  void _confirmarConfiguracao() {
    if (_questionariosSelecionados.isEmpty) {
      _mostrarDialogoErro('Selecione ao menos um questionário.');
      return;
    }

    if (_usuariosSelecionados.isEmpty) {
      _mostrarDialogoErro('Selecione ao menos um usuário.');
      return;
    }

    
    _mostrarDialogoSucesso();
  }

  

  void _mostrarDialogoErro(String mensagem) {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text(
            'Atenção',
            style: TextStyle(
              color: AppColors.azulMarinho,
            ),
          ),
          content: Text(mensagem),
          actions: [
            TextButton(
              onPressed: () => Navigator.of(context).pop(),
              child: const Text(
                'OK',
                style: TextStyle(color: AppColors.azulMarinho),
              ),
            ),
          ],
        );
      },
    );
  }

  void _mostrarDialogoSucesso() {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: const Text(
            'Configuração Salva',
            style: TextStyle(
              color: AppColors.azulMarinho,
            ),
          ),
          content: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Text('Os questionários foram atribuídos com sucesso:'),
              const SizedBox(height: 12),
              const Text(
                'Questionários:',
                style: TextStyle(fontWeight: FontWeight.bold),
              ),
              ..._questionariosSelecionados.map((q) => Text('• ${q.nome}')).toList(),
              const SizedBox(height: 8),
              Text(
                'Para ${_usuariosSelecionados.length} usuário(s) selecionado(s)',
                style: const TextStyle(fontWeight: FontWeight.bold),
              ),
            ],
          ),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.of(context).pop();
                setState(() {
                  _usuariosSelecionados.clear();
                  _questionariosSelecionados.clear();
                });
              },
              child: const Text(
                'OK',
                style: TextStyle(color: AppColors.azulMarinho),
              ),
            ),
          ],
        );
      },
    );
  }
  
  Color _obterCorQuestionario(String tipo) {
    switch (tipo) {
      case 'MINI_CEX':
        return AppColors.azulMarinho;
      case 'AVALIACAO_360_PROFESSOR':
        return AppColors.azulTurquesa;
      default:
        return AppColors.cinzaEscuro;
    }
  }

  String _obterRotuloTipoQuestionario(String tipo) {
    switch (tipo) {
      case 'MINI_CEX':
        return 'Mini-CEX';
      case 'AVALIACAO_360_PROFESSOR':
        return '360° Prof';
      default:
        return tipo;
    }
  }

  Color _obterCorUsuario(String tipo) {
    switch (tipo) {
      case 'ADMINISTRADOR':
        return const Color(0xFFFF3B30); 
      case 'COORDENADOR':
        return const Color(0xFFFF9500); 
      case 'PROFESSOR':
        return AppColors.azulMarinho;
      case 'ESTUDANTE':
        return AppColors.azulTurquesa;
      default:
        return AppColors.cinzaEscuro;
    }
  }

  String _obterRotuloTipoUsuario(String tipo) {
    switch (tipo) {
      case 'ADMINISTRADOR':
        return 'Admin';
      case 'COORDENADOR':
        return 'Coord';
      case 'PROFESSOR':
        return 'Prof';
      case 'ESTUDANTE':
        return 'Aluno';
      default:
        return tipo;
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: AppColors.azulMarinho,
      appBar: AppBar(
        title: const Text(
          'Configurar Questionários',
          style: TextStyle(
            color: AppColors.branco,
            fontWeight: FontWeight.bold,
          ),
        ),
        backgroundColor: AppColors.azulMarinho,
        elevation: 0,
        leading: IconButton(
          icon: const Icon(Icons.arrow_back, color: AppColors.branco),
          onPressed: () => Navigator.of(context).pop(),
        ),
      ),
      body: Column(
        children: [
          Container(
            width: double.infinity,
            padding: const EdgeInsets.all(16),
            decoration: const BoxDecoration(
              color: AppColors.azulMarinho,
            ),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                const Text(
                  'Atribuir Questionários aos Usuários',
                  style: TextStyle(
                    color: AppColors.branco,
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                const SizedBox(height: 4),
                Text(
                  'Selecione os questionários e os usuários que receberão as avaliações',
                  style: TextStyle(
                    color: AppColors.branco.withOpacity(0.8),
                    fontSize: 12,
                  ),
                ),
              ],
            ),
          ),

          Expanded(
            child: Container(
              decoration: const BoxDecoration(
                color: AppColors.branco,
                borderRadius: BorderRadius.only(
                  topLeft: Radius.circular(20),
                  topRight: Radius.circular(20),
                ),
              ),
              child: SingleChildScrollView(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    _construirSecaoQuestionarios(),
                    const SizedBox(height: 24),

                    _construirSecaoUsuarios(),
                    const SizedBox(height: 32),

                    _construirBotaoConfirmacao(),
                  ],
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }

  Widget _construirSecaoQuestionarios() {
    return Card(
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: const [
                Icon(Icons.assignment, color: AppColors.azulMarinho, size: 20),
                SizedBox(width: 8),
                Text(
                  'Questionários Disponíveis',
                  style: TextStyle(
                    color: AppColors.azulMarinho,
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 12),
            const Text(
              'Selecione os questionários que deseja atribuir:',
              style: TextStyle(
                fontSize: 14,
                color: AppColors.cinzaEscuro,
              ),
            ),
            const SizedBox(height: 16),
            Column(
              children: _questionariosDisponiveis.map((questionario) {
                return _construirItemQuestionario(questionario);
              }).toList(),
            ),
          ],
        ),
      ),
    );
  }

  Widget _construirItemQuestionario(Questionario questionario) {
    bool selecionado = _questionariosSelecionados.contains(questionario);
    return Container(
      margin: const EdgeInsets.only(bottom: 8),
      child: Material(
        color: Colors.transparent,
        child: InkWell(
          onTap: () => _alternarSelecaoQuestionario(questionario),
          borderRadius: BorderRadius.circular(8),
          child: Container(
            padding: const EdgeInsets.all(12),
            decoration: BoxDecoration(
              color: selecionado ? AppColors.azulTurquesa.withOpacity(0.1) : AppColors.branco,
              borderRadius: BorderRadius.circular(8),
              border: Border.all(
                color: selecionado ? AppColors.azulTurquesa : AppColors.cinzaEscuro.withOpacity(0.2),
              ),
            ),
            child: Row(
              children: [
                Container(
                  width: 20,
                  height: 20,
                  decoration: BoxDecoration(
                    color: selecionado ? AppColors.azulTurquesa : AppColors.branco,
                    borderRadius: BorderRadius.circular(4),
                    border: Border.all(
                      color: selecionado ? AppColors.azulTurquesa : AppColors.cinzaEscuro,
                    ),
                  ),
                  child: selecionado
                      ? const Icon(Icons.check, size: 14, color: AppColors.branco)
                      : null,
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        questionario.nome,
                        style: const TextStyle(
                          fontSize: 14,
                          fontWeight: FontWeight.bold,
                          color: AppColors.azulMarinho,
                        ),
                      ),
                      const SizedBox(height: 4),
                      Text(
                        questionario.descricao,
                        style: const TextStyle(
                          fontSize: 12,
                          color: AppColors.cinzaEscuro,
                        ),
                        maxLines: 2,
                        overflow: TextOverflow.ellipsis,
                      ),
                    ],
                  ),
                ),
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                  decoration: BoxDecoration(
                    color: _obterCorQuestionario(questionario.tipo),
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: Text(
                    _obterRotuloTipoQuestionario(questionario.tipo),
                    style: const TextStyle(
                      fontSize: 10,
                      color: AppColors.branco,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _construirSecaoUsuarios() {
    return Card(
      elevation: 2,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(12)),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                const Icon(Icons.people, color: AppColors.azulMarinho, size: 20),
                const SizedBox(width: 8),
                const Text(
                  'Lista de Usuários',
                  style: TextStyle(
                    color: AppColors.azulMarinho,
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                  ),
                ),
                const SizedBox(width: 8),
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 2),
                  decoration: BoxDecoration(
                    color: AppColors.amareloDourado,
                    borderRadius: BorderRadius.circular(10),
                  ),
                  child: Text(
                    '${_usuariosSelecionados.length} selecionados',
                    style: const TextStyle(
                      fontSize: 10,
                      color: AppColors.branco,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
              ],
            ),
            const SizedBox(height: 12),
            const Text(
              'Selecione os usuários que receberão os questionários:',
              style: TextStyle(
                fontSize: 14,
                color: AppColors.cinzaEscuro,
              ),
            ),
            const SizedBox(height: 16),
            Container(
              height: 300,
              child: ListView.builder(
                itemCount: _usuarios.length,
                itemBuilder: (context, index) {
                  return _construirItemUsuario(_usuarios[index]);
                },
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _construirItemUsuario(Usuario usuario) {
    bool selecionado = _usuariosSelecionados.contains(usuario);
    Color userColor = _obterCorUsuario(usuario.tipo);

    return Container(
      margin: const EdgeInsets.only(bottom: 8),
      child: Material(
        color: Colors.transparent,
        child: InkWell(
          onTap: () => _alternarSelecaoUsuario(usuario),
          borderRadius: BorderRadius.circular(8),
          child: Container(
            padding: const EdgeInsets.all(12),
            decoration: BoxDecoration(
              color: selecionado ? AppColors.azulTurquesa.withOpacity(0.1) : AppColors.branco,
              borderRadius: BorderRadius.circular(8),
              border: Border.all(
                color: selecionado ? AppColors.azulTurquesa : AppColors.cinzaEscuro.withOpacity(0.2),
              ),
            ),
            child: Row(
              children: [
                Container(
                  width: 36,
                  height: 36,
                  decoration: BoxDecoration(
                    shape: BoxShape.circle,
                    color: userColor,
                  ),
                  child: Center(
                    child: Text(
                      usuario.nome.substring(0, 1),
                      style: const TextStyle(
                        color: AppColors.branco,
                        fontWeight: FontWeight.bold,
                        fontSize: 14,
                      ),
                    ),
                  ),
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        usuario.nome,
                        style: const TextStyle(
                          fontSize: 14,
                          fontWeight: FontWeight.bold,
                          color: AppColors.azulMarinho,
                        ),
                      ),
                      const SizedBox(height: 2),
                      Text(
                        usuario.email,
                        style: const TextStyle(
                          fontSize: 12,
                          color: AppColors.cinzaEscuro,
                        ),
                      ),
                      if (usuario.matricula != null) ...[
                        const SizedBox(height: 2),
                        Text(
                          'Matrícula: ${usuario.matricula}',
                          style: const TextStyle(
                            fontSize: 12,
                            color: AppColors.cinzaEscuro,
                          ),
                        ),
                      ],
                    ],
                  ),
                ),
                Container(
                  padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
                  decoration: BoxDecoration(
                    color: userColor,
                    borderRadius: BorderRadius.circular(12),
                  ),
                  child: Text(
                    _obterRotuloTipoUsuario(usuario.tipo),
                    style: const TextStyle(
                      fontSize: 10,
                      color: AppColors.branco,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
                const SizedBox(width: 8),
                Container(
                  width: 20,
                  height: 20,
                  decoration: BoxDecoration(
                    color: selecionado ? AppColors.azulTurquesa : AppColors.branco,
                    borderRadius: BorderRadius.circular(10),
                    border: Border.all(
                      color: selecionado ? AppColors.azulTurquesa : AppColors.cinzaEscuro,
                    ),
                  ),
                  child: selecionado
                      ? const Icon(Icons.check, size: 14, color: AppColors.branco)
                      : null,
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }

  Widget _construirBotaoConfirmacao() {
    return Center(
      child: SizedBox(
        width: double.infinity,
        child: ElevatedButton(
          onPressed: _confirmarConfiguracao,
          style: ElevatedButton.styleFrom(
            backgroundColor: AppColors.azulTurquesa,
            padding: const EdgeInsets.symmetric(vertical: 16),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(12),
            ),
            elevation: 2,
          ),
          child: const Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              Icon(Icons.send, color: AppColors.branco, size: 18),
              SizedBox(width: 8),
              Text(
                'Confirmar Configuração',
                style: TextStyle(
                  color: AppColors.branco,
                  fontWeight: FontWeight.bold,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}