package org.near.commons.service.impl;

import java.io.Serializable;
import java.util.List;

import org.near.commons.dao.IGenericBaseDAO;
import org.near.commons.service.IGenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class GenericService<PK extends Serializable> implements IGenericService<PK>{

  protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

  @Autowired
  protected IGenericBaseDAO<PK> genericBaseDao;

  @Override
  public <T> PK save(T entity) {
    return genericBaseDao.save(entity);
  }

  @Override
  public <T> void batchSave(List<T> entitys) {
    genericBaseDao.batchSave(entitys);
  }

  @Override
  public <T> void saveOrUpdate(T entity) {
    genericBaseDao.saveOrUpdate(entity);
  }

  @Override
  public <T> void delete(T entity) {
    genericBaseDao.delete(entity);
  }

  @Override
  public <T> T get(Class<T> entityClass, PK id) {
    return genericBaseDao.get(entityClass, id);
  }

  @Override
  public <T> List<T> loadAll(Class<T> entityClass) {
    return genericBaseDao.loadAll(entityClass);
  }

  @Override
  public <T> void update(T entity) {
    genericBaseDao.updateEntitie(entity);

  }
}
