package org.near.commons.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public abstract class SearchBean {

  protected transient final Logger LOGGER = LoggerFactory.getLogger(getClass());

  private final static String FROM = "FROM";

  public static final String BLANK = " ";

  public static final String ASC = "asc";
  public static final String DESC = "desc";

  private Map<String, Object> params = null;

  private StringBuffer query = null;

  private StringBuffer countQuery = null;

  private Integer currentPageNum = null;

  private Integer pageSize = null;

  private String countCol = null;

  private boolean isPaging = false;

  private Map<String, Boolean> orders = new HashMap<String, Boolean>();

  private int size = 256;

  private boolean addOrder = false;

  private String group;

  public SearchBean() {
  }

  public SearchBean(int size) {
    this.size = size;
  }

  public void addParam(String key, Object value) {
    LOGGER.debug("exec searchBean.addParam({},{})", key, value);
    if (null == params) {
      this.params = new HashMap<String, Object>();
    }
    this.params.put(key, value);
  }

  public void removeParam(String key) {
    LOGGER.debug("exec searchBean.removeParam({})", key);
    if (null != params) {
      this.params.remove(key);
    }
  }

  public Integer getCurrentPageNum() {
    if (null == this.currentPageNum || this.currentPageNum == 0) {
      this.currentPageNum = 1;
    }
    return this.currentPageNum;
  }

  public void setCurrentPageNum(Integer currentPageNum) {
    this.currentPageNum = currentPageNum;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
    setIsPaging(true);
  }

  public boolean isPaging() {
    return isPaging;
  }

  public void setIsPaging(boolean isPaging) {
    this.isPaging = isPaging;
  }

  public SearchBean createSearch(String query) {
    this.query = new StringBuffer(size);
    if (!StringUtils.isEmpty(query)) {
      this.query.append(query);
    }
    return this;
  }

  public SearchBean appendQuery(String query) {
    LOGGER.debug("exec searchbean.appendQuery:" + query);
    if (null == this.query) {
      return createSearch(query);
    }

    this.query.append(BLANK).append(query);
    return this;
  }

  public String getQuery() {
    return getQuery(false);
  }

  public String getQuery(boolean notHasOrder) {
    LOGGER.debug("exec searchbean.getQuery");
    if (null != this.query) {
      if (!addOrder && !notHasOrder) {
        if (!StringUtils.isEmpty(group)) {
          this.query.append(BLANK).append("group by").append(BLANK)
              .append(group);
        }
        if (!orders.isEmpty()) {
          this.query.append(BLANK).append("order by");
          int i = 0;
          int size = orders.keySet().size();
          for (String orderCol : orders.keySet()) {
            this.query.append(BLANK).append(orderCol).append(BLANK)
                .append(orders.get(orderCol).booleanValue() ? ASC : DESC);
            if (i < size - 1) {
              this.query.append(",");
            }
            i++;
          }
        }

        addOrder = true;
      }
      return this.query.toString();
    }
    LOGGER.error("exec searchbean.getQuery is null ");
    return null;
  }

  public void setCountCol(String countCol) {
    this.countCol = countCol;
  }

  public Map<String, Object> getParams() {
    return params;
  }

  public String getCountQuery() {
    LOGGER.debug("exec searchbean.getCountQuery");
    if (!isPaging) {
      return null;
    }
    if (null != this.countQuery
        && !StringUtils.isEmpty(this.countQuery.toString())) {
      return this.countQuery.toString();
    }
    if (null != this.query && !StringUtils.isEmpty(this.countCol)) {
      if (null == this.countQuery
          || StringUtils.isEmpty(this.countQuery.toString())) {
        this.countQuery = new StringBuffer(this.query.length());
        this.countQuery.append("select count(").append(this.countCol)
            .append(") ");
        this.countQuery.append(this.query.substring(this.query.indexOf(FROM)));
        // this.queryStr.replace(0, this.queryStr.indexOf(FROM),
        // "select count("+this.countCol+") ").toString();
      }
      return this.countQuery.toString();
    }
    LOGGER.error("exec searchbean.getCountQuery is null ");
    return null;
  }

  public Set<String> getParamKeys() {
    if (null != this.params) {
      return this.params.keySet();
    }
    return null;
  }

  public Object getValueByKey(String key) {
    if (StringUtils.isEmpty(key) || null == this.params) {
      return null;
    }
    return this.params.get(key);
  }

  public Integer getStartNum() {
    if (null != this.currentPageNum && null != this.pageSize) {
      return (getCurrentPageNum() - 1) * getPageSize();
    }
    return 0;
  }

  public boolean containsKey(String key) {
    if (null == this.params) {
      return false;
    }
    return this.params.containsKey(key);
  }

  public void setCountQuery(StringBuffer countQuery) {
    this.countQuery = countQuery;
  }

  public void addOrder(String orderCol, boolean isAsc) {
    if (!StringUtils.isEmpty(orderCol)) {
      this.orders.put(orderCol, Boolean.valueOf(isAsc));
    }
  }

  public void addOrder(String orderCol) {
    if (!StringUtils.isEmpty(orderCol)) {
      this.orders.put(orderCol, Boolean.valueOf(true));
    }
  }

  public void setGroup(String group) {
    this.group = group;
  }

}
