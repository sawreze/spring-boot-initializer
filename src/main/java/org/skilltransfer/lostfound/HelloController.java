package org.skilltransfer.lostfound;

import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping(value = "/", produces = "text/plain;charset=UTF-8")
    public String home() {
        return "분실물 서버가 실행 중입니다.";
    }

    @GetMapping(value = "/api/hello", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> hello(@RequestParam(defaultValue = "학생") String name) {
        return Map.of(
            "message", "안녕하세요, " + name + "님.",
            "service", "lost-found-server"
        );
    }
}
