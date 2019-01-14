package com.enjoywater.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Tong Hai on 09/1/2019.
 * Phone: +84388326555
 * Email: haitongvan@vccorp.vn
 */
public class Order implements Parcelable {
    @SerializedName("items")
    private ArrayList<Product> items = new ArrayList<>();
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
    private String userId;
    @SerializedName("id")
    private String id;
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
    private String createdBy;
    @SerializedName("delivererId")
    private String delivererId;
    @SerializedName("prepayment")
    private int prepayment;
    @SerializedName("payment")
    private int payment;
    @SerializedName("discount")
    private int discount;
    @SerializedName("deliveryFee")
    private int deliveryFee;

    public Order() {
    }

    protected Order(Parcel in) {
        items = in.createTypedArrayList(Product.CREATOR);
        paymentMethod = in.readString();
        deliveryClimb = in.readInt();
        deliveryOpts = in.readString();
        couponCode = in.readString();
        orderBySchedule = in.readLong();
        phone = in.readString();
        receiverName = in.readString();
        address = in.readString();
        district = in.readString();
        province = in.readString();
        notes = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        userId = in.readString();
        id = in.readString();
        comment = in.readString();
        rate = in.readInt();
        received = in.readByte() != 0;
        status = in.readString();
        totalPayment = in.readInt();
        expectedDelivery = in.readString();
        createdBy = in.readString();
        delivererId = in.readString();
        prepayment = in.readInt();
        payment = in.readInt();
        discount = in.readInt();
        deliveryFee = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(items);
        dest.writeString(paymentMethod);
        dest.writeInt(deliveryClimb);
        dest.writeString(deliveryOpts);
        dest.writeString(couponCode);
        dest.writeLong(orderBySchedule);
        dest.writeString(phone);
        dest.writeString(receiverName);
        dest.writeString(address);
        dest.writeString(district);
        dest.writeString(province);
        dest.writeString(notes);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(userId);
        dest.writeString(id);
        dest.writeString(comment);
        dest.writeInt(rate);
        dest.writeByte((byte) (received ? 1 : 0));
        dest.writeString(status);
        dest.writeInt(totalPayment);
        dest.writeString(expectedDelivery);
        dest.writeString(createdBy);
        dest.writeString(delivererId);
        dest.writeInt(prepayment);
        dest.writeInt(payment);
        dest.writeInt(discount);
        dest.writeInt(deliveryFee);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public ArrayList<Product> getItems() {
        return items;
    }

    public void setItems(ArrayList<Product> items) {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDelivererId() {
        return delivererId;
    }

    public void setDelivererId(String delivererId) {
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
