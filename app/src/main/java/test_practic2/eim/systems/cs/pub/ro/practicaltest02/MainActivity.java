package test_practic2.eim.systems.cs.pub.ro.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button connect_button = null;
    private Button get_button = null;
    private EditText server_port = null;
    private EditText client_port = null;
    private EditText url = null;
    private TextView result = null;

    ServerThread serverThread = null;
    ClientThread clientThread = null;

    private Button.OnClickListener buttonClickListener = new Button.OnClickListener() {

        @Override
        public void onClick(View view) {
            String clientPort = client_port.getText().toString();
            String serverPort = server_port.getText().toString();
            String url_maths = url.getText().toString();
            switch (view.getId()) {
                case R.id.connect_button:
                    if(serverPort == null || serverPort.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Server Port Must not be null!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        serverThread = new ServerThread(Integer.parseInt(serverPort));
                        if(serverThread.getServerSocket() == null) {
                            Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Could not create server thread!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        serverThread.start();
                        Toast.makeText(getApplicationContext(), "Server Started! :)", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.query_button:
                    if(clientPort == null || clientPort.isEmpty()
                            || url_maths.isEmpty() || url_maths == null) {
                        Toast.makeText(getApplicationContext(), "Client fields must be completed!", Toast.LENGTH_SHORT).show();
                    }
                    else if(serverThread == null || !serverThread.isAlive()) {
                        Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String urlText = url.getText().toString();
                    if(urlText == null) {
                        Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client should be filled", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    new ClientThread(Integer.parseInt(clientPort), urlText, result).start();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connect_button = findViewById(R.id.connect_button);
        get_button = findViewById(R.id.query_button);
        url = findViewById(R.id.query);
        server_port = findViewById(R.id.server_port);
        client_port = findViewById(R.id.client_port);
        result = findViewById(R.id.result);

        connect_button.setOnClickListener(buttonClickListener);
        get_button.setOnClickListener(buttonClickListener);
    }

    @Override
    protected void onDestroy() {
        if(serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }

}
