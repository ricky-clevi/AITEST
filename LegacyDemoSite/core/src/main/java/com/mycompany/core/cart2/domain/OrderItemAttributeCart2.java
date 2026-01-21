package com.mycompany.core.cart2.domain;

import org.broadleafcommerce.common.presentation.AdminPresentation;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Date;

/**
 * Cart2용 Order Item Attribute 엔티티
 */
@Entity
@Table(name = "BLC_ORDER_ITEM_ATTRIBUTE_CART2")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "blOrderElements")
public class OrderItemAttributeCart2 {

    @Id
    @GeneratedValue(generator = "OrderItemAttributeCart2Id")
    @GenericGenerator(
        name = "OrderItemAttributeCart2Id",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
            @Parameter(name = "sequence_name", value = "BLC_ORDER_ITEM_ATTR_CART2_SEQ"),
            @Parameter(name = "initial_value", value = "1"),
            @Parameter(name = "increment_size", value = "50")
        }
    )
    @Column(name = "ORDER_ITEM_ATTRIBUTE_ID")
    @AdminPresentation(friendlyName = "Order Item Attribute ID", order = 1, prominent = true, gridOrder = 1)
    protected Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORDER_ITEM_ID")
    @AdminPresentation(friendlyName = "Order Item", order = 2)
    protected OrderItemCart2 orderItem;

    @Column(name = "NAME")
    @AdminPresentation(friendlyName = "Name", order = 3)
    protected String name;

    @Column(name = "VALUE")
    @AdminPresentation(friendlyName = "Value", order = 4)
    protected String value;

    @Column(name = "CREATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @AdminPresentation(friendlyName = "Created Date", order = 5, fieldType = SupportedFieldType.DATE)
    protected Date createdDate = new Date();

    @Column(name = "UPDATED_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    @AdminPresentation(friendlyName = "Updated Date", order = 6, fieldType = SupportedFieldType.DATE)
    protected Date updatedDate = new Date();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OrderItemCart2 getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItemCart2 orderItem) {
        this.orderItem = orderItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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