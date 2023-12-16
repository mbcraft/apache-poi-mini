package org.apache.poimini;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;

import java.util.*;

public class ExcelManager {

    private static Workbook wb = null;

    private static FormulaEvaluator evaluator = null;

    private static Sheet currentSheet = null;

    static {
        init();
    }

    /**
     * Completely empties the excel worksheets.
     * It's called anyway automatically on library loading the first time.
     */
    public static void init() {
        wb = new HSSFWorkbook();

        currentSheet = null;

        evaluator = wb.getCreationHelper().createFormulaEvaluator();

        System.gc();
    }

    /**
     * Returns the number of sheets defined for this excel workbook.
     *
     * @return The number of sheets
     */
    public static int getNumberOfSheets() {
        return wb.getNumberOfSheets();
    }

    private static Sheet getCurrentSheet() {
        if (currentSheet==null) currentSheet = wb.createSheet();

        return currentSheet;
    }

    /**
     * Returns a list of cell names inside the specified range.
     *
     * @param fullRange The cell range, for example "C7:C12"
     * @return A list of cell names
     */
    public static List<String> getCellNameListFromRange(String fullRange) {
        List<String> result = new ArrayList<>();
        String[] addresses = fullRange.split(":");
        CellReference start = new CellReference(addresses[0]);
        CellReference end = new CellReference(addresses[1]);

        CellRangeAddress myRange = new CellRangeAddress(start.getRow(),end.getRow(),start.getCol(),end.getCol());

        Iterator<CellAddress> it = myRange.iterator();

        while (it.hasNext()) {
            CellAddress ca = it.next();
            result.add(ca.formatAsString());
        }

        return result;
    }

    /**
     * Returns a list of cell names, included from the startCell and the endCell, specified in two different parameters
     *
     * @param startCell The starting cell, for example "C7"
     * @param endCell The ending cell, for example "C12"
     * @return A list of cell names
     */
    public static List<String> getCellNameListFromStartToEnd(String startCell,String endCell) {
        List<String> result = new ArrayList<>();
        CellReference start = new CellReference(startCell);
        CellReference end = new CellReference(endCell);

        CellRangeAddress myRange = new CellRangeAddress(start.getRow(),end.getRow(),start.getCol(),end.getCol());

        Iterator<CellAddress> it = myRange.iterator();

        while (it.hasNext()) {
            CellAddress ca = it.next();
            result.add(ca.formatAsString());
        }

        return result;
    }

