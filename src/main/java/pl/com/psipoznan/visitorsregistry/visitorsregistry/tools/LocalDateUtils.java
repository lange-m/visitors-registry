package pl.com.psipoznan.visitorsregistry.visitorsregistry.tools;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class LocalDateUtils {

	public static Date LocalDateToDate(LocalDateTime ldt) {
		
		if (ldt == null) return null;
		return java.util.Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
	}
	
}
