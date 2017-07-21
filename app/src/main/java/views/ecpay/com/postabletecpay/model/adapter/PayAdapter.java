package views.ecpay.com.postabletecpay.model.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import views.ecpay.com.postabletecpay.R;
import views.ecpay.com.postabletecpay.presenter.IPayPresenter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.EntityKhachHang;
import views.ecpay.com.postabletecpay.util.entities.sqlite.Bill;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;
import views.ecpay.com.postabletecpay.view.ThanhToan.IPayView;

import static views.ecpay.com.postabletecpay.model.adapter.PayAdapter.BillInsidePayAdapter.IS_DEBT;
import static views.ecpay.com.postabletecpay.model.adapter.PayAdapter.BillInsidePayAdapter.NOT_DEBT;

/**
 * Created by VinhNB on 5/26/2017.
 */

public class PayAdapter extends RecyclerView.Adapter<PayAdapter.PayViewHolder> {
    private Context sContext;

    //giá trị billList[0] = mAdapterList[indexBegin] , billList[billList.size()] = mAdapterList[indexEnd]
    private IPayPresenter payPresenter;

    private List<DataAdapter> data = new ArrayList<>();
//    private List<PayEntityAdapter BillEntityAdapter> billList = new ArrayList<>();
    @BindDrawable(R.drawable.bg_button)
    Drawable green;
    @BindDrawable(R.drawable.bg_button_orange)
    Drawable violet;
    @BindView(R.id.btn_row_thanhtoan_recycler_print)
    Button btnPrintBill;

    private boolean onBind;

    public PayAdapter(Context sContext, IPayPresenter presenter, List<DataAdapter> data, int indexBegin) {
        this.sContext = sContext;
        this.data = data;
        this.payPresenter = presenter;
    }


    public void UpdateAdapter(List<DataAdapter> data, int indexBegin, int indexEnd)
    {
        this.data = data;
        this.notifyDataSetChanged();

    }

    public void UpdateBill(PayAdapter.BillEntityAdapter bill )
    {
        for(int i = 0, n = this.data.size(); i < n; i ++)
        {
            if(this.data.get(i).getInfoKH().getMA_KHANG().equalsIgnoreCase(bill.getMA_KHACH_HANG()))
            {
                for (int j = 0, n2 = this.data.get(i).getBillKH().size(); j < n2; j ++)
                {
                    if(this.data.get(i).getBillKH().get(j).getBillId() == bill.getBillId())
                    {
                        this.data.get(i).getBillKH().get(j).setTRANG_THAI_TT(bill.getTRANG_THAI_TT());
                        this.data.get(i).getBillKH().get(j).setVI_TTOAN(bill.getVI_TTOAN());
                    }
                }
            }
        }
    }

