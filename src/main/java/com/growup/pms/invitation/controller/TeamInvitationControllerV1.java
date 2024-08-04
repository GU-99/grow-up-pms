package com.growup.pms.invitation.controller;

import com.growup.pms.auth.domain.SecurityUser;
import com.growup.pms.invitation.dto.TeamInvitationCreateRequest;
import com.growup.pms.invitation.service.TeamInvitationService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/team/{teamId}/invitation")
@RequiredArgsConstructor
public class TeamInvitationControllerV1 {
    private final TeamInvitationService teamInvitationService;

    // TODO: 권한이나 역할이 구현되면 팀 초대 권한이 있는 팀원만 다른 사용자를 초대할 수 있도록 구현해야 함
    @PostMapping
    public ResponseEntity<Void> invite(@PathVariable Long teamId, @Valid @RequestBody TeamInvitationCreateRequest request) {
        return ResponseEntity.created(
                URI.create("/api/v1/team/" + teamId + "/invitation/"
                        + teamInvitationService.sendInvitation(teamId, TeamInvitationCreateRequest.toServiceDto(request))))
                .build();
    }

    @PostMapping("/accept")
    public ResponseEntity<Void> accept(@AuthenticationPrincipal SecurityUser user, @PathVariable Long teamId) {
        teamInvitationService.acceptInvitation(teamId, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/decline")
    public ResponseEntity<Void> decline(@AuthenticationPrincipal SecurityUser user, @PathVariable Long teamId) {
        teamInvitationService.declineInvitation(teamId, user.getId());
        return ResponseEntity.noContent().build();
    }
}
