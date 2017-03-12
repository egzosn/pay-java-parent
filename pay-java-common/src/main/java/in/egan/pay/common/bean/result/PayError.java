package in.egan.pay.common.bean.result;

/**
 * 支付错误码说明
 *
 * @author egan
 * @email egzosn@gmail.com
 * @date 2017-03-02 22:28:01
 */
public interface PayError {

    /**
     * 获取错误码
     *
     * @return
     */
    String getErrorCode();

    /**
     * 获取错误消息
     *
     * @return
     */
    String getErrorMsg();

    /**
     * 获取异常信息
     * @return
     */
    String getString();

}
