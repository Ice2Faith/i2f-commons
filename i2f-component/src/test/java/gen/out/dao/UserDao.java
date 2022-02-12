package gen.out.dao;

import gen.out.model.UserBean;

import org.apache.ibatis.annotations.Param;

import java.lang.*;
import java.util.*;

/**
 * @desc operation for table sys_user
 */
public interface UserDao {

    List<UserBean> getList(@Param("post")UserBean post);

}
