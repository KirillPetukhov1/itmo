@echo off
echo Генерация JavaDoc...

REM Создаем папку для документации, если её нет
if not exist docs mkdir docs

REM Собираем все JAR-файлы в classpath
set JARS=lib\*
echo Используются JAR-файлы: %JARS%

REM Генерируем документацию
javadoc -d docs -sourcepath src -subpackages src -cp "%JARS%" -encoding UTF-8 -charset UTF-8 -author -version

echo.
echo Готово! Документация в папке docs
pause