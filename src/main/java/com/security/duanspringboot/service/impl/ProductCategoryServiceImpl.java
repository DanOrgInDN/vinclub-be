package com.security.duanspringboot.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.security.duanspringboot.core.response.ErrorData;
import com.security.duanspringboot.core.response.ResponseBody;
import com.security.duanspringboot.dto.request.ProductCategoryRequest;
import com.security.duanspringboot.dto.request.productcategory.ProductCategoryImportRequest;
import com.security.duanspringboot.dto.response.ProductCategoryResponse;
import com.security.duanspringboot.dto.response.productcategory.ProductCategoryImportDataResponse;
import com.security.duanspringboot.dto.response.productcategory.VerifyProductCategoryImportResponse;
import com.security.duanspringboot.dto.response.productcategory.VerifyProductDto;
import com.security.duanspringboot.entity.CategoryModel;
import com.security.duanspringboot.entity.ProductCategoryModel;
import com.security.duanspringboot.entity.ProductModel;
import com.security.duanspringboot.exception.ServiceSecurityException;
import com.security.duanspringboot.repository.CategoryRepository;
import com.security.duanspringboot.repository.ProductCategoryRepository;
import com.security.duanspringboot.repository.ProductRepository;
import com.security.duanspringboot.service.ProductCategoryService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.security.duanspringboot.core.response.ResponseStatus.*;
import static com.security.duanspringboot.utils.DateTimeUtils.convertToGMTPlus7;

