package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.formula.functions.Columns;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.microsoft.schemas.office.visio.x2012.main.CellType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class SampleController {
	
	ObservableList<String> chooseAction = FXCollections.observableArrayList("Round for website", "Get discount price");
	
	

	
	//Kleyzmeron Tab
	@FXML
	private TextField discountPrice;
	@FXML
	private TextField enteredPrice;
	@FXML
	private Button getDiscount;
	@FXML
	private Button Copy;
	@FXML
	private Button Round;
	
	//Excel Tab
	@FXML
	private Button browseFile;
	@FXML
	private Button saveAs;
	@FXML
	private Button showButt;
	@FXML
	private Button preview;
	@FXML 
	private RadioButton dest;
	@FXML 
	private RadioButton newFile;
	@FXML 
	private Button updateFile;
	@FXML
	private Button clearLog;
	@FXML
	private ComboBox<String> actionChooser;
	@FXML
	private TextField openPath;
	@FXML
	private TextField savePath;
	@FXML
	private TextField colNumber;
	@FXML
	private TextField enterPrecent;	
	@FXML
	private TextField destCol;	
	@FXML
	private FileChooser fileChooser;
	@FXML
	private TextArea logText;

	// Excel Tab controller
	private File file;
	private XSSFSheet spreadsheet;
	private XSSFWorkbook workbook;

	private String path;
	private ToggleGroup radioGroup;

	private FileInputStream fileInputStream;




	private XSSFWorkbook wb;




	private XSSFSheet sheet;
	
	
	
	@FXML 
	private void initialize() {
		
		actionChooser.setItems(chooseAction);
		actionChooser.setValue("Choose action");
		final ToggleGroup radioGroup = new ToggleGroup();
		dest.setToggleGroup(radioGroup);
		dest.setSelected(true);
		newFile.setToggleGroup(radioGroup);
		this.radioGroup = radioGroup;
			
	}

public void actionChooser() {
	if (actionChooser.getSelectionModel().getSelectedItem() == "Get discount price") {
		enterPrecent.setDisable(false);
	}
}
	
	
	//Browse .xlxs file
	
	public void openFile() {

		if (openPath != null) {
			colNumber.setDisable(false);
			showButt.setDisable(false);

		}

		final FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"Microsoft Excel 2007 XML files (*.xlsx)", "*.xlsx");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showOpenDialog(openPath.getScene().getWindow());
		String path = file.getAbsolutePath();
		openPath.setText(path);
		this.path = path;
		this.file = file;
		logText.appendText("File Chosen: " + file + "\n");
	}
		
	
	//Display chosen column number and its content
	public void chooseColNum() {
		preview.setDisable(false);
		getColNum();
		logText.appendText("Column Chosen: " + Integer.valueOf(colNumber.getText()) + "\n");
		
		showColNum(getColNum());
		
		actionChooser.setDisable(false);
		dest.setDisable(false);
		newFile.setDisable(false);
		destCol.setDisable(false);
		
	
	}
	
	//Display chosen column number
	public void showColNum(int colNum) {
		
		logText.appendText("Column " + colNum + " Content: " + getCol(colNum).toString() + "\n");
	}
	
	@FXML
	public void performAction() throws IOException {
		dest.setDisable(false);
		newFile.setDisable(false);
		saveAs.setDisable(false);
		Actions actions = new Actions();
		String action = actionChooser.getSelectionModel().getSelectedItem().toString();
		switch(action) {
		case "Round for website":
			
			List<Cell> col1 = getCol(getColNum());
			List<Cell> colAfterRound = actions.roundForWeb(col1);
	
			logText.appendText("Column " + getColNum() + " rounded: \n" + colAfterRound + "\n");
			
			setColContent(colAfterRound, getDestColNum());
				
		  
		  	
		  break;
		case "Get discount price":
				
			double precent = Float.parseFloat(enterPrecent.getText());
			List<Cell> col2 = getCol(getColNum());
			List<Cell> colAfterDis = actions.getDiscount(col2, precent);
			
			logText.appendText("Column " + getColNum() + " after discount: \n" + colAfterDis + "\n");
			
			setColContent(colAfterDis, getDestColNum());
							
			break;
		}}
		

