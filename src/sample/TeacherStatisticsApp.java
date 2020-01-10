package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class ClassTeacherApp extends Application{
    private ListView<Teachers> listView;
    private ObservableList<Teachers> data;
    private TextField fnametxt;
    private TextArea lnametxt;
    private Text actionstatus;
    private TeacherDataAccess dbaccess;

    public static void main(String [] args) {
        Application.launch(args);
    }

    @Override
    public void init() {
        try {
            dbaccess = new TeacherDataAccess();
        }
        catch (Exception e) {
            displayException(e);
        }
    }

    @Override
    public void stop() {
        try {
            dbaccess.closeDb();
        }
        catch (Exception e) {
            displayException(e);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle( "Teacher/Class App");
        // gridPane layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap( 15);
        grid.setVgap( 20);
        grid.setPadding( new Insets( 25, 25 , 25 , 25 ));
        // list view, listener and list data
        listView = new ListView<>();
        listView.getSelectionModel().selectedIndexProperty().addListener(
                new ListSelectChangeListener());
        data = getDbData();
        listView.setItems(data);
        grid.add(listView, 1 , 1 ); // col = 1, row = 1
        // teacher name label and text fld - in a hbox
        Label namelbl = new Label( "First Name:" );
        fnametxt = new TextField();
        fnametxt.setMinHeight( 30.0 );
        fnametxt.setPromptText( "Enter first name (required)." );
        fnametxt.setPrefColumnCount( 20 );
        fnametxt.setTooltip(new Tooltip("First name" ));
        HBox hbox = new HBox();
        hbox.setSpacing( 10 );
        hbox.getChildren().addAll(namelbl, fnametxt);
        // teacher desc text area in a scrollpane
        lnametxt = new TextArea();
        lnametxt.setPromptText( "Enter description (optional)." );
        lnametxt.setWrapText( true );
        ScrollPane sp = new ScrollPane();
        sp.setContent(lnametxt);
        sp.setFitToWidth( true );
        sp.setFitToHeight( true );
        sp.setPrefHeight( 300 );
        sp.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        VBox vbox = new VBox();
        vbox.setSpacing( 10 );
        vbox.getChildren().addAll(hbox, sp);
        grid.add(vbox, 2 , 1 ); // col = 2, row = 1
        // new and delete buttons
        Button newbtn = new Button("New" );
        newbtn.setOnAction(new NewButtonListener());
        Button delbtn = new Button("Delete" );
        delbtn.setOnAction(new DeleteButtonListener());
        HBox hbox2 = new HBox(10 );
        hbox2.getChildren().addAll(newbtn, delbtn);
        grid.add(hbox2,1 ,2 ); // col = 1, row = 2
        // save button to the right anchor pane and grid
        Button savebtn = new Button( "Save" );
        savebtn.setOnAction(new SaveButtonListener());
        AnchorPane anchor = new AnchorPane();
        AnchorPane.setRightAnchor(savebtn, 0.0);
        anchor.getChildren().add(savebtn);
        grid.add(anchor, 2 , 2); // col = 2, row = 2
        // action message (status) text
        actionstatus = new Text();
        actionstatus.setFill(Color.FIREBRICK);
        actionstatus.setText( "" );
        grid.add(actionstatus, 1 , 3 ); // col = 1, row = 3
        // scene
        Scene scene = new Scene(grid, 750 , 400 ); // width = 750, height = 400
        primaryStage.setScene(scene);
        primaryStage.show();
        // initial selection
        listView.getSelectionModel().selectFirst(); // does nothing if no data
    } // start()
    private class ListSelectChangeListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> ov,
                               Number old_val, Number new_val) {
            if ((new_val.intValue() < 0 ) || (new_val.intValue() >= data.size())) {
                return ; // invalid data
            }

            // set name and desc fields for the selected teacher
            Teachers teacher = data.get(new_val.intValue());
            fnametxt.setText(teacher.getFirstname());
            lnametxt.setText(teacher.getLastname());
            actionstatus.setText(teacher.getLastname() + " - selected" );
        }
    }

    private ObservableList<Teachers> getDbData() {
        List<Teachers> list = null ;
        try {
            list = dbaccess.getAllRows();
        }
        catch (Exception e) {
            displayException(e);
        }
        ObservableList<Teachers> dbData = FXCollections.observableList(list);
        return dbData;
    }

    private class NewButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent e) {
            actionstatus.setText( "New" );
            // creates a teacher at first row with name NEW teacher and selects it
            Teachers teacher = new Teachers( 0 , "" , "","" ); // 0 = dummy id
            int ix = 0;
            data.add(ix, teacher);
            listView.getSelectionModel().clearAndSelect(ix);
            fnametxt.clear();
            lnametxt.clear();
            fnametxt.setText( "NEW Teacher" );
            fnametxt.requestFocus();
        }
    }

    private class SaveButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent ae) {
            int ix = listView.getSelectionModel().getSelectedIndex();
            if (ix < 0 ) { // no data selected or no data
                return ;
            }

            String s1 = fnametxt.getText();
            String s2 = lnametxt.getText();
            // validate name
            if ((s1.length() < 5 ) || (s1.length() > 50 )) {
                actionstatus.setText( "Name must be 5 to 50 characters in length" );
                fnametxt.requestFocus();
                fnametxt.selectAll();
                return ;
            }

            // check if name is unique
            Teachers teacher = data.get(ix);
            teacher.setFirstname(s1);
            teacher.setLastname(s2);

            if (isNameAlreadyInDb(teacher)) {
                actionstatus.setText( "Name must be unique!" );
                fnametxt.requestFocus();
                return ;
            }
            if (teacher.getId() == 0 ) { // insert in db (new teacher)
                int id = 0 ;
                try {
                    id = dbaccess.insertRow(teacher);
                }
                catch (Exception e) {
                    displayException(e);
                }
                teacher.setId(id);
                data.set(ix, teacher);
                actionstatus.setText( "Saved (inserted)" );
            }
            else { // db update (existing teacher)
                try {
                    dbaccess.updateRow(teacher);
                }
                catch (Exception e) {
                    displayException(e);
                }
                actionstatus.setText( "Saved (updated)" );
            } // end-if, insert or update in db
            // update list view with teacher name, and select it
            data.set(ix, null ); // required for refresh
            data.set(ix, teacher);
            listView.getSelectionModel().clearAndSelect(ix);
            listView.requestFocus();
        }
    }

    private boolean isNameAlreadyInDb(Teachers teacher) {
        boolean bool = false ;
        try {
            bool = dbaccess.nameExists(teacher);
        }
        catch (Exception e) {
            displayException(e);
        }
        return bool;
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent ae) {
            int ix = listView.getSelectionModel().getSelectedIndex();
            if (ix < 0 ) { // no data or none selected
                return ;
            }
            Teachers teacher = data.remove(ix);
            try {
                dbaccess.deleteRow(teacher);
            }
            catch (Exception e) {
                displayException(e);
            }
            actionstatus.setText( "Deleted" );
            // set next teacher item after delete
            if (data.size() == 0 ) {
                fnametxt.clear();
                lnametxt.clear();
                return ; // no selection
            }
            ix = ix - 1 ;
            if (ix < 0 ) {
                ix = 0 ;
            }
            listView.getSelectionModel().clearAndSelect(ix);
            // selected ix data (not set by list listener);
            // requires this is set
            Teachers itemSelected = data.get(ix);
            fnametxt.setText(itemSelected.getFirstname());
            lnametxt.setText(itemSelected.getLastname());
            listView.requestFocus();
        }
    }

    private void displayException(Exception e) {
        System.out.println( "###### Exception ######" );
        e.printStackTrace();
        System.exit( 0 );
    }
}
