@ECHO OFF

:: use variable specified by the use
IF EXIST "var.local" (
	COPY /B/V/Y "var.local" "var" > NUL
	EXIT /B 0
) 

:: use environment variable
IF not "%JAVA_HOME%" == "" (
	ECHO JAVA_HOME=%JAVA_HOME% > "var"
	EXIT /B 0
) 

