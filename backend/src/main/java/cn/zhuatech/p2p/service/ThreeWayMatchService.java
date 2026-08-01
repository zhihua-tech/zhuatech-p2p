/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.p2p.service;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class ThreeWayMatchService {
    public MatchResult evaluate(MatchRequest request) {
        BigDecimal baseline = request.receiptAmount().signum() == 0
            ? request.purchaseOrderAmount() : request.receiptAmount();
        BigDecimal amountVariance = request.invoiceAmount().subtract(baseline).abs();
        double amountVarianceRate = baseline.signum() == 0 ? 0
            : amountVariance.multiply(BigDecimal.valueOf(100))
                .divide(baseline, 1, RoundingMode.HALF_UP).doubleValue();
        List<String> reasons = new ArrayList<>();
        if (request.duplicateInvoice()) reasons.add("发票号码或票面要素存在重复命中");
        if (!request.taxValid()) reasons.add("发票税务校验未通过");
        if (amountVarianceRate > 2) reasons.add("发票与收货金额差异超过 2%");
        if (request.quantityVarianceRate().compareTo(BigDecimal.valueOf(2)) > 0) reasons.add("收货数量差异超过容差");
        if (request.priceVarianceRate().compareTo(BigDecimal.valueOf(2)) > 0) reasons.add("采购价格差异超过容差");
        String decision = request.duplicateInvoice() || !request.taxValid() ? "BLOCK"
            : reasons.isEmpty() ? "AUTO_APPROVE" : "REVIEW";
        if (reasons.isEmpty()) reasons.add("采购订单、收货和发票满足三单匹配规则");
        return new MatchResult(amountVarianceRate, decision, reasons);
    }

    public record MatchRequest(@NotNull @DecimalMin("0.00") BigDecimal purchaseOrderAmount,
        @NotNull @DecimalMin("0.00") BigDecimal receiptAmount,
        @NotNull @DecimalMin("0.00") BigDecimal invoiceAmount,
        @NotNull @DecimalMin("0.00") @DecimalMax("100.00") BigDecimal quantityVarianceRate,
        @NotNull @DecimalMin("0.00") @DecimalMax("100.00") BigDecimal priceVarianceRate,
        @NotNull Boolean duplicateInvoice, @NotNull Boolean taxValid) {}
    public record MatchResult(double amountVarianceRate, String decision, List<String> reasons) {}
}
