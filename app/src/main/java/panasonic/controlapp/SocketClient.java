package panasonic.controlapp;

/**
 * Created by Christine on 2/8/17.
 */
import android.util.Log;

import java.io.*;
import java.net.*;

public class SocketClient {

    String hostName;
    int portNumber;
    Socket socket;
    protected InputStream in;
    protected OutputStream out;
    private static final String TAG = "SocketClient";

    public SocketClient(String hostName, int portNumber){
        this.hostName = hostName;
        this.portNumber = portNumber;

        try {
            createSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private synchronized void createSocket() throws IOException {
        this.socket = new Socket(hostName, portNumber);
        in = socket.getInputStream();
        out = socket.getOutputStream();
    }







    public synchronized void disconnect() {
    /*    if (this.socket != null) {
            upload.isRunning = false;
            upload.interrupt();
            stream.isRunning = false;
            stream.interrupt();
        }
    */
        try {
            if(this.socket!= null)
                socket.close();

        } catch (IOException e) {
            Log.w(TAG, "Error closing socket peer");
        } finally {
            socket = null;
            in = null;
            out = null;
        /*    torrentClient.peers.remove(this);
            isRunning = false;*/
        }

    }


}