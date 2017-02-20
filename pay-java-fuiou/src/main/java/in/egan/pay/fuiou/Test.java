package in.egan.pay.fuiou;/**
 * Created by Fuzx on 2017/2/10 0010.
 */

import in.egan.pay.common.util.sign.SignUtils;

/**
 * @author Fuzx
 * @create 2017 2017/2/10 0010
 */
public class Test {
    public static void main(String[] args) {
        String content="0002230F0348879|17021013591343700615|805a9aphsvmbf6qih8k66vu1svafj99m";
        System.out.println(SignUtils.valueOf("MD5").createSign(content, "", "UTF-8"));



    }
}
