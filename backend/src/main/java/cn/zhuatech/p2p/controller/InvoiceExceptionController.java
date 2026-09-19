/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.p2p.controller;import cn.zhuatech.p2p.common.ApiResponse;import cn.zhuatech.p2p.service.InvoiceExceptionService;import jakarta.validation.Valid;import org.springframework.web.bind.annotation.*;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
@RestController @RequestMapping("/api/p2p/insights/invoice-exception") public class InvoiceExceptionController{private final InvoiceExceptionService service;/**
                                                                                                                                                              * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                              */
public InvoiceExceptionController(InvoiceExceptionService service){this.service=service;}/**
                                                                                                                                                                                                                                                       * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
                                                                                                                                                                                                                                                       */
@PostMapping ApiResponse<InvoiceExceptionService.Result> evaluate(@Valid @RequestBody InvoiceExceptionService.Request r){return ApiResponse.ok(service.evaluate(r));}}
