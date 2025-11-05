# 📱 Sistema de Avaliação UNIFAE – Distribuição de Tarefas do APP

---

## 🚀 **INFORMAÇÕES GERAIS**

### **Descrição:**
Projeto acadêmico destinado ao desenvolvimento das **telas do aplicativo de avaliação** da UNIFAE, dividido por módulos funcionais e grupos de desenvolvimento.

### **Objetivo:**
Organizar a distribuição de tarefas entre os grupos, definir módulos e manter a padronização visual conforme o guia de design.

---

## 👥 **DISTRIBUIÇÃO DE TAREFAS POR GRUPO**

| Grupo | Módulos Responsáveis |
|:--:|:--|
| **1** | Módulo 1: Autenticação e Acesso <br> Módulo 6: Configuração de Avaliações |
| **2** | Módulo 2: Painel Acadêmico (Visualização) |
| **3** | Módulo 3: Interação e Feedback <br> Módulo 8: Relatórios Acadêmicos |
| **4** | Módulo 4: Gestão Acadêmica |
| **5** | Módulo 5: Agenda de Estágios <br> Suporte Visual e Navegação Global |

---

## ⚙️ **MÓDULOS DE FUNCIONALIDADE**

### **Módulo 1 – Autenticação e Acesso**
- Telas de **Login**, **Esqueci a Senha** e **Cadastro**
- Validações e mensagens de erro personalizadas

### **Módulo 2 – Painel Acadêmico**
- Exibição de **notas**, **horários** e **histórico escolar**
- Filtros e exportação simulada

### **Módulo 3 – Interação e Feedback**
- Tela de envio de feedback com formulário validado  
- Campo de texto entre **10 e 300 caracteres**

### **Módulo 4 – Gestão Acadêmica**
- Telas para **lançamento de notas**, **criação** e **edição de turmas e disciplinas (CRUD completo)**

### **Módulo 5 – Agenda de Estágios**
- Tela de **calendário/lista** com formulário de cadastro, seleção de local e participantes

### **Módulo 6 – Configuração de Avaliações**
- Tela para atribuir questionários **Mini-CEX** e **360**
- Seleção de usuários e validações automáticas

### **Módulo 7 – Preenchimento de Avaliações**
- Formulários com **escala de 1 a 9** e campos de feedback

### **Módulo 8 – Relatórios Acadêmicos**
- Geração e visualização de relatórios com **tabelas**, **gráficos** e **calendário de atividades**

---

## 🧠 **HISTÓRIAS DE USUÁRIO E REQUISITOS FUNCIONAIS**

| Módulo | História(s) | Requisitos (RF) | Caso(s) de Uso (UC) | Descrição |
|:--|:--|:--|:--|:--|
| 1 | US01 | RF#10 | UC#1 | Login, cadastro e recuperação de senha |
| 2 | US07, US08, US09 | RF#07–09 | UC#7–9 | Visualização de notas, horários e histórico |
| 3 | US05 | RF#05 | UC#5 | Envio de feedback |
| 4 | US03, US12 | RF#03–04 | UC#3, UC#12 | Gestão de turmas e notas |
| 5 | US04 | RF#04 | UC#4 | Criação e gerenciamento de estágios |
| 6 | US02 | RF#02 | UC#2 | Atribuição dos questionários Mini-CEX e 360 |
| 7 | US10, US11 | RF#01, RF#10 | UC#10 | Preenchimento dos formulários de avaliação |
| 8 | US06 | RF#06 | UC#6 | Geração e visualização de relatórios |

---

## 🎨 **GUIA DE DESIGN**

### **Paleta de Cores**
```text
Azul Marinho   #003399  → Fundo e botões principais
Azul Turquesa  #3ABDB2  → Destaques e ícones
Cinza Escuro   #808080  → Texto secundário
Amarelo Ouro   #FEB100  → Destaques e gráficos
Branco         #FFFFFF  → Textos sobre fundo azul
