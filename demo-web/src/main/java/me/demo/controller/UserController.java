package me.demo.controller;

import java.util.List;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.demo.annotation.RateLimit;
import me.demo.bean.entity.User;
import me.demo.repository.UserDao;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @date 2022/10/02
 */
@RestController
@Slf4j
public class UserController {

  @Resource private UserDao userDao;

  @GetMapping("/user")
  @RateLimit(value = 1.0)
  public List<User> queryUser() {

    return userDao.findAll();
  }

  @PostMapping("/user")
  public User createUser(@RequestBody @Validated User user) {
    userDao.save(user);
    return user;
  }
}
