package com.security.duanspringboot.service.report;

import com.security.duanspringboot.entity.CategoryModel;
import com.security.duanspringboot.entity.ProductCategoryModel;
import com.security.duanspringboot.entity.ProductOrderDetailModel;
import com.security.duanspringboot.entity.ProductOrderModel;
import com.security.duanspringboot.enumeration.TypeStatusOrder;
import com.security.duanspringboot.repository.*;
import com.security.duanspringboot.service.report.data.RestockProductCategoryReportData;
import com.security.duanspringboot.utils.ReportUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RestockProductCategoryReport {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductOrderRepository productOrderRepository;
    private final ProductOrderDetailRepository productOrderDetailRepository;

    public RestockProductCategoryReport(ProductRepository productRepository, CategoryRepository categoryRepository, ProductCategoryRepository productCategoryRepository, ProductOrderRepository productOrderRepository, ProductOrderDetailRepository productOrderDetailRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productOrderRepository = productOrderRepository;
        this.productOrderDetailRepository = productOrderDetailRepository;
    }

    private final List<String> RestockProductCategoryReportColumns = Arrays.asList(
            "Tên sản phẩm", "Loại hàng", "Tồn kho", "Số lượng cần nhập"
    );

    public Map<String, String> generateReport(String startDateStr, String categoryId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime startDate = LocalDate.parse(startDateStr, formatter).atStartOfDay();
        LocalDateTime endDate = LocalDateTime.now();
        List<CategoryModel> categories = categoryRepository.findAll();
        List<ProductOrderModel> productOrders = productOrderRepository.findByOrderDateBetweenAndStatus(startDate, endDate, TypeStatusOrder.SHIPPING.toString());
        List<String> productOrderIds = productOrders.stream().map(ProductOrderModel::getProductOrderId).toList();
        List<ProductOrderDetailModel> productOrderDetails = new ArrayList<>();
        if(categoryId.equals("all")) {
            productOrderDetails = productOrderDetailRepository.findAllByProductOrderIdIn(productOrderIds);
        } else {
            productOrderDetails = productOrderDetailRepository.findAllByProductOrderIdInAAndCategoryId(productOrderIds, categoryId);
        }
        List<ProductCategoryModel> poList = productCategoryRepository.findAll();
        return createContent(categories, poList, productOrderDetails);
    }

    private Map<String, String> createContent(List<CategoryModel> categories,
                                              List<ProductCategoryModel> poList,
                                              List<ProductOrderDetailModel> productOrderDetails) {
        Map<String, String> content = new HashMap<>();
        Map<String, CategoryModel> categoryMap = categories.stream().collect(Collectors.toMap(CategoryModel::getCategoryId, c -> c));
        Map<String, ProductOrderDetailModel> sellingMap = productOrderDetails.stream()
                .collect(Collectors.toMap(
                        detail -> detail.getProductId() + "+" + detail.getProductCategoryId(),
                        detail -> {
                            ProductOrderDetailModel model = new ProductOrderDetailModel();
                            model.setProductOrderId(detail.getProductOrderId());
                            model.setProductCategoryId(detail.getProductCategoryId());
                            model.setProductName(detail.getProductName());
                            model.setQuantity(detail.getQuantity());
                            model.setPrice(detail.getPrice());
                            model.setSubtotal(detail.getSubtotal());
                            model.setCreateDate(detail.getCreateDate());
                            return model;
                        },
                        (existing, replacement) -> {
                            existing.setQuantity(existing.getQuantity() + replacement.getQuantity());
                            return existing;
                        }
                ));
        Map<String, ProductCategoryModel> poMap = poList.stream().collect(Collectors.toMap(ProductCategoryModel::getProductCategoryId, po -> po));
        String reportName = String.format("%s.csv", "Nhap bu hang");
        String reportContent = generateReportContent(reportName, categoryMap, poMap, sellingMap);
        content.put(reportName, reportContent);
        return content;
    }

    private String generateReportContent(String reportName,
                                         Map<String, CategoryModel> categoryMap,
                                         Map<String, ProductCategoryModel> poMap,
                                         Map<String, ProductOrderDetailModel> sellingMap) {
        if (sellingMap.isEmpty()) {
            return ReportUtils.NO_DATA_FOUND;
        }

        StringBuffer result = new StringBuffer();
        result.append(ReportUtils.generateReportHeader(reportName, RestockProductCategoryReportColumns));
        List<RestockProductCategoryReportData> dataRows = generateReportDataRows(categoryMap, poMap, sellingMap)
                .stream()
                .filter(data -> {
                    try {
                        return Integer.parseInt(data.getQuantityToOrder()) > 0;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .sorted(Comparator.comparing(RestockProductCategoryReportData::getQuantityToOrder))
                .toList();
        dataRows = dataRows.stream()
                .sorted((p1, p2) -> Integer.compare(Integer.parseInt(p2.getQuantityToOrder()), Integer.parseInt(p1.getQuantityToOrder())))
                .collect(Collectors.toList());
        dataRows.forEach(row -> result.append(row.toRow()));
        log.info("HotelListReport - data rows {}", dataRows.size());
        return result.toString();
    }

    private List<RestockProductCategoryReportData> generateReportDataRows(Map<String, CategoryModel> categoryMap,
                                                                     Map<String, ProductCategoryModel> poMap,
                                                                     Map<String, ProductOrderDetailModel> sellingMap) {
        List<RestockProductCategoryReportData> dataRows = new ArrayList<>();
        for (Map.Entry<String, ProductOrderDetailModel> entry : sellingMap.entrySet()) {
            String[] keys = entry.getKey().split("\\+");
            String productOrderId = keys[0];
            String productCategoryId = keys[1];
            ProductOrderDetailModel pod = entry.getValue();
            RestockProductCategoryReportData protoRow = new RestockProductCategoryReportData();
            protoRow.setProductName(pod.getProductName());
            protoRow.setProductCategory(categoryMap.containsKey(poMap.get(productCategoryId).getCategoryId()) ? categoryMap.get(poMap.get(productCategoryId).getCategoryId()).getCategoryName() : "");
            protoRow.setInStock(poMap.containsKey(productCategoryId) ? String.valueOf(poMap.get(productCategoryId).getQuantity()) : "");
            protoRow.setQuantityToOrder(String.valueOf(poMap.get(productCategoryId).getMaxLimit() - poMap.get(productCategoryId).getQuantity()));
            dataRows.add(protoRow);
        }
        return dataRows;
    }
}
