package com.linhnt.velaecommerce.dto.request.order;

import com.linhnt.velaecommerce.dto.request.BaseFilterRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterOrderDto extends BaseFilterRequest {
    private String customerName;
    private Long id;
}
