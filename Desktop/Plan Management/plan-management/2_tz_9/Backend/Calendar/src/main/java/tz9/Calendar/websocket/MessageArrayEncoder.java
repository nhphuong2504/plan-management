package tz9.Calendar.websocket;

import com.google.gson.Gson;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.util.ArrayList;

public class MessageArrayEncoder implements Encoder.Text<ArrayList<Message>> {

    private static Gson gson = new Gson();

    @Override
    public String encode(ArrayList<Message> message) throws EncodeException {
        return gson.toJson(message);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        // Custom initialization logic
    }

    @Override
    public void destroy() {
        // Close resources
    }
}
