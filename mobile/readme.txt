# 📱 Distribuição de Tarefas para Criação de Tela dos APP

> **Projeto acadêmico** — Desenvolvimento de telas do aplicativo com base nos módulos funcionais e guia de design padronizado.

---

## 🧩 1. Atribuição de Tarefas por Grupo

| Grupo | Módulos Responsáveis | Descrição |
|:------:|----------------------|------------|
| **1** | Módulo 1: Autenticação e Acesso <br> Módulo 6: Configuração de Avaliações | Login, Cadastro, Recuperação de Senha e configuração dos questionários Mini-CEX e 360 |
| **2** | Módulo 2: Painel Acadêmico (Visualização) | Visualização de Notas, Horários e Histórico Escolar |
| **3** | Módulo 3: Interação e Feedback <br> Módulo 8: Relatórios Acadêmicos | Envio de feedback e geração de relatórios com filtros e gráficos |
| **4** | Módulo 4: Gestão Acadêmica | CRUD completo de turmas, disciplinas e notas |
| **5** | Módulo 5: Agenda de Estágios <br> Suporte Visual e Navegação Global | Tela de calendário e navegação geral entre módulos |

---

## ⚙️ 2. Descrição dos Módulos

| Módulo | Nome | Descrição |
|:--:|:--|:--|
| **1** | Autenticação e Acesso | Telas de Login, Esqueci a Senha e Cadastro com validações e mensagens de erro |
| **2** | Painel Acadêmico | Visualização de notas, horários e histórico escolar, com filtros e exportação simulada |
| **3** | Interação e Feedback | Envio de feedback com validação (10–300 caracteres) |
| **4** | Gestão Acadêmica | Lançamento de notas, criação e edição de turmas e disciplinas (CRUD completo) |
| **5** | Agenda de Estágios | Tela de calendário/lista para estágios, com formulário de cadastro e seleção de local e participantes |
| **6** | Configuração de Avaliações | Atribuição de questionários Mini-CEX e 360, com seleção de usuários |
| **7** | Preenchimento de Avaliações | Formulários com escalas de 1 a 9 e campos de feedback |
| **8** | Relatórios Acadêmicos | Geração e visualização de relatórios, com filtros, tabelas, gráficos e calendário de atividades |

---

## 🧠 3. Histórias de Usuário e Requisitos Funcionais

| Módulo | História(s) de Usuário | Requisitos Funcionais | Casos de Uso | Descrição |
|:------|:------------------------|:----------------------|:--------------|:-----------|
| **1** | US01 | RF#10 | UC#1 | Login, cadastro e recuperação de senha |
| **2** | US07, US08, US09 | RF#07–09 | UC#7–9 | Visualização de notas, horários e histórico |
| **3** | US05 | RF#05 | UC#5 | Envio de feedback |
| **4** | US03, US12 | RF#03–04 | UC#3, UC#12 | Gestão de notas, turmas e disciplinas |
| **5** | US04 | RF#04 | UC#4 | Gerenciamento de estágios |
| **6** | US02 | RF#02 | UC#2 | Atribuição dos questionários Mini-CEX e 360 |
| **7** | US10, US11 | RF#01, RF#10 | UC#10 | Preenchimento dos formulários |
| **8** | US06 | RF#06 | UC#6 | Geração de relatórios e gráficos |

---

## 🎨 4. Guia de Estilo (Design System)

**🎨 Paleta de Cores**
| Cor | Hex | Uso |
|:--|:--|:--|
| Azul Marinho | `#003399` | Fundo e botões principais |
| Azul Turquesa | `#3ABDB2` | Destaques e ícones |
| Cinza Escuro | `#808080` | Texto secundário |
| Amarelo Dourado | `#FEB100` | Textos de destaque |
| Branco | `#FFFFFF` | Textos sobre fundo azul |

**🅰️ Tipografia**
- **Principal:** *Century751 SeBd BT* (títulos e botões)  
- **Secundária:** *Futura Md BT* (corpo de texto)  
- **Tamanhos:** Títulos 12pt Bold, corpo 7–10pt Regular  

**🧱 Componentes de Interface**
- Botões turquesa com texto branco  
- Campos de entrada brancos com bordas arredondadas  
- Cards com sombra leve e cantos suaves  
- Calendário com seleção azul  
- Gráficos amarelos com eixos cinza  

---

## 🧰 5. Tecnologias e Ferramentas

| Categoria | Ferramentas |
|:--|:--|
| **Design & Prototipagem** | Figma / Adobe XD |
| **Desenvolvimento** | React Native / Flutter / HTML5 / CSS3 |
| **Controle de Versão** | Git & GitHub |
| **Gerenciamento** | Trello / Notion / Google Drive |

---

## 👥 Equipe

| Grupo | Integrantes | Principais Responsabilidades |
|:--:|:--|:--|
| 1 | 🔐 Login e Avaliações | Telas de autenticação e atribuição de formulários |
| 2 | 🎓 Painel Acadêmico | Visualização de desempenho e histórico |
| 3 | 💬 Feedback & Relatórios | Comunicação e geração de relatórios |
| 4 | 🧾 Gestão Acadêmica | CRUD de turmas e notas |
| 5 | 📅 Agenda & Navegação | Organização visual e fluxo entre telas |

---

## 🖼️ 6. Exemplos Visuais

> Os mockups seguem o guia de estilo descrito acima, com foco em **simplicidade, acessibilidade e identidade visual consistente**.  
*(Inserir aqui capturas de tela ou links para o protótipo no Figma.)*

---

## 🚀 7. Como Contribuir

1. Faça um fork do repositório  
2. Crie uma branch com sua feature:  
   ```bash
   git checkout -b feature/nome-da-feature
