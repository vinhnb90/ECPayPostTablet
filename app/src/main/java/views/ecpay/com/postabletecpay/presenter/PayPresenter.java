package views.ecpay.com.postabletecpay.presenter;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import views.ecpay.com.postabletecpay.model.PayModel;
import views.ecpay.com.postabletecpay.model.adapter.PayAdapter;
import views.ecpay.com.postabletecpay.model.adapter.PayBillsDialogAdapter;
import views.ecpay.com.postabletecpay.util.commons.Common;
import views.ecpay.com.postabletecpay.util.entities.ConfigInfo;
import views.ecpay.com.postabletecpay.util.entities.EntityDanhSachThu;
import views.ecpay.com.postabletecpay.util.entities.EntityLichSuThanhToan;
import views.ecpay.com.postabletecpay.util.entities.response.EntityBillOnline.BillingOnlineRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntityCheckTrainOnline.CheckTrainOnlineResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityDeleteBillOnline.DeleteBillOnlineRespone;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.BillInsideCustomer;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.CustomerInsideBody;
import views.ecpay.com.postabletecpay.util.entities.response.EntitySearchOnline.SearchOnlineResponse;
import views.ecpay.com.postabletecpay.util.webservice.SoapAPI;
import views.ecpay.com.postabletecpay.view.Main.MainActivity;
import views.ecpay.com.postabletecpay.view.ThanhToan.IPayView;
import views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment;

import static android.content.ContentValues.TAG;
import static views.ecpay.com.postabletecpay.util.commons.Common.NEGATIVE_ONE;
import static views.ecpay.com.postabletecpay.util.commons.Common.ONE;
import static views.ecpay.com.postabletecpay.util.commons.Common.PARTNER_CODE_DEFAULT;
import static views.ecpay.com.postabletecpay.util.commons.Common.PROVIDER_DEFAULT;
import static views.ecpay.com.postabletecpay.util.commons.Common.TEXT_EMPTY;
import static views.ecpay.com.postabletecpay.util.commons.Common.TIME_OUT_CONNECT;
import static views.ecpay.com.postabletecpay.util.commons.Common.ZERO;
import static views.ecpay.com.postabletecpay.util.commons.Common.getVersionApp;
import static views.ecpay.com.postabletecpay.util.dbs.SQLiteConnection.ERROR_OCCUR;
import static views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment.PAGE_INCREMENT;
import static views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment.ROWS_ON_PAGE;
import static views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment.VISIBLE_BUTTON_DELETE_DIALOG.HIDE_COUNTINUE;
import static views.ecpay.com.postabletecpay.view.ThanhToan.PayFragment.VISIBLE_BUTTON_DELETE_DIALOG.SHOW_ALL;

/**
 * Created by VinhNB on 5/26/2017.
 */

public class PayPresenter implements IPayPresenter {
    private PayModel mPayModel;
    private IPayView mIPayView;
    private List<PayAdapter.DataAdapter> mDataCustomerBill;
    private List<PayAdapter.PayEntityAdapter> mAdapterList = new ArrayList<>();
    private int totalPage;
    private Common.TYPE_SEARCH mTypeSearch;

    //search online
    private SoapAPI.AsyncSoapSearchOnline soapSearchOnline;

    //bill paying online
    List<SoapAPI.AsyncSoapBillOnline> billOnlineAsyncList = new ArrayList<>();
    private SoapAPI.AsyncSoapBillOnline soapBillOnline;
    private int countBillPayedSuccess;
    private long totalAmountBillPayedSuccess;

    //check Train Online
    private SoapAPI.AsyncSoapCheckTrainOnline soapCheckTrainOnlineRequestCancelBill;
    private SoapAPI.AsyncSoapCheckTrainPayingBill soapCheckTrainOnlineRequestPayingBill;

    //delete Bill Online
    private SoapAPI.AsyncSoapDeleteBillOnline soapDeleteBillOnline;

    //delay
    private Handler handlerDelay = new Handler();

    //list bills in fragment
    private int totalBillsFragment = 0;
    private long totalMoneyFragment = 0;
    private List<PayBillsDialogAdapter.Entity> listBillCheckedFragment = new ArrayList<>();

    //list bills in dialog
    private int totalBillsChooseDialog = 0;
    private int totalBillsChooseDialogTemp;
    private long totalMoneyBillChooseDialog = 0;
    private List<PayBillsDialogAdapter.Entity> listBillDialog = new ArrayList<>();

    //info billDeleteOnline to delete
    PayAdapter.BillEntityAdapter billDeleteOnline;
    int posCustomerListDeleteOnline = NEGATIVE_ONE;

    public PayPresenter(IPayView mIPayView) {
        this.mIPayView = mIPayView;
        mDataCustomerBill = mIPayView.getData();
        mPayModel = new PayModel(mIPayView.getContextView());
    }

    @Override
    public void callPayRecycler(String edong, int pageIndex, Common.TYPE_SEARCH typeSearch, String infoSearch, boolean isSeachOnline) {
        if (edong == null)
            return;
        if (infoSearch == null)
            return;
        if (typeSearch == null)
            return;

        mIPayView.showRecyclerFragment();

        this.mTypeSearch = typeSearch;
        List<PayAdapter.DataAdapter> fitter = new ArrayList<>();
        int indexBegin = 0;
        int indexEnd = 0;

        if (pageIndex == PayFragment.FIRST_PAGE_INDEX) {
            mAdapterList.clear();
            int index = 0;
            if (typeSearch == Common.TYPE_SEARCH.ALL)
                for (; index < mDataCustomerBill.size(); index++) {
                    mAdapterList.add(mDataCustomerBill.get(index).getInfoKH());
                }
            else
                mAdapterList = mPayModel.getInforRowCustomerFitterBy(edong, typeSearch, infoSearch);

            totalPage = mAdapterList.size() / ROWS_ON_PAGE + PAGE_INCREMENT;

            indexEnd = pageIndex * ROWS_ON_PAGE;

            if (indexEnd > mAdapterList.size())
                indexEnd = mAdapterList.size();

            fitter = mDataCustomerBill.subList(indexBegin, indexEnd);

        } else {
            if (pageIndex > totalPage)
                return;

            indexBegin = ROWS_ON_PAGE * (pageIndex - PAGE_INCREMENT);
            indexEnd = ROWS_ON_PAGE * (pageIndex);
            if (indexEnd > mAdapterList.size())
                indexEnd = mAdapterList.size();

            int index = indexBegin;
            int maxIndex = indexEnd;

            fitter = mDataCustomerBill.subList(index, maxIndex);
        }

        listBillCheckedFragment = mPayModel.getAllBillOfCustomerCheckedWithStatusPay(edong, Common.STATUS_BILLING.CHUA_THANH_TOAN);
        refreshTotalBillsAndTotalMoneyInFragment(edong, Common.STATUS_BILLING.CHUA_THANH_TOAN);

        mIPayView.showPayRecyclerPage(fitter, indexBegin, indexEnd, pageIndex, totalPage, infoSearch, isSeachOnline);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void callSearchOnline(String mEdong, String infoSearch, boolean isReseach) {
        String textMessage = "";
        Context context = mIPayView.getContextView();
        Boolean isErr = false;

        if (mEdong == null || mEdong.isEmpty() || mEdong.trim().equals("") && !isErr) {
            return;
        }
        if (infoSearch == null && !isErr) {
            return;
        }

        mIPayView.showSearchOnlineProcess();

        //check wifi and network
//        if (!Common.isConnectingWifi(context) && !isErr) {
//            textMessage = Common.MESSAGE_NOTIFY.ERR_WIFI.toString();
//            isErr = true;
//        }
        if (!Common.isNetworkConnected(context) && !isErr) {
            textMessage = Common.MESSAGE_NOTIFY.ERR_NETWORK.toString();
            isErr = true;
        }
        if (isErr) {
            mIPayView.showMessageNotifySearchOnline(textMessage, Common.TYPE_DIALOG.LOI);
            return;
        }

        //setup info login
        ConfigInfo configInfo;
        String versionApp = Common.getVersionApp(context);

        try {
            configInfo = Common.setupInfoRequest(context, mEdong, Common.COMMAND_ID.CUSTOMER_BILL.toString(), versionApp);
        } catch (Exception e) {
            mIPayView.showMessageNotifySearchOnline(textMessage, Common.TYPE_DIALOG.LOI);
            return;
        }

        //param is setup request by ECPAY, please contact ECPAY Server service if rechange.
        String directEvn = Common.DIRECT_EVN;
        String customerCode = infoSearch;
        String pcCode = configInfo.getPC_CODE().trim();
        String bookCmis = Common.TEXT_EMPTY;

        //create request to server
        String jsonRequestSearchOnline = SoapAPI.getJsonRequestSearchOnline(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                directEvn,
                customerCode,
                pcCode,
                configInfo.getAccountId());

        if (jsonRequestSearchOnline == null)
            return;

        try {
            if (soapSearchOnline == null) {
                //if null then create new
                soapSearchOnline = new SoapAPI.AsyncSoapSearchOnline(mTypeSearch, mEdong, infoSearch, soapSearchOnlineCallBack);
            } else if (soapSearchOnline.getStatus() == AsyncTask.Status.PENDING) {
                //if running not yet then run

            } else if (soapSearchOnline.getStatus() == AsyncTask.Status.RUNNING) {
                //if is running
                soapSearchOnline.setEndCallSoap(true);
                soapSearchOnline.cancel(true);

                handlerDelay.removeCallbacks(runnableCountTimeSearchOnline);
                soapSearchOnline = new SoapAPI.AsyncSoapSearchOnline(mTypeSearch, mEdong, infoSearch, soapSearchOnlineCallBack);
            } else {
                //if running or finish
                handlerDelay.removeCallbacks(runnableCountTimeSearchOnline);

                soapSearchOnline = new SoapAPI.AsyncSoapSearchOnline(mTypeSearch, mEdong, infoSearch, soapSearchOnlineCallBack);
            }

            soapSearchOnline.execute(jsonRequestSearchOnline);

            //thread time out
            //sleep
            handlerDelay.postDelayed(runnableCountTimeSearchOnline, TIME_OUT_CONNECT);

        } catch (Exception e) {
            mIPayView.showMessageNotifySearchOnline(e.getMessage(), Common.TYPE_DIALOG.LOI);
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void callPayingBillOnline(final String edong) {
        if (totalBillsChooseDialog == 0) {
            mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10000.getMessage(), false, Common.TYPE_DIALOG.LOI, true);
            return;
        }

        //disable click checkbox list dialog
        ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIPayView.showPayRecyclerListBillsAndDisableCheckBox(listBillDialog, true);
            }
        });

        //totalBillsChooseDialog will minus - 1 change value when every billDeleteOnline is payed online
        //totalBillsChooseDialogTemp to visible count billDeleteOnline paying succes and will be save value begin
        totalBillsChooseDialogTemp = totalBillsChooseDialog;

        mIPayView.showPayingRViewDialogStart();

        Context context = mIPayView.getContextView();
        String versionApp = Common.getVersionApp(context);

        int index = 0;

        //stop all thread
        int maxIndex = billOnlineAsyncList.size();

        //start again count billDeleteOnline pay
        countBillPayedSuccess = 0;
        totalAmountBillPayedSuccess = 0;

        for (; index < maxIndex; index++) {
            SoapAPI.AsyncSoapBillOnline billOnline = billOnlineAsyncList.get(index);
            billOnline.setEndCallSoap(true);
            billOnline.getHandlerDelay().removeCallbacks(runnableCountTimeSearchOnline);
            billOnline.cancel(true);
        }
        billOnlineAsyncList = new ArrayList<>();

        index = 0;
        maxIndex = listBillDialog.size();
        double amount = 0d;
        for (; index < maxIndex; index++) {
            PayBillsDialogAdapter.Entity entity = listBillDialog.get(index);
            if (entity.isChecked())
                amount += entity.getAmount();
        }

