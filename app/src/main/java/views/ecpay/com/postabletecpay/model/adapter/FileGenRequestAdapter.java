package views.ecpay.com.postabletecpay.model.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import views.ecpay.com.postabletecpay.util.entities.response.EntityFileGen.FileGenResponse;

/**
 * Class adapter GSON Serialization because gson not order sequence field when create json string
 */

public class FileGenRequestAdapter extends TypeAdapter<FileGenResponse> {
    @Override
    public void write(JsonWriter out, FileGenResponse value) throws IOException {

//        ArrayList<ListCustomerRespons> customerResponse = value.getCustomerResponse();
//        ArrayList<ListBillResponse> billResponse = value.getBillResponse();
//        int id_change = value.getId_changed();
//        String date_change = value.getDate_changed();
//
//        out.beginObject();
//        //write headerLoginRequest
//        if (customerResponse != null) {
//            out.name("customers").beginArray();
//            for(ListCustomerRespons list : customerResponse.getCusList()){
//                HeaderCustomerResponse headerCustomerResponse = list.getHeaderCustomerResponse();
//                BodyCustomerResponse bodyCustomerResponse = list.getBodyCustomerResponse();
//                FooterCustomerResponse footerCustomerResponse = list.getFooterCustomerResponse();
//
//                if(headerCustomerResponse != null) {
//                    out.name("header").beginObject();
//                    out.name("object").value(headerCustomerResponse.getObject());
//                    out.endObject();
//                }
//
//                if(bodyCustomerResponse != null) {
//                    out.name("body").beginObject();
//                    out.name("id").value(bodyCustomerResponse.getId());
//                    out.name("pcCode").value(bodyCustomerResponse.getPcCode());
//                    out.name("pcCodeExt").value(bodyCustomerResponse.getPcCodeExt());
//                    out.name("customerCode").value(bodyCustomerResponse.getCustomerCode());
//                    out.name("cardNo").value(bodyCustomerResponse.getCardNo());
//                    out.name("name").value(bodyCustomerResponse.getName());
//                    out.name("nameNoSign").value(bodyCustomerResponse.getNameNoSign());
//                    out.name("address").value(bodyCustomerResponse.getAddress());
//                    out.name("addressNoSign").value(bodyCustomerResponse.getAddressNoSign());
//                    out.name("phoneByEVN").value(bodyCustomerResponse.getPhoneByEVN());
//                    out.name("phoneByECP").value(bodyCustomerResponse.getPhoneByECP());
//                    out.name("bookCmis").value(bodyCustomerResponse.getBookCmis());
//                    out.name("electricityMeter").value(bodyCustomerResponse.getElectricityMeter());
//                    out.name("inning").value(bodyCustomerResponse.getInning());
//                    out.name("road").value(bodyCustomerResponse.getRoad());
//                    out.name("station").value(bodyCustomerResponse.getStation());
//                    out.name("taxCode").value(bodyCustomerResponse.getTaxCode());
//                    out.name("trade").value(bodyCustomerResponse.getTrade());
//                    out.name("countPeriod").value(bodyCustomerResponse.getCountPeriod());
//                    out.name("team").value(bodyCustomerResponse.getTeam());
//                    out.name("type").value(bodyCustomerResponse.getType());
//                    out.name("status").value(bodyCustomerResponse.getStatus());
//                    out.name("createdDate").value(bodyCustomerResponse.getCreatedDate());
//                    out.endObject();
//                }
//
//                if(footerCustomerResponse != null) {
//                    out.name("footer").beginObject();
//                    out.name("idChanged").value(footerCustomerResponse.getIdChanged());
//                    out.name("dateChanged").value(footerCustomerResponse.getDateChanged());
//                    out.endObject();
//                }
//            }
//            out.endArray();
//        }
//
//        if (billResponse != null) {
//            out.name("bills").beginArray();
//            for(ListBillResponse list : billResponse.getListBill()){
//                HeaderBillResponse headerBillResponse = list.getHeaderBillResponse();
//                BodyBillResponse bodyBillResponse = list.getBodyBillResponse();
//                FooterBillResponse footerBillResponse = list.getFooterBillResponse();
//
//                if(headerBillResponse != null) {
//                    out.name("header").beginObject();
//                    out.name("object").value(headerBillResponse.getObject());
//                    out.endObject();
//                }
//
//                if(bodyBillResponse != null) {
//                    out.name("body").beginObject();
//                    out.name("id").value(bodyBillResponse.getId());
//                    out.name("customerCode").value(bodyBillResponse.getCustomerCode());
//                    out.name("billId").value(bodyBillResponse.getBillId());
//                    out.name("term").value(bodyBillResponse.getTerm());
//                    out.name("amount").value(bodyBillResponse.getAmount());
//                    out.name("period").value(bodyBillResponse.getPeriod());
//                    out.name("issueDate").value(bodyBillResponse.getIssueDate());
//                    out.name("status").value(bodyBillResponse.getStatus());
//                    out.name("seri").value(bodyBillResponse.getSeri());
//                    out.name("pcCode").value(bodyBillResponse.getPcCode());
//                    out.name("handOverCode").value(bodyBillResponse.getHandOverCode());
//                    out.name("cashierCode").value(bodyBillResponse.getCashierCode());
//                    out.name("bookCmis").value(bodyBillResponse.getBookCmis());
//                    out.name("fromDate").value(bodyBillResponse.getFromDate());
//                    out.name("toDate").value(bodyBillResponse.getToDate());
//                    out.name("home").value(bodyBillResponse.getHome());
//                    out.name("tax").value(bodyBillResponse.getTax());
//                    out.name("billNum").value(bodyBillResponse.getBillNum());
//                    out.name("currency").value(bodyBillResponse.getCurrency());
//                    out.name("priceDetail").value(bodyBillResponse.getPriceDetail());
//                    out.name("numeDetail").value(bodyBillResponse.getNumeDetail());
//                    out.name("amountDetail").value(bodyBillResponse.getAmountDetail());
//                    out.name("oldIndex").value(bodyBillResponse.getOldIndex());
//                    out.name("newIndex").value(bodyBillResponse.getNewIndex());
//                    out.name("nume").value(bodyBillResponse.getNume());
//                    out.name("amountNotTax").value(bodyBillResponse.getAmountNotTax());
//                    out.name("amountTax").value(bodyBillResponse.getAmountTax());
//                    out.name("multiple").value(bodyBillResponse.getMultiple());
//                    out.name("billType").value(bodyBillResponse.getBillType());
//                    out.name("typeIndex").value(bodyBillResponse.getTypeIndex());
//                    out.name("groupTypeIndex").value(bodyBillResponse.getGroupTypeIndex());
//                    out.name("createdDate").value(bodyBillResponse.getCreatedDate());
//                    out.name("edong").value(bodyBillResponse.getEdong());
//                    out.name("billingType").value(bodyBillResponse.getBillType());
//                    out.endObject();
//                }
//
//                if(footerBillResponse != null) {
//                    out.name("footer").beginObject();
//                    out.name("idChanged").value(footerBillResponse.getIdChanged());
//                    out.name("dateChanged").value(footerBillResponse.getDateChanged());
//                    out.endObject();
//                }
//            }
//            out.endArray();
//        }
//
//        out.name("id_changed");
//        out.name("date_changed");
//
//        out.endObject();
    }

