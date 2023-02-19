package me.demo.base;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 实体基类
 *
 * @date 2022/09/26
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

  @Id
  @GenericGenerator(name = "idGenerator", strategy = "uuid")
  @GeneratedValue(generator = "idGenerator")
  private String id;

  @CreatedDate
  @Column(name = "create_time", updatable = false)
  private LocalDateTime createTime;

  @CreatedBy
  @Column(name = "creator", updatable = false, length = 64)
  private String creator;

  @LastModifiedDate
  @Column(name = "update_time")
  private LocalDateTime updateTime;

  @LastModifiedBy
  @Column(name = "update_by", length = 64)
  private String updateBy;
}