        //TODO FR 001.1 (Thanh toán online): Kiểm tra thông tin và thanh toán theo trình tự sau
        //Kiểm tra số dư khả dụng của tài khoản thanh toán
        if (mPayModel.selectBalance() < amount) {
            mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_OFFLINE.e03.getMessage(), false, Common.TYPE_DIALOG.LOI, true);
            return;
        }

        //Kiểm tra trạng thái thanh toán của hóa đơn trong danh sách hóa đơn nợ đã lưu trong máy. Chỉ trạng thái payStatus trong bảng debt = 01 mới tiếp tục thanh toán
        /**
         * 01_Chưa thanh toán
         * 02_Đã thanh toán
         * 03_Đã thanh toán bởi nguồn khác
         * 04_Đã thanh toán bởi số ví khác
         */

        /**
         * Lấy list hóa đơn lỗi và lọc ra không khởi động thread thanh toán hóa đơn đó
         */
        boolean[] errorBills = new boolean[listBillDialog.size()];
        index = 0;
        maxIndex = listBillDialog.size();
        for (; index < maxIndex; index++) {
            PayBillsDialogAdapter.Entity entity = listBillDialog.get(index);

            int payStatusDebt = mPayModel.checkPayStatusDebt(edong, entity.getCode(), entity.getBillId());
            if (payStatusDebt != Common.STATUS_BILLING.CHUA_THANH_TOAN.getCode() || payStatusDebt == ERROR_OCCUR) {
                final int positionIndex = index;
                errorBills[index] = true;
                ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String messageError = Common.CODE_REPONSE_BILL_ONLINE.ex10009.getMessage();
                        listBillDialog.get(positionIndex).setMessageError(messageError);

                        //check full billDeleteOnline is payed
                        boolean isFinishAllThread = checkIsAllThreadFinish();
                        if (isFinishAllThread) {
                            refreshStatusPaySuccessDialog(edong);
                            if (countBillPayedSuccess == totalBillsChooseDialogTemp)
                                mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10001.getMessage(), false, Common.TYPE_DIALOG.THANH_CONG, true);
                            else
                                mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10004.getMessage(), false, Common.TYPE_DIALOG.LOI, true);
                        } else
                            refreshStatusPaySuccessDialogAndDisableCheckbox(edong, true);
                    }
                });
                Log.e(TAG, "callPayingBillOnline: Hóa đơn không có trong danh sách thu");
                break;
            }
        }


        //TODO Khởi động quá trình thanh toán online
        /**
         * Quá trình này thực chất gồm 2 giai đoạn (trong SRS chỉ mô tả chung yêu cầu gồm cả 2
         * Kiểm tra trạng thái hóa đơn hiện tại (gọi API Check-Trains)
         *
         */

        index = 0;
        for (; index < maxIndex; index++) {
            PayBillsDialogAdapter.Entity entity = listBillDialog.get(index);

            if (entity.isChecked() && !errorBills[index])
//                callAPICheckTransAndRequestPayingBill(versionApp, edong, entity);
                payOnlineTheBill(versionApp, edong, entity);
        }
    }

    @Override
    public void callPayingBillOffline(String edong) {
        if (totalBillsChooseDialog == 0) {
            mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10000.getMessage(), false, Common.TYPE_DIALOG.LOI, true);
            return;
        }

        //totalBillsChooseDialogTemp đại diện cho tổng các bill được chọn từ list bill dialog thanh toán trước khi nhấn nút thanh toán
        //totalBillsChooseDialog dại diện tổng các bill được chọn tại list bill dialog thanh toán, giảm dần khi xong mỗi hóa đơn

        //giữ giá trị số bill đc chọn ban đầu
        totalBillsChooseDialogTemp = totalBillsChooseDialog;

        //ẩn thanh process bar bill
        mIPayView.hidePayingRViewDialog();


        //biến đếm số bill đã thanh toán ok
        countBillPayedSuccess = 0;

        //listBillDialog là list bill hiển thị trong dialog thanh toán
        //những bill nào đc chọn thì cho thanh toán offline
        int index = 0;
        int maxIndex = listBillDialog.size();
        double amount = 0d;
        for (; index < maxIndex; index++) {
            PayBillsDialogAdapter.Entity entity = listBillDialog.get(index);
            if (entity.isChecked())
                amount += entity.getAmount();
        }

        //Kiểm tra kỳ hoá đơn thanh toán

        //Kiểm tra số dư khả dụng của tài khoản thanh toán
        if (mPayModel.selectBalance() < amount) {
            mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_OFFLINE.e03.getMessage(), false, Common.TYPE_DIALOG.LOI, true);
            return;
        }
        //Kiểm tra địa bàn thanh toán
        if (mPayModel.getPcCode().substring(0, 2).toUpperCase().equals("PD") || mPayModel.getPcCode().substring(0, 2).toUpperCase().equals("PE")) {
            mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_OFFLINE.e04.getMessage(), false, Common.TYPE_DIALOG.LOI, true);
            return;
        }
        //Thanh toán offline
        index = 0;
        StringBuilder sbMsg = new StringBuilder();
        for (; index < maxIndex; index++) {
            PayBillsDialogAdapter.Entity entity = listBillDialog.get(index);

            if (entity.isChecked()) {
                if (entity.getStatus() == 0) {
                    payOfflineTheBill(entity, index, edong);
                } else if (entity.getStatus() == 3) {
                    sbMsg.append("\nHoá đơn ");
                    sbMsg.append(entity.getBillId());
                    sbMsg.append(" đã được thanh toán bởi nguồn khác");
                } else if (entity.getStatus() == 4) {
                    sbMsg.append("\nHoá đơn ");
                    sbMsg.append(entity.getBillId());
                    sbMsg.append(" đã được thanh toán bởi số ví khác");
                }
            }
        }

        if (!sbMsg.toString().isEmpty()) {
            mIPayView.showMessageNotifyBillOnlineDialog(sbMsg.toString(), false, Common.TYPE_DIALOG.LOI, true);
        }
    }

    private void payOfflineTheBill(PayBillsDialogAdapter.Entity entity, int index, final String edong) {

/**
 *
 * TODO: Thực hiện thanh toán offline .....
 *
 */

        //TODO: Sau khi xong hóa đơn này
        //Khi thực hiện thanh toán thành công 1 hóa dơn
        //update text count billDeleteOnline payed success
        countBillPayedSuccess++;

        //kiểm tra nếu countBillPayedSuccess = tổng số bill được chọn ban đầu ở dialog
        if (index == listBillDialog.size()) {
            if (countBillPayedSuccess == totalBillsChooseDialogTemp)
                mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10001.getMessage(), false, Common.TYPE_DIALOG.THANH_CONG, true);
            else {
                mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10001.getMessage(), false, Common.TYPE_DIALOG.LOI, true);
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy'T'HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());

        if (mPayModel.updatePayOffine(entity.getBillId(), 2, MainActivity.mEdong) != -1) {
            EntityDanhSachThu entityDanhSachThu = mPayModel.selectDebtColectionForDanhSachThu(entity.getBillId());
            entityDanhSachThu.setPayments(2);
            entityDanhSachThu.setPayStatus(2);
            entityDanhSachThu.setStateOfDebt(2);
            entityDanhSachThu.setStateOfCancel("");
            entityDanhSachThu.setStateOfReturn("");
            entityDanhSachThu.setSuspectedProcessingStatus("");
            entityDanhSachThu.setStateOfPush(1);
            entityDanhSachThu.setDateOfPush(currentDateandTime);
            entityDanhSachThu.setCountPrintReceipt(0);
            entityDanhSachThu.setPrintInfo("");
            if (mPayModel.insertDebtCollection(entityDanhSachThu) != -1) {
                EntityLichSuThanhToan entityLichSuThanhToan = mPayModel.selectDebtColectionForLichSu(entity.getBillId());
                entityLichSuThanhToan.setPayments(2);
                entityLichSuThanhToan.setPayStatus(2);
                entityLichSuThanhToan.setStateOfDebt(2);
                entityLichSuThanhToan.setStateOfCancel("");
                entityLichSuThanhToan.setStateOfReturn("");
                entityLichSuThanhToan.setSuspectedProcessingStatus("");
                entityLichSuThanhToan.setStateOfPush(1);
                entityLichSuThanhToan.setDateOfPush(currentDateandTime);
                entityLichSuThanhToan.setCountPrintReceipt(0);
                entityLichSuThanhToan.setPrintInfo("");
                entityLichSuThanhToan.setDateIncurred(currentDateandTime);
                entityLichSuThanhToan.setTradingCode(6);
                if (mPayModel.insertPayLib(entityLichSuThanhToan) != -1) {
                    Log.i("INFO", "");
                }
            }
        }

        //làm mới thanh đếm số bill thành công
        //refreshData lại recyclerview của dialog
        listBillDialog.get(index).setStatus(Common.STATUS_BILLING.DA_THANH_TOAN.getCode());

        ((MainActivity) mIPayView.getContextView()).runOnUiThread(
                new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        totalBillsChooseDialog--;
                        refreshStatusPaySuccessDialog(edong);
                    }
                }
        );
    }

    @Override
    public void refreshTextCountBillPayedSuccess() {
        mIPayView.showTextCountBillsPayedSuccess(countBillPayedSuccess, totalBillsChooseDialogTemp);
    }

    @Override
    public void callShowDialogPay() {
        mIPayView.showDialogPayingOnline();
    }

    @Override
    public void callProcessDeleteBillOnline(String edong, String code, PayAdapter.BillEntityAdapter bill, int posCustomerInside) {
        boolean fail = TextUtils.isEmpty(edong) || TextUtils.isEmpty(code) || bill == null;
        if (fail)
            return;

        mIPayView.showDialogDeleteBillOnline(edong, code, bill, posCustomerInside);
    }

    @Override
    public void callFillInfoBillDeleteDialog(String edong, String code, PayAdapter.BillEntityAdapter bill, int posCustomerInside) {
        boolean fail = TextUtils.isEmpty(edong) || TextUtils.isEmpty(code) || bill == null;
        if (fail)
            return;

        this.billDeleteOnline = bill;
        this.posCustomerListDeleteOnline = posCustomerInside;

        PayAdapter.PayEntityAdapter infoCustomer = mAdapterList.get(posCustomerInside);
        mIPayView.showInfoBillDeleteDialog(code, infoCustomer.getTenKH(), bill.getMonthBill(), bill.getMoneyBill());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void callDeleteOnlineSoap(String edong, String reasonDeleteBill) {
        boolean fail = this.billDeleteOnline == null || posCustomerListDeleteOnline == NEGATIVE_ONE;
        if (fail)
            return;

        if (TextUtils.isEmpty(reasonDeleteBill)) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.CODE_REPONSE_API_CHECK_TRAINS.ex10001.getMessage(), Common.TYPE_DIALOG.LOI);
            return;
        }

        //TODO validate
        //check bill exist
        if (posCustomerListDeleteOnline > mAdapterList.size()) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.CODE_REPONSE_API_CHECK_TRAINS.ex10000.getMessage(), Common.TYPE_DIALOG.LOI);
            return;
        }
        // Kiểm tra trạng thái thanh toán hóa đơn (tương đương API CHECK-TRANS)
        // Nếu hóa đơn không do TNV thanh toán  Thông báo lỗi
        // Nếu hóa đơn không ở trạng thái đã thanh toán sang EVN hoặc chờ xử lý chấm nợ  Thông báo lỗi
        callAPICheckTransAndRequestCancelBill(edong, reasonDeleteBill);
    }

    @Override
    public void callShowDialogBarcode() {
        mIPayView.showDialogBarcode();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void callAPICheckTransAndRequestCancelBill(String edong, String reasonDeleteBill) {

        Context context = mIPayView.getContextView();
        //check wifi
        boolean isHasWifi = Common.isConnectingWifi(mIPayView.getContextView());
        boolean isHasNetwork = Common.isNetworkConnected(mIPayView.getContextView());

//        if (!isHasWifi) {
//            mIPayView.showMessageNotifySearchOnline(Common.MESSAGE_NOTIFY.ERR_WIFI.toString());
//            return;
//        }
        if (!isHasNetwork) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString(), Common.TYPE_DIALOG.LOI);
            return;
        }

        ConfigInfo configInfo = null;
        String versionApp = Common.getVersionApp(context);
        try {
            configInfo = Common.setupInfoRequest(context, edong, Common.COMMAND_ID.CHECK_TRANS.toString(), versionApp);
        } catch (Exception e) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString(), Common.TYPE_DIALOG.LOI);
            return;
        }

        PayAdapter.PayEntityAdapter infoCustomer = mAdapterList.get(posCustomerListDeleteOnline);

        Long amount = billDeleteOnline.getMoneyBill();
        String customerCode = infoCustomer.getMaKH();
        long billId = billDeleteOnline.getBillId();
        String requestDate = billDeleteOnline.getRequestDate();

        //create request to server
        String jsonRequestCheckTrainOnline = SoapAPI.getJsonRequestCheckTrainOnline(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),

                edong,
                amount,
                customerCode,
                billId,
                requestDate,

                configInfo.getAccountId());

        if (jsonRequestCheckTrainOnline == null) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.CODE_REPONSE_API_CHECK_TRAINS.ex10002.getMessage(), Common.TYPE_DIALOG.LOI);
            return;
        }

        mIPayView.showDeleteBillOnlineProcess();
        mIPayView.showPbarDeleteBillOnline();

        try {
            if (soapCheckTrainOnlineRequestCancelBill == null) {
                //if null then create new
                soapCheckTrainOnlineRequestCancelBill = new SoapAPI.AsyncSoapCheckTrainOnline(edong, soapCheckTrainRequestCancelBillCallBack, reasonDeleteBill);
            } else if (soapCheckTrainOnlineRequestCancelBill.getStatus() == AsyncTask.Status.PENDING) {
                //if running not yet then run

            } else if (soapCheckTrainOnlineRequestCancelBill.getStatus() == AsyncTask.Status.RUNNING) {
                //if is running
                soapCheckTrainOnlineRequestCancelBill.setEndCallSoap(true);
                soapCheckTrainOnlineRequestCancelBill.cancel(true);

                handlerDelay.removeCallbacks(runnableCountTimeCheckTrainOnline);
                soapCheckTrainOnlineRequestCancelBill = new SoapAPI.AsyncSoapCheckTrainOnline(edong, soapCheckTrainRequestCancelBillCallBack, reasonDeleteBill);
            } else {
                //if running or finish
                handlerDelay.removeCallbacks(runnableCountTimeCheckTrainOnline);

                soapCheckTrainOnlineRequestCancelBill = new SoapAPI.AsyncSoapCheckTrainOnline(edong, soapCheckTrainRequestCancelBillCallBack, reasonDeleteBill);
            }

            soapCheckTrainOnlineRequestCancelBill.execute(jsonRequestCheckTrainOnline);

            //thread time out
            //sleep
            handlerDelay.postDelayed(runnableCountTimeCheckTrainOnline, TIME_OUT_CONNECT);

        } catch (Exception e) {
            mIPayView.showMessageNotifySearchOnline(e.getMessage(), Common.TYPE_DIALOG.LOI);
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void callAPIUpdateEdongAccount(String edong, PayBillsDialogAdapter.Entity entity) {
        Context context = mIPayView.getContextView();
        //check wifi
        boolean isHasWifi = Common.isConnectingWifi(mIPayView.getContextView());
        boolean isHasNetwork = Common.isNetworkConnected(mIPayView.getContextView());

//        if (!isHasWifi) {
//            mIPayView.showMessageNotifySearchOnline(Common.MESSAGE_NOTIFY.ERR_WIFI.toString());
//            return;
//        }
        if (!isHasNetwork) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString(), Common.TYPE_DIALOG.LOI);
            return;
        }

        ConfigInfo configInfo = null;
        String versionApp = Common.getVersionApp(context);
        try {
            configInfo = Common.setupInfoRequest(context, edong, Common.COMMAND_ID.CHECK_TRANS.toString(), versionApp);
        } catch (Exception e) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString(), Common.TYPE_DIALOG.LOI);
            return;
        }
        Long amount = entity.getAmount();
        String customerCode = entity.getCode();
        long billId = entity.getBillId();
        String requestDate = Common.getDateTimeNow(Common.DATE_TIME_TYPE.ddMMyyyy);

        //create request to server
        String jsonRequestCheckTrainOnline = SoapAPI.getJsonRequestCheckTrainOnline(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),

                edong,
                amount,
                customerCode,
                billId,
                requestDate,

                configInfo.getAccountId());

        if (jsonRequestCheckTrainOnline == null) {
            mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_API_CHECK_TRAINS.ex10002.toString(), false, Common.TYPE_DIALOG.LOI, true);
            return;
        }

        try {
            if (soapCheckTrainOnlineRequestPayingBill == null) {
                //if null then create new
                //Sử dụng lại soap API checkTrain của delete
                soapCheckTrainOnlineRequestPayingBill = new SoapAPI.AsyncSoapCheckTrainPayingBill(edong, soapCheckTrainPayingBillCallBack, entity);
            } else if (soapCheckTrainOnlineRequestPayingBill.getStatus() == AsyncTask.Status.PENDING) {
                //if running not yet then run

            } else if (soapCheckTrainOnlineRequestPayingBill.getStatus() == AsyncTask.Status.RUNNING) {
                //if is running
                soapCheckTrainOnlineRequestPayingBill.setEndCallSoap(true);
                soapCheckTrainOnlineRequestPayingBill.cancel(true);

                handlerDelay.removeCallbacks(runnableCountTimeCheckTrainOnline);
                soapCheckTrainOnlineRequestPayingBill = new SoapAPI.AsyncSoapCheckTrainPayingBill(edong, soapCheckTrainPayingBillCallBack, entity);
            } else {
                //if running or finish
                handlerDelay.removeCallbacks(runnableCountTimeCheckTrainOnline);
                soapCheckTrainOnlineRequestPayingBill = new SoapAPI.AsyncSoapCheckTrainPayingBill(edong, soapCheckTrainPayingBillCallBack, entity);
            }

            soapCheckTrainOnlineRequestPayingBill.execute(jsonRequestCheckTrainOnline);

            //thread time out
            //sleep
            handlerDelay.postDelayed(runnableCountTimeCheckTrainOnline, TIME_OUT_CONNECT);

        } catch (Exception e) {
            mIPayView.showMessageNotifyBillOnlineDialog(e.getMessage(), false, Common.TYPE_DIALOG.LOI, true);
            return;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void payOnlineTheBill(String versionApp, String edong, PayBillsDialogAdapter.Entity entity) {
        ConfigInfo configInfo = null;
        try {
            configInfo = Common.setupInfoRequest(mIPayView.getContextView(), edong, Common.COMMAND_ID.BILLING.toString(), versionApp);
        } catch (Exception e) {
            mIPayView.showMessageNotifyBillOnlineDialog(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString(), false, Common.TYPE_DIALOG.LOI, true);
            return;
        }

        //delete all message Error fill in listBillDialog and refreshData
        int index = 0;
        int maxIndex = listBillDialog.size();
        for (; index < maxIndex; index++) {
            listBillDialog.get(index).setMessageError(TEXT_EMPTY);
        }
        refreshStatusPaySuccessDialog(edong);

        //Số ví edong thực hiện thanh toán
        String phone = edong;
        Long amount = entity.getAmount();

        //Phiên làm việc của TNV đang thực hiện
        String session = mPayModel.getSession(edong);

        /**
         * Luồng quầy thu đang năng : DT0813
         * Luồng tài khoản tiền điện : DT0807
         * Còn lại sử dụng DT0605
         *
         * ECPAY mặc định sử dụng DT0605
         */

        String partnerCode = PARTNER_CODE_DEFAULT;

        String providerCode = PROVIDER_DEFAULT;

        //create request to server
        String jsonRequestBillOnline = SoapAPI.getJsonRequestBillOnline(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),
                entity.getCode(),
                session,
                entity.getBillId(),
                amount,
                phone,
                providerCode,
                partnerCode,
                configInfo.getAccountId());

        if (jsonRequestBillOnline == null)
            return;

        //TODO Trước khi gọi API thanh toán online
        /**
         * Thanh toán hóa đơn trong Danh sách hóa đơn nợ đã lưu trong máy
         * update các thông tin vào danh sách hóa đơn nợ bill
         * Cập nhật Danh sách hóa đơn nợ đã lưu trong máy
         * Trạng thái thanh toán status = 02_Đã thanh toán (trong database status = 1)
         * Ví thanh toán edong = ví đăng nhập
         */

        int rowAffect = mPayModel.updateBillWith(
                edong, entity.getBillId(), //where
                Common.STATUS_BILLING.DA_THANH_TOAN.getCode(), edong);
        Log.d(TAG, "rowAffect updateBillWith = " + rowAffect);

        //Lưu thông tin háo đơn vào danh sách thu: các thông tin như danh sách hóa đơn nợ, chỉ khác bổ sung thêm
        /**
         * Ví thanh toán edong = ví đăng nhập
         * Hình thức thanh toán payments = 02_Offline
         * Trạng thái thanh toán payStatus = 02_Đã thanh toán (trong database payStatus = 1)
         * Trạng thái chấm nợ stateOfDebt = 02_Chưa chấm (thực tế trong SRS viết nhầm, phải là 01_Chưa chấm)
         * Trạng thái hủy stateOfCancel = null
         * Trạng thái hoàn trả stateOfReturn = null
         * Trạng thái xử lý nghi ngờ suspectedProcessingStatus = null
         * Trạng thái đẩy chấm nợ stateOfPush = 01_Chưa đẩy
         * Ngày đẩy dateOfPush = null
         * Số lần in biên nhận countPrintReceipt = 0
         * In thông báo điện printInfo = null
         */

        rowAffect = mPayModel.updateBillDebtWith(
                edong, entity.getBillId(), edong, //where
                Common.PAYMENT_MODE.OFFLINE.getCode(), Common.STATUS_BILLING.DA_THANH_TOAN.getCode(), Common.STATE_OF_DEBT.CHUA_CHAM.getCode(),
                Common.STATE_OF_CANCEL.NULL.getCode(), Common.STATE_OF_RETURN.NULL.getCode(), Common.SUSPECTED_PROCESSING_STATUS.NULL.getCode(),
                Common.STATE_OF_PUSH.CHUA_DAY.getCode(), null, ZERO, Common.STATUS_OF_PRINT_INFO.NULL.getCode());

        Log.d(TAG, "rowAffect updateBillDebtWith = " + rowAffect);

        //Lưu thông tin hóa đơn vào Danh sách lịch sử: các thông tin nhu Danh sách thu, chỉ khác bổ sung thêm thông tin
        /**
         * Ngày phát sinh dateIncurred = ngày thanh toán hóa đơn
         * Mã giao dịch tradingCode = 10_Đẩy chấm nợ
         */

        rowAffect = mPayModel.updateBillHistoryWith(
                edong, entity.getBillId(),//where
                Common.getDateTimeNow(Common.DATE_TIME_TYPE.ddMMyyyy), Common.TRADING_CODE.DAY_CHAM_NO.getCode());
        Log.d(TAG, "rowAffect updateBillHistoryWith = " + rowAffect);

        try {
            //get position stack of  List<SoapAPI.AsyncSoapBillOnline> asyntask object
            final int positionIndex = billOnlineAsyncList.size();

            Handler handlerDelayBillOnline = new Handler();
            soapBillOnline = new SoapAPI.AsyncSoapBillOnline(edong, soapBillOnlineCallBack, handlerDelayBillOnline, positionIndex, entity);

            //add to last index and free soapBillOnline
            billOnlineAsyncList.add(soapBillOnline);

            //call set limit time out
            Runnable runnableCountTimeBillOnline = new Runnable() {
                @Override
                public void run() {
                    if (positionIndex >= billOnlineAsyncList.size())
                        return;
                    SoapAPI.AsyncSoapBillOnline soapBillOnline = billOnlineAsyncList.get(positionIndex);
                    if (soapBillOnline == null) {
                        mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.e9999.getMessage(), false, Common.TYPE_DIALOG.LOI, true);
                        Log.e(TAG, "run: at runnableCountTimeBillOnline has soapBillOnline is null!");
                        return;
                    }
                    if (soapBillOnline.isEndCallSoap())
                        return;

                    soapBillOnline.callCountdown(soapBillOnline);
                    //Do something after 100ms
                   /* BillingOnlineRespone billingOnlineRespone = soapBillOnline.getBillingOnlineRespone();

                    if (billingOnlineRespone == null) {
                        //call time out
                        soapBillOnline.callCountdown(soapBillOnline);
                    }*/
                }
            };

            //free param private common in class
            soapBillOnline = null;

            //count down
            billOnlineAsyncList.get(positionIndex).getHandlerDelay().postDelayed(runnableCountTimeBillOnline, TIME_OUT_CONNECT);

            //run
            billOnlineAsyncList.get(positionIndex).execute(jsonRequestBillOnline);
        } catch (Exception e) {
            mIPayView.showMessageNotifyBillOnlineDialog(e.getMessage(), false, Common.TYPE_DIALOG.LOI, true);
            return;
        }
    }

    @Override
    public void cancelSeachOnline() {
        if (soapSearchOnline != null && !soapSearchOnline.isEndCallSoap()) {
            soapSearchOnline.setEndCallSoap(true);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void reseachOnline(String edong) {
        cancelSeachOnline();
        if (soapSearchOnline != null) {
            mIPayView.showEditTextSearch(soapSearchOnline.getInfoSearch());
            callSearchOnline(edong, soapSearchOnline.getInfoSearch(), true);
        } else {
            mIPayView.showMessageNotifySearchOnline(Common.CODE_REPONSE_SEARCH_ONLINE.e9999.getMessage(), Common.TYPE_DIALOG.LOI);
            Log.e(TAG, "reseachOnline: soapSearchOnline không được khởi tạo");
        }
    }

    @Override
    public void callProcessDataBillFragmentChecked(String edong, String code, int posCustomer, PayAdapter.BillEntityAdapter bill, int posBillInside, int indexBegin, int indexEnd) {
        if (TextUtils.isEmpty(edong))
            return;
        if (TextUtils.isEmpty(code))
            return;
        if (bill == null)
            return;
        if (indexEnd > mAdapterList.size())
            return;

        mPayModel.updateBillIsChecked(edong, code, (int) bill.getBillId(), bill.isChecked());

        //show billDeleteOnline or not show billDeleteOnline
        mPayModel.updateCustomerIsShowBill(edong, code, bill.isChecked());

        mAdapterList.get(indexBegin + posCustomer).setShowBill(bill.isChecked());

        //show total bills and total money of all bills is checked
        refreshTotalBillsAndTotalMoneyInFragment(edong, Common.STATUS_BILLING.CHUA_THANH_TOAN);
    }

    @Override
    public void callPayRecyclerDialog(String edong) {
        if (TextUtils.isEmpty(edong))
            return;

        listBillDialog = mPayModel.getAllBillOfCustomerCheckedWithStatusPay(edong, Common.STATUS_BILLING.CHUA_THANH_TOAN);
        refreshTotalBillsAndTotalMoneyInDialogWhenChecked(edong, Common.STATUS_BILLING.CHUA_THANH_TOAN);

        mIPayView.showPayRecyclerListBills(listBillDialog);
    }

    @Override
    public void callProcessDataBillDialogChecked(String edong, int pos, boolean isChecked) {
        if (listBillCheckedFragment.size() <= pos)
            return;

        listBillDialog.get(pos).setChecked(isChecked);
        refreshTotalBillsAndTotalMoneyInDialogWhenChecked(edong, Common.STATUS_BILLING.CHUA_THANH_TOAN);
    }

    private void refreshTotalBillsAndTotalMoneyInFragment(String edong, Common.STATUS_BILLING statusBilling) {
        totalBillsFragment = 0;
        totalMoneyFragment = 0;
        listBillCheckedFragment.clear();

        listBillCheckedFragment = mPayModel.getAllBillOfCustomerCheckedWithStatusPay(edong, statusBilling);
        totalBillsFragment = mPayModel.countAllBillsIsCheckedWithStatusPay(edong, statusBilling);
        totalMoneyFragment = mPayModel.countMoneyAllBillsIsCheckedWithStatusPay(edong, statusBilling);

        mIPayView.showCountBillsAndTotalMoneyFragment(totalBillsFragment, totalMoneyFragment);
    }

    private void refreshTotalBillsAndTotalMoneyInDialogWhenChecked(String edong, Common.STATUS_BILLING statusBilling) {
//        totalBillsChooseDialog = mPayModel.countAllBillsIsCheckedWithStatusPay(edong, statusBilling);
//        totalMoneyBillChooseDialog = mPayModel.countMoneyAllBillsIsCheckedWithStatusPay(edong, statusBilling);

        totalBillsChooseDialog = 0;
        totalMoneyBillChooseDialog = 0;
        for (PayBillsDialogAdapter.Entity bill : listBillDialog) {
            if (bill.isChecked()) {
                totalBillsChooseDialog++;
                totalMoneyBillChooseDialog += bill.getAmount();
            }
        }
        mIPayView.showCountBillsAndTotalMoneyInDialog(totalBillsChooseDialog, totalMoneyBillChooseDialog);
    }

    //region search online
    private SoapAPI.AsyncSoapSearchOnline.AsyncSoapSearchOnlineCallBack soapSearchOnlineCallBack = new SoapAPI.AsyncSoapSearchOnline.AsyncSoapSearchOnlineCallBack() {
        private Common.TYPE_SEARCH typeSearch;
        private String edong;
        private String infoSearch;

        @Override
        public void onPre(final SoapAPI.AsyncSoapSearchOnline soapSearchOnline) {
            typeSearch = soapSearchOnline.getTypeSearch();
            edong = soapSearchOnline.getEdong();
            infoSearch = soapSearchOnline.getInfoSearch();

            mIPayView.showSearchOnlineProcess();

            //check wifi
            boolean isHasWifi = Common.isConnectingWifi(mIPayView.getContextView());
            boolean isHasNetwork = Common.isNetworkConnected(mIPayView.getContextView());

//            if (!isHasWifi) {
//                mIPayView.showMessageNotifySearchOnline(Common.MESSAGE_NOTIFY.ERR_WIFI.toString());
//
//                soapSearchOnline.setEndCallSoap(true);
//                soapSearchOnline.cancel(true);
//            }
            if (!isHasNetwork) {
                mIPayView.showMessageNotifySearchOnline(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString(), Common.TYPE_DIALOG.LOI);

                soapSearchOnline.setEndCallSoap(true);
                soapSearchOnline.cancel(true);
            }
        }

        @Override
        public void onUpdate(final String message) {
            if (message == null || message.isEmpty() || message.trim().equals(""))
                return;

            ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIPayView.showMessageNotifySearchOnline(message, Common.TYPE_DIALOG.LOI);
                }
            });
        }

        @Override
        public void onPost(SearchOnlineResponse response) {
            if (response == null) {
                return;
            }

            Common.CODE_REPONSE_SEARCH_ONLINE codeResponse = Common.CODE_REPONSE_SEARCH_ONLINE.findCodeMessage(response.getFooterSearchOnlineResponse().getResponseCode());
            if (codeResponse != Common.CODE_REPONSE_SEARCH_ONLINE.e000) {
                mIPayView.showMessageNotifySearchOnline(codeResponse.getMessage(), Common.TYPE_DIALOG.LOI);
                return;
            }

            //get responseLoginResponse from body response
            //because server return string not object
            String customerData = response.getBodySearchOnlineResponse().getCustomer();

            // định dạng kiểu Object JSON
            CustomerInsideBody customerResponse = null;
            try {
                customerResponse = new Gson().fromJson(customerData, CustomerInsideBody.class);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

            if (customerResponse == null)
                return;

            int rowEffect = mPayModel.writeSQLiteCustomerTableFromSearchOnline(edong, customerResponse);
            if (rowEffect == ERROR_OCCUR) {
                Log.d(TAG, "Cannot insert customer to database.");
            }

            List<BillInsideCustomer> listBill = customerResponse.getListBill();
            if (listBill == null)
                return;
            int index = 0;
            int indexMax = listBill.size();
            boolean isOccurInsertBill = false;

            for (; index < indexMax; index++) {
                rowEffect = mPayModel.writeSQliteBillTableOfCustomer(edong, listBill.get(index));

                if (rowEffect == ERROR_OCCUR) {
                    isOccurInsertBill = true;
                }
            }

            if (isOccurInsertBill) {
                Log.d(TAG, "Has several billDeleteOnline of customer cannot insert to database.");
            }

            mIPayView.hideSearchOnlineProcess();
            callPayRecycler(edong, PayFragment.FIRST_PAGE_INDEX, mTypeSearch, infoSearch, false);
        }

        @Override
        public void onTimeOut(final SoapAPI.AsyncSoapSearchOnline soapSearchOnlineCallBack) {
            soapSearchOnlineCallBack.cancel(true);

            //thread call asyntask is running. must call in other thread to update UI
            ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!soapSearchOnlineCallBack.isEndCallSoap()) {
                        mIPayView.showMessageNotifySearchOnline(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString(), Common.TYPE_DIALOG.LOI);
                    }
                }
            });
        }
    };

    private Runnable runnableCountTimeSearchOnline = new Runnable() {
        @Override
        public void run() {
            if (soapSearchOnline != null && soapSearchOnline.isEndCallSoap())
                return;
            //Do something after 100ms
            SearchOnlineResponse searchOnlineResponse = soapSearchOnline.getSearchOnlineResponse();

            if (searchOnlineResponse == null && !soapSearchOnline.isEndCallSoap()) {
                //call time out
                soapSearchOnline.callCountdown(soapSearchOnline);
            }
        }
    };
    //endregion

    //region check train online
    private SoapAPI.AsyncSoapCheckTrainOnline.AsyncSoapCheckTrainOnlineCallBack soapCheckTrainRequestCancelBillCallBack = new SoapAPI.AsyncSoapCheckTrainOnline.AsyncSoapCheckTrainOnlineCallBack() {
        private String edong;
        private String reasonDeleteBill;

        @Override
        public void onPre(final SoapAPI.AsyncSoapCheckTrainOnline soapSearchOnline) {
            edong = soapSearchOnline.getEdong();
            reasonDeleteBill = soapSearchOnline.getReasonDeleteBill();

            mIPayView.showDeleteBillOnlineProcess();
            mIPayView.visibleButtonDeleteDialog(HIDE_COUNTINUE);

            //check wifi
            boolean isHasWifi = Common.isConnectingWifi(mIPayView.getContextView());
            boolean isHasNetwork = Common.isNetworkConnected(mIPayView.getContextView());

//            if (!isHasWifi) {
//                mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_WIFI.toString());
//                soapSearchOnline.setEndCallSoap(true);
//                soapSearchOnline.cancel(true);
//            }
            if (!isHasNetwork) {
                mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString(), Common.TYPE_DIALOG.LOI);

                soapSearchOnline.setEndCallSoap(true);
                soapSearchOnline.cancel(true);
            }
        }

        @Override
        public void onUpdate(final String message) {
            if (message == null || message.isEmpty() || message.trim().equals(""))
                return;

            ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIPayView.showMessageNotifyDeleteOnlineDialog(message, Common.TYPE_DIALOG.LOI);
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPost(CheckTrainOnlineResponse response) {
            if (response == null) {
                return;
            }

            // Kiểm tra trạng thái thanh toán hóa đơn (tương đương API CHECK-TRANS)
            // Nếu hóa đơn không do TNV thanh toán  Thông báo lỗi
            // Nếu hóa đơn không ở trạng thái đã thanh toán sang EVN hoặc chờ xử lý chấm nợ  Thông báo lỗi

            Common.CODE_REPONSE_API_CHECK_TRAINS codeResponse = Common.CODE_REPONSE_API_CHECK_TRAINS.findCodeMessage(response.getFooter().getResponseCode());
            if (codeResponse != Common.CODE_REPONSE_API_CHECK_TRAINS.eBILLING) {
                mIPayView.showMessageNotifyDeleteOnlineDialog(codeResponse.getMessage(), Common.TYPE_DIALOG.LOI);
                mIPayView.visibleButtonDeleteDialog(SHOW_ALL);
                return;
            }

            callAPITransationCancellation(edong, response, reasonDeleteBill);

        }

        @Override
        public void onTimeOut(final SoapAPI.AsyncSoapCheckTrainOnline soapCheckTrainOnlineCallBack) {
            soapCheckTrainOnlineCallBack.cancel(true);

            //thread call asyntask is running. must call in other thread to update UI
            ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!soapCheckTrainOnlineCallBack.isEndCallSoap()) {
                        mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString(), Common.TYPE_DIALOG.LOI);
                    }
                }
            });
        }
    };

    private SoapAPI.AsyncSoapDeleteBillOnline.AsyncSoapDeleteBillOnlineCallBack soapDeleteBillOnlineCallBack = new SoapAPI.AsyncSoapDeleteBillOnline.AsyncSoapDeleteBillOnlineCallBack() {
        private String edong;
        private String reasonDeleteBill;
        private String code;
        private Long billId;

        @Override
        public void onPre(final SoapAPI.AsyncSoapDeleteBillOnline soapDeleteBillOnline) {
            edong = soapDeleteBillOnline.getEdong();
            reasonDeleteBill = soapDeleteBillOnline.getCauseDeleteBill();
            code = soapDeleteBillOnline.getCode();
            billId = soapDeleteBillOnline.getBillId();

            mIPayView.showDeleteBillOnlineProcess();
            mIPayView.visibleButtonDeleteDialog(HIDE_COUNTINUE);

            //check wifi
            boolean isHasWifi = Common.isConnectingWifi(mIPayView.getContextView());
            boolean isHasNetwork = Common.isNetworkConnected(mIPayView.getContextView());

//            if (!isHasWifi) {
//                mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_WIFI.toString());
//                soapDeleteBillOnline.setEndCallSoap(true);
//                soapDeleteBillOnline.cancel(true);
//            }
            if (!isHasNetwork) {
                mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString(), Common.TYPE_DIALOG.LOI);

                soapDeleteBillOnline.setEndCallSoap(true);
                soapDeleteBillOnline.cancel(true);
            }
        }

        @Override
        public void onUpdate(final String message) {
            if (message == null || message.isEmpty() || message.trim().equals(""))
                return;

            ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIPayView.showMessageNotifyDeleteOnlineDialog(message, Common.TYPE_DIALOG.LOI);
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPost(DeleteBillOnlineRespone response) {
            if (response == null) {
                return;
            }

            // Kiểm tra trạng thái thanh toán hóa đơn (tương đương API CHECK-TRANS)
            // Nếu hóa đơn không do TNV thanh toán  Thông báo lỗi
            // Nếu hóa đơn không ở trạng thái đã thanh toán sang EVN hoặc chờ xử lý chấm nợ  Thông báo lỗi

            Common.CODE_REPONSE_TRANSACTION_CANCELLATION codeResponse = Common.CODE_REPONSE_TRANSACTION_CANCELLATION.findCodeMessage(response.getFooter().getResponseCode());
            if (codeResponse != Common.CODE_REPONSE_TRANSACTION_CANCELLATION.e000) {
                mIPayView.showMessageNotifyDeleteOnlineDialog(codeResponse.getMessage(), Common.TYPE_DIALOG.LOI);
                mIPayView.visibleButtonDeleteDialog(SHOW_ALL);
                return;
            }

            //Cập nhật thông tin hủy hóa đơn trên danh sách hóa đơn
            mPayModel.updateBillReasonDelete(edong, code, billId, reasonDeleteBill, Common.STATUS_BILLING.DANG_CHO_HUY);

//            mPayModel.updateBillStatusCancelBillOnline()
            mIPayView.showMessageNotifyDeleteOnlineDialog(codeResponse.getMessage(), Common.TYPE_DIALOG.THANH_CONG);
            mIPayView.visibleButtonDeleteDialog(HIDE_COUNTINUE);
        }

        @Override
        public void onTimeOut(final SoapAPI.AsyncSoapDeleteBillOnline soapDeleteBillOnline) {
            soapDeleteBillOnline.cancel(true);

            //thread call asyntask is running. must call in other thread to update UI
            ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!soapDeleteBillOnline.isEndCallSoap()) {
                        mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString(), Common.TYPE_DIALOG.LOI);
                    }
                }
            });
        }
    };

    private SoapAPI.AsyncSoapCheckTrainPayingBill.AsyncSoapCheckTrainPayingBillCallBack soapCheckTrainPayingBillCallBack = new SoapAPI.AsyncSoapCheckTrainPayingBill.AsyncSoapCheckTrainPayingBillCallBack() {
        private String edong;
        private PayBillsDialogAdapter.Entity entity;

        @Override

        public void onPre(final SoapAPI.AsyncSoapCheckTrainPayingBill soapCheckTrainPayingBill) {
            edong = soapCheckTrainPayingBill.getEdong();
            entity = soapCheckTrainPayingBill.getEntity();

            mIPayView.showPayingRViewDialogStart();

            //check wifi
            boolean isHasWifi = Common.isConnectingWifi(mIPayView.getContextView());
            boolean isHasNetwork = Common.isNetworkConnected(mIPayView.getContextView());

//            if (!isHasWifi) {
//                mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_WIFI.toString());
//                soapSearchOnline.setEndCallSoap(true);
//                soapSearchOnline.cancel(true);
//            }
            if (!isHasNetwork) {
                mIPayView.showMessageNotifyBillOnlineDialog(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString(), false, Common.TYPE_DIALOG.LOI, false);

                soapCheckTrainPayingBill.setEndCallSoap(true);
                soapCheckTrainPayingBill.cancel(true);
            }
        }

        @Override
        public void onUpdate(final String message) {
            if (message == null || message.isEmpty() || message.trim().equals(""))
                return;

            ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIPayView.showMessageNotifyBillOnlineDialog(message, false, Common.TYPE_DIALOG.LOI, false);
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onPost(CheckTrainOnlineResponse response) {
            if (response == null) {
                return;
            }

            //TODO Xử lý nhận kết quả thanh toán các hóa đơn từ server ----- Nếu không thành công
            Common.CODE_REPONSE_API_CHECK_TRAINS codeResponse = Common.CODE_REPONSE_API_CHECK_TRAINS.findCodeMessage(response.getFooter().getResponseCode());
            long billId = response.getBody().getBillId();
            String date = Common.getDateTimeNow(Common.DATE_TIME_TYPE.ddMMyyyy);

            if (codeResponse.getCode() == Common.CODE_REPONSE_API_CHECK_TRAINS.eSOURCE_OTHER.getCode()) {
                /**
                 * Trường hợp hóa đơn đã được thanh toán bởi nguồn khác: Không thực hiện thanh toán hóa đơn
                 * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
                 * Ví thanh toán edong = null
                 * Trạng thái thanh toán status = 03_Đã thanh toán bởi nguồn khác (Trong database = 2)
                 */
                int rowAffect = mPayModel.updateBillWith(
                        edong, (int) billId, //where
                        Common.STATUS_BILLING.DA_THANH_TOAN_BOI_NGUON_KHAC.getCode(), edong);
                Log.d(TAG, "rowAffect updateBillWith = " + rowAffect);

                /**
                 * Lưu thông tin hóa đơn vào Danh sách lịch sử (Bảng History): các thông tin như Danh sách hóa đơn nợ, chỉ khác bổ sung thêm:
                 * Ví thanh toán edong = null
                 * Hình thức thanh toán payments = null
                 * Trạng thái thanh toán payStatus = 03_Đã thanh toán bởi nguồn khác(trong database payStatus = 2)
                 * Trạng thái chấm nợ stateOfDebt = null
                 * Trạng thái hủy stateOfCancel = null
                 * Trạng thái xử lý nghi ngờ suspectedProcessingStatus = null
                 * Trạng thái hoàn trả stateOfReturn = null
                 * Số lần in biên nhận countPrintReceipt = 0
                 * In thông báo điện printInfo = null
                 * Ngày đẩy dateOfPush = ngày thanh toán hóa đơn
                 * Mã giao dịch tradeCode = 02_Cập nhật trạng thái nợ (SRS update thêm trường này)
                 *
                 * Trạng thái đẩy chấm nợ stateOfPush = (SRS không yêu cầu cập nhật trường này nên sẽ ko update)
                 */

                rowAffect = mPayModel.updateBillDebtWithThanhToanBoiNguonKhacOrViKhac(
                        edong, (int) billId, null, //where
                        Common.PAYMENT_MODE.NULL.getCode(), Common.STATUS_BILLING.DA_THANH_TOAN_BOI_NGUON_KHAC.getCode(), Common.STATE_OF_DEBT.NULL.getCode(),
                        Common.STATE_OF_CANCEL.NULL.getCode(), Common.STATE_OF_RETURN.NULL.getCode(), Common.SUSPECTED_PROCESSING_STATUS.NULL.getCode(),
                        date, ZERO, Common.STATUS_OF_PRINT_INFO.NULL.getCode(), Common.TRADING_CODE.CAP_NHAT_TRANG_THAI_NO.getCode());

                Log.d(TAG, "rowAffect updateBillDebtWithThanhToanBoiNguonKhacOrViKhac = " + rowAffect);

                return;
            }

            if (codeResponse.getCode() == Common.CODE_REPONSE_API_CHECK_TRAINS.eEDONG_OTHER.getCode()) {
                /**
                 * Trường hợp hóa đơn đã được thanh toán bởi ví khác: Không thực hiện thanh toán hóa đơn
                 * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
                 * Ví thanh toán edong = Ví thanh toán do server trả về
                 * Trạng thái thanh toán status = 04_Đã thanh toán bởi ví khác (Trong database = 3)
                 */
                int rowAffect = mPayModel.updateBillWith(
                        edong, (int) billId, //where
                        Common.STATUS_BILLING.DA_THANH_TOAN_BOI_VI_KHAC.getCode(), response.getBody().getEdong());
                Log.d(TAG, "rowAffect updateBillWith = " + rowAffect);


                /**
                 * Lưu thông tin hóa đơn vào Danh sách lịch sử (Bảng History): các thông tin như Danh sách hóa đơn nợ, chỉ khác bổ sung thêm:
                 * Ví thanh toán edong = Ví thanh toán do server trả về
                 * Hình thức thanh toán payments = null
                 * Trạng thái thanh toán payStatus = 04_Đã thanh toán bởi ví khác(trong database payStatus = 3)
                 * Trạng thái chấm nợ stateOfDebt = null
                 * Trạng thái hủy stateOfCancel = null
                 * Trạng thái xử lý nghi ngờ suspectedProcessingStatus = null
                 * Trạng thái hoàn trả stateOfReturn = null
                 * Số lần in biên nhận countPrintReceipt = 0
                 * In thông báo điện printInfo = null
                 * Ngày đẩy dateOfPush = ngày thanh toán hóa đơn
                 * Mã giao dịch tradeCode = 02_Cập nhật trạng thái nợ (SRS update thêm trường này)
                 *
                 * Trạng thái đẩy chấm nợ stateOfPush = (SRS không yêu cầu cập nhật trường này nên sẽ ko update)
                 */

                rowAffect = mPayModel.updateBillDebtWithThanhToanBoiNguonKhacOrViKhac(
                        edong, (int) billId,//where
                        response.getBody().getEdong(),
                        Common.PAYMENT_MODE.NULL.getCode(), Common.STATUS_BILLING.DA_THANH_TOAN_BOI_VI_KHAC.getCode(), Common.STATE_OF_DEBT.NULL.getCode(),
                        Common.STATE_OF_CANCEL.NULL.getCode(), Common.STATE_OF_RETURN.NULL.getCode(), Common.SUSPECTED_PROCESSING_STATUS.NULL.getCode(),
                        date, ZERO, Common.STATUS_OF_PRINT_INFO.NULL.getCode(), Common.TRADING_CODE.CAP_NHAT_TRANG_THAI_NO.getCode());

                Log.d(TAG, "rowAffect updateBillDebtWithThanhToanBoiNguonKhacOrViKhac = " + rowAffect);

                return;
            }

            if (codeResponse.getCode() == Common.CODE_REPONSE_API_CHECK_TRAINS.eERROR.getCode()) {
                /**
                 * Hóa đơn chấm nợ lỗi: Không thực hiện thanh toán hóa đơn
                 * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
                 * Ví thanh toán edong = Ví đăng nhập
                 * Trạng thái thanh toán status = (SRS không yêu cầu cập nhật trường này)
                 */
                int rowAffect = mPayModel.updateBillWithWithThanhToanError(
                        edong, (int) billId, //where
                        edong);
                Log.d(TAG, "rowAffect updateBillWithWithThanhToanError = " + rowAffect);


                /**
                 * Lưu thông tin hóa đơn vào Danh sách thu (Bảng Debt): các thông tin như Danh sách hóa đơn nợ, chỉ khác bổ sung thêm:
                 * Ví thanh toán edong = Ví đăng nhập
                 * Hình thức thanh toán payments = 01_Online
                 * Trạng thái thanh toán payStatus = 02_Đã thanh toán(trong database payStatus = 1 tương đương STATUS_BILLING)
                 * Trạng thái chấm nợ stateOfDebt = 04_Chấm lỗi
                 * Trạng thái hủy stateOfCancel = null
                 * Trạng thái xử lý nghi ngờ suspectedProcessingStatus = null
                 * Trạng thái hoàn trả stateOfReturn = null
                 * Số lần in biên nhận countPrintReceipt = 0
                 * In thông báo điện printInfo = null
                 */

                rowAffect = mPayModel.updateBillDebtWithThanhToanErrorOrSuccess(
                        edong, (int) billId,//where
                        edong,
                        Common.PAYMENT_MODE.ONLINE.getCode(), Common.STATUS_BILLING.DA_THANH_TOAN.getCode(), Common.STATE_OF_DEBT.CHAM_LOI.getCode(),
                        Common.STATE_OF_CANCEL.NULL.getCode(), Common.STATE_OF_RETURN.NULL.getCode(), Common.SUSPECTED_PROCESSING_STATUS.NULL.getCode(),
                        ZERO, Common.STATUS_OF_PRINT_INFO.NULL.getCode());

                Log.d(TAG, "rowAffect updateBillDebtWithThanhToanErrorOrSuccess = " + rowAffect);


                /**
                 * Lưu thông tin hóa đơn vào Danh sách lịch sử: các thông tin như Danh sách thu, chỉ khác bổ sung thêm các thông tin
                 * Ngày phát sinh dateIncurred = ngày thanh toán hóa đơn
                 * Mã giao dịch tradeCode = 05_Thanh toán Online_Chấm nợ lỗi
                 */

                rowAffect = mPayModel.updateBillHistoryWithThanhToanErrorOrSuccess(
                        edong, (int) billId,//where
                        edong,
                        Common.PAYMENT_MODE.ONLINE.getCode(), Common.STATUS_BILLING.DA_THANH_TOAN.getCode(), Common.STATE_OF_DEBT.CHAM_LOI.getCode(),
                        Common.STATE_OF_CANCEL.NULL.getCode(), Common.STATE_OF_RETURN.NULL.getCode(), Common.SUSPECTED_PROCESSING_STATUS.NULL.getCode(),
                        ZERO, Common.STATUS_OF_PRINT_INFO.NULL.getCode(), date, Common.TRADING_CODE.THANH_TOAN_ONLINE_CHAM_NO_LOI.getCode());

                Log.d(TAG, "rowAffect updateBillHistoryWithThanhToanErrorOrSuccess = " + rowAffect);
                return;
            }

            // Kiểm tra trạng thái thanh toán hóa đơn (tương đương API CHECK-TRANS)
            // Nếu hóa đơn không do TNV thanh toán  Thông báo lỗi
            // Nếu hóa đơn không ở trạng thái đã thanh toán sang EVN hoặc chờ xử lý chấm nợ  Thông báo lỗi

            if (codeResponse != Common.CODE_REPONSE_API_CHECK_TRAINS.eBILLING) {
                mIPayView.showMessageNotifyDeleteOnlineDialog(codeResponse.getMessage(), Common.TYPE_DIALOG.LOI);
                mIPayView.visibleButtonDeleteDialog(SHOW_ALL);
                return;
            }

            payOnlineTheBill(getVersionApp(mIPayView.getContextView()), edong, entity);
        }

        @Override
        public void onTimeOut(final SoapAPI.AsyncSoapCheckTrainPayingBill soapCheckTrainPayingBill) {
            soapCheckTrainPayingBill.cancel(true);

            //thread call asyntask is running. must call in other thread to update UI
            ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!soapCheckTrainPayingBill.isEndCallSoap()) {
                        mIPayView.showMessageNotifyBillOnlineDialog(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString(), false, Common.TYPE_DIALOG.LOI, false);
                    }
                }
            });
        }
    };

    private SoapAPI.AsyncSoapBillOnline.AsyncSoapBillOnlineCallBack soapBillOnlineCallBack = new SoapAPI.AsyncSoapBillOnline.AsyncSoapBillOnlineCallBack() {
        private String edong;
        PayBillsDialogAdapter.Entity entity;

        @Override
        public void onPre(final SoapAPI.AsyncSoapBillOnline soapBillOnline) {
            edong = soapBillOnline.getEdong();
            entity = soapBillOnline.getEntity();

            mIPayView.showPayingRViewDialogStart();

            //check wifi
            boolean isHasWifi = Common.isConnectingWifi(mIPayView.getContextView());
            boolean isHasNetwork = Common.isNetworkConnected(mIPayView.getContextView());

//            if (!isHasWifi) {
//                mIPayView.showMessageNotifySearchOnline(Common.MESSAGE_NOTIFY.ERR_WIFI.toString());
//
//                soapBillOnline.setEndCallSoap(true);
//                soapBillOnline.cancel(true);
//            }
            if (!isHasNetwork) {
                mIPayView.showMessageNotifyBillOnlineDialog(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString(), false, Common.TYPE_DIALOG.LOI, false);

                soapBillOnline.setEndCallSoap(true);
                soapBillOnline.cancel(true);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onUpdate(final String message, final int positionIndex) {
            if (message == null || message.isEmpty() || message.trim().equals(""))
                return;

            /**
             * Các trường hợp không nhận được kết quả trả về từ Server
             * Connection Reset: Client gửi giao dịch lên Server vào đúng thời điểm Server khởi động lại
             * Connection Refused: Client gửi giao dịch lên Server vào đúng thời điểm Service bị dừng hoạt dộng
             * Connection Timeout: Client gửi giao dịch lên Server nhưng không nhận được kết quả trả lời của Server
             */

            processCasePayedBySupected(null, positionIndex, edong, entity);

            //update text count billDeleteOnline payed success
            countBillPayedSuccess++;
            totalAmountBillPayedSuccess += entity.getAmount();
            /**
             * Hiển thị thông tin thanh toán thành công lên màn hình
             * Số hóa đơn = số hóa đơn thanh toán thành công
             * Tổng tiền = tổng tiền của các hóa đơn thanh toán thành công
             */
            boolean isFinishAllThread = checkIsAllThreadFinish();
            if (isFinishAllThread) {
                String messageNotify = Common.CODE_REPONSE_BILL_ONLINE.getMessageSuccess(countBillPayedSuccess, totalAmountBillPayedSuccess);
                mIPayView.showMessageNotifyBillOnlineDialog(messageNotify, false, Common.TYPE_DIALOG.THANH_CONG, true);
            }

            /**
             * Gửi yêu cầu cập nhật thông tin tài khoản ví TNV
             */

            callAPIUpdateEdongAccount(edong, entity);

            ((MainActivity) mIPayView.getContextView()).runOnUiThread(
                    new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            totalBillsChooseDialog--;
                            refreshStatusPaySuccessDialog(edong);
                        }
                    }
            );
        }

        @Override
        public void onPost(BillingOnlineRespone response, final int positionIndex) {
            if (response == null) {
                return;
            }

            //TODO Xử lý nhận kết quả thanh toán các hóa đơn từ server ----- Nếu không thành công
            final Common.CODE_REPONSE_BILL_ONLINE codeResponse = Common.CODE_REPONSE_BILL_ONLINE.findCodeMessage(response.getFooterBillingOnlineRespone().getResponseCode());
            long billId = response.getBodyBillingOnlineRespone().getBillId();
            String date = Common.getDateTimeNow(Common.DATE_TIME_TYPE.ddMMyyyy);

            //update date payed and tract number
            String dateNow = Common.getDateTimeNow(Common.DATE_TIME_TYPE.ddMMyyyy);
            Long traceNumber = response.getBodyBillingOnlineRespone().getTraceNumber();
            mPayModel.updateBillRequestDateBill(edong, response.getBodyBillingOnlineRespone().getCustomerCode(), response.getBodyBillingOnlineRespone().getBillId(), dateNow, traceNumber);


            if (codeResponse.getCode() == Common.CODE_REPONSE_BILL_ONLINE.e825.getCode()) {
                /**
                 * Trường hợp hóa đơn đã được thanh toán bởi nguồn khác: Không thực hiện thanh toán hóa đơn
                 */
                processCasePayedErrorByOtherSource(response, positionIndex, date, billId, edong);
                return;
            }

            if (codeResponse.getCode() == Common.CODE_REPONSE_BILL_ONLINE.e814.getCode()) {
                /**
                 * Trường hợp hóa đơn đã được thanh toán bởi ví khác: Không thực hiện thanh toán hóa đơn
                 */
                processCasePayedErrorByOtherEdong(response, positionIndex, date, billId, edong);
                return;
            }

            if (codeResponse.getCode() == Common.CODE_REPONSE_BILL_ONLINE.e824.getCode()) {
                /**
                 * Hóa đơn chấm nợ lỗi: Không thực hiện thanh toán hóa đơn
                 */
                processCasePayedErrorByErrorECPAY(response, positionIndex, date, billId, edong);
            }

            //TODO Xử lý nhận kết quả thanh toán các hóa đơn từ server ----- Nếu thành công
            String gateEVN = response.getBodyBillingOnlineRespone().getBillingType();

            /**
             * statusGateEVN = -1: giao dịch nghi ngờ
             * statusGateEVN = 0: mở cổng EVN
             * statusGateEVN = 1: đóng cổng EVN
             */

            /**
             * Trong giờ mở cổng kết nối thanh toán từ ECPAY sang EVN
             * Giờ mở công quy định khi mã lỗi là 000 và cờ mở tới EVN billingType = ON
             */

            /**
             * Trong giờ đóng cổng kết nối hoặc lỗi thanh toán từ ECPAY sang EVN
             * Giờ đóng công quy định khi
             * mã lỗi là e095 và cờ mở tới EVN billingType = ON tức hóa đơn gửi lên server ECPAY rồi gửi lên EVN nhưng EVN không chấp nhận
             * hoặc trường hợp mã lỗi là e000 và cờ mở tới EVN billingType = OFF tức hóa đơn bị giữ tại ECPAY khi bị lỗi thanh toán từ ECPAY sang EVN
             */

            int statusGateEVN = NEGATIVE_ONE;

            //nếu không xác định được gateEVN
            if (gateEVN == null)
                statusGateEVN = NEGATIVE_ONE;
            else if ((codeResponse != Common.CODE_REPONSE_BILL_ONLINE.e000) && gateEVN.equals(Common.GATE_EVN_PAY.ON.getCode()))
                statusGateEVN = ZERO;
            else if ((codeResponse == Common.CODE_REPONSE_BILL_ONLINE.e000) && !gateEVN.equals(Common.GATE_EVN_PAY.ON.getCode())
                    ||
                    (codeResponse == Common.CODE_REPONSE_BILL_ONLINE.e095) && gateEVN.equals(Common.GATE_EVN_PAY.ON.getCode()))
                statusGateEVN = ONE;

            if (statusGateEVN == ZERO) {

                processCasePayedSuccessOpenDoor(response, positionIndex, date, billId, edong);
                //TODO Gửi yêu cầu cập nhật thông tin tài khoản Ví TNV

            }

            if (statusGateEVN == ONE) {
                processCasePayedSuccessCloseDoorOrErrorECPAY(response, positionIndex, date, billId, edong);

                //TODO Gửi yêu cầu cập nhật thông tin tài khoản Ví TNV
            }

            //update text count billDeleteOnline payed success
            countBillPayedSuccess++;
            totalAmountBillPayedSuccess += response.getBodyBillingOnlineRespone().getAmount();
            /**
             * Hiển thị thông tin thanh toán thành công lên màn hình
             * Số hóa đơn = số hóa đơn thanh toán thành công
             * Tổng tiền = tổng tiền của các hóa đơn thanh toán thành công
             */
            boolean isFinishAllThread = checkIsAllThreadFinish();
            if (isFinishAllThread) {
                String message = Common.CODE_REPONSE_BILL_ONLINE.getMessageSuccess(countBillPayedSuccess, totalAmountBillPayedSuccess);
                mIPayView.showMessageNotifyBillOnlineDialog(message, false, Common.TYPE_DIALOG.THANH_CONG, true);
            }


            ((MainActivity) mIPayView.getContextView()).runOnUiThread(
                    new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            totalBillsChooseDialog--;
                            refreshStatusPaySuccessDialog(edong);
                        }
                    }
            );
        }

        @Override
        public void onTimeOut(final SoapAPI.AsyncSoapBillOnline soapBillOnline) {
            soapBillOnline.cancel(true);

            //thread call asyntask is running. must call in other thread to update UI
            ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!soapBillOnline.isEndCallSoap()) {
                        mIPayView.showMessageNotifyBillOnlineDialog(Common.MESSAGE_NOTIFY.ERR_CALL_SOAP_TIME_OUT.toString(), false, Common.TYPE_DIALOG.LOI, false);
                    }
                }
            });
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void callAPITransationCancellation(String edong, CheckTrainOnlineResponse response, String reasonDeleteBill) {
        mIPayView.showDeleteBillOnlineProcess();
        mIPayView.showPbarDeleteBillOnline();
        mIPayView.enableReasonEditText();
        mIPayView.visibleButtonDeleteDialog(SHOW_ALL);

        //check wifi
        boolean isHasWifi = Common.isConnectingWifi(mIPayView.getContextView());
        boolean isHasNetwork = Common.isNetworkConnected(mIPayView.getContextView());

//        if (!isHasWifi) {
//            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_WIFI.toString());
//            soapDeleteBillOnline.setEndCallSoap(true);
//            soapDeleteBillOnline.cancel(true);
//        }
        if (!isHasNetwork) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_NETWORK.toString(), Common.TYPE_DIALOG.LOI);

            soapDeleteBillOnline.setEndCallSoap(true);
            soapDeleteBillOnline.cancel(true);
            return;
        }

        ConfigInfo configInfo = null;
        Context context = mIPayView.getContextView();
        String versionApp = Common.getVersionApp(context);
        try {
            configInfo = Common.setupInfoRequest(context, edong, Common.COMMAND_ID.TRANSACTION_CANCELLATION.toString(), versionApp);
        } catch (Exception e) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.MESSAGE_NOTIFY.ERR_ENCRYPT_AGENT.toString(), Common.TYPE_DIALOG.LOI);
            return;
        }

        Long amount = response.getBody().getAmount();
        String code = response.getBody().getCustomerCode();
        Long billId = response.getBody().getBillId();
        String requestDate = response.getBody().getRequestDate();

        Long traceNumber = mPayModel.getTraceNumberBill(edong, code, billId);

        //create request to server
        String jsonRequestDeleteBillOnline = SoapAPI.getJsonRequestTransationCancellation(
                configInfo.getAGENT(),
                configInfo.getAgentEncypted(),
                configInfo.getCommandId(),
                configInfo.getAuditNumber(),
                configInfo.getMacAdressHexValue(),
                configInfo.getDiskDriver(),
                configInfo.getSignatureEncrypted(),

                amount,
                code,
                billId,
                requestDate,
                traceNumber,
                reasonDeleteBill,

                configInfo.getAccountId());

        if (jsonRequestDeleteBillOnline == null) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(Common.CODE_REPONSE_TRANSACTION_CANCELLATION.ex10002.getMessage(), Common.TYPE_DIALOG.LOI);
            return;
        }

        mIPayView.showDeleteBillOnlineProcess();
        mIPayView.showPbarDeleteBillOnline();

        try {
            if (soapDeleteBillOnline == null) {
                //if null then create new
                soapDeleteBillOnline = new SoapAPI.AsyncSoapDeleteBillOnline(edong, reasonDeleteBill, code, billId, soapDeleteBillOnlineCallBack);
            } else if (soapDeleteBillOnline.getStatus() == AsyncTask.Status.PENDING) {
                //if running not yet then run

            } else if (soapDeleteBillOnline.getStatus() == AsyncTask.Status.RUNNING) {
                //if is running
                soapDeleteBillOnline.setEndCallSoap(true);
                soapDeleteBillOnline.cancel(true);

                handlerDelay.removeCallbacks(runnableCountTimeDeleteBillOnline);
                soapDeleteBillOnline = new SoapAPI.AsyncSoapDeleteBillOnline(edong, reasonDeleteBill, code, billId, soapDeleteBillOnlineCallBack);
            } else {
                //if running or finish
                handlerDelay.removeCallbacks(runnableCountTimeDeleteBillOnline);

                soapDeleteBillOnline = new SoapAPI.AsyncSoapDeleteBillOnline(edong, reasonDeleteBill, code, billId, soapDeleteBillOnlineCallBack);
            }

            soapDeleteBillOnline.execute(jsonRequestDeleteBillOnline);

            //thread time out
            //sleep
            handlerDelay.postDelayed(runnableCountTimeDeleteBillOnline, TIME_OUT_CONNECT);

        } catch (Exception e) {
            mIPayView.showMessageNotifyDeleteOnlineDialog(e.getMessage(), Common.TYPE_DIALOG.LOI);
            return;
        }

    }

    private Runnable runnableCountTimeDeleteBillOnline = new Runnable() {
        @Override
        public void run() {
            if (soapDeleteBillOnline != null && soapDeleteBillOnline.isEndCallSoap())
                return;
            //Do something after 100ms
            DeleteBillOnlineRespone deleteBillOnlineRespone = soapDeleteBillOnline.getDeleteBillOnlineRespone();

            if (deleteBillOnlineRespone == null && !soapDeleteBillOnline.isEndCallSoap()) {
                //call time out
                soapDeleteBillOnline.callCountdown(soapDeleteBillOnline);
            }
        }
    };

    private Runnable runnableCountTimeCheckTrainOnline = new Runnable() {
        @Override
        public void run() {
            if (soapCheckTrainOnlineRequestCancelBill != null && soapCheckTrainOnlineRequestCancelBill.isEndCallSoap())
                return;
            //Do something after 100ms
            CheckTrainOnlineResponse checkTrainOnlineResponse = soapCheckTrainOnlineRequestCancelBill.getCheckTrainOnlineResponse();

            if (checkTrainOnlineResponse == null && !soapCheckTrainOnlineRequestCancelBill.isEndCallSoap()) {
                //call time out
                soapCheckTrainOnlineRequestCancelBill.callCountdown(soapCheckTrainOnlineRequestCancelBill);
            }
        }
    };
    //endregion

    private void refreshStatusPaySuccessDialogAndDisableCheckbox(String edong, boolean isDisableAllCheckbox) {
        if (TextUtils.isEmpty(edong))
            return;

        refreshTextCountBillPayedSuccess();
        mIPayView.showPayRecyclerListBillsAndDisableCheckBox(listBillDialog, isDisableAllCheckbox);
    }

    private boolean checkIsAllThreadFinish() {
        boolean hasThreadRunning = false;
        for (int index = 0; index < billOnlineAsyncList.size(); index++) {
            SoapAPI.AsyncSoapBillOnline soapBillOnline = billOnlineAsyncList.get(index);
            if (!soapBillOnline.isEndCallSoap()) {
                hasThreadRunning = true;
                break;
            }
        }
        return hasThreadRunning ? false : true;
    }

    private boolean checkIsHasThreadRunning(int positionIndex) {

        boolean hasThreadRunning = false;
        for (int index = 0; index < billOnlineAsyncList.size(); index++) {
            if (index != positionIndex) {
                SoapAPI.AsyncSoapBillOnline soapBillOnline = billOnlineAsyncList.get(index);
                if (!soapBillOnline.isEndCallSoap()) {
                    hasThreadRunning = true;
                    break;
                }
            }
        }
        return hasThreadRunning;
    }

    private void refreshStatusPaySuccessDialog(String edong) {
        if (TextUtils.isEmpty(edong))
            return;

        //not refreshData listBillDialog
        //but refreshData
        refreshTextCountBillPayedSuccess();
        mIPayView.showPayRecyclerListBills(listBillDialog);
    }

    private void refreshTotalBillsAndTotalMoneyInDialogWhenCallPayingOnline() {
        mIPayView.showCountBillsAndTotalMoneyInDialog(countBillPayedSuccess, totalBillsChooseDialogTemp);
    }

    //region xử lý các trường hợp thanh toán
    private void processCasePayedBySupected(BillingOnlineRespone respone, final int positionIndex, final String edong, PayBillsDialogAdapter.Entity entity) {
        //TODO Thực hiện thanh toán nghi ngờ
        /**
         * Danh sách hóa đơn: hiển thị kết quả “Thanh toán thành công”
         * In biên nhận:
         * --- In biên nhận thành công printInfo = 'Đã in thông báo' (trong bảng history)
         * --- Cho phép lập phiếu và in lại biên nhận
         * Thông tin tài khoản: Ghi tăng Số tiền chưa đẩy chấm nợ (trong bảng acccount cột notYetPushMoney)
         * Lưu Danh sách hóa đơn vào hệ thống như trường hợp thanh toán Online thành công, chỉ khác bổ sung lưu
         * --- TT thanh toán nghi ngờ = 01_Chưa kiểm tra (bảng debt)
         * Lưu Lịch sử thanh toán vào hệ thống như trường hợp thanh toán Online thành công, chỉ khác bổ sung lưu
         * --- TT thanh toán nghi ngờ = 01_Chưa kiểm tra (update bảng history)
         * Lưu lại log giao dịch Thanh toán nghi ngờ
         * Tổng hợp số Đã thanh toán trên Báo cáo tổng hợp, Báo cáo chi tiết
         */

        //In biên nhận thành công printInfo = 'Đã in thông báo' (trong bảng history)
        int rowAffect = mPayModel.updateBillHistoryWithPrintInfo(
                edong, entity.getBillId(), //where
                Common.STATUS_OF_PRINT_INFO.DA_IN_THONG_BAO.getCode());
        Log.d(TAG, "rowAffect updateBillHistoryWithPrintInfo = " + rowAffect);

        //Thông tin tài khoản: Ghi tăng Số tiền chưa đẩy chấm nợ (trong bảng acccount cột notYetPushMoney)
        int notYetPushMoney = mPayModel.getNotYetPushMoney(edong);
        notYetPushMoney += entity.getAmount();
        rowAffect = mPayModel.updateAccountWith(
                edong, //where
                notYetPushMoney);
        Log.d(TAG, "rowAffect updateAccountWith = " + rowAffect);

        //Lưu Danh sách hóa đơn vào hệ thống như trường hợp thanh toán Online thành công, chỉ khác bổ sung lưu
        // --- TT thanh toán nghi ngờ = 01_Chưa kiểm tra (bảng debt)
        // Đồng thời hiển thị message thành công
        String date = Common.getDateTimeNow(Common.DATE_TIME_TYPE.ddMMyyyy);
        processCasePayedSuccessOpenDoor(null, positionIndex, date, entity.getBillId(), entity.getEdong());

        rowAffect = mPayModel.updateBillDebtWithSuspectedProcessingStatus(
                edong, entity.getBillId(),//where
                Common.SUSPECTED_PROCESSING_STATUS.TRANG_THAI_NGHI_NGO.getCode()
        );
        Log.d(TAG, "rowAffect updateBillDebtWithSuspectedProcessingStatus = " + rowAffect);


        rowAffect = mPayModel.updateBillHistoryWithSuspectedProcessingStatus(
                edong, entity.getBillId(),//where
                Common.SUSPECTED_PROCESSING_STATUS.TRANG_THAI_NGHI_NGO.getCode()
        );
        Log.d(TAG, "rowAffect updateBillHistoryWithSuspectedProcessingStatus = " + rowAffect);
    }

    private void processCasePayedSuccessCloseDoorOrErrorECPAY(BillingOnlineRespone respone, final int positionIndex, String date, long billId, final String edong) {
        /**
         * Trong giờ đóng cổng kết nối hoặc lỗi thanh toán từ ECPAY sang EVN
         * (Trường hợp thanh toán thành công mã e000 && Trạng thái thanh toán ON/OFF billingType API trả về = OFF)
         * hay tương đương với e000 và billingType khác ON
         * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
         * Ví thanh toán edong = Ví đăng nhập
         * Trạng thái thanh toán status = 02_Đã thanh toán
         */
        int rowAffect = mPayModel.updateBillWith(
                edong, (int) billId, //where
                Common.STATUS_BILLING.DA_THANH_TOAN.getCode(), edong);
        Log.d(TAG, "rowAffect updateBillWith = " + rowAffect);

        /**
         * Lưu thông tin hóa đơn vào Danh sách thu (Bảng Debt): các thông tin như Danh sách hóa đơn nợ, chỉ khác bổ sung thêm:
         * Ví thanh toán edong = Ví đăng nhập
         * Hình thức thanh toán payments = 01_Online
         * Trạng thái thanh toán payStatus = 02_Đã thanh toán(trong database payStatus = 1 tương đương STATUS_BILLING)
         * Trạng thái chấm nợ stateOfDebt = 03_Đang chờ xử lý chấm nợ
         * Trạng thái hủy stateOfCancel = null
         * Trạng thái xử lý nghi ngờ suspectedProcessingStatus = null
         * Trạng thái hoàn trả stateOfReturn = null
         * Số lần in biên nhận countPrintReceipt = 0
         * In thông báo điện printInfo = null
         */

        rowAffect = mPayModel.updateBillDebtWithThanhToanErrorOrSuccess(
                edong, (int) billId,//where
                edong,
                Common.PAYMENT_MODE.ONLINE.getCode(), Common.STATUS_BILLING.DA_THANH_TOAN.getCode(), Common.STATE_OF_DEBT.DANG_CHO_XU_LY_CHAM_NO.getCode(),
                Common.STATE_OF_CANCEL.NULL.getCode(), Common.STATE_OF_RETURN.NULL.getCode(), Common.SUSPECTED_PROCESSING_STATUS.NULL.getCode(),
                ZERO, Common.STATUS_OF_PRINT_INFO.NULL.getCode());

        //set new status for billDeleteOnline and refesh recycler bills
        listBillDialog.get(positionIndex).setStatus(Common.STATUS_BILLING.DA_THANH_TOAN.getCode());

        Log.d(TAG, "rowAffect updateBillDebtWithThanhToanErrorOrSuccess = " + rowAffect);

        /**
         * Lưu thông tin hóa đơn vào Danh sách lịch sử: các thông tin như Danh sách thu, chỉ khác bổ sung thêm các thông tin
         * Ngày phát sinh dateIncurred = ngày thanh toán hóa đơn
         * Mã giao dịch tradeCode = 04_Thanh toán Online_Chấm nợ Offline
         */

        rowAffect = mPayModel.updateBillHistoryWithThanhToanErrorOrSuccess(
                edong, (int) billId,//where
                edong,
                Common.PAYMENT_MODE.ONLINE.getCode(), Common.STATUS_BILLING.DA_THANH_TOAN.getCode(), Common.STATE_OF_DEBT.CHAM_LOI.getCode(),
                Common.STATE_OF_CANCEL.NULL.getCode(), Common.STATE_OF_RETURN.NULL.getCode(), Common.SUSPECTED_PROCESSING_STATUS.NULL.getCode(),
                ZERO, Common.STATUS_OF_PRINT_INFO.NULL.getCode(), date, Common.TRADING_CODE.THANH_TOAN_ONLINE_CHAM_NO_OFFLINE.getCode());

        Log.d(TAG, "rowAffect updateBillHistoryWithThanhToanErrorOrSuccess = " + rowAffect);

        /**
         * Hiển thị và cập nhật thông tin thanh toán lên màn hình
         * Thanh toán thành công Trong giờ đóng cổng
         * Hiển thị message : Thanh toán thành công
         */
        ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String messageError = Common.CODE_REPONSE_BILL_ONLINE.ex10008.getMessage();
                listBillDialog.get(positionIndex).setMessageError(messageError);

                //check full billDeleteOnline is payed
                boolean isFinishAllThread = checkIsAllThreadFinish();
                if (isFinishAllThread) {
                    refreshStatusPaySuccessDialog(edong);
                    mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10001.getMessage(), false, Common.TYPE_DIALOG.THANH_CONG, true);
                } else
                    refreshStatusPaySuccessDialogAndDisableCheckbox(edong, true);
            }
        });
    }

    private void processCasePayedSuccessOpenDoor(BillingOnlineRespone respone, final int positionIndex, String date, long billId, final String edongKey) {
        //TODO mark
        /**
         * Trong giờ mở cổng kết nối thanh toán từ ECPAY sang EVN
         * (Trường hợp thanh toán thành công mã e000 && Trạng thái thanh toán ON/OFF billingType API trả về = ON)
         * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
         * Ví thanh toán edong = Ví đăng nhập
         * Trạng thái thanh toán status = 02_Đã thanh toán
         */
        int rowAffect = mPayModel.updateBillWith(
                edongKey, (int) billId, //where
                Common.STATUS_BILLING.DA_THANH_TOAN.getCode(), edongKey);
        Log.d(TAG, "rowAffect updateBillWith = " + rowAffect);

        /**
         * Lưu thông tin hóa đơn vào Danh sách thu (Bảng Debt): các thông tin như Danh sách hóa đơn nợ, chỉ khác bổ sung thêm:
         * Ví thanh toán edong = Ví đăng nhập
         * Hình thức thanh toán payments = 01_Online
         * Trạng thái thanh toán payStatus = 02_Đã thanh toán(trong database payStatus = 1 tương đương STATUS_BILLING)
         * Trạng thái chấm nợ stateOfDebt = 02_Đã chấm
         * Trạng thái hủy stateOfCancel = null
         * Trạng thái xử lý nghi ngờ suspectedProcessingStatus = null
         * Trạng thái hoàn trả stateOfReturn = null
         * Số lần in biên nhận countPrintReceipt = 0
         * In thông báo điện printInfo = null
         */

        rowAffect = mPayModel.updateBillDebtWithThanhToanErrorOrSuccess(
                edongKey, (int) billId,//where
                edongKey,
                Common.PAYMENT_MODE.ONLINE.getCode(), Common.STATUS_BILLING.DA_THANH_TOAN.getCode(), Common.STATE_OF_DEBT.DA_CHAM.getCode(),
                Common.STATE_OF_CANCEL.NULL.getCode(), Common.STATE_OF_RETURN.NULL.getCode(), Common.SUSPECTED_PROCESSING_STATUS.NULL.getCode(),
                ZERO, Common.STATUS_OF_PRINT_INFO.NULL.getCode());

        Log.d(TAG, "rowAffect updateBillDebtWithThanhToanErrorOrSuccess = " + rowAffect);

        //set new status for billDeleteOnline and refesh recycler bills
        listBillDialog.get(positionIndex).setStatus(Common.STATUS_BILLING.DA_THANH_TOAN.getCode());

        /**
         * Lưu thông tin hóa đơn vào Danh sách lịch sử: các thông tin như Danh sách thu, chỉ khác bổ sung thêm các thông tin
         * Ngày phát sinh dateIncurred = ngày thanh toán hóa đơn
         * Mã giao dịch tradeCode = 03_Thanh toán Online_Chấm nợ Online
         */

        rowAffect = mPayModel.updateBillHistoryWithThanhToanErrorOrSuccess(
                edongKey, (int) billId,//where
                edongKey,
                Common.PAYMENT_MODE.ONLINE.getCode(), Common.STATUS_BILLING.DA_THANH_TOAN.getCode(), Common.STATE_OF_DEBT.CHAM_LOI.getCode(),
                Common.STATE_OF_CANCEL.NULL.getCode(), Common.STATE_OF_RETURN.NULL.getCode(), Common.SUSPECTED_PROCESSING_STATUS.NULL.getCode(),
                ZERO, Common.STATUS_OF_PRINT_INFO.NULL.getCode(), date, Common.TRADING_CODE.THANH_TOAN_ONLINE_CHAM_NO_ONLINE.getCode());

        Log.d(TAG, "rowAffect updateBillHistoryWithThanhToanErrorOrSuccess = " + rowAffect);

        /**
         * Hiển thị và cập nhật thông tin thanh toán lên màn hình
         * Thanh toán thành công Trong giờ mở cổng
         * Hiển thị message : Thanh toán thành công
         */
        ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String messageError = Common.CODE_REPONSE_BILL_ONLINE.ex10008.getMessage();
                listBillDialog.get(positionIndex).setMessageError(messageError);

                //check full billDeleteOnline is payed
                boolean isFinishAllThread = checkIsAllThreadFinish();
                if (isFinishAllThread) {
                    refreshStatusPaySuccessDialog(edongKey);
                    mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10001.getMessage(), false, Common.TYPE_DIALOG.THANH_CONG, true);
                } else
                    refreshStatusPaySuccessDialogAndDisableCheckbox(edongKey, true);
            }
        });

    }

    private void processCasePayedErrorByErrorECPAY(BillingOnlineRespone respone, final int positionIndex, String date, long billId, final String edong) {
        /**
         * Hóa đơn chấm nợ lỗi: Không thực hiện thanh toán hóa đơn
         * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
         * Ví thanh toán edong = Ví đăng nhập
         * Trạng thái thanh toán status = (SRS không yêu cầu cập nhật trường này)
         */
        int rowAffect = mPayModel.updateBillWithWithThanhToanError(
                edong, (int) billId, //where
                edong);
        Log.d(TAG, "rowAffect updateBillWithWithThanhToanError = " + rowAffect);


        /**
         * Lưu thông tin hóa đơn vào Danh sách thu (Bảng Debt): các thông tin như Danh sách hóa đơn nợ, chỉ khác bổ sung thêm:
         * Ví thanh toán edong = Ví đăng nhập
         * Hình thức thanh toán payments = 01_Online
         * Trạng thái thanh toán payStatus = 02_Đã thanh toán(trong database payStatus = 1 tương đương STATUS_BILLING)
         * Trạng thái chấm nợ stateOfDebt = 04_Chấm lỗi
         * Trạng thái hủy stateOfCancel = null
         * Trạng thái xử lý nghi ngờ suspectedProcessingStatus = null
         * Trạng thái hoàn trả stateOfReturn = null
         * Số lần in biên nhận countPrintReceipt = 0
         * In thông báo điện printInfo = null
         */

        rowAffect = mPayModel.updateBillDebtWithThanhToanErrorOrSuccess(
                edong, (int) billId,//where
                edong,
                Common.PAYMENT_MODE.ONLINE.getCode(), Common.STATUS_BILLING.DA_THANH_TOAN.getCode(), Common.STATE_OF_DEBT.CHAM_LOI.getCode(),
                Common.STATE_OF_CANCEL.NULL.getCode(), Common.STATE_OF_RETURN.NULL.getCode(), Common.SUSPECTED_PROCESSING_STATUS.NULL.getCode(),
                ZERO, Common.STATUS_OF_PRINT_INFO.NULL.getCode());

        Log.d(TAG, "rowAffect updateBillDebtWithThanhToanErrorOrSuccess = " + rowAffect);

        //set new status for billDeleteOnline and refesh recycler bills
        listBillDialog.get(positionIndex).setStatus(Common.STATUS_BILLING.DA_THANH_TOAN.getCode());

        /**
         * Lưu thông tin hóa đơn vào Danh sách lịch sử: các thông tin như Danh sách thu, chỉ khác bổ sung thêm các thông tin
         * Ngày phát sinh dateIncurred = ngày thanh toán hóa đơn
         * Mã giao dịch tradeCode = 05_Thanh toán Online_Chấm nợ lỗi
         */

        rowAffect = mPayModel.updateBillHistoryWithThanhToanErrorOrSuccess(
                edong, (int) billId,//where
                edong,
                Common.PAYMENT_MODE.ONLINE.getCode(), Common.STATUS_BILLING.DA_THANH_TOAN.getCode(), Common.STATE_OF_DEBT.CHAM_LOI.getCode(),
                Common.STATE_OF_CANCEL.NULL.getCode(), Common.STATE_OF_RETURN.NULL.getCode(), Common.SUSPECTED_PROCESSING_STATUS.NULL.getCode(),
                ZERO, Common.STATUS_OF_PRINT_INFO.NULL.getCode(), date, Common.TRADING_CODE.THANH_TOAN_ONLINE_CHAM_NO_LOI.getCode());

        Log.d(TAG, "rowAffect updateBillHistoryWithThanhToanErrorOrSuccess = " + rowAffect);

        /**
         * Hiển thị và cập nhật thông tin thanh toán lên màn hình
         * Thanh toán hóa đơn không thành công Chấm nợ lỗi Timeout
         * Hiển thị message : Thanh toán thành công
         */
        ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String messageError = Common.CODE_REPONSE_BILL_ONLINE.ex10008.getMessage();
                listBillDialog.get(positionIndex).setMessageError(messageError);

                //check full billDeleteOnline is payed
                boolean isFinishAllThread = checkIsAllThreadFinish();
                if (isFinishAllThread) {
                    refreshStatusPaySuccessDialog(edong);
                    mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10001.getMessage(), false, Common.TYPE_DIALOG.THANH_CONG, true);
                } else
                    refreshStatusPaySuccessDialogAndDisableCheckbox(edong, true);
            }
        });
    }

    private void processCasePayedErrorByOtherEdong(BillingOnlineRespone response, final int positionIndex, String date, long billId, final String edong) {
        /**
         * Trường hợp hóa đơn đã được thanh toán bởi ví khác: Không thực hiện thanh toán hóa đơn
         * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
         * Ví thanh toán edong = Ví thanh toán do server trả về
         * Trạng thái thanh toán status = 04_Đã thanh toán bởi ví khác (Trong database = 3)
         */

        int rowAffect = mPayModel.updateBillWith(
                edong, (int) billId, //where
                Common.STATUS_BILLING.DA_THANH_TOAN_BOI_VI_KHAC.getCode(), response.getBodyBillingOnlineRespone().getPaymentEdong());
        Log.d(TAG, "rowAffect updateBillWith = " + rowAffect);


        /** Lưu thông tin hóa đơn vào Danh sách lịch sử (Bảng History): các thông tin như Danh sách hóa đơn nợ, chỉ khác bổ sung thêm:
         * Ví thanh toán edong = Ví thanh toán do server trả về
         * Hình thức thanh toán payments = null
         * Trạng thái thanh toán payStatus = 04_Đã thanh toán bởi ví khác(trong database payStatus = 3)
         * Trạng thái chấm nợ stateOfDebt = null
         * Trạng thái hủy stateOfCancel = null
         * Trạng thái xử lý nghi ngờ suspectedProcessingStatus = null
         * Trạng thái hoàn trả stateOfReturn = null
         * Số lần in biên nhận countPrintReceipt = 0
         * In thông báo điện printInfo = null
         * Ngày đẩy dateOfPush = ngày thanh toán hóa đơn
         * Mã giao dịch tradeCode = 02_Cập nhật trạng thái nợ (SRS update thêm trường này)
         *
         * Trạng thái đẩy chấm nợ stateOfPush = (SRS không yêu cầu cập nhật trường này nên sẽ ko update)
         * */

        rowAffect = mPayModel.updateBillDebtWithThanhToanBoiNguonKhacOrViKhac(
                edong, (int) billId,//where
                response.getBodyBillingOnlineRespone().getPaymentEdong(),
                Common.PAYMENT_MODE.NULL.getCode(), Common.STATUS_BILLING.DA_THANH_TOAN_BOI_VI_KHAC.getCode(), Common.STATE_OF_DEBT.NULL.getCode(),
                Common.STATE_OF_CANCEL.NULL.getCode(), Common.STATE_OF_RETURN.NULL.getCode(), Common.SUSPECTED_PROCESSING_STATUS.NULL.getCode(),
                date, ZERO, Common.STATUS_OF_PRINT_INFO.NULL.getCode(), Common.TRADING_CODE.CAP_NHAT_TRANG_THAI_NO.getCode());

        Log.d(TAG, "rowAffect updateBillDebtWithThanhToanBoiNguonKhacOrViKhac = " + rowAffect);

        //set new status for billDeleteOnline and refesh recycler bills
        listBillDialog.get(positionIndex).setStatus(Common.STATUS_BILLING.DA_THANH_TOAN_BOI_VI_KHAC.getCode());

        /**
         * Hiển thị và cập nhật thông tin thanh toán lên màn hình
         * Thanh toán hóa đơn không thành công Hóa đơn thanh toán bởi số ví khác
         * Hiển thị message : Không thành công, đã thanh toán bởi số ví khác
         */
        ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String messageError = Common.CODE_REPONSE_BILL_ONLINE.ex10007.getMessage();
                listBillDialog.get(positionIndex).setMessageError(messageError);

                //check full billDeleteOnline is payed
                boolean isFinishAllThread = checkIsAllThreadFinish();
                if (isFinishAllThread) {
                    refreshStatusPaySuccessDialog(edong);
                    if (countBillPayedSuccess == totalBillsChooseDialogTemp)
                        mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10001.getMessage(), false, Common.TYPE_DIALOG.THANH_CONG, true);
                    else
                        mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10004.getMessage(), false, Common.TYPE_DIALOG.LOI, true);
                } else
                    refreshStatusPaySuccessDialogAndDisableCheckbox(edong, true);
            }
        });
    }

    private void processCasePayedErrorByOtherSource(BillingOnlineRespone respone, final int positionIndex, String date, long billId, final String edong) {
        /** Trường hợp hóa đơn đã được thanh toán bởi nguồn khác: Không thực hiện thanh toán hóa đơn
         * Cập nhật Danh sách hóa đơn nợ (Bill table) đã lưu trong máy
         * Ví thanh toán edong = null
         * Trạng thái thanh toán status = 03_Đã thanh toán bởi nguồn khác (Trong database = 2)
         */


        int rowAffect = mPayModel.updateBillWith(
                edong, (int) billId, //where
                Common.STATUS_BILLING.DA_THANH_TOAN_BOI_NGUON_KHAC.getCode(), null);
        Log.d(TAG, "rowAffect updateBillWith = " + rowAffect);

        /** Lưu thông tin hóa đơn vào Danh sách lịch sử (Bảng History): các thông tin như Danh sách hóa đơn nợ, chỉ khác bổ sung thêm:
         * Ví thanh toán edong = null
         * Hình thức thanh toán payments = null
         * Trạng thái thanh toán payStatus = 03_Đã thanh toán bởi nguồn khác(trong database payStatus = 2)
         * Trạng thái chấm nợ stateOfDebt = null
         * Trạng thái hủy stateOfCancel = null
         * Trạng thái xử lý nghi ngờ suspectedProcessingStatus = null
         * Trạng thái hoàn trả stateOfReturn = null
         * Số lần in biên nhận countPrintReceipt = 0
         * In thông báo điện printInfo = null
         * Ngày đẩy dateOfPush = ngày thanh toán hóa đơn
         * Mã giao dịch tradeCode = 02_Cập nhật trạng thái nợ (SRS update thêm trường này)
         *
         * Trạng thái đẩy chấm nợ stateOfPush = (SRS không yêu cầu cập nhật trường này nên sẽ ko update)
         */


        rowAffect = mPayModel.updateBillDebtWithThanhToanBoiNguonKhacOrViKhac(
                edong, (int) billId, //where
                null,
                Common.PAYMENT_MODE.NULL.getCode(), Common.STATUS_BILLING.DA_THANH_TOAN_BOI_NGUON_KHAC.getCode(), Common.STATE_OF_DEBT.NULL.getCode(),
                Common.STATE_OF_CANCEL.NULL.getCode(), Common.STATE_OF_RETURN.NULL.getCode(), Common.SUSPECTED_PROCESSING_STATUS.NULL.getCode(),
                date, ZERO, Common.STATUS_OF_PRINT_INFO.NULL.getCode(), Common.TRADING_CODE.CAP_NHAT_TRANG_THAI_NO.getCode());

        Log.d(TAG, "rowAffect updateBillDebtWithThanhToanBoiNguonKhacOrViKhac = " + rowAffect);


        //set new status for billDeleteOnline and refesh recycler bills
        listBillDialog.get(positionIndex).setStatus(Common.STATUS_BILLING.DA_THANH_TOAN_BOI_NGUON_KHAC.getCode());

        /**
         * Hiển thị và cập nhật thông tin thanh toán lên màn hình
         * Thanh toán hóa đơn không thành công Hóa đơn thanh toán bởi nguồn khác
         * Hiển thị message : Không thành công, đã thanh toán bởi nguồn khác
         */
        ((MainActivity) mIPayView.getContextView()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String messageError = Common.CODE_REPONSE_BILL_ONLINE.ex10006.getMessage();
                listBillDialog.get(positionIndex).setMessageError(messageError);

                //check full billDeleteOnline is payed
                boolean isFinishAllThread = checkIsAllThreadFinish();
                if (isFinishAllThread) {
                    refreshStatusPaySuccessDialog(edong);
                    if (countBillPayedSuccess == totalBillsChooseDialogTemp)
                        mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10001.getMessage(), false, Common.TYPE_DIALOG.THANH_CONG, true);
                    else
                        mIPayView.showMessageNotifyBillOnlineDialog(Common.CODE_REPONSE_BILL_ONLINE.ex10004.getMessage(), false, Common.TYPE_DIALOG.LOI, true);
                } else
                    refreshStatusPaySuccessDialogAndDisableCheckbox(edong, true);
            }
        });
    }
    //endregion
}
