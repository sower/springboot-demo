package me.demo.bean.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import lombok.Data;
import me.demo.aspect.AnyOf;
import me.demo.constant.Gender;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @description
 * @date 2022/09/28
 **/
@Data
@Entity
@Table(name = "user")
@NamedEntityGraph(name = "user",
    attributeNodes = {@NamedAttributeNode("webSites")})
public class User implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GenericGenerator(name = "ntg", strategy = "native")
  @GeneratedValue(generator = "ntg")
  private int id;

  @NotBlank(message = "Name must be not blank")
  @AnyOf(values = {"abc", "Tom"},message = "only special values")
  @Column(unique = true)
  private String name;

  @Positive
  @Max(200)
  private int age;

  @Transient
  private boolean online;

  @Past
  // 后台到前台的时间格式的转换
  @JsonFormat(pattern = "yyyy-MM-dd")
  // 前后到后台的时间格式的转换
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Column(name = "birthday", unique = true)
  private LocalDate birth;

  @Enumerated(EnumType.STRING)
  private Gender gender = Gender.UNKNOWN;

  @Convert(converter = JpaConverterList.class)
  private List<@Email String> emails;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "creator", referencedColumnName = "name")
//  @JoinColumn(name = "name", referencedColumnName = "creator")
//  private WebSite webSite;
  private List<WebSite> webSites;
}
