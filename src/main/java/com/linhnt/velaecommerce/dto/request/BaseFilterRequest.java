package com.linhnt.velaecommerce.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseFilterRequest {
    protected Integer limit = 10;
    protected Integer page = 1;
    protected String sortType = "desc";
    protected String sortField = "id";
}
