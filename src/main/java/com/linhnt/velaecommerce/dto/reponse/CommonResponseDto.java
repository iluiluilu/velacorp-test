package com.linhnt.velaecommerce.dto.reponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommonResponseDto {
    private Object data;
    private String errorMessage;
    private String errorCode;
}
