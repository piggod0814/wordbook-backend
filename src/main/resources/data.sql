-- 初始数据。Spring 会先执行 schema.sql（建表），再执行这个文件。
-- 因为有 spring.sql.init.mode=always，每次启动都会跑一遍，
-- 所以用 INSERT IGNORE：靠 schema.sql 里那个 UNIQUE KEY uk_word 去重，重复执行不会插第二条。
INSERT IGNORE INTO words (word, meaning, example)
VALUES ('abandon', 'v. 放弃，抛弃', 'He abandoned the plan.'),
       ('absorb', 'v. 吸收；理解', 'Plants absorb water.'),
       ('abstract', 'adj. 抽象的 n. 摘要', 'This is an abstract idea.');
