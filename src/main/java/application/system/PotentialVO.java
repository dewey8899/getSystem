package application.system;


import lombok.Data;

/**
 * @author dewey.du
 *@date 2021年5月30日21:49:53
 */
@Data
public class PotentialVO {

    private String createdAt;//建立时间
    private String customerName;//客户姓名
    private String mobilePhone;//手机号
    private String intentSeriesName;//意向车系
    private String primaryChannel;//一级渠道 // 3 自然到店
    private String bookingDate;//预购日期

}
