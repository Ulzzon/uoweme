package helper;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import com.example.tobbe.uoweme.Activities.MainActivity;
import com.example.tobbe.uoweme.Person;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by TobiasOlsson on 15-07-01.
 */
public class Communicator{// extends Activity {

    private static final String userName = "user";
    private static final String phoneNumber = "phoneNr";

    private static final String logHead = "Communicator";
    private static final String connectionUrl = "http://192.168.1.246:8888";
    private static Socket connectionSocket;


    public static void startCommunication(){
        try{
            connectionSocket = IO.socket(connectionUrl);
            connectionSocket.on("hand_shake", handShakeListener);
            connectionSocket.connect();
        }
        catch (Exception e)
        {
            Log.d(logHead, "Faild to connect to socket, error: " + e.getMessage());
        }
    }

    public static void killConnection(){
        connectionSocket.disconnect();
        connectionSocket.off("hand_shake", handShakeListener);
    }


    public static Emitter.Listener handShakeListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            JSONObject data = (JSONObject) args[0];
            connectionSocket.emit("hand_shake_ok", MainActivity.android_id);
//            int numUsers;
//            try {
//                numUsers = data.getInt("numUsers");
//            } catch (JSONException e) {
//                return;
//            }

            //Intent intent = new Intent();
            //intent.putExtra("username", mUsername);
            //intent.putExtra("numUsers", numUsers);
            //setResult(RESULT_OK, intent);
            //finish();
        }
    };


    public static void sendMessage(String message) {
        try {
            JSONObject json = new JSONObject();
            json.putOpt("message", message);
            connectionSocket.emit("message", json);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public static void addPersonToServerDb(Person person){
        try{
            JSONObject json = new JSONObject();
            json.putOpt(userName, person.getName());
            json.putOpt(phoneNumber, person.getNumber());
            connectionSocket.emit("addUser", json);
        }
        catch(Exception e)
        {
            Log.d(logHead, "Faild to create and send personal information");
        }
    }

    public static void updatePersonOnServerDb(Person person){
        try{
            JSONObject json = new JSONObject();
            json.put(userName, person.getName());
            json.put("newPhoneNr", person.getNumber());
            connectionSocket.emit("updateUser", json);
        }
        catch (Exception e){
            Log.d(logHead, "Faild to create json and update person");
        }
    }
}
