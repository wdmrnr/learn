package org.near.commons.service;

import java.io.Serializable;
import java.util.List;

public interface IGenericService<PK extends Serializable>{
  public <T> PK save(T entity);

  public <T> void batchSave(List<T> entitys);

  public <T> void saveOrUpdate(T entity);

  public <T> void update(T entity);

  /**
   * 删除实体
   *
   * @param <T>
   *
   * @param <T>
   *
   * @param <T>
   * @param entity
   */
  public <T> void delete(T entity);

  /**
   * 根据实体名称和主键获取实体
   *
   * @param <T>
   * @param entityClass
   * @param id
   * @return
   */
  public <T> T get(Class<T> entityClass, PK id);

  /**
   * 加载全部实体
   *
   * @param <T>
   * @param entityClass
   * @return
   */
  public <T> List<T> loadAll(final Class<T> entityClass);
}
