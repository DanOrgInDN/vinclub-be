package com.security.duanspringboot.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.duanspringboot.core.response.ErrorData;
import com.security.duanspringboot.core.response.ResponseBody;
import com.security.duanspringboot.dto.request.orderdetail.ProductOrderDetailCreateRequest;
import com.security.duanspringboot.dto.request.orderdetail.ProductOrderDetailRequest;
import com.security.duanspringboot.dto.response.orderdetail.ProductOrderDetailListResponse;
import com.security.duanspringboot.entity.CategoryModel;
import com.security.duanspringboot.entity.ProductCategoryModel;
import com.security.duanspringboot.entity.ProductOrderDetailModel;
import com.security.duanspringboot.exception.ServiceSecurityException;
import com.security.duanspringboot.repository.*;
import com.security.duanspringboot.service.ProductOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.security.duanspringboot.core.response.ResponseStatus.*;
import static com.security.duanspringboot.utils.DateTimeUtils.convertToGMTPlus7;

@Service
@RequiredArgsConstructor
public class ProductOrderDetailServiceImpl implements ProductOrderDetailService {

    final private ProductOrderDetailRepository productOrderDetailRepository;
    final private ProductOrderRepository productOrderRepository;
    final private ProductRepository productRepository;
    final private ProductCategoryRepository productCategoryRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public ResponseBody<Object> getProductOrderDetail(String productOrderId) {
        List<ProductOrderDetailModel> orderDetailModels = productOrderDetailRepository.findAllByProductOrderId(productOrderId);
        if (orderDetailModels.isEmpty()) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(PRODUCT_ORDER_DETAIL_NOT_FOUND.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, PRODUCT_ORDER_DETAIL_NOT_FOUND, errorMapping);
        }

        List<String> productCategoryIds = orderDetailModels.stream().map(ProductOrderDetailModel::getProductCategoryId).toList();
        List<ProductCategoryModel> pods = productCategoryRepository.findAllByProductCategoryIdIn(productCategoryIds);
        Map<String, Integer> stockMap = pods.stream().collect(Collectors.toMap(ProductCategoryModel::getProductCategoryId, ProductCategoryModel::getQuantity));
        List<String> categoryIds = pods.stream().map(ProductCategoryModel::getCategoryId).toList();
        List<CategoryModel> categories = categoryRepository.findAllByCategoryIdIn(categoryIds);
        Map<String, String> categoryNameMap = categories.stream().collect(Collectors.toMap(CategoryModel::getCategoryId, CategoryModel::getCategoryName));
        List<ProductOrderDetailListResponse> productOrderDetailListResponses = new ArrayList<>();
        orderDetailModels.forEach(productOrderDetail -> productOrderDetailListResponses.add(ProductOrderDetailListResponse.builder()
                .productOrderId(productOrderId)
                .productCategoryId(productOrderDetail.getProductCategoryId())
                .productName(productOrderDetail.getProductName())
                .quantity(productOrderDetail.getQuantity())
                .price(productOrderDetail.getPrice())
                .subtotal(productOrderDetail.getSubtotal())
                .createDate(convertToGMTPlus7(productOrderDetail.getCreateDate()))
                .stock(stockMap.get(productOrderDetail.getProductCategoryId() == null ? "" : productOrderDetail.getProductCategoryId()))
                        .categoryName(categoryNameMap.get(productOrderDetail.getProductCategoryId() == null ? "" : productOrderDetail.getProductCategoryId()))
                .build()));

        var json = new ObjectMapper().createObjectNode();
        json.putPOJO("productOrderDetailListResponses", productOrderDetailListResponses);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);
        return response;
    }

    @Override
    public ResponseBody<Object> createProductOrderDetail(ProductOrderDetailCreateRequest request) {
        List<ProductOrderDetailModel> pods = new ArrayList<>();
        for (ProductOrderDetailRequest data : request.getDetailRequests()) {
            var productsOrderModel = productOrderRepository.findByProductOrderIdAndIsDelete(data.getProductOrderId(), Boolean.FALSE);
            if (Objects.isNull(productsOrderModel)) {
                var errorMapping = ErrorData.builder()
                        .errorKey1(PRODUCT_ORDER_NOT_FOUND.getCode())
                        .build();
                throw new ServiceSecurityException(HttpStatus.OK, PRODUCT_ORDER_NOT_FOUND, errorMapping);
            }

            var pc = productCategoryRepository.findById(data.getProductCategoryId()).orElseThrow(() -> {
                var errorMapping = ErrorData.builder()
                        .errorKey1(PRODUCT_CATEGORY_NOT_FOUND.getCode())
                        .build();
                return new ServiceSecurityException(HttpStatus.OK, PRODUCT_CATEGORY_NOT_FOUND, errorMapping);
            });
            String productOrderDetailId = UUID.randomUUID().toString().replaceAll("-", "");
            ProductOrderDetailModel productOrderDetailModel = ProductOrderDetailModel.builder()
                    .productOrderDetailId(productOrderDetailId)
                    .productId(pc.getProductId())
                    .categoryId(pc.getCategoryId())
                    .productOrderId(data.getProductOrderId())
                    .productCategoryId(data.getProductCategoryId())
                    .productName(data.getProductName())
                    .quantity(data.getQuantity())
                    .price(data.getPrice())
                    .subtotal(data.getSubtotal())
                    .createDate(LocalDateTime.now())
                    .build();
            pods.add(productOrderDetailModel);
        }

        productOrderDetailRepository.saveAll(pods);

        var json = new ObjectMapper().createObjectNode();
        json.putPOJO("productOrderDetailList", pods);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);
        return response;
    }
}
