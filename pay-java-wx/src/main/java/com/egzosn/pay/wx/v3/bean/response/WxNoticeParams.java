package com.egzosn.pay.wx.v3.bean.response;

import com.alibaba.fastjson.annotation.JSONField;
import com.egzosn.pay.common.bean.NoticeParams;

/**
 * 微信通知参数
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2021/10/4
 *</pre>
 */
public class WxNoticeParams extends NoticeParams {


    /**
     * 通知的唯一ID
     * 示例值：EV-2018022511223320873
     */
    private String id;

    /**
     *通知创建的时间，遵循rfc3339标准格式，格式为YYYY-MM-DDTHH:mm:ss+TIMEZONE，YYYY-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日13点29分35秒。
     * 示例值：2018-06-08T10:34:56+08:00
     */
    @JSONField(name = "create_time")
    private String createTime;
    /**
     * 通知的类型：
     * TRANSACTION.SUCCESS  支付成功通知
     * REFUND.SUCCESS：退款成功通知
     * REFUND.ABNORMAL：退款异常通知
     * REFUND.CLOSED：退款关闭通知
     * 示例值：REFUND.SUCCESS
     */
    @JSONField(name = "event_type")
    private String eventType;



    /**
     * 通知的资源数据类型，支付成功通知为encrypt-resource
     *  示例值：encrypt-resource
     */
    @JSONField(name = "resource_type")
    private String resourceType;

    /**
     *  通知资源数据
     * json格式，见示例
     */
    private Resource resource;
    /**
     * 通知简要说明
     * 示例值：退款成功
     * 示例值：支付成功
     */
    private String summary;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
}
