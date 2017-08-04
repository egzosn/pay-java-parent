

package com.egzosn.pay.common.util.sign.encrypt;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;

public final class Base64 {

    /**
     * Encodes hex octects into Base64
     *
     * @param binaryData Array containing binaryData
     * @return Encoded Base64 array
     */
    public static String encode(byte[] binaryData) {
        return new String( new BASE64Encoder().encode(binaryData));
    }

    /**
     * Decodes Base64 data into octects
     *
     * @param encoded string containing Base64 data
     * @return Array containind decoded data.
     */
    public static byte[] decode(String encoded) {


        try {
            return  new BASE64Decoder().decodeBuffer(encoded);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

}