    @Override
    public FileGenResponse read(JsonReader in) throws IOException {
        final FileGenResponse fileGenResponse = new FileGenResponse();

//        in.beginObject();
//        while (in.hasNext()) {
//            switch (in.nextName()) {
//                case "header":
//                    final HeaderEVNRequest headerEVNRequest = new HeaderEVNRequest();
//
//                    in.beginObject();
//                    while (in.hasNext()) {
//                        switch (in.nextName()) {
//                            case "agent":
//                                headerEVNRequest.setAgent(in.nextString());
//                                break;
//                            case "password":
//                                headerEVNRequest.setPassword(in.nextString());
//                                break;
//                            case "command-id":
//                                headerEVNRequest.setCommandId(in.nextString());
//                                break;
//                        }
//                    }
//                    in.endObject();
//
//                    evnRequest.setHeaderEVNRequest(headerEVNRequest);
//                    break;
//
//                case "body":
//                    final BodyEVNRequest bodyEVNRequest = new BodyEVNRequest();
//
//                    in.beginObject();
//                    while (in.hasNext()) {
//                        switch (in.nextName()) {
//                            case "audit-number":
//                                bodyEVNRequest.setAuditNumber(in.nextLong());
//                                break;
//                            case "mac":
//                                bodyEVNRequest.setMac(in.nextString());
//                                break;
//                            case "disk-drive":
//                                bodyEVNRequest.setDiskDrive(in.nextString());
//                                break;
//                            case "signature":
//                                bodyEVNRequest.setSignature(in.nextString());
//                                break;
//                            case "edong":
//                                bodyEVNRequest.setEdong(in.nextString());
//                                break;
//                        }
//                    }
//                    in.endObject();
//
//                    evnRequest.setBodyEVNRequest(bodyEVNRequest);
//                    break;
//
//                case "footer":
//                    final FooterEVNRequest footerEVNRequest = new FooterEVNRequest();
//
//                    in.beginObject();
//                    while (in.hasNext()) {
//                        switch (in.nextName()) {
//                            case "account-idt":
//                                footerEVNRequest.setAccountIdt(in.nextString());
//                                break;
//                        }
//                    }
//                    in.endObject();
//
//                    evnRequest.setFooterEVNRequest(footerEVNRequest);
//                    break;
//            }
//        }
//
//        in.endObject();

        return fileGenResponse;
    }
}