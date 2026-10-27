package com.guzhenxing.wordbookbackend;

/**
 * 一条笔记。
 *
 * 和 Word 长得几乎一样，但少两个字段：
 *   result   —— 那是「背没背过」的答题状态，笔记不参与背词
 *   expanded —— 那是「讲解收没收起」的界面状态，纯前端临时用，没必要存库
 *
 * 少存两个字段不是省空间，是「这张表只存它自己的事」。
 */
public class Note {

    private Long id;
    private String word;
    private String meaning;
    private String example;

    // Jackson 反序列化（JSON → 对象）必须有它
    public Note() {
    }

    public Note(Long id, String word, String meaning, String example) {
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
}
