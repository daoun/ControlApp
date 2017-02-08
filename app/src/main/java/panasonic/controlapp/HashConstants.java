package panasonic.controlapp;

import java.nio.ByteBuffer;

/**
 * Created by Christine on 2/8/17.
 */
public class HashConstants {

    public static final ByteBuffer KEY_PEERS = ByteBuffer.wrap(new byte[] {'p', 'e', 'e', 'r', 's' });


    public static final byte[] PDP_CONTROL = {'P','D','P','C','O','N','T','R','O','L'};
    public static final byte[] NT_CONTROL = {'N','T','C','O','N','T','R','O','L'};
    public static final byte[] SPACE = {' '};
    public static final byte[] CARRIAGE_RETURN = {0x0D};
    public static final byte[] START_TEXT = {0x02};
    public static final byte[] END_TEXT = {0x03};
    public static final byte[] ZERO = {0x30};
    public static final byte[] ONE = {0x31};


    public static final byte[] POWER_OFF = {'P','O','F'};
    public static final byte[] POWER_ON = {'P','O','N'};



}
