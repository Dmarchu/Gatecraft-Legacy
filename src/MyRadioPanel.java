import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class MyRadioPanel extends JPanel implements ActionListener{

    //Declaración de variables
    private static JRadioButton button1, button2, button3, buttonmem;
    public static int mode;
    public ButtonGroup buttons;

    //Método Constructor
    public MyRadioPanel(int width, int height, Color bkgnd, MouseListener m) {

        //Creación de botones
        button1 = new JRadioButton(Locales.LINEA);
        button2 = new JRadioButton(Locales.PUERTA);
        button3 = new JRadioButton(Locales.TEXTO);

        button1.addActionListener(this); button1.setBackground(bkgnd); button1.addMouseListener(m);
        button2.addActionListener(this); button2.setBackground(bkgnd); button2.addMouseListener(m);
        button3.addActionListener(this); button3.setBackground(bkgnd); button3.addMouseListener(m);

        button1.setSelected(true);
        buttonmem = button1;

        //Grupo de botones
        buttons = new ButtonGroup();
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);

        //Configuración del RadioPanel
        setBackground(bkgnd);
        setVisible(true);
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true), Locales.MODO));
        setLayout(new GridLayout(1,3, 0, 0));
        setBounds(5,height - 100,width / 5,50);
        add(button1);
        add(button2);
        add(button3);
    }

    //ActionListener

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button1) {
            if (buttonmem == e.getSource()) {
                if (mode != -1) {
                    buttons.clearSelection();
                    mode = -1;
                } else {
                    button1.setSelected(true);
                    mode = 0;
                }
            } else {
                mode = 0;
            }
        } else if (e.getSource() == button2){
            if (buttonmem == e.getSource()) {
                if (mode != -1) {
                    buttons.clearSelection();
                    mode = -1;
                } else {
                    button2.setSelected(true);
                    mode = 1;
                }
            } else {
                mode = 1;
            }
        } else if (e.getSource() == button3){
            if (buttonmem == e.getSource()) {
                if (mode != -1) {
                    buttons.clearSelection();
                    mode = -1;
                } else {
                    button3.setSelected(true);
                    mode = 2;
                }
            } else {
                mode = 2;
            }
        }
        buttonmem = (JRadioButton) e.getSource();
    }

    //Setters

    public static void setMode(int mode) {
        MyRadioPanel.mode = mode;
        switch (mode) {
            case 0: button1.setSelected(true); break;
            case 1: button2.setSelected(true); break;
            case 2: button3.setSelected(true); break;
        }
    }
}
