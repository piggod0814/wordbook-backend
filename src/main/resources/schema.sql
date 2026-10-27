-- 这个文件由 Spring 在启动时自动执行（靠 application.properties 里的 spring.sql.init.mode=always）
-- 注意 IF NOT EXISTS：每次启动都会跑一遍，没有它第二次启动就报「表已存在」

CREATE TABLE IF NOT EXISTS words (
    id       BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键，数据库自己发号',
    word     VARCHAR(100) NOT NULL COMMENT '单词',
    meaning  VARCHAR(255) NOT NULL COMMENT '中文意思',
    example  VARCHAR(500)          COMMENT '例句',
    result   VARCHAR(20)  NOT NULL DEFAULT '' COMMENT '记得 / 不记得 / 空',
    expanded TINYINT(1)   NOT NULL DEFAULT 0 COMMENT '是否展开了扩展讲解',
    -- status 就是「已掌握」这件事本身。
    -- 没有单独建 mastered 表，因为已掌握不是一种新东西，而是单词的一个状态：
    -- 词还是这个词，意思还是这个意思，只是暂时不参与背词了。
    -- 拆表会逼你搬数据（从 words 删、往 mastered 插），搬一次就多一次出错的机会。
    status   VARCHAR(20)  NOT NULL DEFAULT 'learning' COMMENT 'learning=在背 / mastered=已掌握',
    PRIMARY KEY (id),
    UNIQUE KEY uk_word (word)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT '单词表';

-- 笔记表：一个词最多一条笔记（uk_note_word 保证）。
-- 为什么不把笔记做成 words 表的一个字段？
--   因为「加入笔记」和「在背词表里」是两件独立的事：
--   一个词可以既在背词表里、又在笔记里；词被移进「已掌握」了，笔记也还该留着。
--   状态与实体分开，各自有独立的生命周期 —— 这就是拆表的判断依据。
CREATE TABLE IF NOT EXISTS notes (
    id      BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    word    VARCHAR(100) NOT NULL COMMENT '单词',
    meaning VARCHAR(255) NOT NULL COMMENT '中文意思',
    example VARCHAR(500)          COMMENT '例句',
    PRIMARY KEY (id),
    UNIQUE KEY uk_note_word (word)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT '笔记表';
