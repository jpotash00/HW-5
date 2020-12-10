
import java.util.Arrays;

import java.util.*;

public class SpreadSheetPrinterV2{
	public static final int PP = 0;
//Yoni this is a commit on github
	public static final int CSV = 1;
	public static int FORMAT;
    private static char lastCol;
    private static String [][] SpreadSheetCSV;
    private static String [][] SpreadSheet;

    public static void main (String[]args){
        if(args.length == 0){
        	evenInput();
        	return;
        }
        if (!args[0].equalsIgnoreCase("pp") && !args[0].equalsIgnoreCase("csv")){
        	formatError();
        	return;
        }
        if (args[0].equals("pp")){
            SpreadSheetPrinterV2.FORMAT = SpreadSheetPrinterV2.PP;
        }
        else{
            SpreadSheetPrinterV2.FORMAT = SpreadSheetPrinterV2.CSV;
        }
        if (args.length == 1){
            lengthInvalid();
            return;
        }
        if (args.length == 2){
            lengthInvalid();
            return;
        }
        if (!validateRange(args)){
            rangeInvalid();
            return;
        }
        int columnLength = args[1].charAt(0)-64;
        int rowLength = getInteger(args[2]);
        lastCol = (char) (columnLength+64);
        SpreadSheet = new String [rowLength+1][columnLength+1];
        SpreadSheetCSV = new String [rowLength+1][columnLength];
        String [] input = Arrays.copyOfRange(args,3,args.length);

        if (args.length == 3 && SpreadSheetPrinterV2.FORMAT == PP){
            createSheetPP(lastCol,rowLength, input);
            printSheetPP();
            return;
        }
        if (args.length == 3 && SpreadSheetPrinterV2.FORMAT == CSV){
            createSheetCSV(lastCol,rowLength, input);
            printSheetCSV();
            return;
        }
        if (input.length % 2 == 1){
            evenInput();
            return;
        }
        if (validateAllCellLabels(input, lastCol, rowLength) != null){
            outOfRangeCellLabels(input,lastCol, rowLength);
            return;
        }
        if (validateAllCellValues(input) != null && validateAllCellLabels(input, lastCol, rowLength) == null){
            nonDoubleCellValError(input);
            return;
        }
        if (SpreadSheetPrinterV2.FORMAT == PP && validateRange(args) && validateAllCellValues(input) == null && validateAllCellLabels(input, lastCol, rowLength) == null){
            createSheetPP(lastCol,rowLength, input);
            printSheetPP();
            return;
        }
        if (SpreadSheetPrinterV2.FORMAT == CSV && validateRange(args) && validateAllCellValues(input) == null && validateAllCellLabels(input, lastCol, rowLength) == null){
            createSheetCSV(lastCol, rowLength, input);
            printSheetCSV();
            return;
        }
    }

/**
 * @param col   the column of the desired cell
 * @param row   the row of the desired cell
 * @param input the command line input, WITHOUT the spreadsheet range
 *              specifications. In other words, args[0] and args[1] have been
 *              removed
 * @return
 */
public static String getCellValue(char column, int row, String[] input){
    String cell = column + (row + "");
    for (int i = 0; i <= input.length-1; i+=2){
        if (input[i].equals(cell)){
            return((input[i+1]));
        }
    }
            return ""; //" "
        }
    public static boolean isCurrent(char col, int row, String cellLabel){

        if (cellLabel.equals(col + (row + ""))){
            return true;
        }
        else {
            return false;
        }
    }
    public static void createSheetPP(char columnLength, int rowLength, String[] input){
        //add column headers
        if (rowLength >= 100){
            SpreadSheet[0][0] = "   ";
        }
        else if (rowLength >= 10 & rowLength <=99){
            SpreadSheet[0][0] = "  ";
        }
        else{
            SpreadSheet[0][0] = " ";
            }
        char c = 'A';
        for (int col = 1; c <= columnLength;col++,c++){
            SpreadSheet[0][col] = "\t" + c;
        }
        //add row headers
        for(int row = 1; row <= rowLength;row++){
            if(row <= rowLength){
                SpreadSheet[row][0] = row + "\t";
            }else{
                SpreadSheet[row][0] = row + "";
            }
            }  
        //add values to spreadsheet
        for (int i = 0; i < input.length; i+=2){
            int letter = input[i].charAt(0)-64;
            char[] numArr = Arrays.copyOfRange(input[i].toCharArray(), 1, input[i].toCharArray().length);
            int number = Integer.parseInt(new String(numArr));
                if (letter <= rowLength){
                SpreadSheet[number][letter] = input[i+1] + "\t";
            }
        }
    }
    public static void printSheetPP(){
        for(int row = 0;row < SpreadSheet.length;row++){
            for(int col = 0; col <= SpreadSheet[row].length-1;col++){
                if (SpreadSheet[row][col] == null){
                    if (col <= (double)lastCol-64){
                        System.out.print(" ");
                        System.out.print("\t");
                    }
                }
                else{
                    System.out.print(SpreadSheet[row][col]);
                }
            }
            System.out.println();
    }
      return;
    }
    public static void printColumnHeaders(char columnLength, int rowLength){        
        
        for (char c = 'A'; c <= columnLength; c++){
            char currentLetter = c;
            if (SpreadSheetPrinterV2.FORMAT == PP){
                if (currentLetter == 'A'){
                        if (rowLength < 10){
                            System.out.print(" " + "\t" + currentLetter);
                        }
                        else if (rowLength >= 10 && rowLength < 100){
                            System.out.print("  " + "\t" + currentLetter);
                        }
                        else if (rowLength >= 100 && rowLength < 1000){
                            System.out.print("   " + "\t" + currentLetter);
                        }
                        else if (rowLength >= 1000 && rowLength < 10000){
                            System.out.print("    " + "\t" + currentLetter);
                        }
                        else if (rowLength >= 10000){
                            System.out.print("     " + "\t" + currentLetter);
                        }
                    }
                else {
                    System.out.print("\t" + currentLetter);
                }
            }
            if (SpreadSheetPrinterV2.FORMAT == CSV){
                if (currentLetter == 'A'){
                    System.out.print(currentLetter);
                }
                else {
                    System.out.print("," + currentLetter);
                }
            }
        }
        System.out.println();
    }
    //----------------------------------------------
    public static void createSheetCSV(char columnLength, int rowLength, String[] input){
    //add first row
    char c = 'A';
    for (int col = 0; c <= columnLength;col++,c++){
        SpreadSheetCSV[0][col] = c + "";
    }
    //add values to spreadsheet
    for (int i = 0; i < input.length; i+=2){
        int letter = input[i].charAt(0)-65; // -65 instead of -64
        char[] numArr = Arrays.copyOfRange(input[i].toCharArray(), 1, input[i].toCharArray().length);
        int number = Integer.parseInt(new String(numArr));
            if (letter < rowLength){
            SpreadSheetCSV[number][letter] = input[i+1];
        }
            if (SpreadSheetCSV[number][letter] == null){
                SpreadSheetCSV[number][letter] = "";
            }
    }
}
    public static void printSheetCSV(){
        
        for (int row = 0;row <= SpreadSheetCSV.length-1;row++){
            for(int col = 0; col <= SpreadSheetCSV[row].length-1;col++){
                if (SpreadSheetCSV[row][col] == null && col <= (double)lastCol-65){ //check above printing values in correctg spot but null after
                    // if (SpreadSheet[row][col] == null){
                            System.out.print("");
                        
                        if (col < SpreadSheetCSV[row].length-1){
                            System.out.print(",");
                        }
                        else {
                            if (row == SpreadSheetCSV.length-1){
                            }
                            else {
                                System.out.println();
                            }
                        }
                    } 
                else{
                    if(col < SpreadSheetCSV[row].length-1){
                        System.out.print(SpreadSheetCSV[row][col] + ",");
                    }
                    else{
                        System.out.print(SpreadSheetCSV[row][col] + "\n");
                    }  
                }
            }
        }
   System.out.println(); // when last square is null need this else don't
    }

/**
* Check all cell labels to make sure they are an upper case character followed by an integer,
and that both the column and row are within the specified range.
* @param input the command line input, WITHOUT the spreadsheet range specifications. In other
words, args[0] and args[1] have been removed 
* @param lastCol last valid column letter
* @param lastRow last valid row number
* @return the first invalid cell label if there is one, otherwise null 
*/
    public static String validateAllCellLabels(String[] input, char lastCol, int row){
        for (int i = 0; i < input.length; i+=2){
            int number = 0;
            char letter = input[i].charAt(0);
            
            if (Character.isUpperCase(letter)){
            }
            if (input[i].charAt(1)-48 == 1 || input[i].charAt(1)-48 == 2 || input[i].charAt(1)-48 == 3 || input[i].charAt(1)-48 == 4 || input[i].charAt(1)-48 == 5 || input[i].charAt(1)-48 == 6 || input[i].charAt(1)-48 == 7 || input[i].charAt(1)-48 == 8 || input[i].charAt(1)-48 == 9){
            }
            else{
                return input[i];
            }
            if (input[i].toCharArray().length >= 2){
                char[] numArr = Arrays.copyOfRange(input[i].toCharArray(), 1, input[i].toCharArray().length);
                number = Integer.parseInt(new String(numArr));
            }
            else{
                number = (input[i].charAt(1)-48);
            }
            if (letter > lastCol || number > row  || number == 0){
                return input[i];
            }
        }
        return null;
        }
/**
* Checks all cell values to make sure they are numbers - can be ints or doubles
* @param input the command line input, WITHOUT the spreadsheet range specifications. In other
words, args[0] and args[1] have been removed
* @return the first invalid value if there is one, otherwise null */

