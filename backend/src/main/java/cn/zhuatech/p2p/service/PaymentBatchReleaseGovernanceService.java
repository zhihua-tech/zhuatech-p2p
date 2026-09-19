/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.p2p.service;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@Service
public class PaymentBatchReleaseGovernanceService {
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public Assessment assess(Request request) {
        List<String> blockers = new ArrayList<>();
        List<String> actions = new ArrayList<>();
        if (request.invoiceExceptionCount() > 0) blockers.add("付款批次仍包含发票异常");
        if (!request.threeWayMatchComplete()) blockers.add("采购订单、收货与发票尚未全部匹配");
        if (!request.vendorBankVerified()) blockers.add("供应商收款账户未完成独立验证");
        if (!request.sanctionsCleared()) blockers.add("供应商受限方筛查未通过");
        if (!request.liquidityReserved()) blockers.add("付款资金尚未完成头寸预留");
        if (!request.requesterApproverSeparated()) blockers.add("付款申请与审批未实现职责分离");
        if (!request.dualApprovalComplete()) actions.add("完成双人付款授权");
        if (!request.paymentFileSigned()) actions.add("签名并校验付款文件摘要");

        Decision decision = !blockers.isEmpty() ? Decision.BLOCK
                : !actions.isEmpty() ? Decision.APPROVAL_REQUIRED : Decision.RELEASE;
        return new Assessment(request.batchNo(), request.paymentCount(), decision,
                List.copyOf(blockers), List.copyOf(actions));
    }

    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Request(@NotBlank String batchNo, @Min(1) int paymentCount,
                          @Min(0) int invoiceExceptionCount, boolean threeWayMatchComplete,
                          boolean vendorBankVerified, boolean sanctionsCleared,
                          boolean liquidityReserved, boolean requesterApproverSeparated,
                          boolean dualApprovalComplete, boolean paymentFileSigned) {}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public record Assessment(String batchNo, int paymentCount, Decision decision,
                             List<String> blockers, List<String> actions) {}
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public enum Decision { RELEASE, APPROVAL_REQUIRED, BLOCK }
}
