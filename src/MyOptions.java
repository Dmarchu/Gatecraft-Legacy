import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

@SuppressWarnings("DataFlowIssue")
public class MyOptions extends JFrame {

    //Declaración de Variables Estáticas
    public static int rectification;

    //Declaración de Variables
    private boolean state = MyFrame.pantallacompleta, redomem, backmem, delmem, hasGraphicsMem;
    private int countermem, counter2mem, counter3mem, modemem;
    private ArrayList<Point> XPointmem, YPointmem, XimageCornermem, YimageCornermem, nextXPointmem, nextYPointmem, nextXimageCornermem, nextYimageCornermem;
    private ArrayList<Integer> type2mem, lastActionmem, nextType2mem, nextActionmem;
    private ArrayList<String> textmem, nextTextmem;
    private ArrayList<Color> colormem, color2mem, nextColormem, nextColor2mem;
    private final JSpinner help;

    //Método Constructor
    public MyOptions(Color bkgnd) {

        //Logo y fondo
        ImageIcon logo = new ImageIcon("src/media/logo.png");
        setBackground(bkgnd);

        //Creación del elemento de pantalla completa
        JLabel fullscreenl = new JLabel(Locales.FULLSCREEN);
        fullscreenl.setFont(new Font("Verdana", Font.BOLD, 15));
        JToggleButton fullscreen = getFullScreen();

        //Creación y configuración del elemento "Idioma"
        JLabel lenguajel = new JLabel(Locales.IDIOMA);
        lenguajel.setFont(new Font("Verdana", Font.BOLD, 15));
        JComboBox<String> lenguaje = getLenguaje();

        //Creación y configuración del elemento "Rectificación"
        JLabel helpl = new JLabel(Locales.RECTIF);
        helpl.setFont(new Font("Verdana", Font.BOLD, 15));
        help = new JSpinner(new SpinnerNumberModel());
        help.setPreferredSize(new Dimension(40,25));
        help.setFocusable(false);
        help.addChangeListener(e -> {
            //Comprueba si el número de la rectificación es válido
            if (Integer.parseInt(help.getValue().toString()) < 0) help.setValue(0);
            if (Integer.parseInt(help.getValue().toString()) > 100) help.setValue(100);
            rectification = Integer.parseInt(help.getValue().toString());
        });

        //Valor inicial de la rectificación
        help.setValue(15);
        rectification = 15;

        //Creación y configuración del panel de opciones
        JPanel optpanel = new JPanel();
        optpanel.setBounds(0,0, 400,500);
        optpanel.setLayout(new GridLayout(3,2, 0,15));
        optpanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 3), Locales.TITLE_IN));
        optpanel.add(fullscreenl); optpanel.add(fullscreen); optpanel.add(lenguajel); optpanel.add(lenguaje);
        optpanel.add(helpl); optpanel.add(help);

        //Configuración del frame
        setVisible(false);
        setIconImage(logo.getImage());
        setSize(400,200);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle(Locales.TITLE_OUT);
        add(optpanel);
    }

    private JToggleButton getFullScreen() {
        JToggleButton fullscreen;
        if (state) { //Comprueba el estado inicial de la pantalla completa
            fullscreen = new JToggleButton("ON");
        } else {
            fullscreen = new JToggleButton("OFF");
        }

        //Configuración del elemento pantalla completa y Action Listener
        fullscreen.setFocusable(false);
        fullscreen.addActionListener(e -> {
            memOptions();
            if (state) { //Comprueba el estado actual de la pantalla
                state = false; dispose();
                Main.frame.dispose();
                Main.frame = new MyFrame(false);
            } else {
                state = true; dispose();
                Main.frame.dispose();
                Main.frame = new MyFrame(true);
            }
            redoOptions();
        });
        return fullscreen;
    }

    private JComboBox<String> getLenguaje() {
        JComboBox<String> lenguaje = new JComboBox<>();
        lenguaje.addItem(Locales.es);
        lenguaje.addItem(Locales.en);
        lenguaje.setFocusable(false);
        if (Main.locale.getLocalisation().equals("Locale_ES")) {
            lenguaje.setSelectedItem(Locales.es);
        } else {
            lenguaje.setSelectedItem(Locales.en);
        }
        lenguaje.addActionListener(e -> {
            memOptions();
            if (lenguaje.getSelectedItem().toString().equals(Locales.es)) {
                Main.frame.dispose(); dispose();
                Main.locale = new Locales("Locale_ES");
                Main.frame = new MyFrame(state);
            } else if (lenguaje.getSelectedItem().toString().equals(Locales.en)) {
                Main.frame.dispose(); dispose();
                Main.locale = new Locales("Locale_EN");
                Main.frame = new MyFrame(state);
            }
            redoOptions();
        });
        return lenguaje;
    }

    public void redoOptions() {
        MyFrame.panel.repintaUniqueOptions(countermem, counter2mem, counter3mem, backmem, redomem, delmem, hasGraphicsMem);
        MyFrame.panel.repintaNextOptions(nextActionmem, nextXPointmem, nextYPointmem, nextXimageCornermem, nextYimageCornermem,
                nextTextmem, nextType2mem, nextColormem, nextColor2mem);
        MyFrame.panel.repintaOptions(lastActionmem, XPointmem, YPointmem, XimageCornermem, YimageCornermem,
                textmem, type2mem, colormem, color2mem);
        MyRadioPanel.setMode(modemem);
    }

    public void memOptions() { //Almacena los datos del frame en memoria
        countermem = MyPanel.counter; counter2mem = MyPanel.counter2; counter3mem = MyPanel.counter3;
        redomem = MyFrame.redo.isEnabled(); backmem = MyFrame.back.isEnabled(); delmem = MyFrame.del.isEnabled(); hasGraphicsMem = MyPanel.hasGraphics;

        nextActionmem = MyPanel.nextAction; nextXPointmem = MyPanel.nextXPoint; nextYPointmem = MyPanel.nextYPoint; nextXimageCornermem = MyPanel.nextXimageCorner;
        nextYimageCornermem = MyPanel.nextYimageCorner; nextTextmem = MyPanel.nextText; nextType2mem = MyPanel.nextType2;
        nextColormem = MyPanel.nextColor; nextColor2mem = MyPanel.nextColor2;

        lastActionmem = MyPanel.lastAction;
        XPointmem = MyPanel.XPoint; YPointmem = MyPanel.YPoint;
        XimageCornermem = MyPanel.XimageCorner; YimageCornermem = MyPanel.YimageCorner;
        textmem = MyPanel.text; type2mem = MyPanel.type2; colormem = MyPanel.color; color2mem = MyPanel.color2; modemem = MyRadioPanel.mode;
    }
}
