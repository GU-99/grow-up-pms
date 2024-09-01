package com.growup.pms.user.controller;

import com.growup.pms.auth.controller.dto.SecurityUser;
import com.growup.pms.common.aop.annotation.CurrentUser;
import com.growup.pms.user.controller.dto.request.PasswordUpdateRequest;
import com.growup.pms.user.controller.dto.request.RecoverPasswordRequest;
import com.growup.pms.user.controller.dto.request.RecoverUsernameRequest;
import com.growup.pms.user.controller.dto.request.UserCreateRequest;
import com.growup.pms.user.controller.dto.request.UserUploadRequest;
import com.growup.pms.user.controller.dto.request.VerificationCodeCreateRequest;
import com.growup.pms.user.controller.dto.response.RecoverPasswordResponse;
import com.growup.pms.user.controller.dto.response.RecoverUsernameResponse;
import com.growup.pms.user.controller.dto.response.UserSearchResponse;
import com.growup.pms.user.controller.dto.response.UserTeamResponse;
import com.growup.pms.user.service.UserService;
import com.growup.pms.user.service.dto.UserDownloadCommand;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserControllerV1 {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.created(URI.create("/api/v1/user/" + userService.save(request.toCommand()))).build();
    }

    @PostMapping("/file")
    public ResponseEntity<Void> upload(@CurrentUser SecurityUser user, @Valid @RequestBody UserUploadRequest request) {
        userService.uploadImage(user.getId(), request.toCommand());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/file")
    public ResponseEntity<Resource> download(@CurrentUser SecurityUser user) {
        UserDownloadCommand command = userService.imageDownload(user.getId());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + command.imageName() + "\"")
                .body(command.resource());
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserSearchResponse>> search(@RequestParam("nickname") String nicknamePrefix) {
        return ResponseEntity.ok().body(userService.searchUsersByNicknamePrefix(nicknamePrefix));
    }

    @GetMapping("/team")
    public ResponseEntity<List<UserTeamResponse>> getTeams(@CurrentUser SecurityUser user) {
        return ResponseEntity.ok().body(userService.getAllUserTeams(user.getId()));
    }

    @PostMapping("/recover/username")
    public ResponseEntity<RecoverUsernameResponse> recoverUsername(@Valid @RequestBody RecoverUsernameRequest request) {
        return ResponseEntity.ok().body(userService.recoverUsername(request.toCommand()));
    }

    @PostMapping("/recover/password")
    public ResponseEntity<RecoverPasswordResponse> recoverPassword(@Valid @RequestBody RecoverPasswordRequest request) {
        return ResponseEntity.ok().body(userService.recoverPassword(request.toCommand()));
    }

    @PostMapping("/verify/send")
    public ResponseEntity<Void> sendVerificationCode(@Valid @RequestBody VerificationCodeCreateRequest request) {
        userService.sendVerificationCode(request.toCommand());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> updatePassword(
            @CurrentUser SecurityUser user,
            @Valid @RequestBody PasswordUpdateRequest request
    ) {
        userService.updatePassword(user.getId(), request.toCommand());
        return ResponseEntity.ok().build();
    }
}
