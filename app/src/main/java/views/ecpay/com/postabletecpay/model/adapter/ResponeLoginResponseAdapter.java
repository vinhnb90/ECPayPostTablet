package views.ecpay.com.postabletecpay.model.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

import views.ecpay.com.postabletecpay.util.entities.response.EntityLogin.ResponseLoginResponse;

public class ResponeLoginResponseAdapter extends TypeAdapter<ResponseLoginResponse> {

    @Override
    public void write(JsonWriter out, ResponseLoginResponse value) throws IOException {

    }

    @Override
    public ResponseLoginResponse read(JsonReader in) throws IOException {
        return null;
    }
}