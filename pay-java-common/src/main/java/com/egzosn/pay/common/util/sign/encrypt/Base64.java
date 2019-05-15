

package com.egzosn.pay.common.util.sign.encrypt;
/**
 *  Base64
 * @author egan
 * <pre>
 * email egzosn@gmail.com
 *
 * create 2019/05/15 12:50
 * </pre>
 */
public class Base64 {

    private Base64() {}

    public static byte[] decode(String str) {
        return org.apache.commons.codec.binary.Base64.decodeBase64(str);
    }

    public static String encode(byte[] bytes) {
        return org.apache.commons.codec.binary.Base64.encodeBase64String(bytes);
    }

}
