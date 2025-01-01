package com.security.duanspringboot.service.report;

import com.security.duanspringboot.entity.CategoryModel;
import com.security.duanspringboot.entity.ProductCategoryModel;
import com.security.duanspringboot.entity.ProductModel;
import com.security.duanspringboot.entity.ProductOrderDetailModel;
import com.security.duanspringboot.entity.ProductOrderModel;
import com.security.duanspringboot.enumeration.TypeStatusOrder;
import com.security.duanspringboot.repository.CategoryRepository;
import com.security.duanspringboot.repository.ProductCategoryRepository;
import com.security.duanspringboot.repository.ProductOrderDetailRepository;
import com.security.duanspringboot.repository.ProductOrderRepository;
import com.security.duanspringboot.repository.ProductRepository;
import com.security.duanspringboot.service.report.data.TopSellingProductReportData;
import com.security.duanspringboot.utils.ReportUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TopSellingProductReport {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductOrderRepository productOrderRepository;
    private final ProductOrderDetailRepository productOrderDetailRepository;

    public TopSellingProductReport(ProductRepository productRepository, CategoryRepository categoryRepository, ProductCategoryRepository productCategoryRepository, ProductOrderRepository productOrderRepository, ProductOrderDetailRepository productOrderDetailRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productOrderRepository = productOrderRepository;
        this.productOrderDetailRepository = productOrderDetailRepository;
    }

    public Map<String, String> generateReport(String startDate, String endDate, String categoryId) {
        List<ProductModel> products = productRepository.findAll();
        List<CategoryModel> categories = categoryRepository.findAll();
        LocalDate firstDate = LocalDate.parse(startDate);
        LocalDateTime startTime = firstDate.atStartOfDay();
        LocalDate secondDate = LocalDate.parse(endDate);
        LocalDateTime endTime = secondDate.atTime(23, 59, 59);
        List<ProductOrderModel> productOrders = productOrderRepository.findByOrderDateBetweenAndStatus(startTime, endTime, TypeStatusOrder.SHIPPING.toString());
        List<String> productOrderIds = productOrders.stream().map(ProductOrderModel::getProductOrderId).toList();
        List<ProductOrderDetailModel> productOrderDetails = new ArrayList<>();
        if(categoryId.equals("all")) {
            productOrderDetails = productOrderDetailRepository.findAllByProductOrderIdIn(productOrderIds);
        } else {
            productOrderDetails = productOrderDetailRepository.findAllByProductOrderIdInAAndCategoryId(productOrderIds, categoryId);
        }

        List<String> sellingProductCategoryIds = productOrderDetails.stream().map(ProductOrderDetailModel::getProductCategoryId).toList();
        List<ProductCategoryModel> poList = productCategoryRepository.findAllByProductCategoryIdIn(sellingProductCategoryIds);
        return createContent(categories, poList, productOrderDetails);
    }

    private final List<String> TopSellingProductReportColumns = Arrays.asList(
            "Tên sản phẩm", "Loại hàng", "Đã bán", "Tồn kho"
    );

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
        String reportName = String.format("%s.csv", "Báo cáo bán hàng");
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
        result.append(ReportUtils.generateReportHeader(reportName, TopSellingProductReportColumns));
        List<TopSellingProductReportData> dataRows = generateReportDataRows(categoryMap, poMap, sellingMap)
                .stream()
                .sorted(Comparator.comparing(TopSellingProductReportData::getSellingQuantity))
                .toList();
        dataRows = dataRows.stream()
                .sorted((p1, p2) -> Integer.compare(Integer.parseInt(p2.getSellingQuantity()), Integer.parseInt(p1.getSellingQuantity())))
                .collect(Collectors.toList());
        dataRows.forEach(row -> result.append(row.toRow()));
        log.info("HotelListReport - data rows {}", dataRows.size());
        return result.toString();
    }

    private List<TopSellingProductReportData> generateReportDataRows(Map<String, CategoryModel> categoryMap,
                                                                     Map<String, ProductCategoryModel> poMap,
                                                                     Map<String, ProductOrderDetailModel> sellingMap) {
        List<TopSellingProductReportData> dataRows = new ArrayList<>();
        for (Map.Entry<String, ProductOrderDetailModel> entry : sellingMap.entrySet()) {
            String[] keys = entry.getKey().split("\\+");
            String productOrderId = keys[0];
            String productCategoryId = keys[1];
            ProductOrderDetailModel pod = entry.getValue();
            TopSellingProductReportData protoRow = new TopSellingProductReportData();
            protoRow.setProductName(pod.getProductName());
            if(poMap.containsKey(productCategoryId)) {
                protoRow.setProductCategory(categoryMap.containsKey(poMap.get(productCategoryId).getCategoryId()) ? categoryMap.get(poMap.get(productCategoryId).getCategoryId()).getCategoryName() : "");
                protoRow.setInStock(poMap.get(productCategoryId).getQuantity().toString());
            }
            protoRow.setSellingQuantity(pod.getQuantity().toString());
            dataRows.add(protoRow);
        }
        return dataRows;
    }
}
