package com.example.searsnotes.navigators;


public interface AddNoteActivityNavigator {

    void requestMultiplePermissions(int requestCode);
    void openGalary();
    void openCamera();
    void setReminder();

    void setNoteImage(String uri);
    void saveButtonClicked();
    void discardButtonClicked();
}
