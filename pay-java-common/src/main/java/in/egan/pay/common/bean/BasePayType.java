package in.egan.pay.common.bean;

/**
 * 基础的支付类型
 * @author egan
 * @email egzosn@gmail.com
 * @date 2016/11/20 0:47
 */
public interface BasePayType {


    /**
     * 根据支付类型获取交易类型
     * @param transactionType 类型值
     * @return
     */
    TransactionType getTransactionType(String transactionType);

}
