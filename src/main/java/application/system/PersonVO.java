package application.system;


import lombok.Data;

/**
 * Created by deweydu
 * Date on 2019/3/29
 */
@Data
public class PersonVO {

    private String consultant;//销售顾问
    private String customerName;//客户名称
    private String mobilePhone;
    private String relationship;//
    private String remark;//备注
    private String contactType;//联系人类型
    private String contactMobile;//联系人手机号
    private String contactName;//联系人名称

}
