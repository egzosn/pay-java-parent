package com.egzosn.pay.wx.v3.bean.transfer;

import com.alibaba.fastjson.annotation.JSONField;

public class TransferSceneReport {
    @JSONField(name = "info_type")
    private String infoType;
    @JSONField(name = "info_content")
    private String infoContent;

    public TransferSceneReport() {
    }

    public TransferSceneReport(String infoType, String infoContent) {
        this.infoType = infoType;
        this.infoContent = infoContent;
    }

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public String getInfoContent() {
        return infoContent;
    }

    public void setInfoContent(String infoContent) {
        this.infoContent = infoContent;
    }
}
