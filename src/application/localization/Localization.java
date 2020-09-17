package application.localization;

import application.Main;

import java.time.*;
import java.util.*;

public final class Localization {

    public enum RESOURCE_BUNDLE {

        APPLICATION {
            public ResourceBundle Get() {
                return PropertyResourceBundle.getBundle("application.localization.Application", Locale.getDefault());
            }
            public String GetFxml() {
                return "ui/application.fxml";
            }
        }, APPOINTMENT_PAGE {
            public ResourceBundle Get() {
                return PropertyResourceBundle.getBundle("application.localization.AppointmentPage", Locale.getDefault());
            }
            public String GetFxml() {
                return "ui/appointmentPage.fxml";
            }
        }, CUSTOMER_PAGE {
            public ResourceBundle Get() {
                return PropertyResourceBundle.getBundle("application.localization.CustomerPage", Locale.getDefault());
            }
            public String GetFxml() {
                return "ui/customerPage.fxml";
            }
        }, HOMEPAGE {
            public ResourceBundle Get() {
                return PropertyResourceBundle.getBundle("application.localization.Homepage", Locale.getDefault());
            }
            public String GetFxml() {
                return "ui/homepage.fxml";
            }
        }, LOGIN {
            public ResourceBundle Get() {
                return PropertyResourceBundle.getBundle("application.localization.Login", Locale.getDefault());
            }
            public String GetFxml() {
                return "ui/login.fxml";
            }
        }, SEARCH_RESULTS {
            public ResourceBundle Get() {
                return PropertyResourceBundle.getBundle("application.localization.SearchResults", Locale.getDefault());
            }
            public String GetFxml() {
                return "ui/searchResults.fxml";
            }
        };
        public abstract ResourceBundle Get();
        public abstract String GetFxml();
    }

    public static String getLocalizedString(String propertyLabel, RESOURCE_BUNDLE bundle) {
        return bundle.Get().getString(propertyLabel);
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
