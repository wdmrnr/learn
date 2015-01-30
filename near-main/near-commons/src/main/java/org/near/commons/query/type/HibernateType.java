package org.near.commons.query.type;

import org.hibernate.type.BigDecimalType;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.ObjectType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;

public enum HibernateType {

  INTEGER(IntegerType.INSTANCE),
  LONG(LongType.INSTANCE),
  DOUBLE(DoubleType.INSTANCE),
  BIGDECIMAL(BigDecimalType.INSTANCE),

  STRING(StringType.INSTANCE),
  DATE(DateType.INSTANCE),
  OBJECT(ObjectType.INSTANCE);

  private Type type;
  HibernateType(Type type){
    this.type = type;
  }

  public Type type(){
    return this.type;
  }
}
