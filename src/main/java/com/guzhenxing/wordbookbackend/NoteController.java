package com.guzhenxing.wordbookbackend;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * 笔记接口。
 *
 * 【这组接口值得注意的地方：路径是同一个，方法不同】
 *
 *   GET    /api/notes        → 列出所有笔记
 *   POST   /api/notes        → 新增一条
 *   DELETE /api/notes/{word} → 删掉某个词
 *
 * 前两个路径一字不差，靠 HTTP 方法区分。这不是命名偷懒，是 REST 的约定：
 *   URL 指「资源」，方法指「动作」。
 *   所以不要写成 /api/notes/add、/api/notes/delete —— 动作该交给方法，不该塞进路径。
 */
@RestController
public class NoteController {

    private final NoteMapper noteMapper;

    public NoteController(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    @GetMapping("/api/notes")
    public List<Note> list() {
        return noteMapper.findAll();
    }

    @PostMapping("/api/notes")
    public Note add(@RequestBody Note note) {
        // 和单词接口同样的处理：先查后插，重复就 409，不靠唯一键抛 500
        if (noteMapper.findByWord(note.getWord()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "这个单词已经在笔记里了");
        }

        noteMapper.insert(note);
        return note;
    }

    /**
     * 删一条笔记。
     *
     * @PathVariable：把 URL 里 {word} 那一段抠出来，当成方法的参数。
     *   访问 DELETE /api/notes/abandon 时，word 收到的就是 "abandon"。
     *   和 @RequestBody 的区别：前者从 URL 里取，后者从请求体里取。
     *   删除只需要「删哪条」，不需要一整条笔记的内容，所以用路径参数更贴切。
     *
     * 返回 void：删除成功没什么可说的，Spring 会返回 200 空响应。
     * （更讲究的做法是返回 204 No Content，等以后再改。）
     */
    @DeleteMapping("/api/notes/{word}")
    public void remove(@PathVariable String word) {
        noteMapper.deleteByWord(word);
    }
}
