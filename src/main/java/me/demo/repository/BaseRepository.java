package me.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @description
 * @date 2022/09/18
 **/
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {

}
