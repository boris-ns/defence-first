package rs.ac.uns.ftn.siemagent.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static Date parse(String strDate) throws ParseException {
        // 2020-06-11 19:14:19.720329
        // yyyy-MM-dd HH:mm:ss.ssssss
        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strDate);
        return date;
    }

    private DateUtil() {
    }
}
