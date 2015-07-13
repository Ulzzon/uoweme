package helper;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TobiasOlsson on 15-07-01.
 */
public class Communicator extends Activity {

    public static Socket mSocket;
    {
        try{
            mSocket = IO.socket("http://192.168.1.246:8888");
            Log.d("Communicator","Setting up socket");
        }catch (Exception e){
            Log.d("Socket", "ERROR: " +e.toString());
        }
    }

    public void sendHandShake(){
        String deviceId = "1234";
        JSONObject json = new JSONObject();
        try {
            json.putOpt("deviceId", deviceId);
        }catch (JSONException e){
            e.printStackTrace();
        }
        mSocket.emit("hand_shake", "hello");
    }

    public static Emitter.Listener handShakeListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            mSocket.emit("hand_shake", "hello");
            int numUsers;
            try {
                numUsers = data.getInt("numUsers");
            } catch (JSONException e) {
                return;
            }

            //Intent intent = new Intent();
            //intent.putExtra("username", mUsername);
            //intent.putExtra("numUsers", numUsers);
            //setResult(RESULT_OK, intent);
            //finish();
        }
    };




    public void sendMessage(String message) {
        try {
            JSONObject json = new JSONObject();
            json.putOpt("message", message);
            mSocket.emit("user message", json);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }
}
