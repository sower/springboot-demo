package me.demo.controller;

import javax.annotation.Resource;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import me.demo.bean.entity.WebSite;
import me.demo.service.WebSiteService;
import me.demo.utils.Result;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description
 * @date 2022/09/28
 */
@RestController
@Slf4j
@Validated
@CacheConfig(cacheNames = {"website"})
public class WebSiteController {

  @Resource private WebSiteService webSiteService;

  @GetMapping("/website")
  @Cacheable(key = "targetClass")
  public Result<?> queryWebSites(String keyword) {
    return Result.success(webSiteService.queryWebSites(keyword));
  }

  @PostMapping("/website")
  @CachePut(key = "targetClass")
  public Result<?> webSite(@Valid @RequestBody WebSite webSite) {
    webSiteService.save(webSite);
    return Result.success(webSite);
  }

  @DeleteMapping("/website")
  @CacheEvict(key = "targetClass", allEntries = true)
  public Result<?> delete(String webSiteId) {
    webSiteService.delete(webSiteId);
    return Result.success();
  }
}
