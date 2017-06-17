package views.ecpay.com.postabletecpay.model.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import views.ecpay.com.postabletecpay.util.entities.request.EntityChangePass.BodyChangePassRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityChangePass.ChangePassRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityChangePass.FooterChangePassRequest;
import views.ecpay.com.postabletecpay.util.entities.request.EntityChangePass.HeaderChangePassRequest;

/**
 * Class adapter GSON Serialization because gson not order sequence field when create json string
 */

public class ChangePassRequestAdapter extends TypeAdapter<ChangePassRequest> {
    @Override
    public void write(JsonWriter out, ChangePassRequest value) throws IOException {
        HeaderChangePassRequest headerChangePassRequest = value.getHeaderChangePassRequest();
        BodyChangePassRequest bodyChangePassRequest = value.getBodyChangePassRequest();
        FooterChangePassRequest footerChangePassRequest = value.getFooterChangePassRequest();

        out.beginObject();
        //write HeaderChangePassResponse
        if (headerChangePassRequest != null) {
            out.name("header").beginObject();
            out.name("agent").value(headerChangePassRequest.getAgent());
            out.name("password").value(headerChangePassRequest.getPassword());
            out.name("command-id").value(headerChangePassRequest.getCommandId());
            out.endObject();
        }

        //write BodyChangePassResponse
        if (bodyChangePassRequest != null) {
            out.name("body").beginObject();
            out.name("audit-number").value(bodyChangePassRequest.getAuditNumber());
            out.name("mac").value(bodyChangePassRequest.getMac());
            out.name("disk-drive").value(bodyChangePassRequest.getDiskDrive());
            out.name("signature").value(bodyChangePassRequest.getSignature());
            out.name("pin-login").value(bodyChangePassRequest.getPinLogin());
            out.name("session").value(bodyChangePassRequest.getSession());
            out.name("new-pin").value(bodyChangePassRequest.getNewPin());
            out.name("retry-pin").value(bodyChangePassRequest.getRetryPin());
            out.endObject();
        }

        //write FooterChangePassResponse
        if (footerChangePassRequest != null) {
            out.name("footer").beginObject();
            out.name("account-idt").value(footerChangePassRequest.getAccountIdt());
            out.endObject();
        }

        out.endObject();
    }

    @Override
    public ChangePassRequest read(JsonReader in) throws IOException {
        final ChangePassRequest ChangePassRequest = new ChangePassRequest();

        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "header":
                    final HeaderChangePassRequest HeaderChangePassRequest = new HeaderChangePassRequest();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "agent":
                                HeaderChangePassRequest.setAgent(in.nextString());
                                break;
                            case "password":
                                HeaderChangePassRequest.setPassword(in.nextString());
                                break;
                            case "command-id":
                                HeaderChangePassRequest.setCommandId(in.nextString());
                                break;
                        }
                    }
                    in.endObject();

                    ChangePassRequest.setHeaderChangePassRequest(HeaderChangePassRequest);
                    break;

                case "body":
                    final BodyChangePassRequest BodyChangePassRequest = new BodyChangePassRequest();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "audit-number":
                                BodyChangePassRequest.setAuditNumber(in.nextLong());
                                break;
                            case "mac":
                                BodyChangePassRequest.setMac(in.nextString());
                                break;
                            case "disk-drive":
                                BodyChangePassRequest.setDiskDrive(in.nextString());
                                break;
                            case "signature":
                                BodyChangePassRequest.setSignature(in.nextString());
                                break;
                            case "pin-login":
                                BodyChangePassRequest.setPinLogin(in.nextString());
                                break;
                            case "session":
                                BodyChangePassRequest.setSession(in.nextString());
                                break;
                            case "new-pin":
                                BodyChangePassRequest.setNewPin(in.nextString());
                                break;
                            case "retry-pin":
                                BodyChangePassRequest.setRetryPin(in.nextString());
                                break;

                        }
                    }
                    in.endObject();

                    ChangePassRequest.setBodyChangePassRequest(BodyChangePassRequest);
                    break;

                case "footer":
                    final FooterChangePassRequest FooterChangePassRequest = new FooterChangePassRequest();

                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "account-idt":
                                FooterChangePassRequest.setAccountIdt(in.nextString());
                                break;
                        }
                    }
                    in.endObject();

                    ChangePassRequest.setFooterChangePassRequest(FooterChangePassRequest);
                    break;
            }
        }

        in.endObject();

        return ChangePassRequest;
    }
}