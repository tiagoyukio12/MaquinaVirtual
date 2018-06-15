package virtualMachine;

public class Util {
    static String intToWord(int data) {
        if (data > 32767)
            System.out.println("Error: Overflow. Value " + data + " is too big");
        if (data < -32768)
            System.out.println("Error: Underflow. Value " + data + " is too small");
        String ret = Integer.toHexString(data);
        if (ret.length() > 4)
            ret = ret.substring(ret.length() - 4, ret.length());
        while (ret.length() < 4)
            ret = "0".concat(ret);
        return ret;
    }

    static int wordToInt(String data) {
        return (short) Integer.parseInt(data, 16);
    }

    static int signedToUnsigned(int signed) {
        String unsigned = intToWord(signed);
        return Integer.parseInt(unsigned, 16);
    }
}
