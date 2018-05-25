package test_practic2.eim.systems.cs.pub.ro.practicaltest02;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;

/**
 * Created by ciprian on 5/25/2018.
 */

public class CommunicationThread extends Thread {
    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        if(serverThread == null || socket == null) {
            throw new IllegalArgumentException("parameters are null!");
        }
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = Utils.getReader(socket);
            PrintWriter printWriter = Utils.getWriter(socket);
            if (null == bufferedReader || null == printWriter) {
                // error
                return;
            }

            String userQuery = bufferedReader.readLine();
            if (null == userQuery || userQuery.isEmpty()) {
                return; // error
            }

            String result = "";
            if(userQuery.contains("bad")) {
                result += "URL blocked by firewall :(\n";
            }
            else {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(userQuery);

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                String pageSourceCode = httpClient.execute(httpPost, responseHandler);
                result += pageSourceCode;
            }
            printWriter.println(result);
            printWriter.flush();

        } catch (IOException io) {
            // Log.e();
            io.printStackTrace();
        }  finally {
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException io) {
                    io.printStackTrace();
                }
            }
        }
    }
}

