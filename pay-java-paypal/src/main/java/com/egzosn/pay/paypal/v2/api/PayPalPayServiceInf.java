package com.egzosn.pay.paypal.v2.api;

import java.util.Map;

/**
 * PayPal 支付接口
 *
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2021/2/20
 * </pre>
 */
public interface PayPalPayServiceInf {

    /**
     * 注意：最好在付款成功之后回调时进行调用
     * 确认付款订单并返回确认后订单信息
     * 注意：此方法一个订单只能调用一次, 建议在支付回调时进行调用
     * 这里主要用来获取captureId使用，后续退款，查订单等等使用，用来替换下单返回的id
     * 详情： https://developer.paypal.com/docs/api/orders/v2/#orders_capture
     *
     * @param tradeNo paypal下单成功之后返回的订单号
     * @return 确认付款后订单信息
     */
    Map<String, Object> ordersCapture(String tradeNo);

    /**
     * 确认订单之后获取订单信息
     * 详情： https://developer.paypal.com/docs/api/payments/v2/#captures_get
     *
     * @param captureId 确认付款订单之后生成的id
     * @return 确认付款订单详情
     * <pre>
     *     {
     *   "id": "2GG279541U471931P",
     *   "status": "COMPLETED",
     *   "status_details": {},
     *   "amount": {
     *     "total": "10.99",
     *     "currency": "USD"
     *   },
     *   "final_capture": true,
     *   "seller_protection": {
     *     "status": "ELIGIBLE",
     *     "dispute_categories": [
     *       "ITEM_NOT_RECEIVED",
     *       "UNAUTHORIZED_TRANSACTION"
     *     ]
     *   },
     *   "seller_receivable_breakdown": {
     *     "gross_amount": {
     *       "total": "10.99",
     *       "currency": "USD"
     *     },
     *     "paypal_fee": {
     *       "value": "0.33",
     *       "currency": "USD"
     *     },
     *     "net_amount": {
     *       "value": "10.66",
     *       "currency": "USD"
     *     },
     *     "receivable_amount": {
     *       "currency_code": "CNY",
     *       "value": "59.26"
     *     },
     *     "paypal_fee_in_receivable_currency": {
     *       "currency_code": "CNY",
     *       "value": "1.13"
     *     },
     *     "exchange_rate": {
     *       "source_currency": "USD",
     *       "target_currency": "CNY",
     *       "value": "5.9483297432325"
     *     }
     *   },
     *   "invoice_id": "INVOICE-123",
     *   "create_time": "2017-09-11T23:24:01Z",
     *   "update_time": "2017-09-11T23:24:01Z",
     *   "links": [
     *     {
     *       "href": "https://api-m.paypal.com/v2/payments/captures/2GG279541U471931P",
     *       "rel": "self",
     *       "method": "GET"
     *     },
     *     {
     *       "href": "https://api-m.paypal.com/v2/payments/captures/2GG279541U471931P/refund",
     *       "rel": "refund",
     *       "method": "POST"
     *     },
     *     {
     *       "href": "https://api-m.paypal.com/v2/payments/authorizations/0VF52814937998046",
     *       "rel": "up",
     *       "method": "GET"
     *     }
     *   ]
     * }
     *
     * </pre>
     */
    Map<String, Object> getCapture(String captureId);
}
