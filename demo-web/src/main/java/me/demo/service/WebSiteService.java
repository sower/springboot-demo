package me.demo.service;

import java.util.List;
import me.demo.bean.entity.WebSite;

public interface WebSiteService {

  List<WebSite> queryWebSites(String keyword);

  void save(WebSite webSite);

  void delete(String id);
}
