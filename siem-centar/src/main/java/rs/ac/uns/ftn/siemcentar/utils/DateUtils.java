package rs.ac.uns.ftn.siemcentar.utils;

import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

    public static Date dateFrom(int[] date) {
        return new GregorianCalendar(date[2], date[1] - 1, date[0]).getTime();
    }

    private DateUtils() {
    }
}
