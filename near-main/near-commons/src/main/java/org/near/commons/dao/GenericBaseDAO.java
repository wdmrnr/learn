package org.near.commons.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

public interface GenericBaseDAO<PK extends Serializable> {


  public <T> PK save(T entity);

  public <T> void batchSave(List<T> entitys);

  public <T> void saveOrUpdate(T entity);

  public <T> List<T> batchSaveList(List<T> entitys);

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


  public <T> void deleteEntityById(Class<T> entityClass, PK id);

  /**
   * 删除实体集合
   *
   * @param <T>
   * @param entities
   */
  public <T> void deleteAllEntitie(Collection<T> entities);

  /**
   * 更新指定的实体
   *
   * @param <T>
   * @param pojo
   */
  public <T> void updateEntitie(T pojo);


  /**
   * 通过hql 查询语句查找对象
   *
   * @param <T>
   * @param query
   * @return
   */
  public <T> List<T> findByQueryString(String hql);

  /**
   * 通过hql查询唯一对象
   *
   * @param <T>
   * @param query
   * @return
   */
  public <T> T singleResult(String hql);

  /**
   * 根据sql更新
   *
   * @param query
   * @return
   */
  public int updateBySqlString(String sql);

  /**
   * 根据sql查找List
   *
   * @param <T>
   * @param query
   * @return
   */
  public <T> List<T> findListbySql(String query);


  public Session getSession();



  /**
   * 执行SQL
   */
  public Integer executeSql(String sql, List<Object> param);

  /**
   * 执行SQL
   */
  public Integer executeSql(String sql, Object... param);

  /**
   * 执行SQL 使用:name占位符
   */
  public Integer executeSql(String sql, Map<String, Object> param);
  /**
   * 执行SQL 使用:name占位符,并返回插入的主键值
   */
  public Object executeSqlReturnKey(String sql, Map<String, Object> param);
  /**
   * 通过JDBC查找对象集合 使用指定的检索标准检索数据返回数据
   */
  public List<Map<String, Object>> findForJdbc(String sql, Object... objs);

  /**
   * 通过JDBC查找对象集合 使用指定的检索标准检索数据返回数据
   */
  public Map<String, Object> findOneForJdbc(String sql, Object... objs);



  /**
   * 通过hql 查询语句查找对象
   *
   * @param <T>
   * @param query
   * @return
   */
  public <T> List<T> findHql(String hql, Object... param);

  /**
   * 执行HQL语句操作更新
   *
   * @param hql
   * @return
   */
  public Integer executeHql(String hql);

  public <T> List<T> pageList(DetachedCriteria dc, int firstResult, int maxResult);

  public <T> List<T> findByDetached(DetachedCriteria dc);

}
