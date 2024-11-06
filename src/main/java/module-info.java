module org.example.kg_task2_8 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens org.example.kg_task2_8 to javafx.fxml;
    exports org.example.kg_task2_8;
}