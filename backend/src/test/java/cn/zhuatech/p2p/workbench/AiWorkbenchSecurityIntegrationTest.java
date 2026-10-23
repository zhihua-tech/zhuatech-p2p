/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.p2p.workbench;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import cn.zhuatech.p2p.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
@SpringBootTest
@AutoConfigureMockMvc
class AiWorkbenchSecurityIntegrationTest {
  @Autowired MockMvc mvc;
  @Autowired JwtService jwt;

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void securityFilterRejectsAnonymous() throws Exception {
    mvc.perform(
            post("/api/workbench/line-receipt-invoice-match")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"quantityTolerancePercent\":0,\"priceTolerancePercent\":2,\"lines\":[{\"id\":\"L1\",\"ordered\":10,\"received\":8,\"invoicedBefore\":4,\"currentInvoice\":5,\"orderUnitPrice\":100,\"invoiceUnitPrice\":100}]}"))
        .andExpect(status().isUnauthorized());
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void securityFilterRejectsInvalidBearer() throws Exception {
    mvc.perform(
            post("/api/workbench/line-receipt-invoice-match")
                .header("Authorization", "Bearer invalid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"quantityTolerancePercent\":0,\"priceTolerancePercent\":2,\"lines\":[{\"id\":\"L1\",\"ordered\":10,\"received\":8,\"invoicedBefore\":4,\"currentInvoice\":5,\"orderUnitPrice\":100,\"invoiceUnitPrice\":100}]}"))
        .andExpect(status().isUnauthorized());
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void authenticatedCallerCanCompute() throws Exception {
    mvc.perform(
            post("/api/workbench/line-receipt-invoice-match")
                .header("Authorization", "Bearer " + jwt.generate("operator"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"quantityTolerancePercent\":0,\"priceTolerancePercent\":2,\"lines\":[{\"id\":\"L1\",\"ordered\":10,\"received\":8,\"invoicedBefore\":4,\"currentInvoice\":5,\"orderUnitPrice\":100,\"invoiceUnitPrice\":100}]}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void authenticatedCallerGetsValidationError() throws Exception {
    mvc.perform(
            post("/api/workbench/line-receipt-invoice-match")
                .header("Authorization", "Bearer " + jwt.generate("operator"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.success").value(false));
  }

  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  @Test
  void existingBusinessEndpointRegression() throws Exception {
    mvc.perform(
            post("/api/admin/three-way-match")
                .header("Authorization", "Bearer " + jwt.generate("planner"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"purchaseOrderAmount\":100,\"receiptAmount\":0,\"invoiceAmount\":100,\"quantityVarianceRate\":0,\"priceVarianceRate\":0,\"duplicateInvoice\":false,\"taxValid\":true}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.decision").value("REVIEW"))
        .andExpect(jsonPath("$.data.amountVarianceDefined").value(false));
  }
}
