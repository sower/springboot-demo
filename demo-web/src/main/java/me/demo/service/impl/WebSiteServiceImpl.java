package me.demo.service.impl;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import me.demo.annotation.LogInOutParam;
import me.demo.bean.entity.WebSite;
import me.demo.repository.WebSiteDao;
import me.demo.service.WebSiteService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @description
 * @date 2022/10/07
 */
@Slf4j
@Service
public class WebSiteServiceImpl implements WebSiteService {

  @Resource private WebSiteDao webSiteDao;

  @Resource private HttpServletRequest request;

  @Override
  @LogInOutParam
  public List<WebSite> queryWebSites(String keyword) {

    Specification<WebSite> specification =
        (root, query, criteriaBuilder) -> {
          if (StringUtils.isNotBlank(keyword)) {
            return criteriaBuilder.like(root.get("name"), StringUtils.wrap(keyword, "%"));
          }
          //      return criteriaBuilder.isNotNull(root.get("name"));
          return null;
        };

    return webSiteDao.findAll(specification);
  }

  @Override
  public void save(WebSite webSite) {
    webSiteDao.save(webSite);
  }

  @Override
  public void delete(String id) {
    webSiteDao.deleteById(id);
  }
}
