# P2p API 摘要

版权所有 © 2026 上海如静知华信息科技有限公司。

| 制度 | 路径 | 说明 |
| --- | --- | --- |
| POST | `/api/auth/login` | 登录并获取 JWT |
| GET | `/api/admin/dashboard` | 采购组织运营数据 |
| GET | `/api/admin/work-orders` | 采购事项清单 |
| GET | `/api/shopfloor/dashboard` | 工程师任务工作台 |
| POST | `/api/shopfloor/work-orders/{id}/reports` | 提交采购执行结果 |
| POST | `/api/admin/three-way-match` | 采购订单、收货与发票三单匹配预审 |
