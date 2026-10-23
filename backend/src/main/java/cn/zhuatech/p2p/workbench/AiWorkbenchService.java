/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.p2p.workbench;

import static cn.zhuatech.p2p.workbench.WorkbenchInput.*;

import java.math.*;
import java.util.*;
import org.springframework.stereotype.Service;

/** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
@Service
public class AiWorkbenchService {
  /** 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。 */
  public Map<String, Object> execute(Map<String, Object> r) {
    validate(r);

    keys(r, "quantityTolerancePercent", "priceTolerancePercent", "lines");
    var qt = decimal(r, "quantityTolerancePercent", 0, 100).movePointLeft(2);
    var pt = decimal(r, "priceTolerancePercent", 0, 100).movePointLeft(2);
    Set<String> ids = new HashSet<>();
    List<Map<String, Object>> out = new ArrayList<>();
    boolean all = true;
    for (var line : rows(r, "lines", 1, 300)) {
      keys(
          line,
          "id",
          "ordered",
          "received",
          "invoicedBefore",
          "currentInvoice",
          "orderUnitPrice",
          "invoiceUnitPrice");
      String id = id(line, "id");
      unique(ids, id);
      var ordered = decimal(line, "ordered", 0.000001, 1e9);
      var received = decimal(line, "received", 0, 1e9);
      var previous = decimal(line, "invoicedBefore", 0, 1e9);
      var current = decimal(line, "currentInvoice", 0, 1e9);
      var po = money(line, "orderUnitPrice");
      var ip = money(line, "invoiceUnitPrice");
      var cumulative = previous.add(current);
      List<String> issues = new ArrayList<>();
      if (received.compareTo(ordered.multiply(BigDecimal.ONE.add(qt))) > 0)
        issues.add("OVER_RECEIPT");
      if (current.signum() > 0 && received.signum() == 0) issues.add("NO_RECEIPT");
      if (cumulative.compareTo(received.multiply(BigDecimal.ONE.add(qt))) > 0)
        issues.add("OVER_INVOICED");
      if (po.signum() == 0 && ip.signum() > 0) issues.add("NO_PRICE_BASELINE");
      else if (po.signum() > 0 && ip.subtract(po).abs().compareTo(po.multiply(pt)) > 0)
        issues.add("PRICE_VARIANCE");
      if (!issues.isEmpty()) all = false;
      out.add(
          Map.of(
              "id",
              id,
              "cumulativeInvoiced",
              cumulative,
              "receiptRemaining",
              received.subtract(cumulative),
              "invoiceAmount",
              current.multiply(ip).setScale(2, RoundingMode.HALF_UP),
              "matched",
              issues.isEmpty(),
              "issues",
              issues));
    }
    return Map.of("allMatched", all, "lines", out, "automaticPayment", false);
  }
}
