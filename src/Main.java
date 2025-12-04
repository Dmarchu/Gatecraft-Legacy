/**
 * CREADO POR: David Martínez
 * Hola developers, bienvenidos a las entrañas de GateMaster, a continuación os adjuntaré una serie de manuales que os serán
 * útiles a la hora de manejar este código. ¡Buena Suerte!
 * <p>
 * AÑADIR GATES
 * AÑADIR LOCALES
 * AÑADIR STRINGS
 */

//CREAR MANUAL
public class Main {

    //Declaración de Variables
    public static MyFrame frame;
    public static Locales locale;

    //Método Ejecutor Main
    public static void main(String[] args) {
        locale = new Locales("Locale_ES");
        frame = new MyFrame(false);
    }
}