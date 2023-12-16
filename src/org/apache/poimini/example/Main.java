package org.apache.poimini.example;

import org.apache.poimini.ExcelManager;
import org.apache.poi.ss.formula.function.FunctionMetadataRegistry;
import org.apache.poi.ss.util.CellReference;

import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        //testFormuleInCascata();
        //testCalcWithEuro();
        //dumpCellReference();
        //testReadIntValue();
        //testRangeRead();
        //testCellRange();
        //testCellRangeStartEnd();
        //testFunctionRegistry();
        //testEvaluationSomma();
        //testNumberValueTranslation();
    }

    private static void testFormuleInCascata() {
        ExcelManager.setCurrentSheet("PROVA");

        ExcelManager.writeCell("A1",10);
        ExcelManager.writeCell("A2",7);
        ExcelManager.writeCell("A3",1);
        ExcelManager.writeCellFormula("A4","SOMMA(A1:A2)");
        ExcelManager.writeCellFormula("A5","SOMMA(A3:A4)");

        System.out.println("Formula cella A5 : " + ExcelManager.evaluateCellFormula("a5").formatAsString());
    }

    private static void testCalcWithEuro() {
        ExcelManager.setCurrentSheet("TEST");

        ExcelManager.writeCell("A1",10);
        ExcelManager.writeCell("B1","5 €");
        ExcelManager.writeCellFormula("C1","A1*B1");

        String result = ExcelManager.evaluateCellFormula("C1").formatAsString();

        System.out.println("Risultato del calcolo : "+result);
    }

    private static void dumpCellReference() {
        CellReference cr = new CellReference("C12");
        System.out.println("CELL C12 : ");
        System.out.println("COL INDEX : "+cr.getCol());
        System.out.println("ROW INDEX : "+cr.getRow());
    }

    private static void testReadIntValue() {
        ExcelManager.writeCell("T10",7);
        ExcelManager.writeCell("T11",8);

        List<String> values = ExcelManager.readSheetRangeValues(null,"T10","T11");

        System.out.println("Lettura valori interi da excel");

        for (String s : values) {
            System.out.println(s);
        }
    }

    private static void testFunctionRegistry() {
        Set<String> functions = FunctionMetadataRegistry.getInstance().getAllFunctionNames();
        for (String f : functions) {
            System.out.println(f);
        }
    }

    private static void testNumberValueTranslation() {

        System.out.println("Test funzione NUMBERVALUE");

        ExcelManager.init();

        ExcelManager.writeCell("C7",12);
        ExcelManager.writeCell("C6",6);
        ExcelManager.writeCellFormula("C5","NUMBERVALUE(LEFT(C7,1))");

        String result = ExcelManager.evaluateCellFormula("C5").formatAsString();

        System.out.println("Risultato cella C5 : "+result);

        ExcelManager.clearAllCachedFormulaResults();

        String result2 = ExcelManager.evaluateCellFormula("C5").formatAsString();

        System.out.println("Risultato cella C5 : "+result2);

    }

    private static void testEvaluationSomma() {

        System.out.println("Test funzione SOMMA");

        ExcelManager.init();

        ExcelManager.writeCell("C7",1);
        ExcelManager.writeCell("C8",16);
        ExcelManager.writeCellFormula("C9","SOMMA(C7:C8)");

        System.out.println("Prima di write : "+ExcelManager.evaluateCellFormula("C9").formatAsString());

        ExcelManager.writeCell("C7",4);

        ExcelManager.clearAllCachedFormulaResults();

        System.out.println("Dopo write : "+ExcelManager.evaluateCellFormula("C9").formatAsString());

    }

    private static void testCellRangeStartEnd() {

        ExcelManager.init();

        List<String> myList = ExcelManager.getCellNameListFromStartToEnd("C7","C10");
        System.out.println("Test di elaborazione range start end :");
        for (String s : myList) {
            System.out.println(s);
        }
    }

    private static void testCellRange() {

        ExcelManager.init();

        List<String> myList = ExcelManager.getCellNameListFromRange("C7:C10");
        System.out.println("Test di elaborazione range :");
        for (String s : myList) {
            System.out.println(s);
        }
    }

    private static void testRangeRead() {

        System.out.println("Test lettura di range di valori dal foglio");

        ExcelManager.init();

        ExcelManager.setCurrentSheet("TEST");

        ExcelManager.writeCell("C7",1);
        ExcelManager.writeCell("C8",16);
        ExcelManager.writeCellFormula("C9","SOMMA(C7:C8)");
        ExcelManager.writeCell("C10","prova");

        List<String> rangeValues = ExcelManager.readSheetRangeValues("TEST","C7","C10");

        System.out.println("Valori del range :");
        for (String val : rangeValues) {
            System.out.println(val);
        }
    }
}
