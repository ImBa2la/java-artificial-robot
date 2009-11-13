import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Value;

import org.junit.Test;

public class ICalTest {

	@Test
	public void simpleTest() throws Exception {
		Calendar calendar = new Calendar();		
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.set(java.util.Calendar.MONTH, java.util.Calendar.DECEMBER);
		cal.set(java.util.Calendar.DAY_OF_MONTH, 25);

		VEvent christmas = new VEvent(new Date(cal.getTime()), "Christmas Day");
		// initialise as an all-day event..
		christmas.getProperties().getProperty(Property.DTSTART).getParameters().add(Value.DATE);

		calendar.getComponents().add(christmas);
		
		FileOutputStream fout = new FileOutputStream("mycalendar.ics");
		CalendarOutputter outputter = new CalendarOutputter();
		outputter.output(calendar, fout);		
	}

}
