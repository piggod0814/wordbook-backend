package com.guzhenxing.wordbookbackend;

/**
 * 一个单词。
 *
 * 【和之前 JPA 版本的区别】
 * 原来类上有 @Entity / @Table / @Id / @GeneratedValue，那是 JPA 的写法 —— 让 Hibernate 通过注解
 * 自己去建表、自己去读写。现在换了 MyBatis，这几行注解全部删掉：
 * 类是纯 POJO，表由 schema.sql 建，Java 和数据库之间的关系写在 WordMapper 的 SQL 里。
 *
 * 这就是两套框架的根本差别：
 *   JPA    —— 注解写在类上，SQL 由框架生成，你看不到
 *   MyBatis —— SQL 你自己写（在 Mapper 里），框架只负责把查询结果塞进对象
 */
public class Word {

    private Long id;
    private String word;
    private String meaning;
    private String example;

    // 这两个默认值很重要：不写的话 Java 会给 null / false，
    // 前端那句 w.result !== '' 会把 null 当成「已作答」
    private String result = "";
    private boolean expanded = false;

    // 无参构造：第 4 步接收前端发来的 JSON 时，Jackson 要先用它造个空对象
    public Word() {
    }

    // 带参构造：造假数据用，result / expanded 留上面那组默认值
    public Word(Long id, String word, String meaning, String example) {
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.example = example;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    // boolean 的 getter 习惯上叫 isXxx()，不是 getExpanded()
    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
