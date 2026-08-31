# 企业级采购付款批次放行

付款批次联合校验发票异常、三单匹配、供应商收款账户、受限方筛查、资金预留、职责分离、双人授权和付款文件签名。

`POST /api/enterprise/p2p/payment-batch-release` 返回 `RELEASE / APPROVAL_REQUIRED / BLOCK` 决策。生产部署应关联真实付款批次、银行回单、授权矩阵和不可篡改审计证据。
