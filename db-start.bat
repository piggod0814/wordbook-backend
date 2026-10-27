@echo off
chcp 65001 >nul
title 启动 wordbook 数据库（Windows MySQL 8.4.6）

echo ===== 启动 C:\dev\mysql 上的 MySQL =====
echo 注意：这个窗口不能关，关掉数据库就停了。
echo.
call "C:\dev\mysql\start-mysql.bat"