    /**
     * Reads from the specified sheet and the specified cell range all the cell values.
     *
     * @param sheetName The name of the sheet.
     * @param startCell The starting cell
     * @param endCell The ending cell
     * @return The list of the cell values, in string format.
     */
    public static List<String> readSheetRangeValues(String sheetName,String startCell,String endCell) {
        Sheet s;
        if (sheetName!=null) {
            s = wb.getSheet(sheetName);
        } else {
            s = wb.getSheetAt(0);
        }
        List<String> result = new ArrayList<>();

        CellReference start = new CellReference(startCell);
        CellReference end = new CellReference(endCell);

        CellRangeAddress myRange = new CellRangeAddress(start.getRow(),end.getRow(),start.getCol(),end.getCol());

        Iterator<CellAddress> it = myRange.iterator();
        while (it.hasNext()) {
            CellAddress ca = it.next();

            Row r = s.getRow(ca.getRow());
            Cell c = r.getCell(ca.getColumn());
            if (c==null) {
                result.add("");
            }
            else {
                switch (c.getCellType()) {
                    case STRING:
                        result.add(c.getStringCellValue());
                        break;
                    case NUMERIC:
                        result.add("" + c.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        result.add("" + c.getBooleanCellValue());
                        break;
                    case FORMULA:
                        result.add(evaluateCellFormula(ca.formatAsString()).formatAsString());
                        break;
                    case ERROR:
                        throw new IllegalStateException("Cell with errors not permitted as return values");
                    case BLANK:
                    case _NONE:
                        result.add("");
                        break;
                    default:
                        throw new IllegalStateException("Unknown cell type in the specified cell range");
                }
            }
        }

        return result;
    }

    /**
     * Sets the current working sheets in which the cell values will be written. If the sheet does not exist
     * it is created automatically.
     *
     * @param name The name of the sheets.
     */
    public static void setCurrentSheet(String name) {
        currentSheet = wb.getSheet(name);

        if (currentSheet==null) currentSheet = wb.createSheet(name);
    }

    /**
     * Writes a boolean value inside the specified cell.
     *
     * @param cellName The name of the cell
     * @param value The double value
     */
    public static void writeCell(String cellName,double value) {
        Cell c = internalGetCell(cellName);

        c.setCellValue(value);
    }

    /**
     * Writes a boolean value inside the specified cell.
     *
     * @param cellName The name of the cell
     * @param value The boolean value
     */
    public static void writeCell(String cellName,boolean value) {
        Cell c = internalGetCell(cellName);
        c.setCellValue(value);
    }

    /**
     * Writes the specified string inside the specified cell
     *
     * @param cellName The cell name
     * @param value The string to save into the cell
     */
    public static void writeCell(String cellName,String value) {
        Cell c = internalGetCell(cellName);
        c.setCellValue(value);
    }

    /**
     * Writes the specified date inside the specified cell.
     *
     * @param cellName The specified name
     * @param value The date to save
     */
    public static void writeCell(String cellName, Date value) {
        Cell c = internalGetCell(cellName);
        c.setCellValue(value);
    }

    /**
     * Writes the Calendar instance inside the cell
     *
     * @param cellName The name of the cell
     * @param value The calendar instance that rapresents the current datetime.
     */
    public static void writeCell(String cellName, Calendar value) {
        Cell c = internalGetCell(cellName);
        c.setCellValue(value);
    }

    /**
     * Writes a formula inside a cell
     *
     * @param cellName The name of the cell
     * @param formula The content of the formula in string format, without the starting equals (=)
     */
    public static void writeCellFormula(String cellName,String formula) {
        Cell c = internalGetCell(cellName);
        c.setCellFormula(formula);
    }

    private static Row internalGetRow(int index) {

        Sheet sh = getCurrentSheet();

        Row r = sh.getRow(index);

        if (r==null) r = sh.createRow(index);

        return r;
    }

    private static Cell internalGetCellFromRow(Row r,int index) {
        Cell c = r.getCell(index);

        if (c==null) c = r.createCell(index);

        return c;
    }

    private static Cell internalGetCell(String cellName) {
        CellReference ref = new CellReference(cellName);

        String sheetName = ref.getSheetName();
        if (sheetName!=null) setCurrentSheet(sheetName);

        Row r = internalGetRow(ref.getRow());

        Cell c = internalGetCellFromRow(r,ref.getCol());

        return c;
    }

    /**
     * Returns the type of a cell given the name
     *
     * @param cellName The name of the cell
     * @return The type of the cell
     */
    public static ExcelCellType getCellType(String cellName) {
        Cell c = internalGetCell(cellName);
        if (c==null) return ExcelCellType.BLANK;
        switch (c.getCellType()) {
            case FORMULA : return ExcelCellType.FORMULA;
            case STRING: return ExcelCellType.STRING;
            case NUMERIC: return ExcelCellType.NUMERIC;
            case BOOLEAN: return ExcelCellType.BOOLEAN;
            case BLANK: return ExcelCellType.BLANK;
            case ERROR: return ExcelCellType.ERROR;
            case _NONE: return ExcelCellType._NONE;
            default : throw new IllegalStateException("Valore non previsto");
        }
    }

    /**
     * Reads a cell as a string
     *
     * @param cellName The name of the cell
     * @return The value of the cell. If it's a formula, the formula result is returned.
     */
    public static String readCell(String cellName) {
        Cell c = internalGetCell(cellName);
        if (c==null) return "";
        return c.toString();
    }

    /**
     * Reads an error in a cell
     * @param cellName The name of the cell
     * @return The error code
     */
    public static byte readCellError(String cellName) {
        Cell c = internalGetCell(cellName);
        if (c==null) return 0;
        return c.getErrorCellValue();
    }

    /**
     * Reads a cell content as a string
     *
     * @param cellName The name of the cell
     * @return The content of the cell
     */
    public static String readCellAsString(String cellName) {
        Cell c = internalGetCell(cellName);
        if (c==null) return "";
        return c.getStringCellValue();
    }

    /**
     * Reads a cell content as a numeric value
     *
     * @param cellName The name of the cell
     * @return The numeric content of the cell
     */
    public static double readCellAsNumeric(String cellName) {
        Cell c = internalGetCell(cellName);
        if (c==null) return 0;
        return c.getNumericCellValue();
    }

    /**
     * Reads a cell value as a date
     * @param cellName The name of the cell
     * @return The date inside the cell
     */
    public static Date readCellAsDate(String cellName) {
        Cell c = internalGetCell(cellName);
        if (c==null) return null;
        return c.getDateCellValue();
    }

    /**
     * Reads a cell as boolean
     * @param cellName The name of the cell
     * @return The boolean value of the cell
     */
    public static boolean readCellAsBoolean(String cellName) {
        Cell c = internalGetCell(cellName);
        if (c==null) return false;
        return c.getBooleanCellValue();
    }

    /**
     * Reads a formula inside a cell
     *
     * @param cellName The name of the cell
     * @return The formula
     */
    public static String readCellFormula(String cellName) {
        Cell c = internalGetCell(cellName);
        if (c==null) return "";
        return c.getCellFormula();
    }

    /**
     * Removes from the cache all the values previously calculated for the formulas.
     */
    public static void clearAllCachedFormulaResults() {
        evaluator.clearAllCachedResultValues();
    }

    /**
     * Evaluates a formula given a cell.

     * @param cellName The name of the cell
     * @return The result of the formula with type and value.
     */
    public static ExcelCellValue evaluateCellFormula(String cellName) {
        Cell c = internalGetCell(cellName);
        if (c==null) return new ExcelCellValue(null);
        CellValue cv = evaluator.evaluate(c);
        return new ExcelCellValue(cv);
    }

    /**
     * Sets a debug for the next formula evaluation.
     *
     * @param value The flag to set to true to enable the debug od the next formula.
     */
    public static void setDebugEvaluationOutputForNextEval(boolean value) {
        evaluator.setDebugEvaluationOutputForNextEval(value);
    }

    /**
     * Settable flag to ignore missing excel worksheets connected to this one.
     *
     * @param value The flag to set to ignore the missing worksheets
     */
    public static void setIgnoreMissingWorkbooks(boolean value) {
        evaluator.setIgnoreMissingWorkbooks(value);
    }

    /**
     * Returns the name on the left of the specified cell.
     *
     * @param cell The name of the starting cell
     * @return The cell on the left of the specified cell.
     */
    public static final String getLeftCell(String cell) {
        CellReference ref = new CellReference(cell);
        String colName = CellReference.convertNumToColString(ref.getCol()-1);
        String rowName = ""+(ref.getRow()+1);
        return colName+rowName;
    }

    /**
     * Return the name of the cell on the right of the specified cell.
     *
     * @param cell The name of the starting cell
     * @return The cell on the right of the specified cell.
     */
    public static final String getRightCell(String cell) {
        CellReference ref = new CellReference(cell);
        String colName = CellReference.convertNumToColString(ref.getCol()+1);
        String rowName = ""+(ref.getRow()+1);
        return colName+rowName;
    }
}