@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductCategoryServiceImpl(ProductCategoryRepository productCategoryRepository, ProductRepository productRepository,
                                      CategoryRepository categoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ResponseBody<Object> getAllProductCategory() {
        List<ProductCategoryResponse> responses = productCategoryRepository.findAllIncludeProductName();
        responses.forEach(response -> {
            response.setCreateDate(convertToGMTPlus7(response.getCreateDate()));
            response.setModifyDate(convertToGMTPlus7(response.getModifyDate()));
        });
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, responses);
        return response;
    }

    @Override
    public ResponseBody<Object> createProductCategory(ProductCategoryRequest request) {
        var productCategory = productCategoryRepository.existsByProductIdAndCategoryId(request.getProductId(), request.getCategoryId());
        if (productCategory) {
            var errorMapping = ErrorData.builder()
                    .errorKey1(CATEGORY_NAME_EXIST.getCode())
                    .build();
            throw new ServiceSecurityException(HttpStatus.OK, CATEGORY_NAME_EXIST, errorMapping);
        }

        String id = UUID.randomUUID().toString().replaceAll("-", "");
        var newProductCategory = ProductCategoryModel.builder()
                .productCategoryId(id)
                .categoryId(request.getCategoryId())
                .productId(request.getProductId())
                .maxLimit(request.getMaxLimit())
                .minLimit(request.getMinLimit())
                .quantity(request.getQuantity())
                .price(request.getPrice())
                .createDate(LocalDateTime.now())
                .build();
        productCategoryRepository.save(newProductCategory);

        var json = new ObjectMapper().createObjectNode();
        json.putPOJO("productCategoryId", newProductCategory);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);
        return response;
    }

    @Override
    public ResponseBody<Object> updateProductCategory(ProductCategoryRequest request) {
        var pcModel = productCategoryRepository.findById(request.getProductCategoryId()).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(PRODUCT_CATEGORY_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, PRODUCT_CATEGORY_NOT_FOUND, errorMapping);
        });

        pcModel.setQuantity(request.getQuantity());
        pcModel.setMinLimit(request.getMinLimit());
        pcModel.setMaxLimit(request.getMaxLimit());
        pcModel.setModifyDate(LocalDateTime.now());
        productCategoryRepository.save(pcModel);

        var json = new ObjectMapper().createObjectNode();
        json.putPOJO("productCategoryId", request.getCategoryId());
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);
        return response;
    }

    @Override
    public ResponseBody<Object> deleteProductCategoryById(String id) {
        var pcModel = productCategoryRepository.findById(id).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(PRODUCT_CATEGORY_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, PRODUCT_CATEGORY_NOT_FOUND, errorMapping);
        });
        productCategoryRepository.delete(pcModel);

        var json = new ObjectMapper().createObjectNode();
        json.putPOJO("productCategoryId", id);
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, json);
        return response;
    }

    @Override
    public ResponseBody<Object> getAllProductCategoryByCategoryId(String categoryId, String type) {
        var categoriesModel = categoryRepository.findById(categoryId).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(CATEGORY_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, CATEGORY_NOT_FOUND, errorMapping);
        });
        List<ProductCategoryResponse> responses = productCategoryRepository.findAllIncludeProductNameByCategoryId(categoryId);
        List<String> productIds = responses.stream().map(ProductCategoryResponse::getProductId).toList();
        List<ProductModel> products = productRepository.findByProductIdIn(productIds);
        Map<String, List<String>> keywordMap = new HashMap<>();
        Map<String, List<String>> genericNameMap = new HashMap<>();
        products.forEach(product -> {
            List<String> keywords = new ArrayList<>();
            if(product.getKeyword() != null && !product.getKeyword().isEmpty()) {
                for (String item : product.getKeyword().split("\n")) {
                    keywords.add(item.trim());
                }
                keywordMap.put(product.getProductId(), keywords);
            }
            List<String> genericName = new ArrayList<>();
            if(product.getGenericName() != null && !product.getGenericName().isEmpty()) {
                for (String item : product.getGenericName().split("\n")) {
                    genericName.add(item.trim());
                }
                genericNameMap.put(product.getProductId(), genericName);
            }
        });
        responses.forEach(response -> {
            response.setCreateDate(convertToGMTPlus7(response.getCreateDate()));
            response.setModifyDate(convertToGMTPlus7(response.getModifyDate()));
            response.setKeywords(keywordMap.get(response.getProductId()) == null ? new ArrayList<>() : keywordMap.get(response.getProductId()));
            response.setGenericName(genericNameMap.get(response.getProductId()) == null ? new ArrayList<>() : genericNameMap.get(response.getProductId()));
        });
        responses = responses.stream()
                .sorted(Comparator.comparing(ProductCategoryResponse::getProductName))
                .collect(Collectors.toList());
        if (!type.equalsIgnoreCase("admin")) {
            responses = responses.stream().filter(product -> product.getQuantity() > product.getMinLimit())
                    .collect(Collectors.toList());
        }
        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, responses);
        return response;
    }

    @Override
    public ResponseBody<Object> importExcel(ProductCategoryImportRequest request) {
        String categoryId = request.getCategoryId();
        categoryRepository.findById(categoryId).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(CATEGORY_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, CATEGORY_NOT_FOUND, errorMapping);
        });
        Map<String, String> resultMap = new HashMap<>();
        for (ProductCategoryImportDataResponse data : request.getImportData()) {
            resultMap.put(data.getSystemName(), String.valueOf(data.getQuantity()));
        }
        List<String> productNames = new ArrayList<>(resultMap.keySet());
        List<ProductModel> existProducts = productRepository.findByProductNameIn(productNames);

        List<String> existProductIds = existProducts.stream().map(ProductModel::getProductId).toList();
        List<ProductCategoryModel> productCategories = productCategoryRepository.findByProductIdIn(existProductIds, categoryId);
        Map<String, ProductCategoryModel> productCategoryMap = productCategories.stream()
                .collect(Collectors.toMap(ProductCategoryModel::getProductId, e -> e));

        List<ProductCategoryModel> productCategoryList = new ArrayList<>();
        for (ProductModel product : existProducts) {
            if (resultMap.containsKey(product.getProductName())) {
                ProductCategoryModel pc = new ProductCategoryModel();
                if (productCategoryMap.containsKey(product.getProductId())) {
                    pc = productCategoryMap.get(product.getProductId());
                    double quantity = Double.parseDouble(resultMap.get(product.getProductName()));
                    pc.setQuantity(pc.getQuantity() + (int) quantity);
                } else {
                    pc.setProductCategoryId(UUID.randomUUID().toString().replaceAll("-", ""));
                    pc.setCategoryId(categoryId);
                    pc.setProductId(product.getProductId());
                    double quantity = Double.parseDouble(resultMap.get(product.getProductName()));
                    pc.setQuantity((int) quantity);
                    pc.setMaxLimit(0);
                    pc.setMinLimit(0);
                    pc.setPrice(0.0);
                    pc.setCreateDate(LocalDateTime.now());
                    pc.setModifyDate(LocalDateTime.now());
                }
                productCategoryList.add(pc);
            }
        }
        productCategoryRepository.saveAll(productCategoryList);

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, "Import Successfully!");
        return response;
    }

    @Override
    public ResponseBody<Object> verifyImportProducts(String categoryId, MultipartFile file) {
        CategoryModel category = categoryRepository.findById(categoryId).orElseThrow(() -> {
            var errorMapping = ErrorData.builder()
                    .errorKey1(CATEGORY_NOT_FOUND.getCode())
                    .build();
            return new ServiceSecurityException(HttpStatus.OK, CATEGORY_NOT_FOUND, errorMapping);
        });

        Map<String, String> resultMap = getImportFileData(file);
        List<ProductModel> products = productRepository.findAll();
        Map<String, VerifyProductDto> productVerifyMap = new HashMap<>();
        for (ProductModel product : products) {
            List<String> keywords = new ArrayList<>();
            for (String item : product.getKeyword().split("\n")) {
                keywords.add(item.trim());
            }
            keywords.add(product.getProductName());
            productVerifyMap.put(product.getProductName(), VerifyProductDto.builder()
                    .productId(product.getProductId())
                    .productName(product.getProductName())
                    .keyword(keywords)
                    .build());
        }
        List<ProductCategoryImportDataResponse> importDataResponses = new ArrayList<>();
        List<String> productNames = new ArrayList<>(resultMap.keySet());
        List<VerifyProductDto> verifyProductList = new ArrayList<>(productVerifyMap.values());

        for (String productName : productNames) {
            ProductCategoryImportDataResponse importDataResponse = new ProductCategoryImportDataResponse();
            importDataResponse.setImportName(productName);
            double quantity = Double.parseDouble(resultMap.get(productName));
            importDataResponse.setQuantity((int) quantity);

            if (productVerifyMap.containsKey(productName)) {
                VerifyProductDto verifyProduct = productVerifyMap.get(productName);
                importDataResponse.setSystemName(verifyProduct.getProductName());
            } else {
                Optional<VerifyProductDto> matchedProduct = verifyProductList.parallelStream()
                        .filter(verifyProduct -> verifyProduct.getKeyword().contains(productName))
                        .findFirst();
                if (matchedProduct.isPresent()) {
                    importDataResponse.setSystemName(matchedProduct.get().getProductName());
                } else {
                    importDataResponse.setSystemName("");
                }
            }
            importDataResponses.add(importDataResponse);
        }
        importDataResponses = importDataResponses.stream()
                .sorted(Comparator.comparing(ProductCategoryImportDataResponse::getImportName))
                .collect(Collectors.toList());
        VerifyProductCategoryImportResponse verifyProductCategoryImportResponse = VerifyProductCategoryImportResponse.builder()
                .categoryId(categoryId)
                .categoryName(category.getCategoryName())
                .importData(importDataResponses)
                .build();

        var response = new ResponseBody<>();
        response.setOperationSuccess(SUCCESS, verifyProductCategoryImportResponse);
        return response;
    }

    private Map<String, String> getImportFileData(MultipartFile file) {
        Map<String, String> resultMap = new HashMap<>();
        String fileExtension = getFileExtension(file.getOriginalFilename());
        try (InputStream inputStream = file.getInputStream()) {
            if (fileExtension.equalsIgnoreCase("csv")) {
                try (CSVReader csvReader = new CSVReader(new InputStreamReader(inputStream))) {
                    String[] nextLine;
                    while ((nextLine = csvReader.readNext()) != null) {
                        if (nextLine.length >= 2) {
                            String key = nextLine[0];
                            String value = nextLine[1].trim();
                            resultMap.put(key, value);
                        }
                    }
                }
            } else if (fileExtension.equalsIgnoreCase("xlsx") || fileExtension.equalsIgnoreCase("xls")) {
                try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
                    Sheet sheet = workbook.getSheetAt(0);
                    for (Row row : sheet) {
                        Cell keyCell = row.getCell(0);
                        Cell valueCell = row.getCell(1);
                        if (keyCell != null && valueCell != null) {
                            String key = keyCell.getStringCellValue();
                            String value = String.valueOf(valueCell.getNumericCellValue()).trim();
                            resultMap.put(key, value);
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                throw new IllegalArgumentException("Không hỗ trợ định dạng file này: " + fileExtension);
            }
        } catch (IOException | CsvValidationException e) {
            throw new RuntimeException(e);
        }
        return resultMap;
    }

    private String getFileExtension(String filename) {
        if (filename == null || !filename.contains(".")) return "";
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
