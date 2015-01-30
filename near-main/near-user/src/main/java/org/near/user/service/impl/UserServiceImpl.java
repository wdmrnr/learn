package org.near.user.service.impl;

import java.util.List;

import org.near.commons.service.impl.GenericServiceImpl;
import org.near.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class UserServiceImpl extends GenericServiceImpl<Long> implements UserService{

  @Override
  public void testSearchUser() {
    List<Object[]> users = this.genericBaseDao.findListbySql("select * from t_user");
    if (null != users) {
      for (int i = 0; i < users.size(); i++) {
        System.out.println(users.get(i)[1]);
      }
    }
  }

}
