package in.egan.pay.common.bean;

/**
 * 交易类型
 * @author egan
 * @email egzosn@gmail.com
 * @date 2016/10/19 22:30
 */
public interface TransactionType {
    /**
     * 获取交易类型
     * @return
     */
     String getType();

    /**
     * 获取接口
     * @return
     */
     String getMethod();
}

