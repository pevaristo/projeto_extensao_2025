@echo off
echo ========================================
echo CONFIGURACAO AUTOMATICA - NETBEANS 21
echo Sistema de Avaliacao UNIFAE
echo ========================================
echo.

echo [1/5] Verificando Java 21...
java -version
if %errorlevel% neq 0 (
    echo ERRO: Java 21 nao encontrado!
    echo Instale o JDK 21 antes de continuar.
    pause
    exit /b 1
)
echo ✅ Java 21 encontrado!
echo.

echo [2/5] Verificando Maven...
mvn -version
if %errorlevel% neq 0 (
    echo ERRO: Maven nao encontrado!
    echo Instale o Maven antes de continuar.
    pause
    exit /b 1
)
echo ✅ Maven encontrado!
echo.

echo [3/5] Limpando projeto...
call mvn clean
echo ✅ Projeto limpo!
echo.

echo [4/5] Compilando projeto...
call mvn compile
if %errorlevel% neq 0 (
    echo ERRO: Falha na compilacao!
    pause
    exit /b 1
)
echo ✅ Projeto compilado!
echo.

echo [5/5] Gerando WAR...
call mvn package
if %errorlevel% neq 0 (
    echo ERRO: Falha na geracao do WAR!
    pause
    exit /b 1
)
echo ✅ WAR gerado com sucesso!
echo.

echo ========================================
echo CONFIGURACAO CONCLUIDA!
echo ========================================
echo.
echo Proximos passos:
echo 1. Abrir NetBeans 21
echo 2. File → Open Project → Selecionar esta pasta
echo 3. Configurar servidor Tomcat 10.1.42
echo 4. Executar projeto (F6)
echo 5. Acessar: http://localhost:8080/avaliacao-sistema/
echo.
echo ✅ Sistema pronto para uso no NetBeans 21!
echo.
pause

