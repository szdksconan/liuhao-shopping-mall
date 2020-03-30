package com.atguigu.gmall.pms.fegin;

import com.atguigu.gmall.sms.api.GmallApi;
import com.atguigu.gmall.sms.api.SkuSaleVO;
import com.atguigu.core.bean.Resp;
import com.atguigu.gmall.sms.api.GmallApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("sms-service")
public interface GmallSmsClient extends GmallApi {



}
