package application.ui;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

import java.util.concurrent.atomic.AtomicReference;

public class DialogController {
    public static void okModalDialog(String msg) {
        Dialog<ButtonType> okDialog = new Dialog<>();
        okDialog.getDialogPane().setContentText(msg);
        okDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        okDialog.showAndWait().filter(resp -> resp == ButtonType.OK).ifPresent(a -> {
        });
    }

    public static boolean yesNoModalDialog(String dialogContent) {
        AtomicReference<Boolean> retval = new AtomicReference<>(false);
        Dialog<ButtonType> ynDialog = new Dialog<>();
        ynDialog.getDialogPane().setContentText(dialogContent);
        ynDialog.getDialogPane().getButtonTypes().add(ButtonType.YES);
        ynDialog.getDialogPane().getButtonTypes().add(ButtonType.NO);
        ynDialog.showAndWait().filter(resp -> resp == ButtonType.YES).ifPresent(a -> {
            retval.set(true);
        });
        return retval.get();
    }
}
