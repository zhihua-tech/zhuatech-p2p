/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.p2p.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.*;
import java.util.*;
import org.junit.jupiter.api.Test;

/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
class ThreeWayMatchRegressionTests {
  private final ThreeWayMatchService service = new ThreeWayMatchService();

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  private ThreeWayMatchService.MatchResult run(String po, String received, String invoice) {
    return service.evaluate(
        new ThreeWayMatchService.MatchRequest(
            new BigDecimal(po),
            new BigDecimal(received),
            new BigDecimal(invoice),
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            false,
            true));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void missingReceiptDoesNotAutoApprove() {
    var r = run("100", "0", "100");
    assertEquals("REVIEW", r.decision());
    assertFalse(r.amountVarianceDefined());
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void zeroAmountsStillRequireEvidence() {
    var r = run("0", "0", "0");
    assertEquals("REVIEW", r.decision());
    assertFalse(r.amountVarianceDefined());
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void unroundedVarianceAboveTwoRequiresReview() {
    var r = run("10000", "10000", "10204");
    assertEquals("REVIEW", r.decision());
    assertEquals(2.0, r.amountVarianceRate());
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void exactToleranceRemainsAllowed() {
    assertEquals("AUTO_APPROVE", run("10000", "10000", "10200").decision());
  }
}
