module is.hi.messagee2efront {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;

    opens is.hi.messagee2efront.ui.controller to javafx.fxml;
    opens is.hi.messagee2efront.model.request to com.fasterxml.jackson.databind;
    opens is.hi.messagee2efront.model.response to com.fasterxml.jackson.databind;

    exports is.hi.messagee2efront.ui;
}