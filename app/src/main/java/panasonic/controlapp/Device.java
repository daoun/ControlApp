package panasonic.controlapp;

/**
 * Created by Christine on 2/7/17.
 */
public class Device {
    private String ip;
    private String hostname;
    private String status;
    private int sound;


    public Device(String ip){
        this.ip = ip;
    }


    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {

        // hostname: 192.168.0.0
        return this.hostname + ": " + this.ip;
    }
}
