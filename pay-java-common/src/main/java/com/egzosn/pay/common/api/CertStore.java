package com.egzosn.pay.common.api;

import java.io.IOException;
import java.io.InputStream;

/**
 * 证书存储方式
 * @author egan
 *         email egzosn@gmail.com
 *         date 2019/10/13.23:09
 */
public interface CertStore {

    /**
     * 证书信息转化为对应的输入流
     *
     * @param cert 证书信息
     * @return 输入流
     * @throws IOException 找不到文件异常
     */
    public abstract InputStream getInputStream(Object cert) throws IOException;
}
