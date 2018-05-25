package test_practic2.eim.systems.cs.pub.ro.practicaltest02;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by ciprian on 5/25/2018.
 */

public class ServerThread extends Thread {

    private int port = 0;
    private ServerSocket serverSocket;
    private HashMap<String, String> cache = null;

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public ServerThread(int port) {
        this.port = port;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException io) {
            // Log.e(MathsMainActivity.TAG, "An exception has occurred: " + io.getMessage());
        }
        cache = new HashMap<>();
    }

    public void stopThread() {
        interrupt();
        if(serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException io) {
                // Log.e();
            }
        }
    }

    @Override
    public void run() {
        try {
            while(!Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept();
                CommunicationThread communicationThread = new CommunicationThread(this, socket);
                new Thread(communicationThread).start();
            }
        } catch (Exception ex) {
            // Log.e
        }
    }

    public synchronized void setData(String url, String body) {
        cache.put(url, body);
    }

    public synchronized HashMap<String, String> getData() {
        return cache;
    }
}
