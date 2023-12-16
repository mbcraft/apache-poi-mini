package org.apache.poimini.i18n;

public class ItaFormulaTranslator {

    private final String fn_italiano;
    private final String fn_inglese;

    public ItaFormulaTranslator(String row) {
        String[] fn_names = row.split(";");

        if (fn_names.length!=2) throw new RuntimeException("Estrazione traduzioni formule errata!");

        fn_italiano = fn_names[0];
        fn_inglese = fn_names[1];

    }

    public boolean matches(String formula) {
        return formula.equalsIgnoreCase(this.fn_italiano);
    }

    public String traduci() {
        return fn_inglese;
    }

}
