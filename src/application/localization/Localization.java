package application.localization;

import com.sun.tools.internal.xjc.Language;
import sun.util.locale.LocaleUtils;

import java.sql.Time;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;

public final class Localization {

    public enum RESOURCE_BUNDLE {
        LOGIN, APP
    }

    public static Locale getLocale() {
        return Locale.getDefault();
    }

    public static String getLocalizedString(String propertyLabel, RESOURCE_BUNDLE bundle) {
        String bundleName = "application/localization/" + (bundle.equals(RESOURCE_BUNDLE.LOGIN) ? "login" : "app");
        return PropertyResourceBundle.getBundle(bundleName, getLocale()).getString(propertyLabel);
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

    public static LocalDateTime getLocalTime(String isoTime) {
        TemporalAccessor ta = DateTimeFormatter.ISO_INSTANT.parse(isoTime);
        Instant i = Instant.from(ta);
        return LocalDateTime.ofInstant(i, ZoneId.of("UTC"));
    }

    public static String getIsoTime(LocalDateTime localTime, TimeZone localTimeZone) {
        return DateTimeFormatter.ISO_DATE.withZone(localTimeZone.toZoneId()).format(localTime);
    }

    public static ZonedDateTime getUtcNow() {
        return ZonedDateTime.now(getUtcTimeZone().toZoneId());
    }

}
