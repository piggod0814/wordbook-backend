package com.guzhenxing.wordbookbackend;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 数据访问层。注意它是个 interface（接口），不是 class —— 这一点和 C 完全对不上，单独讲：
 *
 * 你没有写任何实现代码，MyBatis 在启动时会给这个接口「现场生成」一个实现类。
 * 它靠的就是方法上面那行 SQL 字符串。所以：
 *
 *   List<Word> findAll()  ← 方法签名告诉你「返回一堆 Word」
 *               ↑
 *   方法上那行 @Select 就是它的实现
 *
 * @Mapper 的作用是告诉 Spring：这个接口要生成实现类，并且放进容器里（这样 Controller 才能注入它）。
 *   不加 @Mapper 也可以，但要在启动类上写 @MapperScan("...包名")，一个接口一个注解更省事。
 */
@Mapper
public interface WordMapper {

    /**
     * 查全部。
     * SQL 里写的列名，MyBatis 会去 Word 对象里找同名的属性（找的是 setWord / setMeaning 这些 setter），
     * 所以 SELECT 出来的列必须和 Word 的字段名对得上 —— 少写一列，那个字段就是 null，
     * 而且不报错。这是 MyBatis 最典型的坑，和之前「JSON 键名写错不报错」是同一类。
     */
    @Select("SELECT id, word, meaning, example, result, expanded, status FROM words ORDER BY id")
    List<Word> findAll();

    /**
     * 按单词查一条，查不到返回 null。
     *
     * 注意：words 表的排序规则是 utf8mb4_unicode_ci（ci = case insensitive，不区分大小写），
     * 所以 WHERE word = 'Abandon' 能匹配到 'abandon' —— 和唯一键 uk_word 的判定规则一致。
     * 这点很重要：如果查询区分大小写、唯一键不区分，就会出现「查不到 → 去插 → 被唯一键拦住」的怪事。
     */
    @Select("SELECT id, word, meaning, example, result, expanded, status FROM words WHERE word = #{word}")
    Word findByWord(String word);

    /**
     * 改状态：在背 ↔ 已掌握。
     *
     * 【这里有两个参数，所以必须写 @Param】
     * MyBatis 只有一个参数时，你在 SQL 里写 #{随便什么} 都能拿到；
     * 但一旦是两个以上，它就没法靠名字猜了（Java 编译后会丢掉参数名，
     * 通常变成 arg0、arg1）。@Param("word") 就是亲手把名字钉上去。
     * 忘了写的结果不是报错，而是启动时报 BindingException —— 早发现，别怕。
     */
    @Update("UPDATE words SET status = #{status} WHERE word = #{word}")
    int updateStatus(@Param("word") String word, @Param("status") String status);

    /**
     * 加一个词。
     *
     * #{word} 是「取参数对象的 word 属性」，不是字符串拼接 —— 它底层用的是 PreparedStatement 的 ?，
     * 所以天然防 SQL 注入。千万别用 ${word}，那是直接拼字符串，能用但危险。
     *
     * @Options(useGeneratedKeys = true, keyProperty = "id")
     *   插入完成后，把数据库生成的自增 id 回填到传进来的那个 Word 对象的 id 字段上。
     *   没有它，插入成功后你手里这个对象的 id 还是 null。
     */
    @Insert("INSERT INTO words (word, meaning, example, result, expanded) " +
            "VALUES (#{word}, #{meaning}, #{example}, #{result}, #{expanded})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Word word);
}
