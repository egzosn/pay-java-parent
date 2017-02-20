package in.egan.pay.common.util;

import in.egan.pay.common.api.PayErrorExceptionHandler;
import in.egan.pay.common.exception.PayErrorException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * LogExceptionHandler 日志处理器
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-6-1 11:28:01
 * @source chanjarster/weixin-java-tools
 */
public class LogExceptionHandler implements PayErrorExceptionHandler {

    protected final Log log = LogFactory.getLog(PayErrorExceptionHandler.class);

    @Override
    public void handle(PayErrorException e) {

        log.error("Error happens", e);

    }

}
