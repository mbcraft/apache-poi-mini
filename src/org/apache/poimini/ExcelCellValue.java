package org.apache.poimini;

import org.apache.poi.ss.usermodel.CellValue;

public class ExcelCellValue {

    private CellValue cv;

    ExcelCellValue(CellValue cv) {
        this.cv = cv;
    }

    public ExcelCellType getCellType() {
        if (cv==null) return ExcelCellType.BLANK;
        switch (cv.getCellType()) {
            case FORMULA : return ExcelCellType.FORMULA;
            case STRING: return ExcelCellType.STRING;
            case NUMERIC: return ExcelCellType.NUMERIC;
            case BOOLEAN: return ExcelCellType.BOOLEAN;
            case BLANK: return ExcelCellType.BLANK;
            case ERROR: return ExcelCellType.ERROR;
            case _NONE: return ExcelCellType._NONE;
            default : throw new IllegalStateException("Type not found.");
        }
    }

    public boolean getBooleanValue() {
        if (cv==null) return false;
        return cv.getBooleanValue();
    }

    public double getNumberValue() {
        if (cv==null) return 0;
        return cv.getNumberValue();
    }

    public String getStringValue() {
        if (cv==null) return "";
        return cv.getStringValue();
    }

    public String formatAsString() {
        if (cv==null) return "";
        return cv.formatAsString();
    }

    public String toString() {
        if (cv==null) return "";
        return cv.toString();
    }

    public byte getErrorValue() {
        if (cv==null) return 0;
        return cv.getErrorValue();
    }
}
