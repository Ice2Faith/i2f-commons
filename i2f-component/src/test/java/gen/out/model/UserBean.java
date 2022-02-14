package gen.out.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @desc operation for table sys_user
 */
@Data
@NoArgsConstructor
public class UserBean {

            // for filed id
        private Integer id;
            // for filed user_name
        private String userName;
            // for filed user_age
        private Integer userAge;
    }
