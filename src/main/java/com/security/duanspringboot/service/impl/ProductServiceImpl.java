package com.security.duanspringboot.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.security.duanspringboot.core.response.ErrorData;
import com.security.duanspringboot.core.response.ResponseBody;
import com.security.duanspringboot.dto.request.products.ProductCreateRequest;
import com.security.duanspringboot.dto.request.products.ProductUpdateRequest;
import com.security.duanspringboot.dto.response.categories.CategoryListDetailResponse;
import com.security.duanspringboot.dto.response.product.ProductDetailListResponse;
import com.security.duanspringboot.entity.CategoryModel;
import com.security.duanspringboot.entity.ProductCategoryModel;
import com.security.duanspringboot.entity.ProductModel;
import com.security.duanspringboot.exception.ServiceSecurityException;
import com.security.duanspringboot.repository.CategoryRepository;
import com.security.duanspringboot.repository.ProductCategoryRepository;
import com.security.duanspringboot.repository.ProductRepository;
import com.security.duanspringboot.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.security.duanspringboot.core.response.ResponseStatus.*;
import static com.security.duanspringboot.utils.DateTimeUtils.convertToGMTPlus7;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private static final String DEFAULT_SORT_FIELD = "createDate";


    @Override
    public ResponseBody<Object> getAllProductDetail() {
        var productsModels = productRepository.findAll();
        var productDetailListResponse = new ArrayList<ProductDetailListResponse>();
        productsModels.forEach(product -> {
            List<String> keywords = new ArrayList<>();
        for (String item : product.getKeyword().split("\n")) {
            keywords.add(item.trim());
        }
            productDetailListResponse.add(ProductDetailListResponse.builder()
                    .productId(product.getProductId())
                    .productName(product.getProductName())
                    .createDate(convertToGMTPlus7(product.getCreateDate()))
                    .modifyDate(convertToGMTPlus7(product.getModifyDate()))
                    .keywords(keywords)
                    .build());
        });


        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, productDetailListResponse);
        return response;
    }

    @Override
    @Transactional
    public ResponseBody<Object> createProduct(ProductCreateRequest request) {
        var existsProductName = productRepository.existsByProductName(request.getProductName());

        if (existsProductName) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(PRODUCT_NAME_EXIST.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, PRODUCT_NAME_EXIST, errorMapping);
        }

        String productId = UUID.randomUUID().toString().replaceAll("-", "");

        var productsModel = ProductModel.builder()
                .productId(productId)
                .productName(request.getProductName())
                .keyword(request.getKeywords() != null ? String.join("\n", request.getKeywords()) : "")
                .genericName(request.getGenericName() != null ? String.join("\n", request.getGenericName()) : "")
                .createDate(LocalDateTime.now())
                .build();

        productRepository.save(productsModel);

        var json = new ObjectMapper().createObjectNode();
        json.putPOJO("productId", productId);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);
        return response;
    }

    @Transactional
    @Override
    public ResponseBody<Object> updateProduct(ProductUpdateRequest request) {
        var productsModel = productRepository.findById(request.getProductId()).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(PRODUCT_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, PRODUCT_NOT_FOUND, errorMapping);
        });

        if (Objects.nonNull(request.getProductName())) {
            this.validateProductName(request.getProductName(), productsModel.getProductName());
        }

        String keyword = request.getKeywords() != null ? String.join("\n", request.getKeywords()) : "";
        String genericName = request.getGenericName() != null ? String.join("\n", request.getGenericName()) : "";
        productsModel.setProductName(request.getProductName());
        productsModel.setModifyDate(LocalDateTime.now());
        productsModel.setKeyword(keyword);
        productsModel.setGenericName(genericName);
        productRepository.save(productsModel);

        var json = new ObjectMapper().createObjectNode();
        json.putPOJO("productId", productsModel.getProductId());

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);
        return response;
    }

    @Transactional
    @Override
    public ResponseBody<Object> deleteProductById(String productId) {
        productRepository.findById(productId).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(PRODUCT_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, PRODUCT_NOT_FOUND, errorMapping);
        });
        productCategoryRepository.deleteAllByProductId(productId);
        productRepository.deleteById(productId);

        var json = new ObjectMapper().createObjectNode();
        json.putPOJO("productId", productId);

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);
        return response;
    }

    @Override
    public ResponseBody<Object> importExcel(MultipartFile file) {

        Map<String, String> resultMap = new HashMap<>();

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                if (nextLine.length >= 2) {
                    String key = nextLine[0];
                    String value = nextLine[1].trim();

                    resultMap.put(key, value);
                }
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }

        List<ProductModel> products = new ArrayList<>();
        for (String key : resultMap.keySet()) {
            ProductModel product = ProductModel.builder()
                    .productId(UUID.randomUUID().toString().replaceAll("-", ""))
                    .productName(key).keyword(resultMap.get(key))
                    .createDate(LocalDateTime.now())
                    .modifyDate(LocalDateTime.now())
                    .build();
            products.add(product);
        }

        productRepository.saveAll(products);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, "Import thành công!");
        return response;

    }
//    @Override
//    public ResponseBody<Object> importExcel1(MultipartFile file) {
//        Map<String, List<String>> resultMap = new HashMap<>();
//
//        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
//            String[] nextLine;
//            while ((nextLine = csvReader.readNext()) != null) {
//                if (nextLine.length >= 2) {
//                    String key = nextLine[0];
//                    String value = nextLine[1];
//
//                    List<String> valuesList = new ArrayList<>();
//                    for (String item : value.split("\n")) {
//                        valuesList.add(item.trim());
//                    }
//
//                    resultMap.put(key, valuesList);
//                }
//            }
//
//
//    } catch (IOException | CsvValidationException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println(resultMap);
//        var response = new ResponseBody<>();
//        response.setOperationSuccess(SUCCESS, resultMap);
//        return response;
//
//    }

    private void validateProductName(String productName, String productNamePresent) {
        var existsProductName = productRepository.existsByProductName(productName);
        if (!Objects.equals(productName, productNamePresent) && existsProductName) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(PRODUCT_NAME_EXIST.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, PRODUCT_NAME_EXIST, errorMapping);
        }
    }
}
