package com.kevin.springbootone.testSession.controller;

import com.kevin.springbootone.JPA.UserJPA;
import com.kevin.springbootone.entity.UserEntity;
import com.kevin.springbootone.testLoggerInsertDB.util.LoggerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/user")
public class LoginController {

    @Autowired
    private UserJPA userJPA;

    @RequestMapping(value = "/login_view", method = RequestMethod.GET)
    public String login_view(){
        return "login";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(){
        return "index";
    }

    @ResponseBody
    @RequestMapping(value = "/login")
    public String login(UserEntity user, HttpServletRequest request) {
        //登录成功
        boolean flag = true;
        String result = "登录成功 - 1";
        //根据用户名查询用户是否存在
        UserEntity userEntity = userJPA.findOne(new Specification<UserEntity>() {
            @Override
            public Predicate toPredicate(Root<UserEntity> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                criteriaQuery.where(criteriaBuilder.equal(root.get("name"), user.getName()));
                return null;
            }
        });
        //用户不存在
        if (userEntity == null) {
            flag = false;
            result = "用户不存在，登录失败 - 2";
        }
        //密码错误
        else if (!userEntity.getPwd().equals(user.getPwd())) {
            flag = false;
            result = "用户密码不相符，登录失败 - 3";
        }
        //登录成功
        if (flag) {
            //将用户写入session
            request.getSession().setAttribute("_session_user", userEntity);
        }
        request.setAttribute(LoggerUtils.LOGGER_RETURN,result);
        return result;
    }


}
