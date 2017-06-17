package views.ecpay.com.postabletecpay.model.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import views.ecpay.com.postabletecpay.util.entities.request.EntityEVN.BodyEVNRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityEVN.EVNRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityEVN.FooterEVNRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityEVN.HeaderEVNRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.BodyLoginRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.FooterLoginRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.HeaderLoginRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityLogin.LoginRequest;

/**
 * Class adapter GSON Serialization because gson not order sequence field when create json string
 */

public class EvnRequestAdapter extends TypeAdapter<EVNRequest> {
    @Override
    public void write(JsonWriter out, EVNRequest value) throws IOException {
        HeaderEVNRequest headerEVNRequest = value.getHeaderEVNRequest();
        BodyEVNRequest bodyEVNRequest = value.getBodyEVNRequest();
        FooterEVNRequest footerEVNRequest = value.getFooterEVNRequest();

        out.beginObject();
        //write headerLoginRequest
        if (headerEVNRequest != null) {
            out.name("header").beginObject();
            out.name("agent").value(headerEVNRequest.getAgent());
            out.name("password").value(headerEVNRequest.getPassword());
            out.name("command-id").value(headerEVNRequest.getCommandId());
            out.endObject();
        }

        //write bodyLoginRequest
        if (bodyEVNRequest != null) {
            out.name("body").beginObject();
            out.name("audit-number").value(bodyEVNRequest.getAuditNumber());
            out.name("mac").value(bodyEVNRequest.getMac());
            out.name("disk-drive").value(bodyEVNRequest.getDiskDrive());
            out.name("signature").value(bodyEVNRequest.getSignature());
            out.name("edong").value(bodyEVNRequest.getEdong());
            out.endObject();
        }

        //write footerLoginRequest
        if (footerEVNRequest != null) {
            out.name("footer").beginObject();
            out.name("account-idt").value(footerEVNRequest.getAccountIdt());
            out.endObject();
        }

        out.endObject();
    }

    @Override
    public EVNRequest read(JsonReader in) throws IOException {
        final EVNRequest evnRequest = new EVNRequest();

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "header":
                    final HeaderEVNRequest headerEVNRequest = new HeaderEVNRequest();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "agent":
                                headerEVNRequest.setAgent(in.nextString());
                                break;
                            case "password":
                                headerEVNRequest.setPassword(in.nextString());
                                break;
                            case "command-id":
                                headerEVNRequest.setCommandId(in.nextString());
                                break;
                        }
                    }
                    in.endObject();

                    evnRequest.setHeaderEVNRequest(headerEVNRequest);
                    break;

                case "body":
                    final BodyEVNRequest bodyEVNRequest = new BodyEVNRequest();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "audit-number":
                                bodyEVNRequest.setAuditNumber(in.nextLong());
                                break;
                            case "mac":
                                bodyEVNRequest.setMac(in.nextString());
                                break;
                            case "disk-drive":
                                bodyEVNRequest.setDiskDrive(in.nextString());
                                break;
                            case "signature":
                                bodyEVNRequest.setSignature(in.nextString());
                                break;
                            case "edong":
                                bodyEVNRequest.setEdong(in.nextString());
                                break;
                        }
                    }
                    in.endObject();

                    evnRequest.setBodyEVNRequest(bodyEVNRequest);
                    break;

                case "footer":
                    final FooterEVNRequest footerEVNRequest = new FooterEVNRequest();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "account-idt":
                                footerEVNRequest.setAccountIdt(in.nextString());
                                break;
                        }
                    }
                    in.endObject();

                    evnRequest.setFooterEVNRequest(footerEVNRequest);
                    break;
            }
        }

        in.endObject();

        return evnRequest;
    }
}