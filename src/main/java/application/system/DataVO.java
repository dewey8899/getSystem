package application.system;


import lombok.Data;

/**
 * Created by deweydu
 * Date on 2019/3/29
 */
@Data
public class DataVO {

    private String address;
    private String certificateNo;
    private String consultant;
    private String createdAt;
    private String ctCode;
    private String customerName;
    private String customerType;
    private String mobilePhone;
    private String primaryChannel = "1";
    private String secondaryChannel;
    private String serviceConsultant;
    private String street;
    private String tertiaryChannel;
    private String fourthChannel;
    private String region;
    private int isRepeat;

}
