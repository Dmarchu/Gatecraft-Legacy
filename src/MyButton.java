import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

public class MyButton extends JButton {

    //Declaración de variables
    private ImageIcon icon;
    private String nombre;

    //Método Constructor
    public MyButton(int type, int width, int height, int x, int y, Color bkgnd, ActionListener l, MouseListener m) {
        switch (type) {
            case 0: icon = new ImageIcon("src/media/gates/AND.png"); nombre = Locales.AND; break;
            case 1: icon = new ImageIcon("src/media/gates/NAND.png"); nombre = Locales.NAND; break;
            case 2: icon = new ImageIcon("src/media/gates/OR.png"); nombre = Locales.OR; break;
            case 3: icon = new ImageIcon("src/media/gates/NOR.png"); nombre = Locales.NOR; break;
            case 4: icon = new ImageIcon("src/media/gates/XOR.png"); nombre = Locales.XOR; break;
            case 5: icon = new ImageIcon("src/media/gates/XNOR.png"); nombre = Locales.XNOR; break;
            case 6: icon = new ImageIcon("src/media/gates/NOT.png"); nombre = Locales.NOT; break;
            case 7: icon = new ImageIcon("src/media/gates/OFF.png"); nombre = Locales.SWITCH; break;
            case 8: icon = new ImageIcon("src/media/gates/LEDOFF.png"); nombre = "LED"; break;
            //Añadir nuevos elementos aquí (procurar que sean todos del mismo tamaño) se deberá cambiar también el método switchGates en MyPanel
        }

        //Configuración de los botones
        this.setVisible(true);
        this.setIcon(icon);
        this.setLocation(x,y);
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), nombre));
        this.setSize(width, height);
        this.setBackground(bkgnd);
        this.addActionListener(l);
        this.addMouseListener(m);
    }
}
