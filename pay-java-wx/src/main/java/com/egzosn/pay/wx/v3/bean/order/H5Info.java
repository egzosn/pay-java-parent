package com.egzosn.pay.wx.v3.bean.order;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * H5场景信息
 *
 * @author Egan
 * email egzosn@gmail.com
 * date 2021/8/1
 */
public class H5Info {

    /**
     * 场景类型
     * 示例值：iOS, Android, Wap
     */
    private String type;
    /**
     * 应用名称
     * 示例值：王者荣耀
     */
    @JSONField(name = "app_name")
    private String appName;
    /**
     * 网站URL
     * 示例值：https://pay.qq.com
     */
    @JSONField(name = "app_url")
    private String appUrl;
    /**
     * iOS平台BundleID
     * 示例值：com.tencent.wzryiOS
     */
    @JSONField(name = "bundle_id")
    private String bundleId;
    /**
     * Android平台PackageName
     * 示例值：com.tencent.tmgp.sgame
     */
    @JSONField(name = "package_name")
    private String packageName;



    public H5Info() {
        this.type = "Wap";
    }

    public H5Info(String type) {
        this.type = type;
    }

    public H5Info(String appName, String appUrl) {
        this();
        this.appName = appName;
        this.appUrl = appUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
