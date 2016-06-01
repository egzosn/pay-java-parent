package in.egan.pay.common.util;

import in.egan.pay.common.api.PayErrorExceptionHandler;
import in.egan.pay.common.exception.PayErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * LogExceptionHandler 日志处理器
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-6-1 11:28:01
 */
public class LogExceptionHandler implements PayErrorExceptionHandler {

    private Logger log = LoggerFactory.getLogger(PayErrorExceptionHandler.class);

    @Override
    public void handle(PayErrorException e) {

        log.error("Error happens", e);

    }

}
