package org.near.commons.query;

import java.util.HashMap;
import java.util.Map;

import org.near.commons.query.entity.BaseEntity;
import org.near.commons.query.type.HibernateType;

public class SqlSearchBean extends SearchBean {
  public SqlSearchBean() {
    super();
  }

  public SqlSearchBean(int size) {
    super(size);
  }

  private Map<String, Class<? extends BaseEntity>> entities;
  private Map<String, HibernateType> colTypes;

  public Map<String, Class<? extends BaseEntity>> getEntities() {
    return entities;
  }

  public void setEntities(Map<String, Class<? extends BaseEntity>> entities) {
    this.entities = entities;
  }

  public Map<String, HibernateType> getColTypes() {
    return colTypes;
  }

  public void setColTypes(Map<String, HibernateType> colTypes) {
    this.colTypes = colTypes;
  }

  public SqlSearchBean addEntity(String alias, Class<? extends BaseEntity> clasz) {
    if (null != clasz) {
      if (null == entities) {
        this.entities = new HashMap<String, Class<? extends BaseEntity>>();
      }
      this.entities.put(alias, clasz);
    }
    return this;
  }

  public SqlSearchBean addScalar(String arg) {
    return addScalar(arg, null);
  }

  public SqlSearchBean addScalar(String arg, HibernateType type) {
    if (null == this.colTypes) {
      this.colTypes = new HashMap<String, HibernateType>();
    }
    if (null == type) {
      type = HibernateType.OBJECT;
    }
    this.colTypes.put(arg, type);
    return this;
  }

}
