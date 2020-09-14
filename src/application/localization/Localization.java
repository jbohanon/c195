package application.localization;

import java.time.*;
import java.util.*;

public final class Localization {

    public enum RESOURCE_BUNDLE {
        LOGIN, APP
    }

    public static String getLocalizedString(String propertyLabel, RESOURCE_BUNDLE bundle) {
        String bundleName = "application/localization/" + (bundle.equals(RESOURCE_BUNDLE.LOGIN) ? "login" : "app");
        return PropertyResourceBundle.getBundle(bundleName, Locale.getDefault()).getString(propertyLabel);
    }

    public static TimeZone getLocalTimeZone() {
        return TimeZone.getDefault();
    }

    public static TimeZone getUtcTimeZone() {
        return TimeZone.getTimeZone("UTC");
    }

    public static ZonedDateTime getZonedUtcTime(String localUtcTime) {
        return ZonedDateTime.ofLocal(LocalDateTime.parse(localUtcTime), ZoneId.of("UTC"), ZoneOffset.UTC);
    }

    public static ZonedDateTime getZonedLocalTime(ZonedDateTime zonedUtcTime, ZoneId targetZoneId) {

        return zonedUtcTime.withZoneSameInstant(targetZoneId);
    }

    public static ZonedDateTime getUtcNow() {
        return ZonedDateTime.now(getUtcTimeZone().toZoneId());
    }

//    public static Locale getDefaultLocale() {
//        return Locale.getDefault();
//    }
//
//    public static LocalDateTime getLocalUtcTime(String isoTime) {
//        TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(isoTime);
//        Instant i = Instant.from(ta);
//        return LocalDateTime.ofInstant(i, ZoneId.of("UTC"));
//    }
//
//    public static String getIsoTime(LocalDateTime localTime, TimeZone localTimeZone) {
//        return DateTimeFormatter.ISO_ZONED_DATE_TIME.withZone(localTimeZone.toZoneId()).format(localTime);
//    }


}
