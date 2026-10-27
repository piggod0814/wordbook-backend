# 背单词本 · 后端

一个用来背单词的小项目后端：单词增删查、笔记、已掌握标记，数据存 MySQL。
配合前端仓库 [vue3-demo](../vue3-demo) 使用（实际地址填你自己的前端仓库链接）。

## 技术栈

| 组件 | 版本 |
|---|---|
| Spring Boot | 4.1.1 |
| Java | 17 |
| 持久层 | MyBatis 4.1.0（注解 SQL，无 XML） |
| 数据库 | MySQL 8.x |
| 构建 | Maven |

## 快速开始

### 1. 准备 MySQL

本地装好 MySQL 后，建库不需要手动做——连接串带了 `createDatabaseIfNotExist=true`，
第一次启动会自动建 `wordbook` 库；表由 `src/main/resources/schema.sql` 自动创建。

如果你本机 MySQL 的账号密码不是 `root / root123456`，有两种改法：

**方式一（推荐）**：不改代码，启动前设环境变量

```bash
# Git Bash
export DB_PASSWORD=你的密码
export DB_USERNAME=root          # 用户名不同才需要
export DB_HOST=127.0.0.1         # 数据库不在本机才需要
```

PowerShell：

```powershell
$env:DB_PASSWORD = "你的密码"
```

**方式二**：直接改 `src/main/resources/application.properties` 里的默认值。
注意这个文件是入库的，改完别把真实生产密码提交上去。

### 2. 启动

```bash
# 项目自带 Maven Wrapper，不需要自己装 Maven
./mvnw spring-boot:run
```

看到 `Tomcat started on port 8080` 就好了。

### 3. 验证

```bash
curl http://127.0.0.1:8080/api/words
```

返回一个 JSON 数组就是通了。

> 用 curl 测 **含中文** 的 POST 时，Windows 的 Git Bash 会把命令行中文按 GBK 编码发出去，
> Spring 按 UTF-8 解析会返回 400。解决办法是把 JSON 写进一个 UTF-8 文件，用
> `curl --data-binary @body.json` 发送。浏览器里的 `fetch` 不受影响。

## 接口

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/api/words` | 全部单词（含 `status` 字段） |
| POST | `/api/words` | 新增单词，重复词返回 **409** |
| PUT | `/api/words/{word}/mastered` | 标记为已掌握 |
| DELETE | `/api/words/{word}/mastered` | 取消已掌握 |
| GET | `/api/notes` | 全部笔记 |
| POST | `/api/notes` | 新增笔记，重复返回 **409** |
| DELETE | `/api/notes/{word}` | 删除笔记 |

单词对象：

```json
{
  "id": 1,
  "word": "abandon",
  "meaning": "v. 放弃",
  "example": "He abandoned the plan.",
  "result": "",
  "expanded": false,
  "status": "learning"
}
```

`status` 只有两个值：`learning`（在背）/ `mastered`（已掌握）。

## 部署：一个端口同时提供页面和接口

前端 `npm run build` 之后，把 `dist/` 里的文件拷到本项目的
`src/main/resources/static/`，重启后端，然后直接访问 `http://127.0.0.1:8080/`
就能看到完整应用——页面和接口同源，不需要处理跨域。

`static/` 目录已加入 `.gitignore`，因为它是构建产物；clone 下来需要自己生成。

## 目录结构

```
src/main/java/com/guzhenxing/wordbookbackend/
├── WordbookBackendApplication.java   启动类
├── Word.java                         单词实体
├── WordMapper.java                   MyBatis 映射（注解 SQL）
├── WordController.java               单词接口
├── Note.java                         笔记实体
├── NoteMapper.java                   笔记映射
└── NoteController.java               笔记接口

src/main/resources/
├── application.properties            配置
├── schema.sql                        建表语句（每次启动执行，IF NOT EXISTS 幂等）
└── data.sql                          种子数据（INSERT IGNORE）
```
