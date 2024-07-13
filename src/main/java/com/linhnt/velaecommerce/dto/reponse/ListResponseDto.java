package com.linhnt.velaecommerce.dto.reponse;

import lombok.Data;

import java.util.List;

@Data
public class ListResponseDto<T> {
    private List<T> items;
    private Long total;
}
