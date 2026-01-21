package com.mycompany.core.cart2.repository;

import com.mycompany.core.cart2.domain.OrderCart2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Cart2용 Order Repository
 */
@Repository
public interface OrderCart2Repository extends JpaRepository<OrderCart2, Long> {

    /**
     * 고객 ID로 Cart2 찾기
     */
    @Query("SELECT o FROM OrderCart2 o WHERE o.customerId = :customerId AND o.status = 'IN_PROGRESS'")
    OrderCart2 findActiveCartByCustomerId(@Param("customerId") Long customerId);

    /**
     * 고객 ID로 모든 Cart2 찾기
     */
    List<OrderCart2> findByCustomerId(Long customerId);

    /**
     * Cart Type으로 찾기
     */
    List<OrderCart2> findByCartType(String cartType);

    /**
     * Order Number로 찾기
     */
    OrderCart2 findByOrderNumber(String orderNumber);
}