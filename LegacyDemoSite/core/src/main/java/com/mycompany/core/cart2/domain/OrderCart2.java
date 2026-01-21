package com.mycompany.core.cart2.domain;

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.client.SupportedFieldType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Cart2용 Order 엔티티
 */
@Entity
@Table(name = "BLC_ORDER_CART2")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blOrderElements")
public class OrderCart2 {

    @Id
    @GeneratedValue(generator = "OrderCart2Id")
    @GenericGenerator(
        name = "OrderCart2Id",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
            @Parameter(name = "sequence_name", value = "BLC_ORDER_CART2_SEQ"),
            @Parameter(name = "initial_value", value = "1"),
            @Parameter(name = "increment_size", value = "50")
        }
    )
    @Column(name = "ORDER_ID")
    @AdminPresentation(friendlyName = "Order ID", order = 1, prominent = true, gridOrder = 1)
    protected Long id;

    @Column(name = "ORDER_NUMBER")
    @AdminPresentation(friendlyName = "Order Number", order = 2, prominent = true, gridOrder = 2)
    protected String orderNumber;

    @Column(name = "CUSTOMER_ID")
    @AdminPresentation(friendlyName = "Customer ID", order = 3)
    protected Long customerId;

    @Column(name = "NAME")
    @AdminPresentation(friendlyName = "Name", order = 4)
    protected String name;

    @Column(name = "EMAIL")
    @AdminPresentation(friendlyName = "Email", order = 5)
    protected String email;

    @Column(name = "PHONE_NUMBER")
    @AdminPresentation(friendlyName = "Phone Number", order = 6)
    protected String phoneNumber;

    @Column(name = "SUBTOTAL", precision = 19, scale = 5)
    @AdminPresentation(friendlyName = "Subtotal", order = 7, fieldType = SupportedFieldType.MONEY)
    protected BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "TOTAL_SHIPPING", precision = 19, scale = 5)
    @AdminPresentation(friendlyName = "Total Shipping", order = 8, fieldType = SupportedFieldType.MONEY)
    protected BigDecimal totalShipping = BigDecimal.ZERO;

    @Column(name = "TOTAL_TAX", precision = 19, scale = 5)
    @AdminPresentation(friendlyName = "Total Tax", order = 9, fieldType = SupportedFieldType.MONEY)
    protected BigDecimal totalTax = BigDecimal.ZERO;

    @Column(name = "TOTAL", precision = 19, scale = 5)
    @AdminPresentation(friendlyName = "Total", order = 10, fieldType = SupportedFieldType.MONEY)
    protected BigDecimal total = BigDecimal.ZERO;

    @Column(name = "STATUS")
    @AdminPresentation(friendlyName = "Status", order = 11)
    protected String status = "IN_PROGRESS";

    @Column(name = "SUBMIT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @AdminPresentation(friendlyName = "Submit Date", order = 12, fieldType = SupportedFieldType.DATE)
    protected Date submitDate;

    @Column(name = "CART_TYPE")
    @AdminPresentation(friendlyName = "Cart Type", order = 13)
    protected String cartType = "CART2";

    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @AdminPresentation(friendlyName = "Created Date", order = 14, fieldType = SupportedFieldType.DATE)
    protected Date createdDate = new Date();

    @Column(name = "UPDATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @AdminPresentation(friendlyName = "Updated Date", order = 15, fieldType = SupportedFieldType.DATE)
    protected Date updatedDate = new Date();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    protected List<OrderItemCart2> orderItems = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotalShipping() {
        return totalShipping;
    }

    public void setTotalShipping(BigDecimal totalShipping) {
        this.totalShipping = totalShipping;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    public String getCartType() {
        return cartType;
    }

    public void setCartType(String cartType) {
        this.cartType = cartType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public List<OrderItemCart2> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemCart2> orderItems) {
        this.orderItems = orderItems;
    }

    public void addOrderItem(OrderItemCart2 orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void removeOrderItem(OrderItemCart2 orderItem) {
        orderItems.remove(orderItem);
        orderItem.setOrder(null);
    }

    public int getItemCount() {
        int count = 0;
        for (OrderItemCart2 item : orderItems) {
            count += item.getQuantity();
        }
        return count;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDate = new Date();
    }

    @PrePersist
    public void prePersist() {
        Date now = new Date();
        this.createdDate = now;
        this.updatedDate = now;
    }
}