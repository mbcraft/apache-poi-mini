package org.apache.poimini.i18n;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ExcelFormulasTranslator {

    private final List<ItaFormulaTranslator> formula_translations = new ArrayList<>();

    public ExcelFormulasTranslator() throws IOException {
        String translations_file_name = System.getProperty("apache.poimini.i18n","ita_eng.csv");

        InputStream is = ExcelFormulasTranslator.class.getResourceAsStream(translations_file_name);

        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);

        String row;
        do {
            row = br.readLine();

            if (row!=null) {
                formula_translations.add(new ItaFormulaTranslator(row));
            }
        } while (row != null);

    }

    public String translateFormula(String formula) {
        for (ItaFormulaTranslator ft : formula_translations) {
            if (ft.matches(formula)) return ft.traduci();
        }
        return formula;
    }
}
