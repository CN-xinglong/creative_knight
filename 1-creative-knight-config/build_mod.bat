@echo off

set GRADLE_HOME=..\gradle\gradle-8.14.4
set PATH=%GRADLE_HOME%\bin;%PATH%

echo Using Gradle from: %GRADLE_HOME%
gradle clean build

if %ERRORLEVEL% equ 0 (
    echo Build successful!
    echo Copying JAR file to mods folder...
    copy build\libs\creative_knight_config-1.0.jar "E:\games\MC\.minecraft\versions\1.20.1-Forge_47.4.16\mods"
    echo Done!
) else (
    echo Build failed!
    pause
)

pause