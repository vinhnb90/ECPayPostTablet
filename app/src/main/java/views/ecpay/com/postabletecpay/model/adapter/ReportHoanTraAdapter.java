package views.ecpay.com.postabletecpay.model.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Bill;

/**
 * Created by duydatpham on 6/26/17.
 */

public class ReportHoanTraAdapter  extends RecyclerView.Adapter<ReportHoanTraAdapter.RowHoanTraHolder> {

    List<Bill> mBills;


    public ReportHoanTraAdapter()
    {
        mBills = new ArrayList<>();
    }

    public List<Bill> getmBills() {
        return mBills;
    }

    public void setmBills(List<Bill> mBills) {
        this.mBills = mBills;
    }

    @Override
    public RowHoanTraHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_baocao_hoantra, parent, false);

        return new RowHoanTraHolder(view);
    }

    @Override
    public void onBindViewHolder(RowHoanTraHolder holder, int position) {
        Bill bill = mBills.get(position);
        holder.tvTenKH.setText(bill.getName());
        holder.tvMaKH.setText(bill.getCustomerCode());
        holder.tv_fragment_thanh_toan_total_bills_money.setText(Common.convertLongToMoney(bill.getAmount()));
        holder.tvTinhTrang.setText("Chua set");
        if(bill.getTerm().length() != 0)
        {
            holder.tvKy.setText(bill.getTerm().substring(4, 6) + "/" + bill.getTerm().substring(0, 4));
        }else
        {
            holder.tvKy.setText("");
        }


    }

    @Override
    public int getItemCount() {
        return mBills.size();
    }

    public class RowHoanTraHolder extends RecyclerView.ViewHolder
    {

        public TextView tvTenKH;
        public TextView tvMaKH;
        public TextView tvKy;
        public TextView tv_fragment_thanh_toan_total_bills_money;
        public TextView tvTinhTrang;

        public RowHoanTraHolder(View itemView) {
            super(itemView);

            tvTenKH = (TextView)itemView.findViewById(R.id.tvTenKH);
            tvMaKH = (TextView)itemView.findViewById(R.id.tvMaKH);
            tvKy = (TextView)itemView.findViewById(R.id.tvKy);
            tv_fragment_thanh_toan_total_bills_money = (TextView)itemView.findViewById(R.id.tv_fragment_thanh_toan_total_bills_money);
            tvTinhTrang = (TextView)itemView.findViewById(R.id.tvTinhTrang);

        }
    }
}
