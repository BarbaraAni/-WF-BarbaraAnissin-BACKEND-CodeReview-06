package sample;

import javafx.application.Application;
import javafx.scene.text.Font;
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

public class TeacherStatisticsApp extends Application{
    private ListView<Teachers> listView;
    private ListView<Teaching> listViewTeaching;
    private ObservableList<Teachers> data;
    private ObservableList<Teaching> dataTeaching;
    private TextField fnametxt;
    private TextField lnametxt;
    private TextField email;
    private TextField id;
    private Text actionstatus;
    private TeacherDataAccess dbaccess;
    private ClassDataAccess dbaccess2;
    private TeachingDataAccess dbaccess3;

    public static void main(String [] args) {
        Application.launch(args);
    }

    @Override
    public void init() {
        try {
            dbaccess = new TeacherDataAccess(); //didn't manage to make sample.Teachers@### show the name
            dbaccess2 = new ClassDataAccess(); //didn't manage to combine class with teaching
            dbaccess3 = new TeachingDataAccess(); //didn't manage to make sample.Teaching@### show the classname
        }
        catch (Exception e) {
            displayException(e);
        }
    }

    @Override
    public void stop() {
        try {
            dbaccess.closeDb();
            dbaccess2.closeDb();
            dbaccess3.closeDb();
        }
        catch (Exception e) {
            displayException(e);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle( "School Statistics of Teachers");
        primaryStage.setResizable(false);
        // gridPane layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(15);
        grid.setVgap(20);
        grid.setPadding( new Insets( 25, 25 , 25 , 25 ));
        // list view, listener and list data
        listView = new ListView<>();
        listView.getSelectionModel().selectedIndexProperty().addListener(new ListSelectChangeListener());
        data = getDbData();
        listView.setItems(data);
        grid.add(listView, 1 , 1 ); // col = 1, row = 1
        //list view of rooms
        listViewTeaching = new ListView<>();
        dataTeaching = getDbDataTeaching();
        listViewTeaching.setItems(getDbDataTeaching());
        grid.add(listViewTeaching,3,1);

        // teacher name label and text fld - in a hbox
        Label lable = new Label("Data");
        lable.setFont(new Font("Arial",30));
        Label namelbl = new Label( "First Name:" );
        fnametxt = new TextField();
        fnametxt.setMinHeight(30.0);
        fnametxt.setPromptText("Enter first name (required).");
        fnametxt.setPrefColumnCount( 20 );
        fnametxt.setTooltip(new Tooltip("First name" ));
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.getChildren().addAll(namelbl, fnametxt);
        // teacher desc text area in a scrollpane
        Label lnamelbl = new Label( "Last Name:" );
        lnametxt = new TextField();
        lnametxt.setMinHeight(30.0);
        lnametxt.setPromptText("Enter last name (required).");
        lnametxt.setPrefColumnCount( 20 );
        lnametxt.setTooltip(new Tooltip("Last name" ));
        HBox hbox2 = new HBox();
        hbox2.setSpacing(10);
        hbox2.getChildren().addAll(lnamelbl,lnametxt);
        Label emaillbl = new Label( "Email:" );
        email = new TextField();
        email.setMinHeight(30.0);
        email.setPromptText("Enter email");
        email.setPrefColumnCount(20);
        email.setTooltip(new Tooltip("Email" ));
        HBox hbox3 = new HBox();
        hbox3.setSpacing(37);
        hbox3.getChildren().addAll(emaillbl,email);
        Label idlbl = new Label( "Id:" );
        id = new TextField();
        id.setMinHeight(30.0);
        id.setPrefColumnCount(20);
        id.setTooltip(new Tooltip("Id" ));
        id.setEditable(false);
        HBox hbox4 = new HBox();
        hbox4.setSpacing(55);
        hbox4.getChildren().addAll(idlbl,id);
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(lable, hbox, hbox2, hbox3, hbox4);
        grid.add(vbox, 2 , 1 ); // col = 2, row = 1
        // new and delete buttons
        Button newbtn = new Button("New");
        newbtn.setOnAction(new NewButtonListener());
        Button delbtn = new Button("Delete");
        delbtn.setOnAction(new DeleteButtonListener());
        HBox hbox5 = new HBox(10 );
        hbox5.getChildren().addAll(newbtn, delbtn);
        grid.add(hbox5,1 ,2 ); // col = 1, row = 2
        // save button to the right anchor pane and grid
        Button savebtn = new Button( "Save");
        savebtn.setOnAction(new SaveButtonListener());
        AnchorPane anchor = new AnchorPane();
        AnchorPane.setBottomAnchor(savebtn, 0.0);
        anchor.getChildren().add(savebtn);
        grid.add(anchor, 2 , 2); // col = 2, row = 2
        // action message (status) text
        actionstatus = new Text();
        actionstatus.setFill(Color.FIREBRICK);
        actionstatus.setText( "" );
        grid.add(actionstatus, 1 , 3 ); // col = 1, row = 3
        // scene
        Scene scene = new Scene(grid, 900 , 400 ); // width = 750, height = 400
        primaryStage.setScene(scene);
        primaryStage.show();
        // initial selection
        listView.getSelectionModel().selectFirst(); // does nothing if no data
        listViewTeaching.getSelectionModel().selectFirst();
    } // start()
    private class ListSelectChangeListener implements ChangeListener<Number> {
        @Override
        public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
            if ((new_val.intValue() < 0 ) || (new_val.intValue() >= data.size())) {
                return ; // invalid data
            }

            // set name and desc fields for the selected teacher
            Teachers teacher = data.get(new_val.intValue());
            fnametxt.setText(teacher.getFirstname());
            lnametxt.setText(teacher.getLastname());
            email.setText(teacher.getEmail());
            id.setText(Integer.toString(teacher.getId()));
            listViewTeaching.setItems(dataTeaching);
            actionstatus.setText(teacher.getLastname() + " - selected" );
        }
    }
    /*
    private ObservableList<Classes> getDbDataClasses(){
        List<Classes> listClasses = null;
        try {
            listClasses = dbaccess2.getAllRows();
        }
        catch (Exception e) {
            displayException(e);
        }
        return FXCollections.observableList(listClasses);
    }
    */
    private ObservableList<Teaching> getDbDataTeaching(){
        List<Teaching> listTeaching = null;
        try {
            listTeaching = dbaccess3.getAllRows();
        }
        catch (Exception e) {
            displayException(e);
        }
        ObservableList<Teaching> dbDataTeaching = FXCollections.observableList(listTeaching);
        return dbDataTeaching;
    }
    private ObservableList<Teachers> getDbData() {
        List<Teachers> list = null;
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
            String s3 = email.getText();
            int s4 = Integer.parseInt(id.getText());
            // validate name
            if ((s1.length() < 2 ) || (s1.length() > 50 )) {
                actionstatus.setText( "Name must be 2 to 50 characters in length" );
                fnametxt.requestFocus();
                fnametxt.selectAll();
                return ;
            }

            Teachers teacher = data.get(ix);
            teacher.setFirstname(s1);
            teacher.setLastname(s2);
            teacher.setEmail(s3);
            teacher.setId(s4);

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
            data.set(ix, null); // required for refresh
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
        @Override //if data is foreign key, un-deletable
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
