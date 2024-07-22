package kusitms.backend.domain.user.controller;

import kusitms.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{userId}")
    public UserInfoResponse getUserInfo(@PathVariable Long userId) {
        return UserService.getUserInfo(userId);
    }


}
