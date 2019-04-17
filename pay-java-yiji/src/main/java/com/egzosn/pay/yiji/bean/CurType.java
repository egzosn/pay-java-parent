package com.egzosn.pay.yiji.bean;

/**
 * 币种
 * @author egan
 *         email egzosn@gmail.com
 *         date 2019/4/16.22:48
 */
public enum CurType implements com.egzosn.pay.common.bean.CurType {
    CNY(156, "人民币"),
    USD(840, "美元"),
    JPY(392, "日元"),
    HKD(344, "港币"),
    GBP(826, "英镑"),
    EUR(978, "欧元"),
    AUD(30, "澳元"),
    CAD(124, "加元"),
    SGD(702, "坡币"),
    NZD(554, "新西"),
    TWD(901, "台币"),
    KRW(410, "韩元"),
    DKK(208, "丹朗"),
    TRY(949, "土拉"),
    MYR(458, "马来"),
    THB(764, "泰铢"),
    INR(356, "印卢"),
    PHP(608, "菲比"),
    CHF(756, "瑞士"),
    SEK(752, "瑞典"),
    ILS(376, "以谢"),
    ZAR(710, "南非"),
    RUB(643, "俄卢"),
    NOK(578, "挪威克朗"),
    AED(784, "阿联酋"),
    BRL(986, "巴西雷亚尔"),
    IDR(360, "印尼卢比"),
    SAR(682, "沙特里亚尔"),
    MXN(484, "墨西哥比索"),
    PLN(985, "波兰兹罗提"),
    VND(704, "越南盾"),
    CLP(152, "智利比索"),
    KZT(398, "哈萨克腾格"),
    CZK(203, "捷克克朗"),
    EGP(818, "埃及镑"),
    VEF(937, "委玻利瓦尔"),
    ARS(26, "阿根廷比索"),
    MOP(446, "澳门元"),
    UAH(980, "乌格里夫纳"),
    LBP(422, "黎巴嫩镑"),
    JOD(400, "黎巴嫩镑"),
    PEN(604, "秘鲁新索尔"),
    PKR(586, "巴基斯坦卢比"),
    RON(946, "罗马尼亚列伊"),
    QAR(634, "卡塔尔里亚尔"),
    KWD(414, "科威特第纳尔"),
    NGN(566, "尼日利亚奈拉"),
    COP(170, "哥伦比亚比索"),
    HUF(348, "匈牙利福林");

    private int code;
    /**
     * 币种名称
     */
    private String name;

    CurType(int code, String name) {
        this.name = name;
        this.code = code;
    }

    /**
     * 获取货币类型
     *
     * @return 货币类型
     */
    @Override
    public String getType() {
        return this.name();
    }

    /**
     * 货币名称
     *
     * @return 货币名称
     */
    @Override
    public String getName() {
        return name;
    }

    public int getCode() {
        return code;
    }
}
