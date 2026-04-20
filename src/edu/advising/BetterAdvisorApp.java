package edu.advising;

import edu.advising.auth.AuthenticationContext;
import edu.advising.auth.BasicAuthentication;
import edu.advising.state.ViewContext;
import javafx.application.Application;
import javafx.stage.Stage;
import edu.advising.gui.LoginScreen;
import edu.advising.users.UserFactory;

public class BetterAdvisorApp extends Application {

    public static Stage primaryStage;
    public static ViewContext viewContext;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;

        // Set up authentication
        AuthenticationContext authContext = new AuthenticationContext(new BasicAuthentication());

        // Set up the ViewContext (navigation brain)
        viewContext = new ViewContext();
        viewContext.setAuthContext(authContext);

        // Create a test student for development
        UserFactory userFactory = new UserFactory();
        userFactory.createUser("STUDENT", "testStudent", "Password1!",
                "test@school.edu", "John", "Doe", "S12345");


        // Set window title and size
        stage.setTitle("BetterAdvisor");
        stage.setWidth(900);
        stage.setHeight(650);

        // Initialize the state machine
        viewContext.start();

        // Show the login screen
        stage.setScene(LoginScreen.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}