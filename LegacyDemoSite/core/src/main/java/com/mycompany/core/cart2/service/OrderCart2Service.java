package com.mycompany.core.cart2.service;

import com.mycompany.core.cart2.domain.OrderCart2;
import com.mycompany.core.cart2.domain.OrderItemCart2;
import com.mycompany.core.cart2.repository.OrderCart2Repository;
import org.broadleafcommerce.core.catalog.domain.Product;
import org.broadleafcommerce.core.catalog.domain.Sku;
import org.broadleafcommerce.core.catalog.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

/**
 * Cart2용 Order Service
 */
@Service
public class OrderCart2Service {

    @Autowired
    private OrderCart2Repository orderCart2Repository;

    @Autowired
    private CatalogService catalogService;

    /**
     * 고객의 활성 Cart2를 가져오거나 생성
     */
    @Transactional
    public OrderCart2 getOrCreateCart2(Long customerId) {
        OrderCart2 cart2 = orderCart2Repository.findActiveCartByCustomerId(customerId);
        
        if (cart2 == null) {
            cart2 = createNewCart2(customerId);
        }
        
        return cart2;
    }

    /**
     * 새로운 Cart2 생성
     */
    @Transactional
    public OrderCart2 createNewCart2(Long customerId) {
        OrderCart2 cart2 = new OrderCart2();
        cart2.setCustomerId(customerId);
        cart2.setOrderNumber(generateOrderNumber());
        cart2.setStatus("IN_PROGRESS");
        cart2.setCartType("CART2");
        cart2.setCreatedDate(new Date());
        cart2.setUpdatedDate(new Date());
        
        return orderCart2Repository.save(cart2);
    }

    /**
     * Cart2에 상품 추가 (지정된 수량만큼)
     */
    @Transactional
    public OrderItemCart2 addItemToCart2(Long customerId, Long productId, Long skuId, int quantity) {
        OrderCart2 cart2 = getOrCreateCart2(customerId);
        
        // 기존에 같은 상품이 있는지 확인
        OrderItemCart2 existingItem = findExistingItem(cart2, productId, skuId);
        
        if (existingItem != null) {
            // 기존 상품이 있으면 수량만 증가
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setUpdatedDate(new Date());
            return orderCart2Repository.save(cart2).getOrderItems().stream()
                .filter(item -> item.getId().equals(existingItem.getId()))
                .findFirst()
                .orElse(existingItem);
        } else {
            // 새 상품 추가
            OrderItemCart2 newItem = createOrderItem(cart2, productId, skuId, quantity);
            cart2.addOrderItem(newItem);
            updateCartTotals(cart2);
            return newItem;
        }
    }

    /**
     * Cart2에서 상품 제거
     */
    @Transactional
    public void removeItemFromCart2(Long cart2Id, Long orderItemId) {
        OrderCart2 cart2 = orderCart2Repository.findById(cart2Id).orElse(null);
        if (cart2 != null) {
            OrderItemCart2 itemToRemove = cart2.getOrderItems().stream()
                .filter(item -> item.getId().equals(orderItemId))
                .findFirst()
                .orElse(null);
            
            if (itemToRemove != null) {
                cart2.removeOrderItem(itemToRemove);
                updateCartTotals(cart2);
                orderCart2Repository.save(cart2);
            }
        }
    }

    /**
     * Cart2의 총액 업데이트
     */
    private void updateCartTotals(OrderCart2 cart2) {
        BigDecimal subtotal = BigDecimal.ZERO;
        
        for (OrderItemCart2 item : cart2.getOrderItems()) {
            BigDecimal itemPrice = item.getSalePrice() != null ? item.getSalePrice() : item.getPrice();
            if (itemPrice != null) {
                subtotal = subtotal.add(itemPrice.multiply(new BigDecimal(item.getQuantity())));
            }
        }
        
        cart2.setSubtotal(subtotal);
        cart2.setTotal(subtotal.add(cart2.getTotalShipping()).add(cart2.getTotalTax()));
        cart2.setUpdatedDate(new Date());
    }

    /**
     * 기존 상품 찾기
     */
    private OrderItemCart2 findExistingItem(OrderCart2 cart2, Long productId, Long skuId) {
        return cart2.getOrderItems().stream()
            .filter(item -> item.getProductId().equals(productId) && item.getSkuId().equals(skuId))
            .findFirst()
            .orElse(null);
    }

    /**
     * Order Item 생성
     */
    private OrderItemCart2 createOrderItem(OrderCart2 cart2, Long productId, Long skuId, int quantity) {
        Product product = catalogService.findProductById(productId);
        Sku sku = catalogService.findSkuById(skuId);
        
        OrderItemCart2 item = new OrderItemCart2();
        item.setOrder(cart2);
        item.setProductId(productId);
        item.setSkuId(skuId);
        item.setQuantity(quantity);
        item.setName(product != null ? product.getName() : "Unknown Product");
        
        if (sku != null) {
            item.setPrice(sku.getPrice());
            item.setSalePrice(sku.getSalePrice());
            item.setRetailPrice(sku.getRetailPrice());
        }
        
        item.setCreatedDate(new Date());
        item.setUpdatedDate(new Date());
        
        return item;
    }

    /**
     * Order Number 생성
     */
    private String generateOrderNumber() {
        return "CART2-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Cart2 ID로 조회
     */
    public OrderCart2 findById(Long cart2Id) {
        return orderCart2Repository.findById(cart2Id).orElse(null);
    }

    /**
     * Cart2 저장
     */
    @Transactional
    public OrderCart2 save(OrderCart2 cart2) {
        return orderCart2Repository.save(cart2);
    }
}