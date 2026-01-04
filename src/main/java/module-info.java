module org.example.xmljsonconverter {
    requires javafx.controls;
    requires javafx.fxml;

    // Lignes obligatoires pour Jackson
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.xml;

    // Permet à Jackson d'accéder à vos classes
    opens org.example.xmljsonconverter to javafx.fxml, com.fasterxml.jackson.databind;

    exports org.example.xmljsonconverter;
}