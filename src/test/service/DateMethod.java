import application.utils.DateUtils;

import java.util.Date;

/**
 * Created by deweydu
 * Date on 2019/11/19 14:59
 */
public class DateMethod {
    public static void main(String[] args) {
        Date date = DateUtils.endDateByToday(-1);
        System.out.println(DateUtils.format(date,DateUtils.LONG_WEB_FORMAT));
        Date date1 = DateUtils.firstDayOfTheMonthFromTargetDate(date);
        System.out.println(DateUtils.format(date1,DateUtils.LONG_WEB_FORMAT));

    }
}
