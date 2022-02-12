package gen.out.service;

import gen.out.model.UserBean;

import java.lang.*;
import java.util.*;

/**
 * @desc operation for table sys_user
 */
public interface UserService {

    List<UserBean> getList(UserBean post);

}
