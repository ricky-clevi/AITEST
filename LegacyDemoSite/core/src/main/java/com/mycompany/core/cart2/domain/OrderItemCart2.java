package com.mycompany.core.cart2.domain;

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.broadleafcommerce.common.presentation.client.SupportedFieldType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Cart2용 Order Item 엔티티
 */
@Entity
@Table(name = "BLC_ORDER_ITEM_CART2")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blOrderElements")
public class OrderItemCart2 {

    @Id
    @GeneratedValue(generator = "OrderItemCart2Id")
    @GenericGenerator(
        name = "OrderItemCart2Id",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
            @Parameter(name = "sequence_name", value = "BLC_ORDER_ITEM_CART2_SEQ"),
            @Parameter(name = "initial_value", value = "1"),
            @Parameter(name = "increment_size", value = "50")
        }
    )
    @Column(name = "ORDER_ITEM_ID")
    @AdminPresentation(friendlyName = "Order Item ID", order = 1, prominent = true, gridOrder = 1)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ID")
    @AdminPresentation(friendlyName = "Order", order = 2)
    protected OrderCart2 order;

    @Column(name = "SKU_ID")
    @AdminPresentation(friendlyName = "SKU ID", order = 3)
    protected Long skuId;

    @Column(name = "PRODUCT_ID")
    @AdminPresentation(friendlyName = "Product ID", order = 4)
    protected Long productId;

    @Column(name = "QUANTITY")
    @AdminPresentation(friendlyName = "Quantity", order = 5)
    protected int quantity = 1;

    @Column(name = "PRICE", precision = 19, scale = 5)
    @AdminPresentation(friendlyName = "Price", order = 6, fieldType = SupportedFieldType.MONEY)
    protected BigDecimal price;

    @Column(name = "SALE_PRICE", precision = 19, scale = 5)
    @AdminPresentation(friendlyName = "Sale Price", order = 7, fieldType = SupportedFieldType.MONEY)
    protected BigDecimal salePrice;

    @Column(name = "RETAIL_PRICE", precision = 19, scale = 5)
    @AdminPresentation(friendlyName = "Retail Price", order = 8, fieldType = SupportedFieldType.MONEY)
    protected BigDecimal retailPrice;

    @Column(name = "NAME")
    @AdminPresentation(friendlyName = "Name", order = 9)
    protected String name;

    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @AdminPresentation(friendlyName = "Created Date", order = 10, fieldType = SupportedFieldType.DATE)
    protected Date createdDate = new Date();

    @Column(name = "UPDATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @AdminPresentation(friendlyName = "Updated Date", order = 11, fieldType = SupportedFieldType.DATE)
    protected Date updatedDate = new Date();

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    protected List<OrderItemAttributeCart2> orderItemAttributes = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderCart2 getOrder() {
        return order;
    }

    public void setOrder(OrderCart2 order) {
        this.order = order;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<OrderItemAttributeCart2> getOrderItemAttributes() {
        return orderItemAttributes;
    }

    public void setOrderItemAttributes(List<OrderItemAttributeCart2> orderItemAttributes) {
        this.orderItemAttributes = orderItemAttributes;
    }

    public void addOrderItemAttribute(OrderItemAttributeCart2 attribute) {
        orderItemAttributes.add(attribute);
        attribute.setOrderItem(this);
    }

    public void removeOrderItemAttribute(OrderItemAttributeCart2 attribute) {
        orderItemAttributes.remove(attribute);
        attribute.setOrderItem(null);
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