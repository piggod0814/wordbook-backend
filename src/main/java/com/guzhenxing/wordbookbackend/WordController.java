package com.guzhenxing.wordbookbackend;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

    @PostMapping("/api/words")
    public Word add(@RequestBody Word word) {
        // 先查一遍再插，而不是「直接插、等着数据库报错」。
        //
        // 为什么不靠 uk_word 唯一键兜底？因为唯一键报的是 DuplicateKeyException，
        // Spring 会把它翻成 500 Internal Server Error —— 500 的含义是「服务器自己出 bug 了」，
        // 但「这个词你已经加过」明显不是 bug，是用户操作问题。用错状态码，前端就没法区分
        // 「我该提示用户」还是「该报警修服务器」。
        //
        // 409 Conflict 的含义正好是：请求本身合法，但和服务器现有状态冲突。
        if (wordMapper.findByWord(word.getWord()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "这个单词已经在词库里了");
        }

        wordMapper.insert(word);
        return word;
    }

    /**
     * 标记为已掌握。
     *
     * 注意这里没有「搬家」：词还在 words 表里，只是 status 从 learning 变成 mastered。
     * 前端按 status 分成两拨显示，数据库里始终只有一份数据 —— 不会出现
     * 「words 删了、mastered 还没插进去」这种中间状态。
     *
     * 路径 /api/words/{word}/mastered 可以理解成「这个词的已掌握标记」这个资源：
     *   PUT    它 → 打上标记
     *   DELETE 它 → 拿掉标记
     * 用状态而不是用 /master 这种动作词，好处是前端一眼能看出它是可逆的。
     */
    @PutMapping("/api/words/{word}/mastered")
    public void markMastered(@PathVariable String word) {
        wordMapper.updateStatus(word, "mastered");
    }

    @DeleteMapping("/api/words/{word}/mastered")
    public void unmarkMastered(@PathVariable String word) {
        wordMapper.updateStatus(word, "learning");
    }
}