    @Override
    public PayViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_thanhtoan_recycler, parent, false);
        ButterKnife.bind(this, view);

        return new PayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PayViewHolder holder, int position) {

        onBind = true;

        holder.setDataAdapter(data.get(position));

        EntityKhachHang entityAdapter = data.get(position).getInfoKH();
        final String code = entityAdapter.getMA_KHANG();
        final String edong = entityAdapter.getE_DONG();

        holder.tvTenKH.setText(entityAdapter.getTEN_KHANG());
        holder.tvDiaChi.setText(entityAdapter.getDIA_CHI());
        holder.tvLoTrinh.setText((entityAdapter.getLO_TRINH() != null && entityAdapter.getLO_TRINH().trim().length() != 0 ? (entityAdapter.getLO_TRINH().trim() + "-") : "") + entityAdapter.getSO_GCS());
        holder.tvTongTien.setText(Common.convertLongToMoney(data.get(position).getTotalMoney()));
        holder.tvMaKH.setText(code);

        boolean isPayed = data.get(position).isPayed();
        holder.setPayed(isPayed);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            holder.btnTrangThaiNo.setBackground(isPayed ? green : violet);
        } else {
            holder.btnTrangThaiNo.setBackgroundDrawable(isPayed ? green : violet);
        }
        holder.btnTrangThaiNo.setText(isPayed ? NOT_DEBT : IS_DEBT);

        Common.runAnimationClickViewScale(holder.cardView, R.anim.twinking_view, Common.TIME_DELAY_ANIM);

        DataAdapter dataAdapter = getObjectDataAdapter(code);


        holder.billsInsideAdapter.setBillList(edong, code, dataAdapter.getBillKH(), position);
        holder.rvBill.invalidate();


        boolean isShowBill = false;
        for (int i = 0, n = data.get(position).getBillKH().size(); i < n; i ++)
        {
            if(payPresenter.getPayModel().containBillInSelected(data.get(position).getBillKH().get(i).getBillId()))
            {
                isShowBill = true;
                data.get(position).setBill(data.get(position).getBillKH().get(i));
                break;
            }
        }

        data.get(position).setShowBill(isShowBill);

        holder.showBills(isShowBill);
        onBind = false;

    }

    private DataAdapter getObjectDataAdapter(String code) {
        DataAdapter dataAdapter = null;
        int index = 0;
        for(; index<data.size(); index++)
        {
            if(data.get(index).getInfoKH().getMA_KHANG().equals(code)) {
                dataAdapter = data.get(index);
                break;
            }
        }

        return dataAdapter;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class PayViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_row_pay_recycler_customer)
        TextView tvTenKH;
        @BindView(R.id.tv_row_pay_recycler_diaChi)
        TextView tvDiaChi;
        @BindView(R.id.tv_row_pay_recycler_loTrinh)
        TextView tvLoTrinh;
        @BindView(R.id.tv_row_pay_recycler_maKhachHang)
        TextView tvMaKH;
        @BindView(R.id.tv_row_pay_recycler_tongTien)
        TextView tvTongTien;
        @BindView(R.id.btn_row_pay_recycler_trangThaiNo)
        Button btnTrangThaiNo;
        @BindView(R.id.btn_row_thanhtoan_recycler_print)
        Button btn_row_thanhtoan_recycler_print;
        @BindView(R.id.card_row_thanh_toan_recycler)
        LinearLayout cardView;
        @BindView(R.id.rvHoaDon)
        RecyclerView rvBill;
        //        CardView cardView;
        @BindView(R.id.ll_row_thanhtoan_recycler_print)
        LinearLayout ll_row_thanhtoan_recycler_print;
        public View itemView;


        boolean isPayed;


        DataAdapter dataAdapter;

        public PayViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.itemView = itemView;

            billsInsideAdapter = new BillInsidePayAdapter(sContext);
            rvBill.setLayoutManager(new LinearLayoutManager(sContext, LinearLayoutManager.VERTICAL, false));
            rvBill.setAdapter(billsInsideAdapter);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!onBind) {
                        boolean isShowBill = !dataAdapter.isShowBill();

                        dataAdapter.setShowBill(isShowBill);
                        showBills(isShowBill);
                    }
                }
            });

            btn_row_thanhtoan_recycler_print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    payPresenter.getIPayView().PrintThongBaoDien(dataAdapter);
                }
            });

            //set 10 rows and a page
