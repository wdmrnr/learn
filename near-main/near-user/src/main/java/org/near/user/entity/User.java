package org.near.user.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="t_user")
public class User implements Serializable{

  /**
   *
   */
  private static final long serialVersionUID = -2069635070513942546L;

  private Long id;
  private String userName;// 用户名
  private String passWord;//用户密码

  public User() {
    super();
  }

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "USER_SEQ")
  @SequenceGenerator(name = "USER_SEQ", allocationSize = 1, initialValue = 1, sequenceName = "USER_SEQ")
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }

  @Column(name="user_name", length=50)
  public String getUserName() {
    return userName;
  }
  public void setUserName(String userName) {
    this.userName = userName;
  }

  @Column(name="password", length=50)
  public String getPassWord() {
    return passWord;
  }
  public void setPassWord(String passWord) {
    this.passWord = passWord;
  }


}
