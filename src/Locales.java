import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Locales {

    //Declaración de Variables
    private Scanner reader;
    private boolean isValidFile, isThereSomeSortOfError;
    public static String Language;

    //Declaración de Locales
    public static String ARCHIVO, GUARDAR, AÑADIR, OPTS, COLORES, ATRAS, REHACER, BORRAR, SELEC_COLOR, OPCIONES, INSERTAR, VISTA, TITULO,
    LINEA, PUERTA, TEXTO, MODO, TITULOS_DIALOGS, CONF_BORRAR, ERR_EDITOR_VACIO, EJEMPLO_TEXTO, ERR_NACC_PREV, ERR_NACC_POSI, 
    SUCC_ARCH_CREAD, ERR_ARCH_CREAD, INF_SCHEMPTY, ERR_OP_CANCEL, ERR_LEER_ARCH, SUCC_LEER_ARCH, ERR_EXT_NVALID, ERR_CODIGO, DIALOG_TEXT, ERR_INV_CHAR,
    FULLSCREEN, IDIOMA, RECTIF, TITLE_IN, TITLE_OUT, AND, NAND, ANDNAND, TRINAND, OR, NOR, ORNOR, XOR, XNOR, 
    XORNOR, BUFF, NOT, DRIV, DECO1, DECO2, MUX, BIEST1, BIEST2, BIEST3, INTEGR, SWITCH, ON, OFF, es, en;

    //Método Constructor
    public Locales(String localisation) {
        isValidFile = true;
        isThereSomeSortOfError = false;
        Language = localisation;

        try {
            switch(localisation) {
                case "Locale_ES": reader = new Scanner(new File("src/locales/ES_es"));
                    break;
                case "Locale_EN": reader = new Scanner(new File("src/locales/EN_en"));
                    break;
                default: isValidFile = false;
                    break;
            }
        } catch (FileNotFoundException ignored) {}

        ARCHIVO = nextLine();
        GUARDAR = nextLine();
        AÑADIR = nextLine();
        OPTS = nextLine();
        COLORES = nextLine();
        ATRAS = nextLine();
        REHACER = nextLine();
        BORRAR = nextLine();
        SELEC_COLOR = nextLine();
        OPCIONES = nextLine();
        INSERTAR = nextLine();
        VISTA = nextLine();
        TITULO = nextLine();

        LINEA = nextLine();
        PUERTA = nextLine();
        TEXTO = nextLine();
        MODO = nextLine();

        TITULOS_DIALOGS = nextLine();
        CONF_BORRAR = nextLine();
        ERR_EDITOR_VACIO = nextLine();
        EJEMPLO_TEXTO = nextLine();
        ERR_NACC_PREV = nextLine();
        ERR_NACC_POSI = nextLine();
        SUCC_ARCH_CREAD = nextLine();
        ERR_ARCH_CREAD = nextLine();
        INF_SCHEMPTY = nextLine();
        ERR_OP_CANCEL = nextLine();
        ERR_LEER_ARCH = nextLine();
        SUCC_LEER_ARCH = nextLine();
        ERR_EXT_NVALID = nextLine();
        ERR_CODIGO = nextLine();
        DIALOG_TEXT = nextLine();
        ERR_INV_CHAR = nextLine();

        FULLSCREEN = nextLine();
        IDIOMA = nextLine();
        RECTIF = nextLine();
        TITLE_IN = nextLine();
        TITLE_OUT = nextLine();

        AND = nextLine();
        NAND = nextLine();
        ANDNAND = nextLine();
        TRINAND = nextLine();
        OR = nextLine();
        NOR = nextLine();
        ORNOR = nextLine();
        XOR = nextLine();
        XNOR = nextLine();
        XORNOR = nextLine();
        BUFF = nextLine();
        NOT = nextLine();
        DRIV = nextLine();
        DECO1 = nextLine();
        DECO2 = nextLine();
        MUX = nextLine();
        BIEST1 = nextLine();
        BIEST2 = nextLine();
        BIEST3 = nextLine();
        INTEGR = nextLine();

        SWITCH = nextLine();
        ON = nextLine();
        OFF = nextLine();

        es = nextLine();
        en = nextLine();

        if(isThereSomeSortOfError) JOptionPane.showMessageDialog(null, ERR_CODIGO, "Fatal ERROR in Locale", JOptionPane.ERROR_MESSAGE);
    }

    @SuppressWarnings("StringConcatenationInLoop")
    public String nextLine() {
        if (isValidFile) {
            String line = reader.nextLine();
            String result = "";

            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == '=') {
                    i++;
                    while (line.charAt(i) != ';') {
                        result += line.charAt(i);
                        i++;
                    }
                    break;
                }
            }
            return result;
        } else {
            isThereSomeSortOfError = true;
            return "LOCALE_ERROR in " + Language;
        }
    }

    public String getLocalisation() {
        return Language;
    }
}
