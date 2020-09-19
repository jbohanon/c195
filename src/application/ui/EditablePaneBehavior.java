package application.ui;

public interface EditablePaneBehavior<T> {
    void PopulatePane(T t);
    void SetEditable(boolean editable);
    void EditBtnHandler();
    void BackBtnHandler();
    void SaveBtnHandler();
    void DiscardBtnHandler();
    void DeleteBtnHandler();
    boolean ValidateInput();
}
