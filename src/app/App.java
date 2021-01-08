package app;


import java.io.File;
import java.io.IOException;
import java.util.Arrays;
//import java.util.Collection;
import java.util.Optional;

import data.Data__;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
//import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextField;
//import javafx.scene.control.TextInputDialog;
//import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class App extends Application
{	
	
	final FileChooser fileChooser = new FileChooser();
	private File myFile = null;
	private Data__ myData;
	private final String[] months =
        {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
        "Aug", "Sep", "Oct", "Nov", "Dec"};	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Electric consumption -- ");
		BorderPane root = new BorderPane();
		
		MenuBar menu = new MenuBar();
		Menu file = new Menu("File");
		menu.getMenus().add(file);
		//MenuItem newfile = new MenuItem("New");
		MenuItem open = new MenuItem("Open...");
        MenuItem save = new MenuItem("Save");
        save.setDisable(true);
        //MenuItem saveAs = new MenuItem("Save As");
        MenuItem exit = new MenuItem("Exit");       
        file.getItems().addAll( open, save, new SeparatorMenuItem(), exit);
        //TextInputDialog ask = new TextInputDialog();
		
        FlowPane topbar = new FlowPane();
        topbar.setAlignment(Pos.CENTER);
        topbar.setPadding(new Insets(5, 5, 5, 5));
        topbar.setHgap(10);
        Button button = new Button("update");
        button.setDisable(true);
        ObservableList<String> options = 
                FXCollections.observableArrayList("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                        "Aug", "Sep", "Oct", "Nov", "Dec");
        ComboBox<String> comboBox = new ComboBox<>(options);
        //comboBox.setStyle("-fx-font: bold italic 12pt \"Arial\"");
        //TextField textField1 = new TextField();
        TextField textField2 = new TextField();
        textField2.setDisable(true);
        Label labelcombo = new Label("Month");
        Label labelTF2 = new Label("Value (kWh)");
        topbar.getChildren().add(labelcombo);
        topbar.getChildren().add(comboBox);
        topbar.getChildren().add(labelTF2);
        topbar.getChildren().add(textField2);
        topbar.getChildren().add(button);
        
        VBox top = new VBox();
		top.getChildren().add(menu);
		top.getChildren().add(topbar);
		root.setTop(top);
        
        Text resultText = new Text();
        resultText.setTextAlignment(TextAlignment.CENTER);
        resultText.setText("Year: N/A	Min: N/A	Max: N/A	Avg: N/A	Total: N/A");
        root.setBottom(resultText);
        
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();  
        BarChart<String, Number> barchart = new BarChart<>(xAxis,yAxis);
        barchart.setLegendVisible(false); //Just one series, legend is not needed
        xAxis.setLabel("Month");  
        xAxis.setAnimated(false);
        yAxis.setLabel("Value (kWh)");
        XYChart.Series<String , Number> series1 = new Series<>();
        root.setCenter(barchart);     
        
        Scene scene = new Scene(root, 800, 600);
        barchart.getData().add(series1);
        primaryStage.setScene(scene);
        primaryStage.show();
        
		/*
		 * saveAs.setOnAction(new EventHandler<ActionEvent>() {
		 * 
		 * 
		 * @Override public void handle(ActionEvent event) { File f =
		 * fileChooser.showSaveDialog(primaryStage); if (f != null) { curFile = f;
		 * fileHandler.saveToFile(f.getName()); }
		 * 
		 * }
		 * 
		 * });
		 */
        
        save.setOnAction(new EventHandler<ActionEvent>() {
        	public void handle(ActionEvent e) {
        		saveToCurrent();
        	}
		});
        
        open.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent e) {
				/*
				 * File file = fileChooser.showOpenDialog(primaryStage); if (file != null) {
				 * curFile = file; fileHandler.readFile(curFile.getName()); EnergyLevel view =
				 * new EnergyLevel(fileHandler.fileContent); for (int i=0; i<12; i++) {
				 * series1.getData().get(i).setYValue(view.months.get(EnergyLevel.MonthsOfYear[i
				 * ])); } }
				 */           
            	if (myFile != null) {
            		Alert alert = new Alert(AlertType.CONFIRMATION);
            		alert.setTitle("Work in progress");
            		
            		alert.setContentText("Save current work?");

            		ButtonType buttonTypeOne = new ButtonType("Yes");
            		ButtonType buttonTypeTwo = new ButtonType("No");
            		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

            		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

            		Optional<ButtonType> result = alert.showAndWait();
            		if (result.get() == buttonTypeOne){
            			saveToCurrent();
            		} else if (result.get() == buttonTypeCancel) {
            			return;
            		}
            	}
            	File newFile = fileChooser.showOpenDialog(primaryStage);
            	if (newFile != null) {
            		myFile = newFile;
            		if ((myData = FileHandler.readFile(myFile)) == null) {
            			Alert alert = new Alert(AlertType.ERROR, "Failed to open file");
            			alert.showAndWait();
            			return;
            		}
            		else { 
            			save.setDisable(false);
            			button.setDisable(false);
            			textField2.setDisable(false);
            			System.out.println(myData);
            			resultText.setText("Year: "+myData.year+"	Min: "+myData.min()+"	Max: "+myData.max()+"	Avg: "+myData.avg()+"	Total: "+myData.total);
            			for (int i=0; i<12; i++) {
            				series1.getData().add(
            				new Data<>(months[i], (myData.values.get(i+1)==null)?0.0:myData.values.get(i+1))
            				);
            			}
            		}
            	}
            }
        });

        exit.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				//System.exit(0);
				Alert alert = new Alert(AlertType.CONFIRMATION);
        		alert.setTitle("Work in progress");
        		
        		alert.setContentText("Save current work?");

        		ButtonType buttonTypeOne = new ButtonType("Yes");
        		ButtonType buttonTypeTwo = new ButtonType("No");
        		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        		alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

        		Optional<ButtonType> result = alert.showAndWait();
        		if (result.get() == buttonTypeOne){
        			saveToCurrent();
        		} else if (result.get() == buttonTypeCancel) {
        			return;
        		}
				Platform.exit();
			}
		});
        
        button.setOnAction(new EventHandler<ActionEvent>() {
        	
        	@Override
        	public void handle(ActionEvent e) {
        		double newVal;
        		try {
        			newVal = Double.parseDouble(textField2.getText());
        			if (newVal < 0) throw new NumberFormatException();
        		}
        		catch(NumberFormatException e1) {
        			Alert alert = new Alert(AlertType.ERROR, "Invalid input!");
                    textField2.requestFocus();
                    textField2.selectAll();
                    alert.showAndWait();
                    return;
        		}
        		String month = comboBox.getValue();
        		if (month == null) {
        			Alert alert = new Alert(AlertType.ERROR, "Please specify a month");
                    comboBox.requestFocus();
                    alert.showAndWait();
                    return;
        		}
        		myData.update(Arrays.asList(months).indexOf(month)+1, newVal);
        		series1.getData().get(Arrays.asList(months).indexOf(month)).setYValue(newVal);
        		resultText.setText(String.format("Year: "+myData.year+"	Min: "+myData.min()+"	Max: "+myData.max()+"	Avg: %.3f	Total: "+myData.total, myData.avg()));
        	}
        });
        
		/*
		 * newfile.setOnAction(new EventHandler<ActionEvent>() {
		 * 
		 * @Override public void handle(ActionEvent e) { //textArea.setText(null);
		 * curFile = null; //statusLabel.setText(null);
		 * 
		 * } });
		 */
       
	}
	
	private void saveToCurrent() {
		/*
		 * if (curFile == null) { File f = fileChooser.showSaveDialog(primaryStage); if
		 * (f != null) { curFile = f; fileHandler.saveToFile(f.getName()); } } else {
		 * fileHandler.saveToFile(curFile.getName()); }
		 */
		try { 
			FileHandler.saveToFile(myFile, myData);
		}
		catch(IOException e1) {
			Alert alert = new Alert(AlertType.ERROR, "File is missing or broken!");
			alert.showAndWait();
			return;
		}
	}
    
	public static void main(String[] args) {
		launch(args);
		/*
		 * String myFile = "./data.log"; Data myData = FileHandler.readFile(myFile);
		 * myData.year = 2001; myData.values.put(2, 201.0);
		 * FileHandler.saveToFile(myFile, myData);
		 */
	}
}
