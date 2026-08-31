/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.p2p.service;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class PaymentBatchReleaseGovernanceServiceTest {
    private final PaymentBatchReleaseGovernanceService service = new PaymentBatchReleaseGovernanceService();

    @Test void releasesControlledPaymentBatch() {
        var result = service.assess(new PaymentBatchReleaseGovernanceService.Request(
                "PAY-001", 18, 0, true, true, true, true, true, true, true));
        assertThat(result.decision()).isEqualTo(PaymentBatchReleaseGovernanceService.Decision.RELEASE);
        assertThat(result.blockers()).isEmpty();
    }

    @Test void blocksUncontrolledPaymentBatch() {
        var result = service.assess(new PaymentBatchReleaseGovernanceService.Request(
                "PAY-002", 8, 2, false, false, false, false, false, false, false));
        assertThat(result.decision()).isEqualTo(PaymentBatchReleaseGovernanceService.Decision.BLOCK);
        assertThat(result.blockers()).hasSize(6);
        assertThat(result.actions()).hasSize(2);
    }
}
