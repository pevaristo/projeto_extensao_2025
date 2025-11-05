# Sistema de Avaliação UNIFAE - NetBeans 21

## 🎯 **PROJETO CONFIGURADO PARA NETBEANS 21**

Este projeto foi especificamente configurado e testado para funcionar perfeitamente no **NetBeans 21** com **Apache Tomcat 10.1.42** e **JDK 21**.

---

## 🚀 **CONFIGURAÇÃO RÁPIDA**

### **Pré-requisitos:**
- ✅ **NetBeans 21** instalado
- ✅ **JDK 21** configurado
- ✅ **Apache Tomcat 10.1.42** instalado
- ✅ **MariaDB 10.4.32+** rodando

### **Passos para Executar:**

#### 1. **Abrir Projeto no NetBeans**
```
File → Open Project → Selecionar pasta 'avaliacao-sistema'
```

#### 2. **Configurar Servidor Tomcat**
```
Tools → Servers → Add Server...
- Tipo: Apache Tomcat or TomEE
- Localização: C:\apache-tomcat-10.1.42 (ou seu caminho)
- Usuário/Senha: (se configurado)
```

#### 3. **Configurar Propriedades do Projeto**
```
Botão direito no projeto → Properties
- Run → Server: Apache Tomcat 10.1.42
- Run → Context Path: /avaliacao-sistema
- Run → Relative URL: /
```

#### 4. **Executar o Sistema**
```
F6 (Run) ou botão direito → Run
```

#### 5. **Acessar a Aplicação**
```
URL: http://localhost:8080/avaliacao-sistema/
```

---

## 📁 **ESTRUTURA DO PROJETO**

```
avaliacao-sistema/
├── src/main/java/
│   └── com/unifae/med/
│       ├── entity/          # Entidades JPA
│       ├── dao/             # Data Access Objects
│       ├── servlet/         # Controladores Servlet
│       └── util/            # Utilitários (JPAUtil)
├── src/main/resources/
│   └── META-INF/
│       └── persistence.xml  # Configuração JPA
├── src/main/webapp/
│   ├── WEB-INF/
│   │   ├── web.xml         # Configuração Jakarta EE 5.0
│   │   └── views/          # Páginas JSP
│   ├── css/                # Estilos CSS
│   ├── META-INF/
│   │   └── context.xml     # Context path configurado
│   └── index.jsp           # Página inicial funcional
├── pom.xml                 # Dependências Maven
└── nb-configuration.xml    # Configuração NetBeans
```

---

## 🔧 **CONFIGURAÇÕES APLICADAS**

### **1. Jakarta EE 5.0**
- ✅ Web.xml atualizado para Jakarta EE
- ✅ Todas as classes migradas de javax para jakarta
- ✅ Dependências Maven atualizadas

### **2. Context Path**
- ✅ Configurado para `/avaliacao-sistema`
- ✅ URLs consistentes em todo o sistema
- ✅ Deploy automático funcionando

### **3. NetBeans Integration**
- ✅ Deploy automático ativo
- ✅ Hot reload configurado
- ✅ Debug integrado
- ✅ JDK 21 especificado

### **4. Encoding UTF-8**
- ✅ Filtro de encoding configurado
- ✅ Páginas JSP com charset UTF-8
- ✅ Banco de dados com collation UTF-8

---

## 🗄️ **BANCO DE DADOS**

### **Configuração:**
```properties
# persistence.xml
URL: jdbc:mariadb://localhost:3306/unifae_med_app
Driver: org.mariadb.jdbc.Driver
Usuário: root
Senha: (vazia)
```

### **Script de Dados de Teste:**
```bash
# Execute o script SQL fornecido
mysql -u root -p unifae_med_app < script_definitivo_corrigido.sql
```

---

## 🎯 **FUNCIONALIDADES TESTADAS**

### **✅ Sistema Base:**
- [x] Compilação sem erros
- [x] Deploy automático
- [x] Página inicial funcional
- [x] Context path correto

### **✅ Formulários:**
- [x] Mini CEX
- [x] Avaliação 360 - Professor
- [x] Avaliação 360 - Pares
- [x] CSS responsivo aplicado

### **✅ Backend:**
- [x] Entidades JPA funcionando
- [x] DAOs implementados
- [x] Servlets mapeados
- [x] Conexão com banco

---

## 🚀 **URLS DO SISTEMA**

### **Página Principal:**
```
http://localhost:8080/avaliacao-sistema/
```

### **Servlets:**
```
http://localhost:8080/avaliacao-sistema/avaliacoes
http://localhost:8080/avaliacao-sistema/avaliacao/form
http://localhost:8080/avaliacao-sistema/avaliacao/view
http://localhost:8080/avaliacao-sistema/avaliacao/delete
```

### **Formulários JSP:**
```
/WEB-INF/views/avaliacoes/minicex-form.jsp
/WEB-INF/views/avaliacoes/avaliacao360-professor-form.jsp
/WEB-INF/views/avaliacoes/avaliacao360-pares-form.jsp
```

---

## 🔧 **TROUBLESHOOTING**

### **Problema: Aplicação não abre**
**Solução:** Verificar se Tomcat 10.1.42 está configurado no NetBeans

### **Problema: Erro 404**
**Solução:** Verificar context path em `META-INF/context.xml`

### **Problema: Erro de encoding**
**Solução:** Filtro já configurado no `web.xml`

### **Problema: JPA não funciona**
**Solução:** Verificar se MariaDB está rodando e banco existe

---

## 📞 **SUPORTE**

### **Versões Testadas:**
- ✅ NetBeans 21
- ✅ Apache Tomcat 10.1.42
- ✅ JDK 21
- ✅ MariaDB 10.4.32+
- ✅ Maven 3.9+

### **Compatibilidade:**
- ✅ Windows 10/11
- ✅ Linux Ubuntu 20.04+
- ✅ macOS 12+

---

## 🎉 **SISTEMA PRONTO!**

O projeto está **100% configurado** e **testado** para NetBeans 21. 

**Basta abrir, configurar o servidor Tomcat e executar!**

---

*Desenvolvido para UNIFAE - Sistema de Avaliação Médica*
*Configurado especificamente para NetBeans 21 + Tomcat 10.1.42 + JDK21*

