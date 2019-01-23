package com.enjoywater.adapter.order;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.Barrier;
import android.support.constraint.Group;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.enjoywater.R;
import com.enjoywater.listener.OrderListener;
import com.enjoywater.model.Order;
import com.enjoywater.utils.Constants;
import com.enjoywater.utils.Utils;
import com.enjoywater.view.LoadmoreViewHolder;
import com.enjoywater.view.TvRobotoMedium;
import com.enjoywater.view.TvSegoeuiSemiBold;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryOrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_LOADMORE = 0;
    private static final int TYPE_NORMAL = 1;
    private Context mContext;
    private ArrayList<Order> mOrders;
    private LayoutInflater mInflater;
    private DecimalFormat formatVND = new DecimalFormat("###,###,###");
    private OrderListener mOrderListener;

    public HistoryOrdersAdapter(Context context, ArrayList<Order> orders, OrderListener orderListener) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mOrders = orders;
        this.mOrderListener = orderListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (mOrders.get(position).isLoadmore()) return TYPE_LOADMORE;
        return TYPE_NORMAL;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LOADMORE:
                return new LoadmoreViewHolder(mInflater.inflate(R.layout.item_loadmore, parent, false));
            default:
                return new CustomViewHolder(mInflater.inflate(R.layout.item_history_order, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CustomViewHolder) ((CustomViewHolder) holder).setData(position);
    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_order_code)
        ImageView ivOrderCode;
        @BindView(R.id.tv_order_code)
        TvRobotoMedium tvOrderCode;
        @BindView(R.id.tv_order_canceled)
        TvRobotoMedium tvOrderCanceled;
        @BindView(R.id.view_status_line)
        View viewStatusLine;
        @BindView(R.id.iv_status_ordered)
        ImageView ivStatusOrdered;
        @BindView(R.id.tv_status_ordered)
        TextView tvStatusOrdered;
        @BindView(R.id.view_status_ordered)
        View viewStatusOrdered;
        @BindView(R.id.iv_status_confirmed)
        ImageView ivStatusConfirmed;
        @BindView(R.id.tv_status_confirmed)
        TextView tvStatusConfirmed;
        @BindView(R.id.view_status_confirmed)
        View viewStatusConfirmed;
        @BindView(R.id.iv_status_delivering)
        ImageView ivStatusDelivering;
        @BindView(R.id.tv_status_delivering)
        TextView tvStatusDelivering;
        @BindView(R.id.view_status_delivering)
        View viewStatusDelivering;
        @BindView(R.id.iv_status_received)
        ImageView ivStatusReceived;
        @BindView(R.id.tv_status_received)
        TextView tvStatusReceived;
        @BindView(R.id.barrier_under_order_status)
        Barrier barrierUnderOrderStatus;
        @BindView(R.id.text_order_time)
        TextView textOrderTime;
        @BindView(R.id.iv_order_time)
        ImageView ivOrderTime;
        @BindView(R.id.tv_order_time)
        TvRobotoMedium tvOrderTime;
        @BindView(R.id.text_delivery_time)
        TextView textDeliveryTime;
        @BindView(R.id.tv_delivery_time)
        TvRobotoMedium tvDeliveryTime;
        @BindView(R.id.iv_delivery_time)
        ImageView ivDeliveryTime;
        @BindView(R.id.text_total_payment)
        TvSegoeuiSemiBold textTotalPayment;
        @BindView(R.id.tv_total_payment)
        TvSegoeuiSemiBold tvTotalPayment;
        @BindView(R.id.btn_cancel_order)
        Button btnCancelOrder;
        @BindView(R.id.btn_go_order_details)
        Button btnGoOrderDetails;
        @BindView(R.id.group_order_info)
        Group groupOrderInfo;
        @BindView(R.id.group_order_status)
        Group groupOrderStatus;

        CustomViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setData(int position) {
            Order order = mOrders.get(position);
            if (order != null) {
                // code
                tvOrderCode.setText("Mã đơn hàng: #" + order.getId());
                // status
                String status = order.getStatus();
                if (status.equals(Constants.Value.CANCELED)) {
                    btnCancelOrder.setVisibility(View.GONE);
                    groupOrderInfo.setVisibility(View.GONE);
                    groupOrderStatus.setVisibility(View.GONE);
                    tvOrderCanceled.setVisibility(View.VISIBLE);
                } else {
                    tvOrderCanceled.setVisibility(View.GONE);
                    groupOrderInfo.setVisibility(View.VISIBLE);
                    btnCancelOrder.setVisibility(View.GONE);
                    viewStatusOrdered.setBackgroundResource(R.color.black_c);
                    viewStatusConfirmed.setBackgroundResource(R.color.black_c);
                    viewStatusDelivering.setBackgroundResource(R.color.black_c);
                    viewStatusLine.setBackgroundResource(R.color.black_c);
                    ivStatusOrdered.setImageResource(R.drawable.ic_unsuccess);
                    ivStatusConfirmed.setImageResource(R.drawable.ic_unsuccess);
                    ivStatusDelivering.setImageResource(R.drawable.ic_unsuccess);
                    ivStatusReceived.setImageResource(R.drawable.ic_unsuccess);
                    tvStatusOrdered.setTextColor(mContext.getResources().getColor(R.color.black_c));
                    tvStatusConfirmed.setTextColor(mContext.getResources().getColor(R.color.black_c));
                    tvStatusDelivering.setTextColor(mContext.getResources().getColor(R.color.black_c));
                    tvStatusReceived.setTextColor(mContext.getResources().getColor(R.color.black_c));
                    textDeliveryTime.setText(R.string.expected_delivery_time);
                    switch (status) {
                        case Constants.Value.PENDING: {
                            groupOrderStatus.setVisibility(View.VISIBLE);
                            btnCancelOrder.setVisibility(View.VISIBLE);
                            viewStatusOrdered.setBackgroundResource(R.color.colorAccent);
                            ivStatusOrdered.setImageResource(R.drawable.ic_success);
                            tvStatusOrdered.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                            break;
                        }
                        case Constants.Value.VERIFIED: {
                            groupOrderStatus.setVisibility(View.VISIBLE);
                            btnCancelOrder.setVisibility(View.VISIBLE);
                            viewStatusOrdered.setBackgroundResource(R.color.colorAccent);
                            viewStatusConfirmed.setBackgroundResource(R.color.colorAccent);
                            ivStatusOrdered.setImageResource(R.drawable.ic_success);
                            ivStatusConfirmed.setImageResource(R.drawable.ic_success);
                            tvStatusOrdered.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                            tvStatusConfirmed.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                            break;
                        }
                        case Constants.Value.DELIVERING: {
                            groupOrderStatus.setVisibility(View.VISIBLE);
                            btnCancelOrder.setVisibility(View.GONE);
                            viewStatusOrdered.setBackgroundResource(R.color.colorAccent);
                            viewStatusConfirmed.setBackgroundResource(R.color.colorAccent);
                            viewStatusDelivering.setBackgroundResource(R.color.colorAccent);
                            ivStatusOrdered.setImageResource(R.drawable.ic_success);
                            ivStatusConfirmed.setImageResource(R.drawable.ic_success);
                            ivStatusDelivering.setImageResource(R.drawable.ic_success);
                            tvStatusOrdered.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                            tvStatusConfirmed.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                            tvStatusDelivering.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                            break;
                        }
                        case Constants.Value.DELIVERED: {
                            groupOrderStatus.setVisibility(View.GONE);
                            btnCancelOrder.setVisibility(View.GONE);
                            viewStatusOrdered.setBackgroundResource(R.color.colorAccent);
                            viewStatusConfirmed.setBackgroundResource(R.color.colorAccent);
                            viewStatusDelivering.setBackgroundResource(R.color.colorAccent);
                            viewStatusLine.setBackgroundResource(R.color.colorAccent);
                            ivStatusOrdered.setImageResource(R.drawable.ic_success);
                            ivStatusConfirmed.setImageResource(R.drawable.ic_success);
                            ivStatusDelivering.setImageResource(R.drawable.ic_success);
                            ivStatusReceived.setImageResource(R.drawable.ic_success);
                            tvStatusOrdered.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                            tvStatusConfirmed.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                            tvStatusDelivering.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                            tvStatusReceived.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                            textDeliveryTime.setText(R.string.delivered_time);
                            break;
                        }
                        default:
                            break;
                    }
                }
                // time
                String createdTime = order.getCreatedAt();
                if (createdTime != null && !createdTime.isEmpty())
                    tvOrderTime.setText(Utils.convertDateTimeToDateTime(createdTime, "GMT", 5, 8));
                String expectedDeliveryTime = order.getExpectedDelivery();
                if (expectedDeliveryTime != null && !expectedDeliveryTime.isEmpty())
                    tvDeliveryTime.setText(Utils.convertDateTimeToDateTime(expectedDeliveryTime, "GMT", 5, 5));
                // total payment
                tvTotalPayment.setText(formatVND.format(order.getTotalPayment()) + " đ");
                btnCancelOrder.setOnClickListener(v -> mOrderListener.cancelOrder(order));
                btnGoOrderDetails.setOnClickListener(v -> mOrderListener.goOrderDetails(order));
                itemView.setOnClickListener(v -> mOrderListener.goOrderDetails(order));
                ((RecyclerView.LayoutParams)itemView.getLayoutParams()).topMargin = position == 0 ? 0 : mContext.getResources().getDimensionPixelSize(R.dimen.size_15);
            }
        }
    }
}
