package com.guzhenxing.wordbookbackend;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 笔记的数据访问层。
 *
 * 和 WordMapper 的写法一模一样 —— MyBatis 里每张表配一个 Mapper 接口，
 * 接口方法上写 SQL，@Mapper 让 Spring 给它生成实现类并放进容器。
 */
@Mapper
public interface NoteMapper {

    @Select("SELECT id, word, meaning, example FROM notes ORDER BY id")
    List<Note> findAll();

    @Select("SELECT id, word, meaning, example FROM notes WHERE word = #{word}")
    Note findByWord(String word);

    @Insert("INSERT INTO notes (word, meaning, example) VALUES (#{word}, #{meaning}, #{example})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Note note);

    /**
     * 删除笔记。
     *
     * 参数就是普通的 String，不是对象 —— 所以 #{word} 取的是这整个参数的值。
     * （只有一个参数且不加 @Param 时，MyBatis 不关心你叫什么名字，写 #{anything} 都行，
     *   但写成 #{word} 最好读。多个参数时就不同了，那时候必须用 @Param 起名。）
     *
     * @Delete 是 MyBatis 的注解，和 Spring 的 @DeleteMapping 是两回事：
     * 一个写 SQL，一个定 URL。名字像，包不一样，别混。
     */
    @Delete("DELETE FROM notes WHERE word = #{word}")
    int deleteByWord(String word);
}