//            cardView.getLayoutParams().height = (int) (height / PayFragment.ROWS_ON_PAGE);
        }


        public DataAdapter getDataAdapter() {
            return dataAdapter;
        }

        public void setDataAdapter(DataAdapter dataAdapter) {
            this.dataAdapter = dataAdapter;
        }

        public void showBills(boolean show)
        {

            rvBill.setVisibility(show ? View.VISIBLE : View.GONE);

            if(isPayed)
            {
                ll_row_thanhtoan_recycler_print.setVisibility(View.GONE);
            }else
            {
                ll_row_thanhtoan_recycler_print.setVisibility(show ? View.VISIBLE : View.GONE);
            }

        }


        public boolean isPayed() {
            return isPayed;
        }

        public void setPayed(boolean payed) {
            isPayed = payed;
        }

        private BillInsidePayAdapter billsInsideAdapter;

        public View getItemView() {
            return itemView;
        }
    }

    public static class BillEntityAdapter implements Comparable<BillEntityAdapter>{

        private Date THANG_THANH_TOAN;
        private long TIEN_THANH_TOAN;
        private boolean isPrint;
        private String TRANG_THAI_TT;
        private boolean isChecked;
        private String requestDate;
        private boolean isPrintEnable;
        private boolean isCheckEnable;
        private String MA_KHACH_HANG;
        private String TEN_KHACH_HANG;
        private String DIA_CHI;
        private String MA_DIEN_LUC;
        private String SO_GCS;
        private String SO_HO;
        private String SO_CONG_TO;
        private String CSDK;
        private String CSCK;
        private String PHIEN_THANH_TOAN;
        private String MA_HOA_DON;
        private String CHI_TIET_KG;
        private String CHI_TIET_MCS;
        private String CHI_TIET_TIEN_MCS;
        private String DNTT;
        private String TONG_TIEN_CHUA_THUE;
        private String TONG_TIEN_THUE;
        private String MA_DL_MO_RONG;
        private String TU_NGAY;
        private String DEN_NGAY;

        private String messageError = "";

        //info extension
        private long billId;
        private String billingBy;

        public BillEntityAdapter() {
        }

        public String getMessageError() {
            return messageError;
        }

        public void setMessageError(String messageError) {
            this.messageError = messageError;
        }

        public String getTEN_KHACH_HANG() {
            return TEN_KHACH_HANG;
        }

        public void setTEN_KHACH_HANG(String TEN_KHACH_HANG) {
            this.TEN_KHACH_HANG = TEN_KHACH_HANG;
        }

        public String getMA_KHACH_HANG() {
            return MA_KHACH_HANG;
        }

        public void setMA_KHACH_HANG(String MA_KHACH_HANG) {
            this.MA_KHACH_HANG = MA_KHACH_HANG;
        }

        public String getSO_GCS() {
            return SO_GCS;
        }

        public void setSO_GCS(String SO_GCS) {
            this.SO_GCS = SO_GCS;
        }

        public String getPHIEN_THANH_TOAN() {
            return PHIEN_THANH_TOAN;
        }

        public void setPHIEN_THANH_TOAN(String PHIEN_THANH_TOAN) {
            this.PHIEN_THANH_TOAN = PHIEN_THANH_TOAN;
        }

        public String getDIA_CHI() {
            return DIA_CHI;
        }

        public void setDIA_CHI(String DIA_CHI) {
            this.DIA_CHI = DIA_CHI;
        }

        public boolean isCheckEnable() {
            return isCheckEnable;
        }

        public void setCheckEnable(boolean checkEnable) {
            isCheckEnable = checkEnable;
        }

        public boolean isPrintEnable() {
            return isPrintEnable;
        }

        public void setPrintEnable(boolean printEnable) {
            isPrintEnable = printEnable;
        }

        public Date getTHANG_THANH_TOAN() {
            return THANG_THANH_TOAN;
        }

        public void setTHANG_THANH_TOAN(Date THANG_THANH_TOAN) {
            this.THANG_THANH_TOAN = THANG_THANH_TOAN;
        }

        public long getTIEN_THANH_TOAN() {
            return TIEN_THANH_TOAN;
        }

        public void setTIEN_THANH_TOAN(long TIEN_THANH_TOAN) {
            this.TIEN_THANH_TOAN = TIEN_THANH_TOAN;
        }

        public boolean isPrint() {
            return isPrint;
        }

        public void setPrint(boolean print) {
            isPrint = print;
        }

        public String getTRANG_THAI_TT() {
            return TRANG_THAI_TT;
        }

        public void setTRANG_THAI_TT(String TRANG_THAI_TT) {
            this.TRANG_THAI_TT = TRANG_THAI_TT;
        }

        public boolean isChecked() {
            return isChecked;
        }

        public String getMA_DIEN_LUC() {
            return MA_DIEN_LUC;
        }

        public void setMA_DIEN_LUC(String MA_DIEN_LUC) {
            this.MA_DIEN_LUC = MA_DIEN_LUC;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getRequestDate() {
            return requestDate;
        }

        public void setRequestDate(String requestDate) {
            this.requestDate = requestDate;
        }

        public long getBillId() {
            return billId;
        }

        public void setBillId(long billId) {
            this.billId = billId;
        }


        public String getVI_TTOAN() {
            return billingBy;
        }

        public void setVI_TTOAN(String billingBy) {
            this.billingBy = billingBy;
        }

        public String getSO_HO() {
            return SO_HO;
        }

        public void setSO_HO(String SO_HO) {
            this.SO_HO = SO_HO;
        }

        public String getSO_CONG_TO() {
            return SO_CONG_TO;
        }

        public void setSO_CONG_TO(String SO_CONG_TO) {
            this.SO_CONG_TO = SO_CONG_TO;
        }

        public String getCSDK() {
            return CSDK;
        }

        public void setCSDK(String CSDK) {
            this.CSDK = CSDK;
        }

        public String getCSCK() {
            return CSCK;
        }

        public void setCSCK(String CSCK) {
            this.CSCK = CSCK;
        }

        public String getMA_HOA_DON() {
            return MA_HOA_DON;
        }

        public void setMA_HOA_DON(String MA_HOA_DON) {
            this.MA_HOA_DON = MA_HOA_DON;
        }

        public String getCHI_TIET_KG() {
            return CHI_TIET_KG;
        }

        public void setCHI_TIET_KG(String CHI_TIET_KG) {
            this.CHI_TIET_KG = CHI_TIET_KG;
        }

        public String getCHI_TIET_MCS() {
            return CHI_TIET_MCS;
        }

        public void setCHI_TIET_MCS(String CHI_TIET_MCS) {
            this.CHI_TIET_MCS = CHI_TIET_MCS;
        }

        public String getCHI_TIET_TIEN_MCS() {
            return CHI_TIET_TIEN_MCS;
        }

        public void setCHI_TIET_TIEN_MCS(String CHI_TIET_TIEN_MCS) {
            this.CHI_TIET_TIEN_MCS = CHI_TIET_TIEN_MCS;
        }

        public String getDNTT() {
            return DNTT;
        }

        public void setDNTT(String DNTT) {
            this.DNTT = DNTT;
        }

        public String getTONG_TIEN_CHUA_THUE() {
            return TONG_TIEN_CHUA_THUE;
        }

        public void setTONG_TIEN_CHUA_THUE(String TONG_TIEN_CHUA_THUE) {
            this.TONG_TIEN_CHUA_THUE = TONG_TIEN_CHUA_THUE;
        }

        public String getTONG_TIEN_THUE() {
            return TONG_TIEN_THUE;
        }

        public void setTONG_TIEN_THUE(String TONG_TIEN_THUE) {
            this.TONG_TIEN_THUE = TONG_TIEN_THUE;
        }

        public String getMA_DL_MO_RONG() {
            return MA_DL_MO_RONG;
        }

        public void setMA_DL_MO_RONG(String MA_DL_MO_RONG) {
            this.MA_DL_MO_RONG = MA_DL_MO_RONG;
        }

        public String getTU_NGAY() {
            return TU_NGAY;
        }

        public void setTU_NGAY(String TU_NGAY) {
            this.TU_NGAY = TU_NGAY;
        }

        public String getDEN_NGAY() {
            return DEN_NGAY;
        }

        public void setDEN_NGAY(String DEN_NGAY) {
            this.DEN_NGAY = DEN_NGAY;
        }

        @Override
        public int compareTo(@NonNull BillEntityAdapter billEntityAdapter) {
            long termThis = this.getTHANG_THANH_TOAN().getTime();
            long termThat = billEntityAdapter.getTHANG_THANH_TOAN().getTime();

            if(termThat > termThis)
                return  -1;
            else if(termThat < termThis)
                return  1;
            else
            {
                if(!billEntityAdapter.getTRANG_THAI_TT().equalsIgnoreCase(Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode()))
                    return -1;
                if(!this.getTRANG_THAI_TT().equalsIgnoreCase(Common.TRANG_THAI_TTOAN.CHUA_TTOAN.getCode()))
                    return 1;
                return 0;
            }
        }

        public static Comparator<BillEntityAdapter> TermComparatorBillEntityAdapter
                = new Comparator<BillEntityAdapter>() {

            public int compare(BillEntityAdapter entity, BillEntityAdapter entityNew) {

                //giảm dần
                return entityNew.compareTo(entity);

                //descending order
                //return fruitName2.compareTo(fruitName1);
            }

        };
    }

    public class BillInsidePayAdapter extends RecyclerView.Adapter<BillInsidePayAdapter.BillInsidePayViewHolder> {
        public static final String IS_PAY = "Đã thanh toán";
        public static final String NOT_PAY_YET = "Chưa thanh toán";
        public static final String IS_DEBT = "Còn nợ";
        public static final String NOT_DEBT = "Hết nợ";

        private List<BillEntityAdapter> billList = new ArrayList<>();
        private String code;
        private String edong;

        //extends
        private int posCustomerInside;

        public BillInsidePayAdapter(Context context) {
        }

        public void setBillList(String edong, String code, List<BillEntityAdapter> billList, int posCustomer) {

            Collections.sort(billList, BillEntityAdapter.TermComparatorBillEntityAdapter);
            this.billList = new ArrayList<>();
            this.billList.addAll(billList);
            this.code = code;
            this.edong = edong;
            this.posCustomerInside = posCustomer;
            notifyDataSetChanged();
        }

        @Override
        public BillInsidePayAdapter.BillInsidePayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v1 = inflater.inflate(R.layout.row_bill_inside_pay, parent, false);
            BillInsidePayViewHolder viewHolder = new BillInsidePayViewHolder(v1);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(BillInsidePayAdapter.BillInsidePayViewHolder holder, int position) {
            final BillEntityAdapter entity = billList.get(position);
            holder.setData(entity);
            holder.setPosition(position);

            holder.setAdapterParent(this);


            entity.setChecked(payPresenter.getPayModel().containBillInSelected(entity.getBillId()));


            holder.cb.setChecked(entity.isChecked());
            if (entity.getTRANG_THAI_TT().equalsIgnoreCase(Common.STATUS_BILLING.CHUA_THANH_TOAN.getCode())) {
                holder.cb.setEnabled(true);
                holder.ibtnDelete.setVisibility(View.INVISIBLE);

                holder.tvStatusBill.setText(Common.STATUS_BILLING.CHUA_THANH_TOAN.getMessage());
            } else {
                holder.cb.setEnabled(false);
                holder.cb.setChecked(true);
                holder.ibtnDelete.setVisibility(View.VISIBLE);
                holder.tvStatusBill.setText(Common.STATUS_BILLING.DA_THANH_TOAN.getMessage());
                entity.setPrint(entity.getTRANG_THAI_TT().equalsIgnoreCase(Common.STATUS_BILLING.DA_THANH_TOAN.getCode()));
            }
            holder.tvDate.setText(Common.parse(entity.getTHANG_THANH_TOAN(), Common.DATE_TIME_TYPE.MMyyyy.toString()));
            holder.tvMoneyBill.setText(Common.convertLongToMoney(entity.getTIEN_THANH_TOAN()));

            if (entity.getTRANG_THAI_TT().equalsIgnoreCase(Common.STATUS_BILLING.CHUA_THANH_TOAN.getCode())){
                holder.tvStatusBill.setTextColor(ContextCompat.getColor(sContext, R.color.colorRed));
            } else {
                holder.tvStatusBill.setTextColor(ContextCompat.getColor(sContext, R.color.colorTextGreen));
            }
            holder.ibtnPrintInside.setVisibility(entity.isPrint ? View.VISIBLE : View.INVISIBLE);

        }

        @Override
        public int getItemCount() {
            return this.billList.size();
        }

        public class BillInsidePayViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.cb_row_bill_inside_pay)
            CheckBox cb;
            @BindView(R.id.tv_row_bill_inside_pay_date)
            TextView tvDate;
            @BindView(R.id.tv_row_bill_inside_pay_money)
            TextView tvMoneyBill;
            @BindView(R.id.tv_row_bill_inside_pay_trang_thai)
            TextView tvStatusBill;
            @BindView(R.id.btn_row_bill_inside_pay_print)
            ImageButton ibtnPrintInside;
            @BindView(R.id.btn_row_bill_inside_pay_delete)
            ImageButton ibtnDelete;

            BillInsidePayAdapter adapterParent;


            BillEntityAdapter data;
            int position;


            public BillInsidePayViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);

                ibtnPrintInside.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        payPresenter.getIPayView().PrintHoaDon(data);
                    }
                });

            }

            public BillInsidePayAdapter getAdapterParent() {
                return adapterParent;
            }

            public void setAdapterParent(BillInsidePayAdapter adapterParent) {
                this.adapterParent = adapterParent;
            }

            public void setPosition(int position) {
                this.position = position;
            }

            public BillEntityAdapter getData() {
                return data;
            }

            public void setData(BillEntityAdapter data) {
                this.data = data;
            }

            @OnCheckedChanged(R.id.cb_row_bill_inside_pay)
            public void onCheckedChanged(final CheckBox checkBox, boolean checked) {
                if (checkBox.isPressed()) {

                    int index = position + 1;
                    int indexNotPayedTermOldest = Common.NEGATIVE_ONE;

                    BillEntityAdapter bill = billList.get(position);
                    //TODO check hóa đơn xa nhất
                    for (; index < billList.size(); index++) {
                        if (billList.get(index).getTRANG_THAI_TT().equalsIgnoreCase(Common.STATUS_BILLING.CHUA_THANH_TOAN.getCode()) &&
                                !billList.get(index).isChecked() && bill.compareTo(billList.get(index)) != 0)
                            indexNotPayedTermOldest = index;
                    }

                    if (checked) {
                        //TODO check hóa đơn liên tục fragment khi checked

                        if (indexNotPayedTermOldest != Common.NEGATIVE_ONE) {
                            ((MainActivity) sContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    checkBox.setChecked(false);
                                }
                            });

                            for (indexNotPayedTermOldest += 1; indexNotPayedTermOldest < billList.size(); indexNotPayedTermOldest++) {
                                if (billList.get(indexNotPayedTermOldest).getTRANG_THAI_TT().equalsIgnoreCase(Common.STATUS_BILLING.CHUA_THANH_TOAN.getCode()) && billList.get(indexNotPayedTermOldest).isChecked())
                                {
                                    payPresenter.getIPayView().processUnCheckedBillDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10005.getMessage() + Common.TEXT_MULTI_SPACE + billList.get(indexNotPayedTermOldest).getMA_KHACH_HANG(), Common.TYPE_DIALOG.LOI);
//                                    payPresenter.getIPayView().showMessageNotifyPayfrag(Common.CODE_REPONSE_BILL_ONLINE.ex10005.getMessage() + Common.TEXT_MULTI_SPACE + bill.getMA_KHACH_HANG());
                                    return;
                                }
                            }


                            payPresenter.getIPayView().processUnCheckedBillDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10003.getMessage() + Common.TEXT_MULTI_SPACE + bill.getMA_KHACH_HANG(), Common.TYPE_DIALOG.LOI);
