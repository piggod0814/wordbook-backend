@echo off
chcp 65001 >nul
echo ===== 启动 WSL 里的 MySQL =====
wsl -d Ubuntu -e bash -c "sudo service mysql start"
echo.
echo ===== 确认能连上（版本 + 现有数据库）=====
wsl -d Ubuntu -e bash -c "mysql -uroot -proot123456 -e 'select version(); show databases;'"
echo.
pause
