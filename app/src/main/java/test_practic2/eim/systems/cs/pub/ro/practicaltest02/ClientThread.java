package test_practic2.eim.systems.cs.pub.ro.practicaltest02;

import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by ciprian on 5/25/2018.
 */


public class ClientThread extends Thread {
    private int port = 0;
    private String url;
    private TextView result;

    private Socket socket;

    public ClientThread(int port, String url, TextView result) {
        this.port = port;
        this.url = url;
        this.result = result;
    }

    @Override
    public void run() {
        try {
            socket = new Socket("localhost", port);
            if(socket == null) {
                // error
                return;
            }
            BufferedReader bufferedReader = Utils.getReader(socket);
            PrintWriter printWriter = Utils.getWriter(socket);
            if(bufferedReader == null || printWriter == null) {
                return; // error
            }

            printWriter.println(this.url);
            printWriter.flush();

            String final_result = "";
            String line;
            while( (line = bufferedReader.readLine()) != null ) {
                final_result += line;
            }
            final String fs = final_result;
            this.result.post(new Runnable() {
                @Override
                public void run() {
                    result.setText(fs);
                }
            });

        } catch (IOException io) {
            // Log.e();
        } finally {
            if(socket != null) {
                try {
                    socket.close();
                } catch (IOException io) {
                    // Log.e();
                }
            }
        }
    }
}