public void UpdateFile() throws IOException {		  
		  
	
	
	//Update current file		  
	
	writeToFile(getWorkbook(), openPath.getText());
		  
	
}
			
//			String radio = radioGroup.getSelectedToggle().toString();
//			
////			switch(radio) {
//			//Update current file
//			case "Destination column":
//				
//				if (savePath.getText() != null) {
//				
//					
//				writeToFile(getWorkbook(), savePath.getText());
//				logText.appendText("File updated. Please close xlsx and reopen.\n");
//				
//				
//				}
//				break;
//			//write to new file
//			case "New File":
//				if (savePath.getText() != null) {
//				writeToFile(getWorkbook(), savePath.getText());
//				logText.appendText("File Created Successfully.\n");
//			}
//				break;
//			}
//			
			
			
	

	
public void clearLog() {
	logText.clear();
}
	
	
	//
	//
	//
	//Return column number from textField
	public int getColNum() {
		
		if (colNumber.getText() != null) {
			return Integer.parseInt(colNumber.getText());			
		}
		else {
			return 0;
		}
	}
	
public int getDestColNum() {
		
		if (destCol.getText() != null) {
			return Integer.parseInt(destCol.getText());			
		}
		else {
			return 0;
		}
	}
	
	

public void saveAs() {
FileChooser fc = new FileChooser(); 
File file = fc.showSaveDialog(savePath.getScene().getWindow());
String path = file.getAbsolutePath();
savePath.setText(path);
logText.appendText("Saved as: " + file + "\n");	
}

