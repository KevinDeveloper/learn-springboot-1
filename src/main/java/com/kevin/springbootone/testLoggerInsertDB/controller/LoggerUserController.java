package com.kevin.springbootone.testLoggerInsertDB.controller;

import com.kevin.springbootone.JPA.TestJPA;
import com.kevin.springbootone.JPA.UserJPA;
import com.kevin.springbootone.entity.UserEntity;
import com.kevin.springbootone.testLoggerInsertDB.util.LoggerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping(value = "/looger/user")
public class LoggerUserController {

    private static Logger logger = LoggerFactory.getLogger(LoggerUserController.class);

    @Autowired
    private UserJPA userJPA;

    @Autowired
    TestJPA testJPA;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<UserEntity> userList(HttpServletRequest request) {
        List<UserEntity> userEntityList = userEntityList =  testJPA.findAll();;//userJPA.findAll();
        request.setAttribute(LoggerUtils.LOGGER_RETURN, userEntityList);
        logger.info("user, list");
        return userEntityList;
    }

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public UserEntity userSave(UserEntity userEntity, HttpServletRequest request) {
        UserEntity entity = userJPA.save(userEntity);
        request.setAttribute(LoggerUtils.LOGGER_RETURN, entity);
        return entity;
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public UserEntity userUpdate(UserEntity userEntity) {
        userJPA.delete(userEntity);
        return userJPA.save(userEntity);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public List<UserEntity> userDelete(Long id, HttpServletRequest request) {
        userJPA.delete(id);
        List<UserEntity> userEntityList = userJPA.findAll();
        request.setAttribute(LoggerUtils.LOGGER_RETURN, userEntityList);
        return userEntityList;
    }

    @RequestMapping(value = "/listQuery", method = RequestMethod.GET)
    public List<UserEntity> listQuery(HttpServletRequest request, int age) {
        List<UserEntity> userEntityList = userJPA.nativeQuery(age);
        request.setAttribute(LoggerUtils.LOGGER_RETURN, userEntityList);
        logger.info("user, listQuery");
        return userEntityList;
    }


    @RequestMapping(value = "/deleteQuery", method = RequestMethod.GET)
    public void deleteQuery(HttpServletRequest request, String name, String pwd) {
        userJPA.deleteQuery(name, pwd);
        request.setAttribute(LoggerUtils.LOGGER_RETURN, "");
        logger.info("user, deleteQuery");
        return;
    }

    @RequestMapping(value = "/listPage", method = RequestMethod.GET)
    public List<UserEntity> listPage(HttpServletRequest request, int page) {

        UserEntity user = new UserEntity();
        user.setSize(2);
        user.setSord("desc");
        user.setPage(page);

        //获取排序对象
        Sort.Direction sort_direction = Sort.Direction.ASC.toString().equalsIgnoreCase(user.getSord()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        //设置排序对象参数
        Sort sort = new Sort(sort_direction, user.getSidx());
        //创建分页对象
        PageRequest pageRequest = new PageRequest(user.getPage() - 1, user.getSize(), sort);
        //执行分页查询
        request.setAttribute(LoggerUtils.LOGGER_RETURN, user);
        return userJPA.findAll(pageRequest).getContent();

    }


}
