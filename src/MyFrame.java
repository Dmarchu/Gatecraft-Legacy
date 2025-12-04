import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class MyFrame extends JFrame {

    //Declaración de Variables estáticas
    public static JMenuItem redo, back, del;
    public static Color thiscolor = Color.BLACK, theme = Color.WHITE;
    public static Clip clip;
    public static int tipo;
    public static boolean pantallacompleta;
    public static MyPanel panel;

    //Declaración de Variables
    public int width, height, range;
    public MyOptions optionmenu;
    public JLabel label;
    public JPanel minipanel;
    public JMenuBar menuBar;
    public MyRadioPanel radioPanel;
    public JScrollPane scrollPanel, scrollPane;

    @SuppressWarnings("ClassEscapesDefinedScope")
    public MouseStateListener mlistener;

    //Método Constructor
    public MyFrame(boolean Fullscreen) {
        if (Fullscreen) { //Detecta si estamos en pantalla completa
            range = 1550;
        } else {
            range = 1250;
        }

        MyFrame.pantallacompleta = Fullscreen;
        mlistener = new MouseStateListener();

        //Genera el tamaño a partir del range, determinado por la pantalla completa / no completa
        this.width = range;
        this.height = (int) (range * 0.55);

        //Carga las imágenes de la interfaz (No las Gates)
        ImageIcon logo = new ImageIcon("src/media/logo.png");
        ImageIcon archivoi = new ImageIcon("src/media/archivo.png");
        ImageIcon savei = new ImageIcon("src/media/save.png");
        ImageIcon createi = new ImageIcon("src/media/create.png");
        ImageIcon deli = new ImageIcon("src/media/close.png");
        ImageIcon backi = new ImageIcon("src/media/back.png");
        ImageIcon redoi = new ImageIcon("src/media/redo.png");
        ImageIcon optsi = new ImageIcon("src/media/options.png");
        ImageIcon colori = new ImageIcon("src/media/color.png");

        //Declaración de los elementos de la interfaz
        optionmenu = new MyOptions(theme);
        panel = new MyPanel(width, height, theme);
        radioPanel = new MyRadioPanel(width,height, theme, mlistener);
        menuBar = new JMenuBar();
        minipanel = new JPanel();
        label = new JLabel();

        //Declaración de los elementos de la barra de herramientas
        JMenuItem archivo = new JMenuItem(Locales.ARCHIVO);
        JMenuItem save = new JMenuItem(Locales.GUARDAR);
        JMenuItem create = new JMenuItem(Locales.AÑADIR);
        JMenuItem opts = new JMenuItem(Locales.OPTS);
        JMenuItem color = new JMenuItem(Locales.COLORES);
        back = new JMenuItem(Locales.ATRAS);
        redo = new JMenuItem(Locales.REHACER);
        del = new JMenuItem(Locales.BORRAR);

        //Abre el sonido de clic
        try {
            clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File("src/media/click.wav")));
            clip.setMicrosecondPosition(0);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }

        //Color de fondo y mantener options visible al cerrar
        setBackground(theme);
        optionmenu.setAlwaysOnTop(true);

        //Creación de los elementos de la barra de herramientas
        archivo.setIcon(archivoi);archivo.setVisible(true); archivo.setBackground(theme);
        archivo.setVerticalTextPosition(SwingConstants.BOTTOM); archivo.setHorizontalTextPosition(SwingConstants.CENTER);
        archivo.setVerticalAlignment(SwingConstants.CENTER); archivo.setHorizontalAlignment(SwingConstants.CENTER); archivo.addMouseListener(mlistener);
        archivo.addActionListener(e -> panel.leerArchivo());

        save.setIcon(savei);save.setVisible(true); save.setBackground(theme);
        save.setVerticalTextPosition(SwingConstants.BOTTOM); save.setHorizontalTextPosition(SwingConstants.CENTER);
        save.setVerticalAlignment(SwingConstants.CENTER); save.setHorizontalAlignment(SwingConstants.CENTER); save.addMouseListener(mlistener);
        save.addActionListener(e -> panel.descargarArchivo());

        create.setIcon(createi);create.setVisible(true); create.setBackground(theme);
        create.setVerticalTextPosition(SwingConstants.BOTTOM); create.setHorizontalTextPosition(SwingConstants.CENTER);
        create.setVerticalAlignment(SwingConstants.CENTER); create.setHorizontalAlignment(SwingConstants.CENTER); create.addMouseListener(mlistener);
        create.addActionListener(e -> panel.runCircuito());

        color.setIcon(colori); color.setVisible(true); color.setBackground(theme);
        color.setVerticalTextPosition(SwingConstants.BOTTOM); color.setHorizontalTextPosition(SwingConstants.CENTER);
        color.setVerticalAlignment(SwingConstants.CENTER); color.setHorizontalAlignment(SwingConstants.CENTER); color.addMouseListener(mlistener);
        color.addActionListener(e -> {
            JColorChooser colorChooser = new JColorChooser(Color.BLACK);
            JOptionPane.showConfirmDialog(null, colorChooser, Locales.SELEC_COLOR, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            MyFrame.thiscolor = colorChooser.getColor();
        });

        opts.setIcon(optsi);opts.setVisible(true); opts.setBackground(theme);
        opts.setVerticalTextPosition(SwingConstants.BOTTOM); opts.setHorizontalTextPosition(SwingConstants.CENTER);
        opts.setVerticalAlignment(SwingConstants.CENTER); opts.setHorizontalAlignment(SwingConstants.CENTER); opts.addMouseListener(mlistener);
        opts.addActionListener(e -> optionmenu.setVisible(true));

        back.setIcon(backi);back.setVisible(true); back.setBackground(theme);
        back.setVerticalTextPosition(SwingConstants.BOTTOM); back.setHorizontalTextPosition(SwingConstants.CENTER); back.addMouseListener(mlistener);
        back.setVerticalAlignment(SwingConstants.CENTER); back.setHorizontalAlignment(SwingConstants.CENTER); back.setEnabled(false);
        back.addActionListener(e -> {
            panel.KeyAction();
            repaint();
        });

        redo.setIcon(redoi);redo.setVisible(true); redo.setEnabled(false); redo.setBackground(theme);
        redo.setVerticalTextPosition(SwingConstants.BOTTOM); redo.setHorizontalTextPosition(SwingConstants.CENTER);
        redo.setVerticalAlignment(SwingConstants.CENTER); redo.setHorizontalAlignment(SwingConstants.CENTER); redo.addMouseListener(mlistener);
        redo.addActionListener(e -> {
            panel.KeyAction2();
            repaint();
        });

        del.setIcon(deli);del.setVisible(true); del.setBackground(theme);
        del.setVerticalTextPosition(SwingConstants.BOTTOM); del.setHorizontalTextPosition(SwingConstants.CENTER); del.addMouseListener(mlistener);
        del.setVerticalAlignment(SwingConstants.CENTER); del.setHorizontalAlignment(SwingConstants.CENTER); del.setEnabled(false);
        del.addActionListener(e -> {
            //Poner sonido papelera
            panel.clear(0);
            repaint();
        });

        //Crea de la barra de herramientas y añade todos sus elementos
        menuBar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true), Locales.OPCIONES));
        menuBar.setBounds(5 + width / 5,0, ((4 * width) / 5) - height / 5 - 28, height / 5);
        menuBar.setLayout(new FlowLayout(FlowLayout.CENTER)); menuBar.setBackground(theme);
        menuBar.add(archivo); menuBar.add(save); menuBar.add(opts); menuBar.add(create); menuBar.add(color); menuBar.add(back); menuBar.add(redo); menuBar.add(del);

        //Crea el logo
        label.setIcon(logo);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2,true));
        label.setVisible(true); label.setBackground(theme);
        label.setBounds(5 + width / 5 + ((4 * width) / 5) - height / 5 - 28, 8, height / 5, height / 5 - 10);

        //Crea el panel de selección de las Gates y añade los botones
        minipanel.setVisible(true);
        minipanel.setLayout(new GridLayout(9,1));
        minipanel.setBounds(5,0,width / 5,height);
        for (int i = 0, y = 0; i < 9; i++, y += (((4 * height) / 5) - 50) / 9) {
            int finalI = i;
            //Constructor de los botones
            minipanel.add(new MyButton(i, width / 5 - 100, 100, 0, y + 5, theme, e -> {
                tipo = finalI;
            }, mlistener));
        }

        //Añadir ScrollBar al panel anterior
        scrollPane = new JScrollPane(minipanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setLayout(new ScrollPaneLayout());
        scrollPane.setVisible(true); scrollPane.setBackground(theme);
        scrollPane.setBounds(5,0,width / 5,height - 100);
        scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true), Locales.INSERTAR));

        //Añadir scrollBar si es necesaria (ZOOM) al editor
        scrollPanel = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPanel.setLayout(new ScrollPaneLayout());
        scrollPanel.setVisible(true); scrollPanel.setBackground(theme);
        scrollPanel.setBounds(5 + width / 5, height / 5, ((4 * width) / 5) - 25, ((4 * height) / 5) - 50);
        scrollPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true), Locales.VISTA));

        //Configuración del Frame y añade todos los objetos a la interfaz
        this.setVisible(true);
        this.setIconImage(logo.getImage());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle(Locales.TITULO);
        this.setSize(width,height);
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.add(menuBar); this.add(scrollPanel); this.add(scrollPane); this.add(radioPanel); this.add(label);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    //Getters

    @Override
    public int getWidth() {return width;}
    public static int getTipo() {return tipo;}

    private class MouseStateListener extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent e) {
            if (e.getComponent().isEnabled() && MyRadioPanel.mode != -1) {
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (MyRadioPanel.mode != -1) {
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }
}
