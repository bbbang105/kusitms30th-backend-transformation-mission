package kusitms.backend.domain.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


//추후 삭제 예정 (테스트용)
@Controller
public class MyController {

    @GetMapping("/my")
    @ResponseBody
    public String myAPI(){
        return "my route";
    }
}
