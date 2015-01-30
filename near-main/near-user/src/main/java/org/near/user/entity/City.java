package org.near.user.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="T_BC_CITY")
public class City implements Serializable{

  /**
   *
   */
  private static final long serialVersionUID = 2443146037151811706L;
  @Id
  private Long id;
  @Column(name="name", length=100)
  private String name;
  @Column(name="city_level")
  private Integer level;

  //经度
  @Column(name="longitude")
  private Double longitude;

  // 纬度
  @Column(name="latitude")
  private Double latitude;


  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public Integer getLevel() {
    return level;
  }
  public void setLevel(Integer level) {
    this.level = level;
  }
  public Double getLongitude() {
    return longitude;
  }
  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }
  public Double getLatitude() {
    return latitude;
  }
  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

}
