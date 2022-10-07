package me.demo.repository;

import me.demo.bean.entity.WebSite;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface WebSiteDao extends BaseRepository<WebSite, String>,
    JpaSpecificationExecutor<WebSite> {

}
