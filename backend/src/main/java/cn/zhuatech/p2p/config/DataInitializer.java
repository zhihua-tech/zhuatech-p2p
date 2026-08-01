/* Copyright 2026 上海如静知华信息科技有限公司 */
package cn.zhuatech.p2p.config;

import cn.zhuatech.p2p.model.*;
import cn.zhuatech.p2p.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner seed(OperatingUnitRepository operatingUnits, WorkRecordRepository orders,
                           ResourceRegisterRepository resources, ReviewRecordRepository reviewRecords,
                           UserRepository users, PasswordEncoder encoder) {
        return args -> {
            if (operatingUnits.count() > 0) return;
            OperatingUnit primaryUnit = operatingUnits.save(new OperatingUnit("P2P-CHEM", "华东采购组", "采购运营中心", 180));
            OperatingUnit secondaryUnit = operatingUnits.save(new OperatingUnit("P2P-MICRO", "生产采购组", "研发中心", 120));
            OperatingUnit tertiaryUnit = operatingUnits.save(new OperatingUnit("P2P-MAT", "物流采购组", "工程中心", 96));

            WorkRecord t1 = orders.save(new WorkRecord("PO-260801-018", "GB-T-228", "研发测试设备采购", tertiaryUnit, 24, 16, 1, LocalDate.now().plusDays(1), WorkRecord.Status.RUNNING, "GW-Q3"));
            WorkRecord t2 = orders.save(new WorkRecord("PO-260801-021", "PO-TERM-2612", "云资源年度框架续签", primaryUnit, 18, 8, 0, LocalDate.now().plusDays(1), WorkRecord.Status.RUNNING, "TERM-12"));
            WorkRecord t3 = orders.save(new WorkRecord("PO-260802-006", "ISO-4833", "工厂劳保用品集采", secondaryUnit, 12, 0, 0, LocalDate.now().plusDays(3), WorkRecord.Status.RELEASED, "SP-2026"));
            WorkRecord t4 = orders.save(new WorkRecord("PO-260728-015", "PO-OVERSEA-09", "包装材料季度订单", primaryUnit, 20, 20, 1, LocalDate.now(), WorkRecord.Status.COMPLETED, "SEA-09"));

            resources.saveAll(List.of(
                new ResourceRegister("CAT-HPLC-03", "IT 设备框架协议", primaryUnit, ResourceRegister.Status.RUNNING, 88),
                new ResourceRegister("CAT-ICP-02", "办公用品采购目录", primaryUnit, ResourceRegister.Status.IDLE, 76),
                new ResourceRegister("CAT-UTM-05", "物流服务协议", tertiaryUnit, ResourceRegister.Status.RUNNING, 91),
                new ResourceRegister("CAT-INC-08", "劳保用品框架协议", secondaryUnit, ResourceRegister.Status.ALARM, 62)
            ));
            reviewRecords.saveAll(List.of(
                new ReviewRecord("ISS-260801-032", t1, "收货数量核验", 6, 0, ReviewRecord.Result.PASSED, "孟舟"),
                new ReviewRecord("ISS-260801-011", t2, "报价合规核验", 3, 0, ReviewRecord.Result.PASSED, "苏言"),
                new ReviewRecord("ISS-260801-018", t4, "三单匹配复核", 5, 1, ReviewRecord.Result.FAILED, "孟舟"),
                new ReviewRecord("ISS-260802-003", t3, "请购审批确认", 4, 0, ReviewRecord.Result.PENDING, "苏言")
            ));
            String demo = encoder.encode("Demo@2026");
            users.saveAll(List.of(
                new UserAccount("operator", demo, "苏言", UserAccount.Role.DOMAIN_USER, "P2P-CHEM"),
                new UserAccount("planner", demo, "孟舟", UserAccount.Role.DOMAIN_OPERATOR, null),
                new UserAccount("quality", demo, "顾清", UserAccount.Role.QUALITY, null),
                new UserAccount("admin", encoder.encode("ZhuaTech@2026"), "系统管理员", UserAccount.Role.ADMIN, null)
            ));
        };
    }
}
