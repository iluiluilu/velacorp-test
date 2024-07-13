package com.linhnt.velaecommerce.controller;

import com.linhnt.velaecommerce.constant.ErrorCode;
import com.linhnt.velaecommerce.dto.reponse.CommonResponseDto;
import com.linhnt.velaecommerce.dto.reponse.ListResponseDto;
import com.linhnt.velaecommerce.dto.request.product.CreateProductDto;
import com.linhnt.velaecommerce.dto.request.product.FilterProductDto;
import com.linhnt.velaecommerce.dto.request.product.UpdateProductDto;
import com.linhnt.velaecommerce.entity.ProductEntity;
import com.linhnt.velaecommerce.exception.VeNotFoundException;
import com.linhnt.velaecommerce.mapper.Dto2EntityMapper;
import com.linhnt.velaecommerce.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public CommonResponseDto filterProducts(@ModelAttribute FilterProductDto filterDto) {
        ListResponseDto<ProductEntity> response = new ListResponseDto<>();
        response.setItems(productService.filter(filterDto));
        response.setTotal(productService.filterCount(filterDto));
        return CommonResponseDto.builder().data(response).build();
    }

    @GetMapping("/{id}")
    public CommonResponseDto getProductById(@PathVariable("id") Long id) {
        ProductEntity product = productService.findById(id);
        if (product == null) {
            throw new VeNotFoundException(ErrorCode.NOT_FOUND);
        }
        return CommonResponseDto.builder().data(product).build();
    }

    @PostMapping
    public CommonResponseDto createProduct(@RequestBody @Validated CreateProductDto productDto) {
        ProductEntity product = productService.save(Dto2EntityMapper.INSTANCE.createProduct(productDto));
        return CommonResponseDto.builder().data(product).build();
    }

    @PutMapping("/{id}")
    public CommonResponseDto updateProduct(@PathVariable("id") Long id, @RequestBody @Validated UpdateProductDto productDto) {
        ProductEntity product = productService.findById(id);
        if (product == null) {
            throw new VeNotFoundException(ErrorCode.NOT_FOUND);
        }
        product = Dto2EntityMapper.INSTANCE.updateProduct(productDto);
        product.setId(id);
        ProductEntity productUpdated = productService.save(product);
        return CommonResponseDto.builder().data(productUpdated).build();
    }

    @DeleteMapping("/{id}")
    public CommonResponseDto deleteProduct(@PathVariable("id") Long id) {
        productService.softDeleteById(id);
        return CommonResponseDto.builder().build();
    }
}
