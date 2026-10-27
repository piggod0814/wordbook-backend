package com.guzhenxing.wordbookbackend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class WordController {

    /**
     * 构造器注入：Spring 启动时会把 WordMapper 的实现类塞进来。
     *
     * 为什么不用 new WordMapper()？因为接口不能 new。
     * 为什么不用 @Autowired 写在字段上？能跑，但构造器注入有三个好处：
     *   1. 字段可以写 final（这个依赖一旦确定就不许换）
     *   2. 少写一个注解
     *   3. 谁依赖了谁，看一眼构造器就知道，不用翻注解
     * Spring 4.3 之后，只有一个构造器时 @Autowired 可以省略 —— 这里就是。
     */
    private final WordMapper wordMapper;

    public WordController(WordMapper wordMapper) {
        this.wordMapper = wordMapper;
    }

    @GetMapping("/api/words")
    public List<Word> list() {
        // 现在是真从 MySQL 的 words 表里查，不再是内存里那三个假词
        return wordMapper.findAll();
    }
}
