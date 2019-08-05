package com.example.searsnotes.navigators;


public interface AddNoteActivityNavigator {

    void requestMultiplePermissions(int requestCode);
    void openGalary();
    void openCamera();

    void setNoteImage(String uri);
    void saveButtonClicked();
    void discardButtonClicked();
}
