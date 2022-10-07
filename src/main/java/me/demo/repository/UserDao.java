package me.demo.repository;

import java.util.List;
import me.demo.bean.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Integer> {

  @Override
  @EntityGraph(value = "user")
  List<User> findAll();
}
