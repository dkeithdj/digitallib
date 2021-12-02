@echo off

echo initializing . . .

call mvn clean compile
call mvn assembly:single

echo done.
pause
