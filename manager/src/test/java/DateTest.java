import java.util.Calendar;
import java.util.Date;

/**
 * Created by ji on 16-11-16.
 */
public class DateTest {

    public static void main(String[] args) {
        Calendar c1 = Calendar.getInstance();
        c1.add(Calendar.DATE, -10);
        Date start = c1.getTime();

        Calendar c2 = Calendar.getInstance();
        c2.add(Calendar.DATE, 5);
        Date end = c2.getTime();
        System.out.println("start" + start);
        System.out.println("end:"+end);

        while (c1.before(c2)) {
            Date d = c1.getTime();
            System.out.println(d);
            c1.add(Calendar.DATE, 1);
        }
        System.out.println(c1.getTime());
    }
}
