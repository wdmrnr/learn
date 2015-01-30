package org.near.commons.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.transform.Transformers;
import org.near.commons.dao.IGenericBaseDAO;
import org.near.commons.query.HqlSearchBean;
import org.near.commons.query.SearchBean;
import org.near.commons.query.SqlSearchBean;
import org.near.commons.query.bean.PageList;
import org.near.commons.query.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
public class GenericBaseDAO<PK extends Serializable> implements
    IGenericBaseDAO<PK> {

  protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

  /**
   * 注入一个sessionFactory属性,并注入到父类(HibernateDaoSupport)
   * **/
  @Autowired
  @Qualifier("sessionFactory")
  private SessionFactory sessionFactory;

  @Autowired
  @Qualifier("jdbcTemplate")
  private JdbcTemplate jdbcTemplate;

  @Autowired
  @Qualifier("namedParameterJdbcTemplate")
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public <T> PK save(T entity) {
    return (PK) getSession().save(entity);
  }

  @Override
  public <T> void batchSave(List<T> entitys) {
    for (int i = 0; i < entitys.size(); i++) {
      getSession().save(entitys.get(i));
      if (i % 500 == 0) {
        // 20个对象后才清理缓存，写入数据库
        getSession().flush();
        getSession().clear();
      }
    }
    // 最后清理一下----防止大于20小于40的不保存
    getSession().flush();
    getSession().clear();
  }

  public <T> List<T> batchSaveList(List<T> entitys) {
    List<T> list = new ArrayList<T>();
    for (int i = 0; i < entitys.size(); i++) {
      getSession().save(entitys.get(i));
      list.add(entitys.get(i));
      if (i % 20 == 0) {
        // 20个对象后才清理缓存，写入数据库
        getSession().flush();
        getSession().clear();
      }
    }
    // 最后清理一下----防止大于20小于40的不保存
    getSession().flush();
    getSession().clear();
    return list;
  }

  @Override
  public <T> void saveOrUpdate(T entity) {
    try {
      getSession().saveOrUpdate(entity);
      getSession().flush();
      LOGGER.debug("添加或更新成功," + entity.getClass().getName());
    } catch (RuntimeException e) {
      LOGGER.error("添加或更新异常", e);
      throw e;
    }
  }

  @Override
  public <T> void delete(T entity) {
    try {
      getSession().delete(entity);
      getSession().flush();
      LOGGER.debug("删除成功," + entity.getClass().getName());
    } catch (RuntimeException e) {
      LOGGER.error("删除异常", e);
      throw e;
    }

  }

  @Override
  public <T> T get(Class<T> entityClass, PK id) {
    return (T) getSession().get(entityClass, id);
  }

  @Override
  public <T> List<T> loadAll(Class<T> entityClass) {
    Criteria criteria = createCriteria(entityClass);
    return criteria.list();
  }

  @Override
  public <T> void deleteEntityById(Class<T> entityClass, PK id) {
    delete(get(entityClass, id));
    getSession().flush();
  }

  @Override
  public <T> void deleteAllEntitie(Collection<T> entities) {
    for (Object entity : entities) {
      getSession().delete(entity);
    }
    getSession().flush();
  }

  @Override
  public <T> void updateEntitie(T pojo) {
    getSession().update(pojo);
  }

  @Override
  public <T> List<T> findByQueryString(String hql) {
    Query queryObject = getSession().createQuery(hql);
    List<T> list = queryObject.list();
    if (list.size() > 0) {
      getSession().flush();
    }
    return list;
  }

  @Override
  public <T> T singleResult(String hql) {
    T t = null;
    Query queryObject = getSession().createQuery(hql);
    List<T> list = queryObject.list();
    if (list.size() == 1) {
      getSession().flush();
      t = list.get(0);
    } else if (list.size() > 0) {
      throw new BusinessException("查询结果数:\\d大于1", Integer.valueOf(list.size()));
    }
    return t;
  }

  @Override
  public int updateBySqlString(String sql) {
    Query querys = getSession().createSQLQuery(sql);
    return querys.executeUpdate();
  }

  @Override
  public <T> List<T> findListbySql(String query) {
    Query querys = getSession().createSQLQuery(query);
    return querys.list();
  }

  @Override
  public Session getSession() {
    return sessionFactory.getCurrentSession();
  }

  @Override
  public Integer executeSql(String sql, List<Object> param) {
    return this.jdbcTemplate.update(sql, param);
  }

  @Override
  public Integer executeSql(String sql, Object... param) {
    return this.jdbcTemplate.update(sql, param);
  }

  @Override
  public Integer executeSql(String sql, Map<String, Object> param) {
    return this.namedParameterJdbcTemplate.update(sql, param);
  }

  @Override
  public Object executeSqlReturnKey(String sql, Map<String, Object> param) {
    Object keyValue = null;
    try {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      SqlParameterSource sqlp = new MapSqlParameterSource(param);
      this.namedParameterJdbcTemplate.update(sql, sqlp, keyHolder);
      keyValue = keyHolder.getKey().longValue();
    } catch (Exception e) {
      keyValue = null;
    }
    return keyValue;
  }

  @Override
  public List<Map<String, Object>> findForJdbc(String sql, Object... objs) {
    return this.jdbcTemplate.queryForList(sql, objs);
  }

  @Override
  public Map<String, Object> findOneForJdbc(String sql, Object... objs) {
    try {
      return this.jdbcTemplate.queryForMap(sql, objs);
    } catch (EmptyResultDataAccessException e) {
      return null;
    }
  }

  @Override
  public <T> List<T> findHql(String hql, Object... param) {
    Query q = getSession().createQuery(hql);
    if (param != null && param.length > 0) {
      for (int i = 0; i < param.length; i++) {
        q.setParameter(i, param[i]);
      }
    }
    return q.list();
  }

  @Override
  public Integer executeHql(String hql) {
    Query q = getSession().createQuery(hql);
    return q.executeUpdate();
  }

  @Override
  public <T> List<T> pageList(DetachedCriteria dc, int firstResult,
      int maxResult) {
    Criteria criteria = dc.getExecutableCriteria(getSession());
    criteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
    criteria.setFirstResult(firstResult);
    criteria.setMaxResults(maxResult);
    return criteria.list();
  }

  @Override
  public <T> List<T> findByDetached(DetachedCriteria dc) {
    return dc.getExecutableCriteria(getSession()).list();
  }

  /**
   * 创建Criteria对象带属性比较
   *
   * @param <T>
   * @param entityClass
   * @param criterions
   * @return
   */
  protected <T> Criteria createCriteria(Class<T> entityClass,
      Criterion... criterions) {
    Criteria criteria = getSession().createCriteria(entityClass);
    for (Criterion c : criterions) {
      criteria.add(c);
    }
    return criteria;
  }

  /**
   * 通用 分页
   *
   * @param <E>
   * @param searchBean
   * @param clasz
   * @return
   */
  protected <T> PageList findCurrentPageData(final SearchBean searchBean,
      final Class<T> clasz) {
    if (null == searchBean || StringUtils.isEmpty(searchBean.getQuery(true))) {
      return null;
    }
    Integer totalCount = null;
    List<T> list = null;
    PageList page = null;
    try {
      totalCount = findTotalCount(searchBean);
      list = findList(searchBean, clasz);
      page = new PageList(searchBean.getCurrentPageNum(), totalCount,
          searchBean.getPageSize(), list);
    } finally {
      totalCount = null;
      list = null;
    }
    return page;
  }

  /**
   * 通用 查询方法
   *
   * @param <E>
   *          E为查询返回的List泛型 类型
   * @param searchBean
   * @param clasz
   * @return
   */
  protected <T> List<T> findList(final SearchBean searchBean,
      final Class<T> clasz) {
    if (null == searchBean || StringUtils.isEmpty(searchBean.getQuery())) {
      return null;
    }
    final Session session = getSession();
    List<T> list = null;
    Query query = null;
    try {
      if (searchBean instanceof HqlSearchBean) {
        query = session.createQuery(searchBean.getQuery());
      } else if (searchBean instanceof SqlSearchBean) {
        query = session.createSQLQuery(searchBean.getQuery());

      }
      setParams(query, searchBean);
      if (searchBean.isPaging()) {
        query.setFirstResult(searchBean.getStartNum());
        query.setMaxResults(searchBean.getPageSize());
      }
      if (searchBean instanceof SqlSearchBean) {
        SQLQuery sqlQuery = (SQLQuery) query;
        SqlSearchBean sqlBean = (SqlSearchBean) searchBean;
        if (null != sqlBean.getEntities() && !sqlBean.getEntities().isEmpty()) {
          for (String alias : sqlBean.getEntities().keySet()) {
            sqlQuery.addEntity(alias, sqlBean.getEntities().get(alias));
          }
        } else if (null != sqlBean.getColTypes()
            && !sqlBean.getColTypes().isEmpty()) {
          for (String colAlias : sqlBean.getColTypes().keySet()) {
            sqlQuery.addScalar(colAlias, sqlBean.getColTypes().get(colAlias)
                .type());
          }
          sqlQuery.setResultTransformer(Transformers.aliasToBean(clasz));
        }
      }

      List<?> tmp = query.list();
      if (null != tmp && !tmp.isEmpty()) {
        list = (List<T>) tmp;
      }
    } finally {
      query = null;
    }
    return list;
  }

  protected <T> T findSingle(SearchBean searchBean, final Class<T> clasz) {
    searchBean.setPageSize(Integer.valueOf(1));
    List<T> list = findList(searchBean, clasz);
    if (null != list && !list.isEmpty()) {
      return list.get(0);
    }
    return null;
  }

  protected Integer findTotalCount(final SearchBean searchBean) {
    if (null == searchBean || StringUtils.isEmpty(searchBean.getCountQuery())) {
      return 0;
    }
    Session session = getSession();
    Query query = null;
    try {

      if (searchBean instanceof HqlSearchBean) {
        query = session.createQuery(searchBean.getCountQuery());
      } else if (searchBean instanceof SqlSearchBean) {
        query = session.createSQLQuery(searchBean.getCountQuery());
      }
      setParams(query, searchBean);
      Object tmp = query.uniqueResult();
      if (null != tmp) {
        return Integer.valueOf(tmp.toString());
      }
    } finally {
      query = null;

    }
    return Integer.valueOf(0);
  }

  protected void setParams(Query query, SearchBean searchBean) {
    if (null != searchBean.getParamKeys()) {
      Object tmp = null;
      String className = null;
      try {

        for (String key : searchBean.getParamKeys()) {
          LOGGER.debug("param key : {}", key);
          tmp = searchBean.getValueByKey(key);
          if (null == tmp) {
            query.setString(key, "");
          } else {
            try {
              className = tmp.getClass().getSimpleName();
              if ("String".equals(className)) {
                if (String.valueOf(tmp).indexOf("%") > -1) {
                  query.setString(key,
                      String.valueOf(tmp).replaceAll("[ ]*%[ ]*", "%").trim());
                } else {
                  query.setString(key, String.valueOf(tmp).trim());
                }

              } else if ("Integer".equals(className)) {
                query.setInteger(key, Integer.valueOf(tmp.toString()));
              } else if ("Double".equals(className)) {
                query.setDouble(key, Double.valueOf(tmp.toString()));
              } else if (tmp instanceof List) {
                query.setParameterList(key, (List) tmp);
              } else if ("Long".equals(className)) {
                query.setLong(key, Long.valueOf(tmp.toString()));
              } else if (tmp instanceof Date) {
                query.setDate(key, (Date) tmp);
              } else if ("Boolean".equals(className)) {
                query.setBoolean(key, (Boolean) tmp);
              } else {
                query.setParameter(key, tmp);
              }
            } catch (NumberFormatException e) {
              query.setParameter(key, null);
            }
          }
        }
      } finally {
        tmp = null;
        className = null;
      }
    }
  }

  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
    return namedParameterJdbcTemplate;
  }

}
