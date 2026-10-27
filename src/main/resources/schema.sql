-- 这个文件由 Spring 在启动时自动执行（靠 application.properties 里的 spring.sql.init.mode=always）
-- 注意 IF NOT EXISTS：每次启动都会跑一遍，没有它第二次启动就报「表已存在」

CREATE TABLE IF NOT EXISTS words (
    id       BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键，数据库自己发号',
    word     VARCHAR(100) NOT NULL COMMENT '单词',
    meaning  VARCHAR(255) NOT NULL COMMENT '中文意思',
    example  VARCHAR(500)          COMMENT '例句',
    result   VARCHAR(20)  NOT NULL DEFAULT '' COMMENT '记得 / 不记得 / 空',
    expanded TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '是否展开了扩展讲解',
    PRIMARY KEY (id),
    UNIQUE KEY uk_word (word)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT '单词表';
