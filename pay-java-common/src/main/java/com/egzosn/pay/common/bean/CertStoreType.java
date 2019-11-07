package com.egzosn.pay.common.bean;

import com.egzosn.pay.common.api.CertStore;
import com.egzosn.pay.common.http.HttpRequestTemplate;

import java.io.*;

/**
 * 证书存储类型
 *
 * @author egan
 *         email egzosn@gmail.com
 *         date 2019/4/14.23:04
 */
public enum CertStoreType implements CertStore {

    /**
     * 路径，建议绝对路径
     */
    PATH {
        /**
         * 证书信息转化为对应的输入流
         *
         * @param cert 证书信息
         * @return 输入流
         * @throws IOException 找不到文件异常
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
         * @throws IOException 找不到文件异常
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
         * @throws IOException 找不到文件异常
         */
        @Override
        public InputStream getInputStream(Object cert) throws IOException {
            return (InputStream) cert;
        }
    },

    /**
     * URL获取的方式
     */
    URL {
        /**
         * 证书信息转化为对应的输入流
         *
         * @param url 获取证书信息的URL
         * @return 输入流
         * @throws IOException 找不到文件异常
         */
        @Override
        public InputStream getInputStream(Object url) throws IOException {
            return new HttpRequestTemplate().getForObject((String) url, InputStream.class);
        }
    },

    /**
     * URL获取的方式
     */
    BEAN {
        /**
         * 证书信息转化为对应的输入流
         *
         * @param beanClazz 获取证书信息的类路径（字符串），该类必须实现{@link CertStore}
         * @return 输入流
         * @throws IOException 找不到文件异常
         */
        @Override
        public InputStream getInputStream(Object beanClazz) throws IOException {
            try {
                Class<?> clazz = Class.forName((String) beanClazz);
                CertStore certStore =   (CertStore)clazz.newInstance();
                return certStore.getInputStream(beanClazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            return null;
        }
    };



}
