package gen.out.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import gen.out.service.UserService;
import gen.out.model.UserBean;
import java.lang.*;
import java.util.*;

/**
* @desc operation for table sys_user
*/
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("list")
    public List<UserBean> getList(UserBean post){
        return userService.getList(post);
    }
}
