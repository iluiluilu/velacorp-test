package com.linhnt.velaecommerce.dto.request.product;

import com.linhnt.velaecommerce.dto.request.BaseFilterRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilterProductDto extends BaseFilterRequest {
    private String keyword;
}
