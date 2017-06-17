package views.ecpay.com.postabletecpay.model.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import views.ecpay.com.postabletecpay.util.entities.response.EntityChangePass.BodyChangePassResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityChangePass.ChangePassResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityChangePass.FooterChangePassResponse;
import views.ecpay.com.postabletecpay.util.entities.response.EntityChangePass.HeaderChangePassResponse;


/**
 * Created by VinhNB on 5/19/2017.
 */

public class ChangePassResponseAdapter extends TypeAdapter<ChangePassResponse> {
    @Override
    public void write(JsonWriter out, ChangePassResponse value) throws IOException {
        HeaderChangePassResponse headerChangePassResponse = value.getHeaderChangePassResponse();
        BodyChangePassResponse bodyChangePassResponse = value.getBodyChangePassResponse();
        FooterChangePassResponse footerChangePassResponse = value.getFooterChangePassResponse();

        out.beginObject();
        //write HeaderChangePassResponse
        if (headerChangePassResponse != null) {
            out.name("header").beginObject();
            out.name("agent").value(headerChangePassResponse.getAgent());
            out.name("password").value(headerChangePassResponse.getPassword());
            out.name("command-id").value(headerChangePassResponse.getCommandId());
            out.endObject();
        }

        //write BodyChangePassResponse
        if (bodyChangePassResponse != null) {
            out.name("body").beginObject();
            out.name("audit-number").value(bodyChangePassResponse.getAuditNumber());
            out.name("mac").value(bodyChangePassResponse.getMac());
            out.name("disk-drive").value(bodyChangePassResponse.getDiskDrive());
            out.name("signature").value(bodyChangePassResponse.getSignature());
            out.endObject();
        }

        //write FooterChangePassResponse
        if (footerChangePassResponse != null) {
            out.name("footer").beginObject();
            out.name("account-idt").value(footerChangePassResponse.getAccountIdt());
            out.name("response-code").value(footerChangePassResponse.getResponseCode());
            out.name("description").value(footerChangePassResponse.getDescription());
            out.endObject();
        }

        out.endObject();
    }

    @Override
    public ChangePassResponse read(JsonReader in) throws IOException {
        final ChangePassResponse changePassResponse = new ChangePassResponse();

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "header":
                    final HeaderChangePassResponse headerChangePassResponse = new HeaderChangePassResponse();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "agent":
                                headerChangePassResponse.setAgent(in.nextString());
                                break;
                            case "password":
                                headerChangePassResponse.setPassword(in.nextString());
                                break;
                            case "command-id":
                                headerChangePassResponse.setCommandId(in.nextString());
                                break;
                        }
                    }
                    in.endObject();

                    changePassResponse.setHeaderChangePassResponse(headerChangePassResponse);
                    break;

                case "body":
                    final BodyChangePassResponse BodyChangePassResponse = new BodyChangePassResponse();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "audit-number":
                                BodyChangePassResponse.setAuditNumber(in.nextLong());
                                break;
                            case "mac":
                                BodyChangePassResponse.setMac(in.nextString());
                                break;
                            case "disk-drive":
                                BodyChangePassResponse.setDiskDrive(in.nextString());
                                break;
                            case "signature":
                                BodyChangePassResponse.setSignature(in.nextString());
                                break;
                        }
                    }
                    in.endObject();

                    changePassResponse.setBodyChangePassResponse(BodyChangePassResponse);
                    break;

                case "footer":
                    final FooterChangePassResponse FooterChangePassResponse = new FooterChangePassResponse();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "account-idt":
                                FooterChangePassResponse.setAccountIdt(in.nextString());
                                break;
                            case "response-code":
                                FooterChangePassResponse.setResponseCode(in.nextString());
                                break;
                            case "description":
                                FooterChangePassResponse.setDescription(in.nextString());
                                break;
                        }
                    }
                    in.endObject();

                    changePassResponse.setFooterChangePassResponse(FooterChangePassResponse);
                    break;
            }
        }

        in.endObject();

        return changePassResponse;
    }
}