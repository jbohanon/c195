package application.ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.InputEvent;
import javafx.util.StringConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeField extends TextField {
    enum Unit {
        HOURS {
            LocalTime Set(LocalTime time, int hours) {
                return time.withHour(hours);
            }
            void Select(TimeField time) {
                time.selectRange(0, time.getText().indexOf(':'));
            }
        },
        MINUTES {
            LocalTime Set(LocalTime time, int minutes) {
                return time.withMinute(minutes);
            }
            void Select(TimeField time) {
                int hrIndex = time.getText().indexOf(':');
                time.selectRange(hrIndex + 1, time.getText().indexOf(':', hrIndex + 1));
            }
        },
        SECONDS {
            LocalTime Set(LocalTime time, int seconds) {
                return time.withSecond(seconds);
            }
            void Select(TimeField time) {
                int minIndex = time.getText().indexOf(':', time.getText().indexOf(':') + 1);
                time.selectRange(minIndex + 1, time.getText().indexOf(' '));
            }
        },
        AM_PM {
            LocalTime Set(LocalTime time, int isAm) {
                boolean _isAm = isAm == 1;
                if(_isAm && time.getHour() > 12) {
                    return time.minusHours(12);
                } else if(!_isAm && time.getHour() < 12) {
                    return time.plusHours(12);
                }
                return time;
            }
            void Select(TimeField time) {
                int secIndex = time.getText().indexOf(' ');
                time.selectRange(secIndex + 1, time.getText().length()-1);
            }
        };
        abstract LocalTime Set(LocalTime time, int unit);
        abstract void Select(TimeField time);
    }
    private LocalTime _timeValue = LocalTime.now();
    private final ObjectProperty<Unit> _unit = new SimpleObjectProperty<>(Unit.HOURS);
    public ObjectProperty<Unit> UnitProperty() {
        return _unit;
    }
    public final Unit GetUnit() {
        return UnitProperty().get();
    }
    public final void SetUnit(Unit unit) {
        UnitProperty().setValue(unit);
    }

    public TimeField(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss aa");

        StringConverter<LocalTime> localTimeConverter = new StringConverter<LocalTime>() {

            @Override
            public String toString(LocalTime time) {
                return formatter.format(time);
            }

            @Override
            public LocalTime fromString(String string) {
                String[] tokens = string.split(":");
                int hours = getIntField(tokens, 0);
                int minutes = getIntField(tokens, 1) ;
                int seconds = getIntField(tokens, 2);
                int totalSeconds = (hours * 60 + minutes) * 60 + seconds ;
                return LocalTime.of((totalSeconds / 3600) % 24, (totalSeconds / 60) % 60, seconds % 60);
            }

            private int getIntField(String[] tokens, int index) {
                if (tokens.length <= index || tokens[index].isEmpty()) {
                    return 0 ;
                }
                return Integer.parseInt(tokens[index]);
            }

        };

        // The textFormatter both manages the text <-> LocalTime conversion,
        // and vetoes any edits that are not valid. We just make sure we have
        // two colons and only digits in between:

        TextFormatter<LocalTime> textFormatter = new TextFormatter<>(localTimeConverter, LocalTime.now(), c -> {
            String newText = c.getControlNewText();
            if (newText.matches("[0-1][0-9]:[0-5][0-9]:[0-9]{2} (AM|PM)")) {
                return c ;
            }
            return null ;
        });

        // The spinner value factory defines increment and decrement by
        // delegating to the current editing mode:

//        TextFieldValueFactory<LocalTime> valueFactory = new SpinnerValueFactory<LocalTime>() {
//
//
//            {
//
//                setConverter(localTimeConverter);
//                setValue(time);
//            }
//
//            @Override
//            public void decrement(int steps) {
//                setValue(mode.get().decrement(getValue(), steps));
//                mode.get().select(TimeSpinner.this);
//            }
//
//            @Override
//            public void increment(int steps) {
//                setValue(mode.get().increment(getValue(), steps));
//                mode.get().select(TimeSpinner.this);
//            }
//
//        };
//
//        this.setValueFactory(valueFactory);
        this.setTextFormatter(textFormatter);

        // Update the mode when the user interacts with the editor.
        // This is a bit of a hack, e.g. calling spinner.getEditor().positionCaret()
        // could result in incorrect state. Directly observing the caretPostion
        // didn't work well though; getting that to work properly might be
        // a better approach in the long run.
        this.addEventHandler(InputEvent.ANY, e -> {
            int caretPos = this.getCaretPosition();
            int hrIndex = this.getText().indexOf(':');
            int minIndex = this.getText().indexOf(':', hrIndex + 1);
            int secIndex = this.getText().indexOf(' ', minIndex + 1);
            if (caretPos <= hrIndex) {
                SetUnit(Unit.HOURS);
            } else if (caretPos <= minIndex) {
                SetUnit(Unit.MINUTES);
            } else if (caretPos <= secIndex) {
                SetUnit(Unit.SECONDS);
            } else {
                SetUnit(Unit.AM_PM);
            }
        });

        // When the mode changes, select the new portion:
        UnitProperty().addListener((obs, oldUnit, newUnit) -> newUnit.Select(this));

    }

    public TimeField() {
        this(LocalTime.now());
    }
}
