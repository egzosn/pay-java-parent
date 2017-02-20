package in.egan.pay.fuiou.bean;/**
 * Created by Fuzx on 2017/1/24 0024.
 */

import in.egan.pay.common.bean.CurType;

/**
 * @author Fuzx
 * @create 2017 2017/1/24 0024
 */
public enum FuiouCurType  implements CurType {

        CNY("人民币"),
     	USD("美元"),
     	HKD("港币"),
    	MOP("澳门元"),
        EUR("欧元"),
    	TWD("新台币"),
      	KRW("韩元"),
        JPY("日元"),
    	SGD("新加坡元"),
        AUD("澳大利亚元");

    private String name;
    private int index;

    private FuiouCurType(String name) {
        this.name = name;
    }
    @Override
    public String getCurType(){
        return this.name();
    }

    }
