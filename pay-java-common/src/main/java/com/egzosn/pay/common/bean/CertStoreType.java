package com.egzosn.pay.common.bean;

import java.io.*;

/**
 * 证书存储类型
 *
 * @author egan
 *         email egzosn@gmail.com
 *         date 2019/4/14.23:04
 */
public enum CertStoreType {

    /**
     * 路径，建议绝对路径
     */
    PATH {
        /**
         * 证书信息转化为对应的输入流
         *
         * @param cert 证书信息
         * @return 输入流
         */
        @Override
        public InputStream getInputStream(Object cert) throws IOException {
            return new FileInputStream(new File((String) cert));
        }
    },
    /**
     * 文件流转化成字符串存储至文件或者数据库中
     */
    STR {
        /**
         * 证书信息转化为对应的输入流
         *
         * @param cert 证书信息
         * @return 输入流
         */
        @Override
        public InputStream getInputStream(Object cert) throws IOException {
            return new ByteArrayInputStream(((String) cert).getBytes("ISO-8859-1"));
        }
    },

    /**
     * 文件流
     */
    INPUT_STREAM {
        /**
         * 证书信息转化为对应的输入流
         *
         * @param cert 证书信息
         * @return 输入流
         */
        @Override
        public InputStream getInputStream(Object cert) throws IOException {
            return (InputStream) cert;
        }
    };

    /**
     * 证书信息转化为对应的输入流
     *
     * @param cert 证书信息
     * @return 输入流
     */
    public abstract InputStream getInputStream(Object cert) throws IOException;

}
