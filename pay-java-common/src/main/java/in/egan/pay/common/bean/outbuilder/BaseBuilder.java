package in.egan.pay.common.bean.outbuilder;

import in.egan.pay.common.bean.PayOutMessage;

/**
 * @source chanjarster/weixin-java-tools
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-6-1 11:40:30
 */
public abstract class BaseBuilder<BuilderType, ValueType> {


    public abstract ValueType build();

    public void setCommon(PayOutMessage m) {

    }

}