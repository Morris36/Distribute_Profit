module com.example.distributeprofit {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires commons.math;
    requires org.jetbrains.annotations;
    requires org.apache.poi.poi;
    requires lombok;
    requires org.apache.poi.ooxml;
    requires org.apache.commons.io;

    opens App to javafx.fxml;
    opens Backpack;
    opens DataBank;
    opens Simplex;
    opens FileReader;
    exports App;

}