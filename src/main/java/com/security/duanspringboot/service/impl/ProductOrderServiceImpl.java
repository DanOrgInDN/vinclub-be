package com.security.duanspringboot.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.duanspringboot.core.response.ErrorData;
import com.security.duanspringboot.core.response.ResponseBody;
import com.security.duanspringboot.dto.request.order.ProductOrderCreateRequest;
import com.security.duanspringboot.dto.request.order.ProductOrderSearchRequest;
import com.security.duanspringboot.dto.request.order.ProductOrderUpdateRequest;
import com.security.duanspringboot.dto.request.order.StartShippingRequest;
import com.security.duanspringboot.dto.request.orderdetail.ProductOrderDetailRequest;
import com.security.duanspringboot.dto.response.order.ProductOrderListResponse;
import com.security.duanspringboot.dto.response.order.ProductOrderResponse;
import com.security.duanspringboot.dto.response.orderdetail.ProductOrderDetailListResponse;
import com.security.duanspringboot.entity.CategoryModel;
import com.security.duanspringboot.entity.ProductCategoryModel;
import com.security.duanspringboot.entity.ProductOrderDetailModel;
import com.security.duanspringboot.entity.ProductOrderModel;
import com.security.duanspringboot.enumeration.TypeStatusOrder;
import com.security.duanspringboot.exception.ServiceSecurityException;
import com.security.duanspringboot.repository.CategoryRepository;
import com.security.duanspringboot.repository.ProductCategoryRepository;
import com.security.duanspringboot.repository.ProductOrderDetailRepository;
import com.security.duanspringboot.repository.ProductOrderRepository;
import com.security.duanspringboot.repository.UsersRepository;
import com.security.duanspringboot.service.ProductOrderService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.security.duanspringboot.core.response.ResponseStatus.*;
import static com.security.duanspringboot.utils.DateTimeUtils.addSevenHours;
import static com.security.duanspringboot.utils.DateTimeUtils.convertToGMTPlus7;

@Service
@RequiredArgsConstructor
public class ProductOrderServiceImpl implements ProductOrderService {

    final private UsersRepository usersRepository;
    final private ProductOrderRepository productOrderRepository;
    final private ProductOrderDetailRepository productOrderDetailRepository;
    final private ProductCategoryRepository productCategoryRepository;
    private final CategoryRepository categoryRepository;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final String MONEY = "MONEY";
    private static final int TRACKING_NUMBER_LENGTH = 12;
    private static final String DEFAULT_SORT_FIELD = "orderDate";


