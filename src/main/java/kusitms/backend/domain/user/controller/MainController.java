package kusitms.backend.domain.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


//추후 삭제 예정 (태스트용)
@Controller
public class MainController {

    @GetMapping("/")
    @ResponseBody
    public String mainAPI(){
        return "main route";
    }

}