//                           payPresenter.getIPayView().showMessageNotifyPayfrag(Common.CODE_REPONSE_BILL_ONLINE.ex10003.getMessage() + Common.TEXT_MULTI_SPACE + bill.getMA_KHACH_HANG());
                            return;
                        }
                        payPresenter.addSelectBillToPay(bill, true);
                        bill.setChecked(checked);
//                        interaction.processCheckedBillFragment(edong, code, posCustomerInside, billList, position, indexBegin, indexEnd);
                    } else {
                        //TODO check hóa đơn liên tục fragment khi unchecked

                        bill.setChecked(checked);
                        payPresenter.addSelectBillToPay(bill, false);
                        boolean hasChange = false;
                        for (int i = 0; i < position; i ++)
                        {
                            if(billList.get(i).getTRANG_THAI_TT().equalsIgnoreCase(Common.STATUS_BILLING.CHUA_THANH_TOAN.getCode()) && billList.get(i).compareTo(bill) != 0)
                            {
                                if(i != position && billList.get(i).isChecked())
                                {
                                    hasChange = true;
                                }
                                billList.get(i).setChecked(false);
                                payPresenter.addSelectBillToPay(billList.get(i), false);
                            }
                        }

                        if(hasChange)
                        {
                            notifyDataSetChanged();
                        }

                        return;
                    }
                }
            }

            @OnClick(R.id.btn_row_bill_inside_pay_delete)
            public void doClickDelete(View view) {
                int position = getAdapterPosition();
                BillEntityAdapter bill = billList.get(position);

                payPresenter.getIPayView().processDialogDeleteBillOnline(edong, bill, adapterParent);
            }

            public CheckBox getCb() {
                return cb;
            }

            public TextView getTvDate() {
                return tvDate;
            }

            public TextView getTvMoneyBill() {
                return tvMoneyBill;
            }

            public TextView getTvStatusBill() {
                return tvStatusBill;
            }

            public ImageButton getIbtnPrintInside() {
                return ibtnPrintInside;
            }

            public ImageButton getIbtnDelete() {
                return ibtnDelete;
            }
        }
    }

