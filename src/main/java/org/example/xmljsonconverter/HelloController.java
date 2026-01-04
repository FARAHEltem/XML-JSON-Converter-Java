package org.example.xmljsonconverter;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;
import java.io.File;
import java.nio.file.Files;
import java.io.PrintWriter;

public class HelloController {
    @FXML private TextArea inputArea;
    @FXML private TextArea outputArea;
    @FXML private ComboBox<String> strategieCombo;

    private final ConverterService manualService = new ConverterService();
    private final ConverterServiceAuto autoService = new ConverterServiceAuto();

    @FXML
    public void initialize() {
        if (strategieCombo != null) {
            strategieCombo.getItems().setAll("Manuel (Regex)", "Automatique (Jackson)");
            strategieCombo.setValue("Manuel (Regex)");
        }
    }

    @FXML
    protected void handleXmlToJson() {
        String input = inputArea.getText();
        if (input == null || input.isEmpty()) return;

        String strategy = (strategieCombo != null) ? strategieCombo.getValue() : "Manuel (Regex)";

        if ("Automatique (Jackson)".equals(strategy)) {
            outputArea.setText(autoService.xmlToJsonAuto(input));
        } else {
            outputArea.setText(manualService.xmlToJsonManual(input));
        }
    }

    @FXML
    protected void handleJsonToXml() {
        String input = inputArea.getText();
        if (input == null || input.isEmpty()) return;

        String strategy = (strategieCombo != null) ? strategieCombo.getValue() : "Manuel (Regex)";

        if ("Automatique (Jackson)".equals(strategy)) {
            outputArea.setText(autoService.jsonToXmlAuto(input));
        } else {
            outputArea.setText(manualService.jsonToXmlManual(input));
        }
    }

    @FXML
    protected void handleImport() {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(inputArea.getScene().getWindow());
        if (file != null) {
            try {
                inputArea.setText(Files.readString(file.toPath()));
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    @FXML
    protected void handleSave() {
        if (outputArea.getText().isEmpty()) return;
        FileChooser fc = new FileChooser();
        File file = fc.showSaveDialog(outputArea.getScene().getWindow());
        if (file != null) {
            try (PrintWriter pw = new PrintWriter(file)) {
                pw.println(outputArea.getText());
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
}