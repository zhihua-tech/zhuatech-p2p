/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.p2p.controller;

import cn.zhuatech.p2p.common.ApiResponse;
import cn.zhuatech.p2p.service.ThreeWayMatchService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController
@RequestMapping("/api/admin/three-way-match")
public class ThreeWayMatchController {
    private final ThreeWayMatchService service;
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    public ThreeWayMatchController(ThreeWayMatchService service) { this.service = service; }
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @PostMapping
    ApiResponse<ThreeWayMatchService.MatchResult> evaluate(
        @Valid @RequestBody ThreeWayMatchService.MatchRequest request) {
        return ApiResponse.ok(service.evaluate(request));
    }
}
