package panasonic.controlapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;

public class MainActivity extends Activity {


    private Button button;
    private ListView deviceLV;
    private ArrayList<Device> deviceList;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        button = (Button) findViewById(R.id.btn_run);
        deviceLV = (ListView) findViewById(R.id.listView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("Main: ", "Clicked");
                AsyncTaskRunner runner = new AsyncTaskRunner(context);
                runner.execute("");
            }
        });

        deviceLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Device device = deviceList.get(i);

                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            }
        });
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, ArrayList<Device>> {

        ProgressDialog progressDialog;
        private final String TAG = "MainActivity";
        private WeakReference<Context> mContextRef;

        public AsyncTaskRunner(Context context) {
            mContextRef = new WeakReference<>(context);
        }


        @Override
        protected ArrayList<Device> doInBackground(String... params) {

            ArrayList<Device> list = GetDevices();


        /*  ArrayList<Device> list = new ArrayList<>(); //GetDevices();

            Device device1 = new Device("192.168.2.151");
            device1.setHostname("DaounsiPhone");


            Device device2 = new Device("192.168.2.75");
            device2.setHostname("android-12345");

            list.add(device2);
            list.add(device1);*/

            return list;
        }

        private ArrayList<Device> GetDevices() {

            ArrayList<Device> list = new ArrayList<>();

            try {
                Log.d("Async:","Starting Background:");
                Context context = mContextRef.get();

                if (context != null) {

                    ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

                    WifiInfo connectionInfo = wm.getConnectionInfo();
                    int ipAddress = connectionInfo.getIpAddress();



                    Log.d(TAG, "DHCP info: " + wm.getDhcpInfo().gateway);


                    Log.d(TAG, "IP Address: " + ipAddress);

                    String ipString = InetAddress.getLocalHost().getHostAddress();

                    Log.d(TAG, "activeNetwork: " + String.valueOf(activeNetwork));
                    Log.d(TAG, "ipString: " + String.valueOf(ipString));

                    String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
                    prefix = "192.168.2.";
                    Log.d(TAG, "prefix: " + prefix);


                    //String mirrorIP = "192.168.2.94";

                    for (int i = 0; i < 255; i++) {
                        //Log.d(TAG, "testIp" + i);
                        String testIp = prefix + String.valueOf(i);
                        //Log.d(TAG, "testIp: " + testIp);

                        InetAddress address = InetAddress.getByName(testIp);
                        boolean reachable = address.isReachable(1000);
                        String hostName = address.getCanonicalHostName();


                        if (reachable) {
                            Log.i(TAG, "Host: " + String.valueOf(hostName) + "(" + String.valueOf(testIp) + ") is reachable!");
                            Device device = new Device(testIp);
                            device.setHostname(hostName);
                            list.add(device);
                        }
                    }
                    Log.d(TAG, "Search is complete");
                }
            } catch (Throwable t) {
                Log.e(TAG, "Well that's not good.", t);
            }

            return list;
        }

        @Override
        protected void onProgressUpdate(String... text) {
            //finalResult.setText(text[0]);

        }

        @Override
        protected void onPostExecute(ArrayList<Device> result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            Log.d(TAG, result.toString());
            ArrayAdapter<Device> listAdapter = new DeviceAdapter(context, R.layout.simple_list_item_1 , result);
            deviceList = result;
            deviceLV.setAdapter(listAdapter);


        }


        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Progress",
                    "Searching for devices...");
        }


        /* possible method of getting 192.168.0.0... NOT WORKING*/
        private void parseLinux() {
            Log.d(TAG, "PARSE LINUX");
            try {
                BufferedReader reader = new BufferedReader(new FileReader("/proc/net/route"));
                String line;
                while((line = reader.readLine())!=null) {
                    Log.d(TAG, "line: " + line);
                    line = line.trim();
                    String [] tokens = line.split(" ");//= Tokenizer.parse(line, '\t', true, true);
                    if(tokens.length > 1 && tokens[1].equals("00000000")) {
                        String gateway = tokens[2]; //0102A8C0
                        if(gateway.length() == 8) {
                            String[] s4 = new String[4];
                            s4[3] = String.valueOf(Integer.parseInt(gateway.substring(0, 2), 16));
                            s4[2] = String.valueOf(Integer.parseInt(gateway.substring(2, 4), 16));
                            s4[1] = String.valueOf(Integer.parseInt(gateway.substring(4, 6), 16));
                            s4[0] = String.valueOf(Integer.parseInt(gateway.substring(6, 8), 16));
                            gateway = s4[0] + "." + s4[1] + "." + s4[2] + "." + s4[3];
                            Log.d(TAG, "Gateway: " + gateway);
                        }
                        String iface = tokens[0];
                        NetworkInterface nif = NetworkInterface.getByName(iface);
                        Enumeration addrs = nif.getInetAddresses();
                        while(addrs.hasMoreElements()) {
                            Object obj = addrs.nextElement();
                            if(obj instanceof Inet4Address) {
                                String ip =  obj.toString();
                                Log.d(TAG, "IP: " + ip);
                                if(ip.startsWith("/"))
                                    ip = ip.substring(1);
                                return;
                            }
                        }
                        return;
                    }
                }
                reader.close();
            }
            catch(IOException e) {
                System.err.println(e);
                e.printStackTrace();
            }
        }


    }
}
