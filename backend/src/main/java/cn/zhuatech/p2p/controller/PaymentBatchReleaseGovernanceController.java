/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.p2p.controller;

import cn.zhuatech.p2p.common.ApiResponse;
import cn.zhuatech.p2p.service.PaymentBatchReleaseGovernanceService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/enterprise/p2p")
public class PaymentBatchReleaseGovernanceController {
    private final PaymentBatchReleaseGovernanceService service;
    public PaymentBatchReleaseGovernanceController(PaymentBatchReleaseGovernanceService service) { this.service = service; }

    @PostMapping("/payment-batch-release")
    public ApiResponse<PaymentBatchReleaseGovernanceService.Assessment> assess(
            @Valid @RequestBody PaymentBatchReleaseGovernanceService.Request request) {
        return ApiResponse.ok("采购付款批次评估完成", service.assess(request));
    }
}
