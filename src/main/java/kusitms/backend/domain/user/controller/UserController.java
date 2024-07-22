package kusitms.backend.domain.user.controller;

import kusitms.backend.domain.onboarding.dto.request.ModifyUserInfoRequest;
import kusitms.backend.domain.onboarding.dto.response.UserInfoResponse;
import kusitms.backend.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{userId}")
    public UserInfoResponse getUserInfo(@PathVariable Long userId) {
        UserInfoResponse userInfoResponse = userService.getUserInfo(userId);
        return userInfoResponse;
    }

    @PutMapping("/users/{userId}")
    public void modifyUserInfo(@PathVariable Long userId, @RequestBody ModifyUserInfoRequest modifyUserInfoRequest) {
        userService.modifyUserInfo(userId, modifyUserInfoRequest);
    }

}
