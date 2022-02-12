package gen.out.service.impl;

import gen.out.service.UserService;
import gen.out.dao.UserDao;
import gen.out.model.UserBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.*;
import java.util.*;

/**
 * @desc operation for table sys_user
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public List<UserBean> getList(UserBean post){
        return userDao.getList(post);
    }
}