    public static String validateAllCellValues(String[] input){ /**keeps skipping for loops because when there's an empty value int i = 0*/
        for (int i = 1; i < input.length; i+=2){
            if (!isValidDouble(input[i]) && !isValidInt(input[i])){
                return input[i];
            }
        }
        return null;
    }
    public static boolean validateRange(String[]args){
        int rowLength = getInteger(args[2]);
        int columnLength = args[1].charAt(0)-64;
        if (columnLength >= 1 && columnLength <=26 && rowLength >= 1 && (args[1].toCharArray().length == 1)){
            return true;
        }
        else {
        return false;
        }
    }
    public static int getInteger(String letter){
        try{
            return Integer.parseInt(letter);
        } catch (NumberFormatException e){
            return -1;
        }
    }
    public static boolean isValidDouble(String token){
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException s){
            return false;
        }
    }
    private static boolean isValidInt(String token){
        try {
            Integer.parseInt(token);
            return true;
        } catch (NumberFormatException s){
            return false;
            }
    }
    public static void formatError(){ 
        System.out.println("The first argument must specify either csv or pp");
    }
    public static void rangeInvalid(){ 
        System.out.println("Please specify a valid spreadsheet range, with highest column between A and Z and highest row as an integer > 0");
        }
    public static void lengthInvalid(){ 
        System.out.println("Invalid input: must specify at least a format (csv or pp), as well as the highest column and row");
        }
    public static void evenInput(){ 
        System.out.println("Invalid input: must specify the format, spreadsheet range, and then cell-value pairs. You entered an even number of inputs");
        }
    public static void outOfRangeCellLabels(String[]input, char lastCol, int rowLength){ //need to fiz parameter so it inputs the wrong label
        System.out.println("Invalid cell label: " + validateAllCellLabels(input, lastCol, rowLength));
        }
    public static void nonDoubleCellValError(String[] input){ 
        System.out.println("Invalid cell value: " + validateAllCellValues(input));
        }
}