public void writeToFile(XSSFWorkbook workbook , String filePath) {
	
	if (filePath != null) {	
		
		
		try {
			

			File newFile  = new File(filePath);
			FileOutputStream output_file = new FileOutputStream(newFile);
			
			wb.write(output_file);
			output_file.close();
			
			if (newFile != null) {
				logText.appendText("File successfuly updated. \n");
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
		
	}
	
	//Get column content
		public List<Cell> getCol(int colNum) {
			List<Cell> colContent = new ArrayList<>();
			for (int i=0; i<= getSheet().getLastRowNum(); i++) {
				
				
				
				colContent.add(getSheet().getRow(i).getCell(colNum));

			}
			
			return colContent;
		}
		
	//Set column content
		public void setColContent(List<Cell> content, int destColNum) throws IOException {
			
			//get destination cells from sheet and set its value from content list
			
			FileInputStream file = new FileInputStream(new File(openPath.getText()));
			
			//Create new updated sheet
			//new workbook on new file
			XSSFWorkbook wb = new XSSFWorkbook(file);
		
		
			
			String name = "Kleyzmeron Output ";
			XSSFSheet sheet = wb.createSheet(name);
			this.sheet = sheet;
	
				
			//creating new column to destination specified by user and saving it as "newCol"
			for (Cell cl : content) {				
			Row row = (this.sheet).createRow(cl.getRowIndex());
			Cell cell = row.createCell(destColNum, org.apache.poi.ss.usermodel.CellType.NUMERIC);
			cell.setCellValue((content.get(cl.getRowIndex()).getNumericCellValue()));		
	
			}
			
			List<Cell> newCol = getCol(destColNum);
			
			this.wb = wb;
//			file.close();
			
			logText.appendText("Copied result to new sheet, column " + destColNum + ": \n"+ content + "\n"
					+ "Please close Excel file before pressing 'Update'. \n");
		}
					
		
		
		public static void setColumnNumeric(List<Cell> col) {

			for (Cell cl : col) {
				cl.setCellType(org.apache.poi.ss.usermodel.CellType.NUMERIC);
			}
		}
		
		public void setColumnString(List<Cell> col) {

			for (Cell cl : col) {
				cl.setCellType(org.apache.poi.ss.usermodel.CellType.STRING);
			}
		}
		
	
		
	
	//Get spreadsheet from chosen file
	
	public XSSFSheet getSheet() {
					//Read from file
			if (path != null) {			

				try {

					FileInputStream fis = new FileInputStream(new File(path));
					XSSFWorkbook workbook = new XSSFWorkbook(fis);
					XSSFSheet spreadsheet = workbook.getSheetAt(0);
					this.spreadsheet = spreadsheet;
					
					this.fileInputStream = fis;
					this.workbook = workbook;
						
					
				}

				 catch (IOException e) {
					e.printStackTrace();	

			}
			}
			return spreadsheet;
			}
			
public XSSFWorkbook getWorkbook() {
				//Read from file
		if (this.path != null) {			

			
			try {

				FileInputStream fis = new FileInputStream(new File(this.path));
				XSSFWorkbook workbook = new XSSFWorkbook(fis);
				this.workbook = workbook;
					
					
				
			}

			 catch (IOException e) {
				e.printStackTrace();	

		}
		}
		return workbook;
		}
	
		
	
	
	

	// First Tab controller
	
	public void discountButtonPushed() {

		FloatStringConverter sc = new FloatStringConverter();
		String price = this.enteredPrice.getText();
		float enteredPriceValue = sc.fromString(price);
		float finalPrice = enteredPriceValue * 0.9f;
		this.enteredPrice.setText(sc.toString(enteredPriceValue));
		this.discountPrice.setText(sc.toString(finalPrice));
	}

	public void copyToClipboard() {
		final Clipboard clipboard = Clipboard.getSystemClipboard();
		final ClipboardContent content = new ClipboardContent();

		content.putString(this.discountPrice.getText());
		clipboard.setContent(content);

	}

	public void round() {
		FloatStringConverter sc = new FloatStringConverter();
		IntegerStringConverter ins = new IntegerStringConverter();
		String price1 = this.discountPrice.getText();
		String price2 = this.enteredPrice.getText();
		float discountPriceValue = sc.fromString(price1);
		float enteredPriceValue = sc.fromString(price2);
		Integer enteredRounded = Math.round(enteredPriceValue);
		Integer roundedDisPrice = Math.round(discountPriceValue);
		this.enteredPrice.setText(ins.toString(enteredRounded));

		int lastDigitIndex = roundedDisPrice.toString().length();
		int lastDigit = roundedDisPrice % 10;

		if (lastDigit == 0 || lastDigit == 5 || lastDigit == 9) {
			this.discountPrice.setText(ins.toString(roundedDisPrice));
		} else if (lastDigit == 1 || lastDigit == 2) {

			lastDigit = 0;
			this.discountPrice.setText(ins.toString(roundedDisPrice).substring(0, lastDigitIndex - 1) + lastDigit);
		} else if (lastDigit == 3 || lastDigit == 4 || lastDigit == 6) {

			lastDigit = 5;
			this.discountPrice.setText(ins.toString(roundedDisPrice).substring(0, lastDigitIndex - 1) + lastDigit);
		}

		else if (lastDigit == 7 || lastDigit == 8) {

			lastDigit = 9;
			this.discountPrice.setText(ins.toString(roundedDisPrice).substring(0, lastDigitIndex - 1) + lastDigit);
		}
	}

	private void installEventHandler(final Node keyNode) {

		final EventHandler<KeyEvent> keyEventHandler = new EventHandler<KeyEvent>() {
			public void handle(final KeyEvent keyEvent) {
				if (keyEvent.getCode() == KeyCode.ENTER) {
					setPressed(keyEvent.getEventType() == KeyEvent.KEY_PRESSED);
					keyEvent.consume();
				}
			}

			private void setPressed(boolean b) {
				// TODO Auto-generated method stub

			}
		};

		keyNode.setOnKeyPressed(keyEventHandler);
		keyNode.setOnKeyReleased(keyEventHandler);

	}

}
