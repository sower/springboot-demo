package me.demo.service;

import com.alibaba.fastjson2.JSONObject;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ServerDemoFeignClient",
    url = "http://httpbin.org")
public interface FeignService {

  @GetMapping("/uuid")
  Map<String, String> uuid();

  @GetMapping("/get")
  JSONObject get(@RequestParam("msg") String msg);

}
