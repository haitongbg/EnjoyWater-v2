package com.enjoywater.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tong Hai on 09/1/2019.
 * Phone: +84388326555
 * Email: haitongvan@vccorp.vn
 */
public class Order {
    @SerializedName("items")
    private ArrayList<Item> items = new ArrayList<>();
    @SerializedName("paymentMethod")
    private String paymentMethod;
    @SerializedName("deliveryClimb")
    private int deliveryClimb;
    @SerializedName("deliveryOpts")
    private String deliveryOpts;
    @SerializedName("couponCode")
    private String couponCode;
    @SerializedName("orderBySchedule")
    private long orderBySchedule;
    @SerializedName("phone")
    private String phone;
    @SerializedName("receiverName")
    private String receiverName;
    @SerializedName("address")
    private String address;
    @SerializedName("district")
    private String district;
    @SerializedName("province")
    private String province;
    @SerializedName("notes")
    private String notes;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("updatedAt")
    private String updatedAt;
    @SerializedName("userId")
    private int userId;
    @SerializedName("id")
    private int id;
    @SerializedName("comment")
    private String comment;
    @SerializedName("rate")
    private int rate;
    @SerializedName("received")
    private boolean received;
    @SerializedName("status")
    private String status;
    @SerializedName("totalPayment")
    private int totalPayment;
    @SerializedName("expectedDelivery")
    private String expectedDelivery;
    @SerializedName("createdBy")
    private int createdBy;
    @SerializedName("delivererId")
    private int delivererId;
    @SerializedName("prepayment")
    private int prepayment;
    @SerializedName("payment")
    private int payment;
    @SerializedName("discount")
    private int discount;
    @SerializedName("deliveryFee")
    private int deliveryFee;

    public static class Item {
        @SerializedName("productId")
        private String productId;
        @SerializedName("volume")
        private int volume;

        public Item(String productId, int volume) {
            this.productId = productId;
            this.volume = volume;
        }

        public int getVolume() {
            return volume;
        }

        public void setVolume(int volume) {
            this.volume = volume;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }
    }

    public Order() {
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getDeliveryClimb() {
        return deliveryClimb;
    }

    public void setDeliveryClimb(int deliveryClimb) {
        this.deliveryClimb = deliveryClimb;
    }

    public String getDeliveryOpts() {
        return deliveryOpts;
    }

    public void setDeliveryOpts(String deliveryOpts) {
        this.deliveryOpts = deliveryOpts;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public long getOrderBySchedule() {
        return orderBySchedule;
    }

    public void setOrderBySchedule(long orderBySchedule) {
        this.orderBySchedule = orderBySchedule;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalPayment() {
        return totalPayment;
    }

    public void setTotalPayment(int totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getExpectedDelivery() {
        return expectedDelivery;
    }

    public void setExpectedDelivery(String expectedDelivery) {
        this.expectedDelivery = expectedDelivery;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getDelivererId() {
        return delivererId;
    }

    public void setDelivererId(int delivererId) {
        this.delivererId = delivererId;
    }

    public int getPrepayment() {
        return prepayment;
    }

    public void setPrepayment(int prepayment) {
        this.prepayment = prepayment;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(int deliveryFee) {
        this.deliveryFee = deliveryFee;
    }
}
