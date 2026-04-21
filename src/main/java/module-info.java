module is.hi.messagee2efront {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires java.prefs;
    requires java.desktop;

    opens is.hi.messagee2efront.ui.controller to javafx.fxml;
    opens is.hi.messagee2efront.model to com.fasterxml.jackson.databind;
    opens is.hi.messagee2efront.model.request to com.fasterxml.jackson.databind;
    opens is.hi.messagee2efront.model.response to com.fasterxml.jackson.databind;

    exports is.hi.messagee2efront.ui;
    exports is.hi.messagee2efront.ui.controller;
    exports is.hi.messagee2efront.model;
    exports is.hi.messagee2efront.model.request;
    exports is.hi.messagee2efront.model.response;
}