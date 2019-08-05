package com.example.searsnotes.navigators;

public interface EditNoteActivityNavigator {
    void openGalary();
    void openCamera();
    void setNoteImage(String uri);
    void requestMultiplePermissions(int requestCode);
    void saveButtonClicked();
    void discardButtonClicked();
}
