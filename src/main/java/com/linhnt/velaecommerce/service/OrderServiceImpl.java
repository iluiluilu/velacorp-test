package com.linhnt.velaecommerce.service;

import com.linhnt.velaecommerce.constant.ErrorCode;
import com.linhnt.velaecommerce.dto.request.order.CreateOrderDetailDto;
import com.linhnt.velaecommerce.dto.request.order.FilterOrderDto;
import com.linhnt.velaecommerce.dto.request.order.UpdateOrderDetailDto;
import com.linhnt.velaecommerce.entity.OrderDetailEntity;
import com.linhnt.velaecommerce.entity.OrderEntity;
import com.linhnt.velaecommerce.entity.ProductEntity;
import com.linhnt.velaecommerce.exception.VeNotFoundException;
import com.linhnt.velaecommerce.exception.VeValidateException;
import com.linhnt.velaecommerce.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl extends BaseService implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    protected OrderServiceImpl(EntityManager entityManager) {
        super(entityManager);
    }

    @Override
    public List<OrderEntity> filter(FilterOrderDto filterDto) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<OrderEntity> criteriaQuery = criteriaBuilder.createQuery(OrderEntity.class);
        Root<OrderEntity> root = criteriaQuery.from(OrderEntity.class);

        if (Objects.equals(filterDto.getSortType(), "desc")) {
            criteriaQuery.orderBy(criteriaBuilder.desc(root.get(filterDto.getSortField())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.asc(root.get(filterDto.getSortField())));
        }

        criteriaQuery.select(root).where(this.buildPredicate(criteriaBuilder, root, filterDto));

        TypedQuery<OrderEntity> query = entityManager.createQuery(criteriaQuery);
        if (filterDto.getLimit() != null && filterDto.getPage() != null) {
            query.setFirstResult((filterDto.getPage() - 1) * filterDto.getLimit());
            query.setMaxResults(filterDto.getLimit());
        }

        return query.getResultList();
    }

    @Override
    public Long filterCount(FilterOrderDto filterDto) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<OrderEntity> root = countQuery.from(OrderEntity.class);

        countQuery.select(criteriaBuilder.count(root))
                .where(this.buildPredicate(criteriaBuilder, root, filterDto));

        return entityManager.createQuery(countQuery).getSingleResult();
    }

    @Override
    public OrderEntity findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public OrderEntity save(OrderEntity order, List<ProductEntity> products) {
        productService.saveAll(products);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public OrderEntity saveOrder(OrderEntity order, List<ProductEntity> products) {
        productService.saveAll(products);
        return orderRepository.save(order);
    }

    @Override
    public void softDeleteById(Long id) throws VeNotFoundException {
        OrderEntity order = findById(id);
        if (order == null) {
            throw new VeNotFoundException(ErrorCode.NOT_FOUND);
        }
        order.setDeleted(true);
        orderRepository.save(order);
    }

    /**
     * Re-calculate Product quantity, validate product of order
    * */
    @Override
    public Map<Long, ProductEntity> validateCreateOrderProduct(List<CreateOrderDetailDto> orderDetailDtos) {
        List<Long> productIds = new ArrayList<>();
        Map<Long, CreateOrderDetailDto> orderDetailDtoMap = new HashMap<>();
        for (CreateOrderDetailDto dto : orderDetailDtos) {
            productIds.add(dto.getProductId());
            orderDetailDtoMap.put(dto.getProductId(), dto);
        }

        List<ProductEntity> invalidProductQuantity = new ArrayList<>();

        List<ProductEntity> existedProductEntities = productService.findByIds(productIds);
        for (ProductEntity product : existedProductEntities) {
            if (productIds.contains(product.getId())) { // Product existed in db
                productIds.remove(product.getId()); // Remove existed product id

                int newQuantity = product.getQuantity() - orderDetailDtoMap.get(product.getId()).getQuantity();

                if (newQuantity < 0) {
                    invalidProductQuantity.add(product);
                    continue;
                }
                product.setQuantity(newQuantity);
            }
        }

        Map<String, Object> errorData = new HashMap<>();
        if (!productIds.isEmpty()) {
            errorData.put("invalidProductIds", productIds);
        }
        if (!invalidProductQuantity.isEmpty()) {
            errorData.put("invalidProductQuantity", invalidProductQuantity);
        }

        if (!errorData.isEmpty()) {
            // Throw exception if exist invalid Product
            throw new VeValidateException(ErrorCode.UPDATE_ORDER_DATA_INCONSISTENT, errorData);
        }

        return existedProductEntities.stream().collect(
                Collectors.toMap(ProductEntity::getId, p -> p)
        ); // Return Product need update quantity
    }

    private Predicate buildPredicate(CriteriaBuilder criteriaBuilder, Root<OrderEntity> root, FilterOrderDto filterDto) {
        List<Predicate> predicates = new ArrayList<>();
        if (StringUtils.hasText(filterDto.getCustomerName())) {
            predicates.add(criteriaBuilder.like(root.get("customerName"), "%" + filterDto.getCustomerName() + "%"));
        }

        if (filterDto.getId() != null) {
            predicates.add(criteriaBuilder.equal(root.get("id"), filterDto.getId()));
        }

        Predicate finalPredicate = null;
        if (!predicates.isEmpty()) {
            finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }

        return customCriteriaBuilder.and(root, finalPredicate);
    }

    public Map<Long, ProductEntity> validateUpdateOrderProduct(
            List<UpdateOrderDetailDto> updateOrderDetailDtos, // Danh sách update order detail
            List<OrderDetailEntity> orderDetailEntities // Danh sách order detail trong db đang có
    ) {
        Set<Long> updateProductIds = new HashSet<>();
        Set<Long> updateOrderDetailIds = new HashSet<>();
        Set<Long> currentOrderDetailIds = new HashSet<>();

        Map<Long, Integer> addQuantityMap = new HashMap<>(); // Map số lượng sẽ được cộng vào sp
        Map<Long, Integer> subQuantityMap = new HashMap<>(); // Map số lượng sẽ bị trừ đi khỏi sp

        List<Long> invalidProductIds = new ArrayList<>();

        Map<Long, OrderDetailEntity> orderDetailEntityMap = new HashMap<>();
        for (OrderDetailEntity orderDetailEntity : orderDetailEntities) {
            if (!orderDetailEntity.getDeleted()) {
                orderDetailEntityMap.put(orderDetailEntity.getId(), orderDetailEntity);
                currentOrderDetailIds.add(orderDetailEntity.getId());
            }
        }

        for (UpdateOrderDetailDto orderDetailDto : updateOrderDetailDtos) {
            if (Objects.equals(orderDetailDto.getDeleted(), true)) {
                if (orderDetailDto.getId() == null) {
                    // nếu sp của order bị xóa nhưng không có id -> bỏ qua không xử lý
                    continue;
                }
            }
            if (orderDetailDto.getId() != null) {
                updateOrderDetailIds.add(orderDetailDto.getId());
            }
            orderDetailDto.setOldOrderDetail(orderDetailEntityMap.getOrDefault(orderDetailDto.getId(), null));

            if (orderDetailDto.getId() != null && orderDetailDto.getOldOrderDetail() == null) {
                // sp cần update không còn tồn tại trong db
                invalidProductIds.add(orderDetailDto.getProductId());
                continue;
            }

            updateProductIds.add(orderDetailDto.getProductId());
            if (orderDetailDto.getOldOrderDetail() != null) {
                updateProductIds.add(orderDetailDto.getOldOrderDetail().getProduct().getId());
            }
            if (orderDetailDto.getId() == null) {
                int tmpSub = subQuantityMap.getOrDefault(orderDetailDto.getProductId(), 0);
                subQuantityMap.put(orderDetailDto.getProductId(), tmpSub + orderDetailDto.getQuantity());
                continue;
            }
            if (Objects.equals(orderDetailDto.getDeleted(), true)) {
                // Xóa data
                int tmpAdd = addQuantityMap.getOrDefault(orderDetailDto.getOldOrderDetail().getProduct().getId(), 0);
                addQuantityMap.put(orderDetailDto.getOldOrderDetail().getProduct().getId(), tmpAdd + orderDetailDto.getQuantity());
            } else {
                // update data
                int tmpAdd = addQuantityMap.getOrDefault(orderDetailDto.getOldOrderDetail().getProduct().getId(), 0);
                addQuantityMap.put(orderDetailDto.getOldOrderDetail().getProduct().getId(), tmpAdd + orderDetailDto.getOldOrderDetail().getQuantity());

                int tmpSub = subQuantityMap.getOrDefault(orderDetailDto.getProductId(), 0);
                subQuantityMap.put(orderDetailDto.getProductId(), tmpSub + orderDetailDto.getQuantity());
            }
        }

        Map<String, Object> errorData = new HashMap<>();
        if (!Objects.equals(updateOrderDetailIds, currentOrderDetailIds)) {
            // id update khác với id đang có trong db
            updateOrderDetailIds.removeAll(currentOrderDetailIds);
            errorData.put("invaliOrderDetailIds", updateOrderDetailIds);
            throw new VeValidateException(ErrorCode.UPDATE_ORDER_DATA_INCONSISTENT, errorData);
        }

        if (!invalidProductIds.isEmpty()) {
            // sp update không tồn tại trong db
            errorData.put("invalidProductIds", invalidProductIds);
            throw new VeValidateException(ErrorCode.UPDATE_ORDER_DATA_INCONSISTENT, errorData);
        }

        List<ProductEntity> outOfStockProductIds = new ArrayList<>();
        List<ProductEntity> updateProductEntity = new ArrayList<>();
        Set<Long> availableProductIds = new HashSet<>();

        List<ProductEntity> existedProductEntities = productService.findByIds(new ArrayList<>(updateProductIds));

        for (ProductEntity product : existedProductEntities) {
            if (product.getDeleted()) {
                // ignore deleted product
                continue;
            }

            availableProductIds.add(product.getId());

            int newProductQuantity = product.getQuantity()
                    + addQuantityMap.getOrDefault(product.getId(), 0)
                    - subQuantityMap.getOrDefault(product.getId(), 0);
            if (newProductQuantity < 0) {
                outOfStockProductIds.add(product);
                continue;
            }
            product.setQuantity(newProductQuantity);
            updateProductEntity.add(product);
        }

        updateProductIds.removeAll(availableProductIds);
        if (!updateProductIds.isEmpty()) {
            errorData.put("invalidProductIds", updateProductIds);
        }
        if (!outOfStockProductIds.isEmpty()) {
            errorData.put("outOfStockProductIds", outOfStockProductIds);
        }

        if (!errorData.isEmpty()) {
            // Throw exception if exist invalid Product
            throw new VeValidateException(ErrorCode.UPDATE_ORDER_DATA_INCONSISTENT, errorData);
        }

        return updateProductEntity.stream().collect(
                Collectors.toMap(ProductEntity::getId, p -> p));
    }
}
