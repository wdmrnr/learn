package org.near.user.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.near.commons.service.GenericService;
import org.near.user.entity.City;
import org.near.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml","classpath:spring-mvc-hibernate.xml","classpath:spring-user.xml"})
public class UserTest {

  @Autowired
  private GenericService<Long> genericService;

  @Autowired
  private UserService userService;
  @Test
  public void test() {
    try{
       City city = genericService.get(City.class, 232907L);
       System.out.println(city.getName());
    } catch(Exception e) {
      e.printStackTrace();
    }

    userService.testSearchUser();
  }

}
