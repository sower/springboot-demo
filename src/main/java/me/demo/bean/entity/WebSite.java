package me.demo.bean.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * @description
 * @date 2022/09/26
 **/
@Data
@Entity
@Table(name = "website")
@SecondaryTable(name = "user", pkJoinColumns = @PrimaryKeyJoinColumn(name = "id"))
public class WebSite extends BaseEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Size(min = 1, max = 10, message = "Name must be between {min} and {max} characters long")
  private String name;

  @NotBlank
  private String url;

  @Column(insertable = false, updatable = false)
  private String description;

  @Column(name = "name", table = "user")
  private String userName;

  @Column(table = "user")
  private String age;
}
