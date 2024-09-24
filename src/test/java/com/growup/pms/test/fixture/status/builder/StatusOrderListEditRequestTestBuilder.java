package com.growup.pms.test.fixture.status.builder;

import static com.growup.pms.test.fixture.status.builder.StatusOrderEditRequestTestBuilder.상태_정렬순서_변경_요청은;

import com.growup.pms.status.controller.dto.request.StatusOrderEditRequest;
import com.growup.pms.status.controller.dto.request.StatusOrderListEditRequest;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StatusOrderListEditRequestTestBuilder {

    private List<StatusOrderEditRequest> statuses = List.of(상태_정렬순서_변경_요청은().이다());

    public static StatusOrderListEditRequestTestBuilder 상태_정렬순서_목록_변경_요청은() {
        return new StatusOrderListEditRequestTestBuilder();
    }

    public StatusOrderListEditRequestTestBuilder 정렬순서_변경_목록은(List<StatusOrderEditRequest> sortOrders) {
        this.statuses = sortOrders;
        return this;
    }

    public StatusOrderListEditRequest 이다() {
        return StatusOrderListEditRequest.builder()
                .statuses(statuses)
                .build();
    }
}