    @Override
    public ResponseBody<Object> getAllProductOrder() {
        List<ProductOrderModel> productsOrderModels = productOrderRepository.findAllByIsDelete(Boolean.FALSE);
        LocalDateTime now = LocalDateTime.now();
        productsOrderModels = productsOrderModels.stream()
                .filter(order -> (order.getStatus().equals(TypeStatusOrder.SHIPPING.toString()) ||
                        (order.getStatus().equals(TypeStatusOrder.PENDING.toString()) &&
                                order.getOrderDate().isAfter(now.minusHours(24)))))
                .collect(Collectors.toList());
        List<ProductOrderListResponse> productOrderListResponse = new ArrayList<>();
        for (ProductOrderModel productOrder : productsOrderModels) {
            productOrderListResponse.add(ProductOrderListResponse.builder()
                    .productOrderId(productOrder.getProductOrderId())
                    .userId(productOrder.getUserId())
                    .fullName(productOrder.getFullName())
                    .email(productOrder.getEmail())
                    .phoneNumber(productOrder.getPhoneNumber())
                    .totalAmount(productOrder.getTotalAmount())
                    .shippingAddress(productOrder.getShippingAddress())
                    .note(productOrder.getNote())
                    .status(productOrder.getStatus())
                    .orderDate(addSevenHours(productOrder.getOrderDate()))
                    .trackingNumber(productOrder.getTrackingNumber())
                    .isDelete(productOrder.getIsDelete())
                    .build());
        }
        productOrderListResponse = productOrderListResponse.stream()
                .sorted(Comparator.comparing(ProductOrderListResponse::getOrderDate).reversed())
                .collect(Collectors.toList());
        var json = new ObjectMapper().createObjectNode();
        json.putPOJO("productOrderListResponse", productOrderListResponse);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);
        return response;
    }

    @Override
    public ResponseBody<Object> createProductOrder(ProductOrderCreateRequest request) {
        if (Objects.nonNull(request.getUserId()) && !request.getUserId().isEmpty()) {
            usersRepository.findById(request.getUserId()).orElseThrow(() -> {
                var errorMapping = ErrorData.builder()
                        .errorKey1(USER_NOT_FOUND.getCode())
                        .build();
                return new ServiceSecurityException(HttpStatus.OK, USER_NOT_FOUND, errorMapping);
            });
        }
        var totalAmount = 0.0;

        var productOrderId = UUID.randomUUID().toString().replaceAll("-", "");
        var productOrder = ProductOrderModel.builder()
                .productOrderId(productOrderId)
                .userId(request.getUserId())
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .totalAmount(totalAmount)
                .shippingAddress(request.getShippingAddress())
                .note(request.getNote())
                .orderDate(LocalDateTime.now())
                .trackingNumber(generateTrackingNumber())
                .status(TypeStatusOrder.PENDING.toString())
                .isDelete(Boolean.FALSE)
                .build();
        productOrderRepository.save(productOrder);

        var json = new ObjectMapper().createObjectNode();
        json.putPOJO("productOrderId", productOrderId);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);
        return response;
    }

    @Override
    public ResponseBody<Object> getProductOrderByIdDetail(String productOrderId) {
        var productsOrderModel = productOrderRepository.findByProductOrderIdAndIsDelete(productOrderId, Boolean.FALSE);
        if (Objects.isNull(productsOrderModel)) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(PRODUCT_ORDER_NOT_FOUND.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, PRODUCT_ORDER_NOT_FOUND, errorMapping);
        }

        ProductOrderResponse productOrderListResponse = getProductOrderResponse(productOrderId, productsOrderModel);

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, productOrderListResponse);
        return response;
    }

    @Override
    public ResponseBody<Object> checkTrackingNumber(String trackingNumber) {
        var productsOrderModel = productOrderRepository.findByTrackingNumberAndIsDelete(trackingNumber, Boolean.FALSE);
        if (Objects.isNull(productsOrderModel)) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(PRODUCT_ORDER_NOT_FOUND.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, PRODUCT_ORDER_NOT_FOUND, errorMapping);
        }
        ProductOrderResponse productOrderListResponse = getProductOrderResponse(productsOrderModel.getProductOrderId(), productsOrderModel);

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, productOrderListResponse);
        return response;
    }

    @Override
    public ResponseBody<Object> updateProductOrderStatus(ProductOrderUpdateRequest request) {
        var productsOrderModel = productOrderRepository.findByProductOrderIdAndIsDelete(request.getProductOrderId(), Boolean.FALSE);
        if (Objects.isNull(productsOrderModel)) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(PRODUCT_ORDER_NOT_FOUND.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, PRODUCT_ORDER_NOT_FOUND, errorMapping);
        }

        if (!CollectionUtils.isEmpty(request.getProductOrderDetails()) && productsOrderModel.getStatus().equalsIgnoreCase(TypeStatusOrder.SHIPPING.toString())) {
            Map<String, Integer> requestChangeMap = request.getProductOrderDetails().stream().collect(Collectors.toMap(ProductOrderDetailRequest::getProductCategoryId, ProductOrderDetailRequest::getQuantity));
            List<ProductOrderDetailModel> pods = productOrderDetailRepository.findAllByProductOrderId(request.getProductOrderId());
            Map<String, Integer> savedMap = pods.stream().collect(Collectors.toMap(ProductOrderDetailModel::getProductCategoryId, ProductOrderDetailModel::getQuantity));
            List<String> productCategoryIds = pods.stream().map(ProductOrderDetailModel::getProductCategoryId).toList();
            List<ProductCategoryModel> pcs = productCategoryRepository.findAllByProductCategoryIdIn(productCategoryIds);
            for (ProductCategoryModel pc : pcs) {
                String productCategoryId = pc.getProductCategoryId();
                if (requestChangeMap.containsKey(productCategoryId) && savedMap.containsKey(productCategoryId)) {
                    int newQuantity = pc.getQuantity() + savedMap.get(productCategoryId) - requestChangeMap.get(productCategoryId);
                    pc.setQuantity(newQuantity);
                }
            }
            productCategoryRepository.saveAll(pcs);
            for (ProductOrderDetailModel pod : pods) {
                if (requestChangeMap.containsKey(pod.getProductCategoryId())) {
                    pod.setQuantity(requestChangeMap.get(pod.getProductCategoryId()));
                }
            }
            productOrderDetailRepository.saveAll(pods);
        } else {
            Map<String, Integer> requestChangeMap = request.getProductOrderDetails().stream().collect(Collectors.toMap(ProductOrderDetailRequest::getProductCategoryId, ProductOrderDetailRequest::getQuantity));
            List<ProductOrderDetailModel> pods = productOrderDetailRepository.findAllByProductOrderId(request.getProductOrderId());

            for (ProductOrderDetailModel pod : pods) {
                if (requestChangeMap.containsKey(pod.getProductCategoryId())) {
                    pod.setQuantity(requestChangeMap.get(pod.getProductCategoryId()));
                }
            }
            productOrderDetailRepository.saveAll(pods);
        }



        productsOrderModel.setStatus(request.getStatus());
        productOrderRepository.save(productsOrderModel);
        var json = new ObjectMapper().createObjectNode();
        json.putPOJO("productOrderId", request.getProductOrderId());
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);
        return response;
    }

    @Override
    public ResponseBody<Object> deleteProductOrderById(String productOrderId) {
        var productsOrderModel = productOrderRepository.findByProductOrderIdAndIsDelete(productOrderId, Boolean.FALSE);
        if (Objects.isNull(productsOrderModel)) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(PRODUCT_ORDER_NOT_FOUND.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, PRODUCT_ORDER_NOT_FOUND, errorMapping);
        }
        productsOrderModel.setIsDelete(Boolean.TRUE);
        productOrderRepository.save(productsOrderModel);
        var json = new ObjectMapper().createObjectNode();
        json.putPOJO("productOrderId", productOrderId);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);
        return response;
    }

    @Override
    public ResponseBody<Object> getAllProductOrderPage(ProductOrderSearchRequest request) {
        var mapper = new ObjectMapper();
        var json = mapper.createObjectNode();

        Pageable pageable;

        if (request.getSortBy() == null || request.getSortBy().isEmpty()) {
            request.setSortBy(DEFAULT_SORT_FIELD);
        }

        if (request.getSortDirection() == null || request.getSortDirection().isEmpty()) {
            request.setSortDirection("asc");
        }

        if (request.getSortDirection().equalsIgnoreCase("desc")) {
            pageable = PageRequest.of(Integer.parseInt(request.getPageNumber()) - 1, Integer.parseInt(request.getPageSize()), Sort.by(request.getSortBy()).descending());
        } else {
            pageable = PageRequest.of(Integer.parseInt(request.getPageNumber()) - 1, Integer.parseInt(request.getPageSize()), Sort.by(request.getSortBy()).ascending());
        }

        Page<ProductOrderModel> productsOrderModels = productOrderRepository.findByProductOrderIdAndStatusAndIsDelete(request.getProductOrderId(), request.getStatus(), Boolean.FALSE, pageable);
        var listProductOrderDetail = productsOrderModels.getContent();

        var productOrderDetailListResponse = new ArrayList<ProductOrderResponse>();
        listProductOrderDetail.forEach(productOrder -> productOrderDetailListResponse.add(this.getProductOrderResponse(productOrder.getProductOrderId(), productOrder)));

        json.putPOJO("page_number", request.getPageNumber());
        json.putPOJO("total_records", productsOrderModels.getTotalElements());
        json.putPOJO("page_size", request.getPageSize());
        json.putPOJO("list_product_order", productOrderDetailListResponse);

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);

        return response;
    }

    @Override
    public ResponseBody<Object> getProductOrderStatusByIdDetail(String productOrderId) {
        var productsOrderModel = productOrderRepository.findByProductOrderIdAndIsDelete(productOrderId, Boolean.FALSE);
        if (Objects.isNull(productsOrderModel)) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(PRODUCT_ORDER_NOT_FOUND.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, PRODUCT_ORDER_NOT_FOUND, errorMapping);
        }

        ProductOrderListResponse productOrderListResponse = ProductOrderListResponse.builder()
                .productOrderId(productsOrderModel.getProductOrderId())
                .userId(productsOrderModel.getUserId())
                .fullName(productsOrderModel.getFullName())
                .email(productsOrderModel.getEmail())
                .phoneNumber(productsOrderModel.getPhoneNumber())
                .totalAmount(productsOrderModel.getTotalAmount())
                .shippingAddress(productsOrderModel.getShippingAddress())
                .note(productsOrderModel.getNote())
                .status(productsOrderModel.getStatus())
                .orderDate(productsOrderModel.getOrderDate())
                .isDelete(productsOrderModel.getIsDelete())
                .build();

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, productOrderListResponse);
        return response;
    }

    @Override
    public ResponseBody<Object> startShipping(StartShippingRequest request) {
        List<ProductCategoryModel> pcList = new ArrayList<>();
        String productOrderId = "";
        if (!CollectionUtils.isEmpty(request.getDetailRequests())) {
            productOrderId = request.getDetailRequests().get(0).getProductOrderId();
        }
        var productsOrderModel = productOrderRepository.findByProductOrderIdAndIsDelete(productOrderId, Boolean.FALSE);
        if (Objects.isNull(productsOrderModel)) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(PRODUCT_ORDER_NOT_FOUND.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, PRODUCT_ORDER_NOT_FOUND, errorMapping);
        }
        for (ProductOrderDetailRequest data : request.getDetailRequests()) {
            var pc = productCategoryRepository.findById(data.getProductCategoryId()).orElseThrow(() -> {
                var errorMapping = ErrorData.builder()
                        .errorKey1(PRODUCT_CATEGORY_NOT_FOUND.getCode())
                        .build();
                return new ServiceSecurityException(HttpStatus.OK, PRODUCT_CATEGORY_NOT_FOUND, errorMapping);
            });
            pc.setQuantity(pc.getQuantity() - data.getQuantity());
            pcList.add(pc);
        }

        productCategoryRepository.saveAll(pcList);
        productsOrderModel.setStatus(TypeStatusOrder.SHIPPING.toString());
        productOrderRepository.save(productsOrderModel);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, "Xuất kho thành công!");
        return response;
    }

    private ProductOrderResponse getProductOrderResponse(String productOrderId, ProductOrderModel productOrderModel) {
        var orderDetailModels = productOrderDetailRepository.findAllByProductOrderId(productOrderId);
        if (orderDetailModels.isEmpty()) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(PRODUCT_ORDER_DETAIL_NOT_FOUND.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, PRODUCT_ORDER_DETAIL_NOT_FOUND, errorMapping);
        }

        List<String> productCategoryIds = orderDetailModels.stream().map(ProductOrderDetailModel::getProductCategoryId).toList();
        List<ProductCategoryModel> pods = productCategoryRepository.findAllByProductCategoryIdIn(productCategoryIds);
        Map<String, Integer> stockMap = pods.stream().collect(Collectors.toMap(ProductCategoryModel::getProductCategoryId, ProductCategoryModel::getQuantity));
        Map<String, String> categoryMap = pods.stream().collect(Collectors.toMap(ProductCategoryModel::getProductCategoryId, ProductCategoryModel::getCategoryId));
        List<String> categoryIds = pods.stream().map(ProductCategoryModel::getCategoryId).toList();
        List<CategoryModel> categories = categoryRepository.findAllByCategoryIdIn(categoryIds);
        Map<String, String> categoryNameMap = categories.stream().collect(Collectors.toMap(CategoryModel::getCategoryId, CategoryModel::getCategoryName));

        List<ProductOrderDetailListResponse> productOrderDetailListResponses = new ArrayList<>();
        for(ProductOrderDetailModel productOrderDetail : orderDetailModels) {
            productOrderDetailListResponses.add(ProductOrderDetailListResponse.builder()
                    .productOrderId(productOrderId)
                    .productCategoryId(productOrderDetail.getProductCategoryId())
                    .productName(productOrderDetail.getProductName())
                    .quantity(productOrderDetail.getQuantity())
                    .price(productOrderDetail.getPrice())
                    .subtotal(productOrderDetail.getSubtotal())
                    .createDate(productOrderDetail.getCreateDate())
                    .stock(stockMap.get(productOrderDetail.getProductCategoryId() == null ? "" : productOrderDetail.getProductCategoryId()))
                    .categoryName(categoryNameMap.get(categoryMap.get(productOrderDetail.getProductCategoryId()) == null ? "" : categoryMap.get(productOrderDetail.getProductCategoryId())))
                    .build());
        }
        productOrderDetailListResponses = productOrderDetailListResponses.stream()
                .sorted(Comparator.comparing(ProductOrderDetailListResponse::getCategoryName)
                        .thenComparing(ProductOrderDetailListResponse::getProductName))
                .collect(Collectors.toList());
        return ProductOrderResponse.builder()
                .productOrderId(productOrderModel.getProductOrderId())
                .userId(productOrderModel.getUserId())
                .fullName(productOrderModel.getFullName())
                .email(productOrderModel.getEmail())
                .phoneNumber(productOrderModel.getPhoneNumber())
                .totalAmount(productOrderModel.getTotalAmount())
                .shippingAddress(productOrderModel.getShippingAddress())
                .note(productOrderModel.getNote())
                .status(productOrderModel.getStatus())
                .orderDate(productOrderModel.getOrderDate())
                .isDelete(productOrderModel.getIsDelete())
                .productOrderDetailListResponses(productOrderDetailListResponses)
                .build();
    }

    public static String generateTrackingNumber() {
        StringBuilder trackingNumber = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < TRACKING_NUMBER_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            trackingNumber.append(CHARACTERS.charAt(index));
        }
        return trackingNumber.toString();
    }
}
