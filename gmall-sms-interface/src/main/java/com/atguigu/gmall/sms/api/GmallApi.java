package com.atguigu.gmall.sms.api;

import com.atguigu.core.bean.Resp;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface GmallApi {

    @PostMapping("sms/skubounds/sku/sale/save")
    Resp<Object> saveSale(@RequestBody SkuSaleVO skuSaleVO);
}
