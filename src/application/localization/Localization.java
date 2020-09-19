package application.localization;

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

    public static ZonedDateTime getUtcNow() {
        return ZonedDateTime.now(TimeZone.getTimeZone("UTC").toZoneId());
    }
}