//    public interface OnInterationBillInsidePayAdapter {
//        void processCheckedBillFragment(String edong, String code, int posCustomer, List<BillEntityAdapter> billList, int posBillInside, int indexBegin, int indexEnd);
//
//        void processDeleteBillOnlineFragment(String edong, String code, BillEntityAdapter bill, int posCustomerInside);
//
//        void processUnCheckedBillFragment(String message);
//    }

    public static class DataAdapter {
        private EntityKhachHang infoKH;
        private List<BillEntityAdapter> billKH;
        private BillEntityAdapter bill;
        private long totalMoney;
        private boolean isShowBill;


        public DataAdapter(EntityKhachHang infoKH, List<BillEntityAdapter> billKH, long total) {
            this.infoKH = infoKH;
            this.billKH = billKH;
            this.totalMoney = total;
        }

        public long getTotalMoney() {
            return totalMoney;
        }

        public void setTotalMoney(long totalMoney) {
            this.totalMoney = totalMoney;
        }

        public BillEntityAdapter getBill() {
            return bill;
        }

        public void setBill(BillEntityAdapter bill) {
            this.bill = bill;
        }

        public boolean isShowBill() {
            return isShowBill;
        }

        public void setShowBill(boolean showBill) {
            isShowBill = showBill;
        }

        public EntityKhachHang getInfoKH() {
            return infoKH;
        }

        public List<BillEntityAdapter> getBillKH() {
            return billKH;
        }

        public void sortBills()
        {

            Collections.sort(billKH, PayAdapter.BillEntityAdapter.TermComparatorBillEntityAdapter);
        }


        public boolean isPayed()
        {
            for (int i = 0; i < billKH.size(); i ++)
            {
                if(billKH.get(i).getTRANG_THAI_TT().equalsIgnoreCase(Common.STATUS_BILLING.CHUA_THANH_TOAN.getCode()))
                    return false;
            }
            return true;
        }
    }
}