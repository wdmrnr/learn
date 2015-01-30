package org.near.commons.query.bean;

import java.util.ArrayList;
import java.util.List;

public class PageList {
  public static final Integer DEFAULT_PAGE_NO = 1;

  public static final Integer DEFAULT_PAGE_SIZE = 10;

  private List rows;

  private Integer pageNo; // 当前显示的页数

  private Integer pageSize;

  private Integer total; // 数据的总条数

  public PageList() {
    this(DEFAULT_PAGE_NO, DEFAULT_PAGE_SIZE);
  }

  public PageList(final Integer pageNo) {
    this(pageNo, DEFAULT_PAGE_SIZE);
  }

  public PageList(final Integer pageNo, final Integer pageSize) {
    super();
    this.pageNo = pageNo;
    this.pageSize = pageSize;
    this.total = 0;
  }

  public <E> PageList(final Integer pageNo, final Integer pageSize, List<E> rows) {
    super();
    this.pageNo = pageNo;
    this.pageSize = pageSize;
    this.total = 0;
    this.rows = rows;
  }

  public PageList(final PageList page) {
    this.rows = page.rows;
    this.pageNo = page.pageNo;
    this.pageSize = page.pageSize;
    this.total = page.total;
  }

  public <E> PageList(Integer pageNo, Integer dataCount, Integer pageSize,
      List<E> list) {
    this.pageSize = pageSize;
    this.pageNo = pageNo;
    this.total = dataCount;
    this.rows = list;
  }

  public Integer getPageNo() {
    return pageNo;
  }

  public void setPageNo(Integer pageNo) {
    this.pageNo = pageNo;
  }

  public Integer getPageSize() {
    return pageSize;
  }

  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  public Integer getPageTotal() {
    return (int) Math.ceil(total.doubleValue() / pageSize.doubleValue());
  }

  public int getFirstRow() {
    return (pageNo - 1) * pageSize + 1;
  }

  public int getMaxRow() {
    return (int) (pageNo * pageSize > total ? total : pageNo * pageSize);
  }

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public List getRows() {
    if (null == rows) {
      return new ArrayList(0);
    }
    return rows;
  }

  public void setRows(List rows) {
    this.rows = rows;
  }

}
