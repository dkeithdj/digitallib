@echo off

echo initializing . . .

call mvn -q clean compile
call mvn -q assembly:single

echo done.
pause
