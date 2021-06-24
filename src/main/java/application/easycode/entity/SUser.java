package application.easycode.entity;

import lombok.Data;
 
/**
 * (SUser)实体类
 *
 * @author dewey.du
 * @created 2020-02-22 15:19:57
 */

@Data
public class SUser {

     
    private Long id;
     
    private String userName;
    //密码 
    private String pswd;

}