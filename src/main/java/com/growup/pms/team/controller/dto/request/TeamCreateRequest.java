package com.growup.pms.team.controller.dto.request;

import com.growup.pms.team.service.dto.TeamCreateCommand;
import com.growup.pms.team.service.dto.TeamCreateCommand.TeamCoworkerCommand;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;

@Builder
public record TeamCreateRequest(
        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,10}$")
        String name,

        @Size(max = 300)
        String content,

        @NotNull
        Long creatorId,

        @Valid
        @NotNull
        List<TeamCoworkerRequest> coworkers
) {
    public TeamCreateCommand toCommand() {
        return TeamCreateCommand.builder()
                .name(name())
                .content(content)
                .creatorId(creatorId)
                .coworkers(coworkers.stream()
                        .map(TeamCoworkerRequest::toCommand)
                        .toList())
                .build();
    }

    @Builder
    public record TeamCoworkerRequest(
            @NotNull
            Long id,

            @NotBlank
            String role
    ) {
        public TeamCoworkerCommand toCommand() {
            return TeamCoworkerCommand.builder()
                    .id(id)
                    .role(role)
                    .build();
        }
    }
}
