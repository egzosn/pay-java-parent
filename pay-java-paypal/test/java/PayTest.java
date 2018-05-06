import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.paypal.api.PayPalConfigStorage;
import com.egzosn.pay.paypal.api.PayPalPayService;
import com.egzosn.pay.paypal.bean.PayPalTransactionType;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * Created by egzosn on 2018/4/28.
 */
public class PayTest {

    public static void main(String[] args) {
        PayPalConfigStorage storage = new PayPalConfigStorage();
        storage.setClientID("AUWunqrIzeSLQTQqRp_gQwkt1vLRcVa5kJIO4mp0ZvQnTreLmxaji9bqOFpqz-0h8mdeQpYxix6g_PRD");
        storage.setClientSecret("EJFAZoD_ZG7PoRutRhX93TmeWs-DQ-PjdBkgr0j4GZA6lqAgmS2Z7yKQWqnTP5O74KqBaYOZnUEcq3Ug");
        storage.setTest(true);
        storage.setReturnUrl("http://127.0.0.1:8088/pay/success");
        PayPalPayService service = new PayPalPayService(storage);
        PayOrder order = new PayOrder("订单title", "摘要", new BigDecimal(0.01) , UUID.randomUUID().toString().replace("-", ""), PayPalTransactionType.sale);
        Map<String, Object> orderInfo = service.orderInfo(order);
        System.out.println(orderInfo);

    }
}
