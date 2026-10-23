# 订单行累计收货与开票核验

> 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/
> 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。

## 实现与使用

对采购行比较订单、实收、历史开票和本次开票，使用未舍入比率判定数量与价格容差，未收货不得伪装匹配通过。

最多300行，容差0–100%；累计开票=历史+本次，必须在实收容差内。金额至分；数量不支持负数红冲，需单独退货/贷项流程。采购单价0而发票单价>0不伪造0%偏差；比较采用精确乘法。这里只核验输入，重复发票、税务、资金和审批另行处理，不自动付款。

- 入口：真实账号登录后，右下角“业务计算工具”。默认沿用当前登录JWT；演示模式的demo-token不可执行。
- 接口：`POST /api/workbench/line-receipt-invoice-match`，`Content-Type: application/json`，`Authorization: Bearer <登录令牌>`。
- 返回：200含`success:true,data`；身份缺失/失效401；输入错误400含可读message。独立计算只允许调用者提交数据，不读取数据库、其他用户数据、文件或URL。
- 结果在内存中保留，刷新或退出丢失；可导出完整JSON。列表仅预览100行，导出不会截断。
- 请求限制：数组最多500项（各业务数组有更小上限），嵌套12层，10000节点，文本总量128000字符，单文本64000字符。通用数值最多30位有效数字、尺度绝对值≤12；金额最多2位小数。日期1900–2200年；本地时间精确到整分钟。
- 页面限制请求128 KiB、超时15秒。服务端字段限制在JSON解码后执行；生产反向代理仍须配置请求体上限、速率限制和部署级访问控制，不将这些校验当作网络层防护。

## 请求示例（合成数据）

```json
{
  "quantityTolerancePercent": 0,
  "priceTolerancePercent": 2,
  "lines": [
    {
      "id": "L1",
      "ordered": 10,
      "received": 8,
      "invoicedBefore": 4,
      "currentInvoice": 5,
      "orderUnitPrice": 100,
      "invoiceUnitPrice": 100
    }
  ]
}
```

## 验证

后端Java21：`cd backend && mvn test`；前端：`cd frontend && npm ci && npm run build`。

新增测试覆盖算法样例、特定边界、异常输入、零分母或缺失证据、重复执行不修改输入、过深/过长请求、过高数字精度；MockMvc覆盖匿名401、无效JWT401、有效JWT200和参数错误400。用例位于`backend/src/test/java/cn/zhuatech/p2p/workbench/`。实际结果以本批交付测试报告为准，不以文档替代测试执行。

## 能力边界 / Scope

This is an authenticated, deterministic calculation over caller-supplied data. Checks ordered, received, previously invoiced and newly invoiced quantities per purchase line, with exact tolerance comparisons and explicit missing-receipt and zero-price-baseline exceptions. It does not run external inference, retrieve protected records, persist workflows or prove production readiness of the whole application. Results require business review; do not use them as automatic approval, regulatory certification or payment authorization.

原有业务模块、数据模式与许可不变；没有引入数据库迁移。该功能是可使用的辅助工具，不代表整个项目所有历史演示页面已改造成生产业务系统。

## 原有业务修复

修复原有三单匹配接口：没有有效收货金额时不能使用订单金额替代并给出AUTO_APPROVE；零金额没有匹配证据时进入REVIEW。金额差异是否超过2%以未舍入BigDecimal判断，amountVarianceRate仍为原有一位展示精度，新增amountVarianceDefined供前端区分无分母与真实0%。本服务返回规则建议，不实际支付或审批。回归包含无收货、全0、2.04%及恰好2%，以及真实JWT的HTTP响应。
