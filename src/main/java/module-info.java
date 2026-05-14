module com.brianwallenrod.thehollowthrone.hollowthrone {
    requires javafx.controls;
    requires javafx.fxml;
    requires jbcrypt;
    requires com.google.gson;

    opens com.brianwallenrod.thehollowthrone to javafx.fxml;
    opens com.brianwallenrod.thehollowthrone.controllers to javafx.fxml;
    opens com.brianwallenrod.thehollowthrone.auth to com.google.gson;
    opens com.brianwallenrod.thehollowthrone.game to com.google.gson;
    opens com.brianwallenrod.thehollowthrone.save to com.google.gson;
    opens com.brianwallenrod.thehollowthrone.world to com.google.gson;
    

    exports com.brianwallenrod.thehollowthrone;
}