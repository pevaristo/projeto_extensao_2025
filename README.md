# Sistema de Avaliação UNIFAE - NetBeans 21

---

## 🚀 **CONFIGURAÇÃO RÁPIDA**

### **Pré-requisitos:**
- ✅ **Docker Desktop** instalado

### **Passos para Executar:**

#### 1. **Executar com Docker**
```
docker-compose up -d --build
```

#### 2. **Acessar a Aplicação**
```
URL: http://localhost:8082/avaliacao-sistema/
```

## 🗄️ **BANCO DE DADOS**

### **Configuração:**
```properties
# persistence.xml
URL: jdbc:mariadb://db:3306/unifae_med_app
Driver: org.mariadb.jdbc.Driver
Usuário: unifae_med_app
Senha: unifae_med_app
```


## 🎯 **PROJETO CONFIGURADO PARA NETBEANS 21**

Confira em avaliacao-sistema-atual/README_NETBEANS21.md


*Desenvolvido para UNIFAE - Sistema de Avaliação Médica*
*Configurado especificamente para NetBeans 21 + Tomcat 10.1.42 + JDK21*
