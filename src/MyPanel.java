import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings("DuplicatedCode")
public class MyPanel extends JPanel {

    //Declaración de variables estáticas
    public static Point imageCorner, prevPt, imageCorner2;
    public static ArrayList<Point> XPoint, YPoint, XimageCorner, YimageCorner, nextXPoint, nextYPoint, nextXimageCorner, nextYimageCorner;
    public static ArrayList<Integer> type2, lastAction, nextType2, nextAction;
    public static ArrayList<String> text, nextText;
    public static ArrayList<Color> color, nextColor, color2, nextColor2;
    public static int width, height, señal, counter = -1, counter2 = -1, prevCounter, prevCounter2, image = -1, counter3 = -1, prevCounter3;
    public static boolean hasGraphics = false, pointspaint = false;

    public static ArrayList<Line2D> lines, nextLines;
    public static ArrayList<Gate> gates, nextGates;
    public static ArrayList<Cables> cables, nextCables; //Inicializar y implementar redos
    public static Rectangle2D selectedPoint;

    //Declaración de variables
    private boolean isInFrame = false, repintado = false, confirmed = true;
    private ImageIcon icon;
    private int lineSignal = 0;

    private static final double MIN_ZOOM = 0.9;
    private static final double MAX_ZOOM = 3.0;
    private static final double ZOOM_STEP = 0.1;
    public static double zoomFactor = 0.9;
    private final Dimension size;

    //Método Constructor
    public MyPanel(int width, int height, Color bkgnd) {

        //Inicialización de variables
        MyPanel.width = width;
        MyPanel.height = height;

        XPoint = new ArrayList<>();
        YPoint = new ArrayList<>();
        XimageCorner = new ArrayList<>();
        YimageCorner = new ArrayList<>();
        type2 = new ArrayList<>();
        text = new ArrayList<>();
        color = new ArrayList<>();
        color2 = new ArrayList<>();

        nextXPoint = new ArrayList<>();
        nextYPoint = new ArrayList<>();
        nextXimageCorner = new ArrayList<>();
        nextYimageCorner = new ArrayList<>();
        nextType2 = new ArrayList<>();
        nextText = new ArrayList<>();
        nextColor = new ArrayList<>();
        nextColor2 = new ArrayList<>();

        nextAction = new ArrayList<>();
        nextAction.add(0);

        lastAction = new ArrayList<>();
        lastAction.add(0);

        lines = new ArrayList<>();
        nextLines = new ArrayList<>();

        gates = new ArrayList<>();
        nextGates = new ArrayList<>();

        cables = new ArrayList<>();
        nextCables = new ArrayList<>();

        imageCorner = new Point(0, 25);
        imageCorner2 = new Point(0, 25);
        prevPt = new Point(0, 25);

        ClickListener clickListener = new ClickListener();
        addMouseListener(clickListener);

        MotionListener motionListener = new MotionListener();
        addMouseMotionListener(motionListener);

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) zoomFactor += ZOOM_STEP;
                else if (e.getWheelRotation() > 0) zoomFactor -= ZOOM_STEP;

                if (zoomFactor < MIN_ZOOM) {
                    zoomFactor = MIN_ZOOM;
                } else if (zoomFactor > MAX_ZOOM) {
                    zoomFactor = MAX_ZOOM;
                } else {
                    setPreferredSize(new Dimension((int) (size.width * zoomFactor), (int) (size.height * zoomFactor)));
                    revalidate();
                    repaint();
                }
            }
        });

        //Configuraciones del panel (cambiar si se desea)
        setVisible(true);
        setBackground(bkgnd);
        setLayout(null);
        setBounds(5 + width / 5, height / 5, ((4 * width) / 5) - 25, ((4 * height) / 5) - 50);
        setPreferredSize(new Dimension((int) ((((4 * width) / 5) - 25) * zoomFactor), (int) ((((4 * height) / 5) - 50) * zoomFactor)));
        size = getSize();
    }

    public void clear(int call) { //Método para limpiar panel
        if (hasGraphics) { //Comprueba si hay gráficos en el editor
            confirmed = JOptionPane.showConfirmDialog(null, Locales.CONF_BORRAR, Locales.TITULOS_DIALOGS, JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE) == 0;
            if (confirmed) { //Comprueba la confirmación del diálogo
                counter = -1;
                counter2 = -1;
                counter3 = -1;
                image = -1;

                XPoint.clear();
                YPoint.clear();
                XimageCorner.clear();
                YimageCorner.clear();
                type2.clear();
                text.clear();
                color.clear();
                color2.clear();

                lastAction.clear();
                lastAction.add(0);

                lines.clear();
                gates.clear();

                refreshRedo();
                hasGraphics = false;
                MyFrame.back.setEnabled(false);
                MyFrame.del.setEnabled(false);

                //Reinicia los puntos
                imageCorner = new Point(0, 25);
                prevPt = new Point(0, 25);
            }
        } else { //Salida en caso de que se llame desde el ActionListener del botón de borrar o desde abrir archivo (Una salida para cada caso)
            if (call == 0) JOptionPane.showMessageDialog(null, Locales.ERR_EDITOR_VACIO, Locales.TITULOS_DIALOGS, JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void paintComponent(Graphics g) { //Método que contiene la creación de líneas y los previews

        //Parte común a todos los if
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2.5f));

        g2d.scale(zoomFactor, zoomFactor);

        prevCounter = counter;
        prevCounter2 = counter2;
        prevCounter3 = counter3;

        while (counter > -1) {
            g.setColor(color.get(counter));
            g.drawLine((int) XPoint.get(counter).getX(), (int) XPoint.get(counter).getY(), (int) YPoint.get(counter).getX(), (int) YPoint.get(counter).getY());
            counter--;
        }
        while (counter2 > -1) {
            SwitchGate(type2.get(counter2));
            icon.paintIcon(this, g, (int) XimageCorner.get(counter2).getX(), (int) XimageCorner.get(counter2).getY());
            counter2--;
        }
        while (counter3 > -1) {
            g.setColor(color2.get(counter3));
            g2d.setFont(new Font("Verdana", Font.BOLD, 14));
            g.drawString(text.get(counter3), (int) YimageCorner.get(counter3).getX(), (int) YimageCorner.get(counter3).getY());
            counter3--;
        }

        if (MyRadioPanel.mode == 0 && (lineSignal == 1 || lineSignal == 2)) {
            counter3 = prevCounter3;
            counter2 = prevCounter2;
            counter = prevCounter;

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g.drawLine((int) (prevPt.getX() / zoomFactor), (int) (prevPt.getY() / zoomFactor),
                    (int) (imageCorner2.getX() / zoomFactor), (int) (imageCorner2.getX() / zoomFactor));
        } else if (MyRadioPanel.mode == 1) { //Preview de las Logical Gates
            counter3 = prevCounter3;
            counter2 = prevCounter2;
            counter = prevCounter;

            SwitchGate(MyFrame.getTipo());

            try {
                if (isInFrame) {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                    boolean isInAnotherGate = false;
                    for (Gate gate : gates) {
                        if (imageCorner2.getX() / zoomFactor + MyOptions.rectification > gate.getGateBounds().getX()
                                && imageCorner2.getX() / zoomFactor - MyOptions.rectification < gate.getGateBounds().getX()) {
                            isInAnotherGate = true;
                            icon.paintIcon(this, g, (int) (gate.getGateBounds().getX()), (int) (imageCorner2.getY() / zoomFactor));
                            break;
                        } else if (imageCorner2.getY() / zoomFactor + MyOptions.rectification > gate.getGateBounds().getY()
                                && imageCorner2.getY() / zoomFactor - MyOptions.rectification < gate.getGateBounds().getY()) {
                            isInAnotherGate = true;
                            icon.paintIcon(this, g, (int) (imageCorner2.getX() / zoomFactor), (int) (gate.getGateBounds().getY()));
                            break;
                        }
                    }
                    if (!isInAnotherGate) {
                        icon.paintIcon(this, g, (int) (imageCorner2.getX() / zoomFactor),(int) (imageCorner2.getY() / zoomFactor));
                    }
                    repintado = true;
                    repaint();
                }
            } catch (NullPointerException e) {repintado = true;}
        } else if (MyRadioPanel.mode == 2) { //Preview de texto. Cambiar "Texto aquí" para modificar el texto de ejemplo
            counter3 = prevCounter3;
            counter2 = prevCounter2;
            counter = prevCounter;

            if (isInFrame) {
                g.setColor(MyFrame.thiscolor);
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g2d.setFont(new Font("Verdana", Font.BOLD, 14));
                g.drawString((int) (imageCorner2.getX() / zoomFactor) + "x " + (int) (imageCorner2.getY() / zoomFactor) + "y", (int) (imageCorner2.getX() / zoomFactor), (int) (imageCorner2.getY() / zoomFactor));
                repaint();
            }
        } else if (repintado) { //Para actualizar el paint
            counter3 = prevCounter3;
            counter2 = prevCounter2;
            counter = prevCounter;
            repintado = false;
        } else if (pointspaint) { //Pintar puntos de las gates
            for (Gate gate : gates) {
                for (int j = 0; j < gate.numOfEntries(); j++) {
                    g.fillOval((int) gate.getEntryPoints().get(j).getX(), (int) gate.getEntryPoints().get(j).getY(),
                            (int) gate.getEntryPoints().get(j).getWidth(), (int) gate.getEntryPoints().get(j).getHeight());
                }
                for (int j = 0; j < gate.numOfExits(); j++) {
                    g.fillOval((int) gate.getExitPoints().get(j).getX(), (int) gate.getExitPoints().get(j).getY(),
                            (int) gate.getExitPoints().get(j).getWidth(), (int) gate.getExitPoints().get(j).getHeight());
                }
            }
            counter3 = prevCounter3;
            counter2 = prevCounter2;
            counter = prevCounter;
            pointspaint = false;
        } else { //Salida por si no se pinta nada nuevo
            counter3 = prevCounter3;
            counter2 = prevCounter2;
            counter = prevCounter;
        }
        g2d.dispose();
    }

    public void paintLine(Graphics g, Point point) { //Método para pintar Líneas
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2.5f));
        prevCounter = counter;
        prevCounter2 = counter2;
        prevCounter3 = counter3;

        while (counter > -1) {
            g.setColor(color.get(counter));
            g.drawLine((int) XPoint.get(counter).getX(), (int) XPoint.get(counter).getY(), (int) YPoint.get(counter).getX(), (int) YPoint.get(counter).getY());
            counter--;
        }
        while (counter2 > -1) {
            SwitchGate(type2.get(counter2));
            icon.paintIcon(this, g, (int) XimageCorner.get(counter2).getX(), (int) XimageCorner.get(counter2).getY());
            counter2--;
        }
        while (counter3 > -1) {
            g.setColor(color2.get(counter3));
            g2d.setFont(new Font("Verdana", Font.BOLD, 14));
            g.drawString(text.get(counter3), (int) YimageCorner.get(counter3).getX(), (int) YimageCorner.get(counter3).getY());
            counter3--;
        }

        counter3 = prevCounter3;
        counter2 = prevCounter2 + 1;
        counter = prevCounter;

        g.drawLine((int) (prevPt.getX() / zoomFactor), (int) (prevPt.getY() / zoomFactor),
                (int) (imageCorner2.getX() / zoomFactor), (int) (imageCorner2.getX() / zoomFactor));
        XPoint.add(new Point((int) (prevPt.getX() / zoomFactor), (int) (prevPt.getY() / zoomFactor)));
        YPoint.add(new Point((int) (imageCorner2.getX() / zoomFactor), (int) (imageCorner2.getX() / zoomFactor)));

        if (!hasGraphics) hasGraphics = true;

        refreshRedo();
    }

    public void paintGate(Graphics g, int type, Point point) { //Método para pintar Logical Gates
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2.5f));
        prevCounter = counter;
        prevCounter2 = counter2;
        prevCounter3 = counter3;

        while (counter > -1) {
            g.setColor(color.get(counter));
            g.drawLine((int) XPoint.get(counter).getX(), (int) XPoint.get(counter).getY(), (int) YPoint.get(counter).getX(), (int) YPoint.get(counter).getY());
            counter--;
        }
        while (counter2 > -1) {
            SwitchGate(type2.get(counter2));
            icon.paintIcon(this, g, (int) XimageCorner.get(counter2).getX(), (int) XimageCorner.get(counter2).getY());
            counter2--;
        }
        while (counter3 > -1) {
            g.setColor(color2.get(counter3));
            g2d.setFont(new Font("Verdana", Font.BOLD, 14));
            g.drawString(text.get(counter3), (int) YimageCorner.get(counter3).getX(), (int) YimageCorner.get(counter3).getY());
            counter3--;
        }

        counter3 = prevCounter3;
        counter2 = prevCounter2 + 1;
        counter = prevCounter;

        SwitchGate(type);

        boolean isInAnotherGate = false;
        for (int i = 0; i < gates.size(); i++) {
            if ((int) (point.getX() / zoomFactor) + MyOptions.rectification > (int) (gates.get(i).getGateBounds().getX())
                    && (int) (point.getX() / zoomFactor) - MyOptions.rectification < (int) (gates.get(i).getGateBounds().getX())) {
                isInAnotherGate = true;
                icon.paintIcon(this, g, (int) (gates.get(i).getGateBounds().getX()),(int) (point.getY() / zoomFactor));
                XimageCorner.add(new Point((int) (gates.get(i).getGateBounds().getX()),(int) (point.getY() / zoomFactor)));
                gates.add(new Gate(new Rectangle2D.Double((int) (gates.get(i).getGateBounds().getX()),
                        (int) (point.getY() / zoomFactor),
                        (int) (icon.getIconWidth() / zoomFactor),
                        (int) (icon.getIconHeight() / zoomFactor)), type));
                break;
            } else if ((int) (point.getY() / zoomFactor) + MyOptions.rectification > (int) (gates.get(i).getGateBounds().getY())
                    && (int) (point.getY() / zoomFactor) - MyOptions.rectification < (int) (gates.get(i).getGateBounds().getY())) {
                isInAnotherGate = true;
                icon.paintIcon(this, g, (int) (point.getX() / zoomFactor),(int) (gates.get(i).getGateBounds().getY()));
                XimageCorner.add(new Point((int) (point.getX() / zoomFactor),(int) (gates.get(i).getGateBounds().getY())));
                gates.add(new Gate(new Rectangle2D.Double((int) (point.getX() / zoomFactor),
                        (int) (gates.get(i).getGateBounds().getY()),
                        (int) (icon.getIconWidth() / zoomFactor),
                        (int) (icon.getIconHeight() / zoomFactor)), type));
                break;
            }
        }
        if (!isInAnotherGate) {
            icon.paintIcon(this, g, (int) (point.getX() / zoomFactor),(int) (point.getY() / zoomFactor));
            XimageCorner.add(new Point((int) (point.getX() / zoomFactor),(int) (point.getY() / zoomFactor)));
            gates.add(new Gate(new Rectangle2D.Double((int) (point.getX() / zoomFactor),
                    (int) (point.getY() / zoomFactor),
                    (int) (icon.getIconWidth() / zoomFactor),
                    (int) (icon.getIconHeight() / zoomFactor)), type));
        }

        type2.add(type);
        lastAction.add(2);

        if (!hasGraphics) hasGraphics = true;

        refreshRedo();
    }

    public void paintText(Graphics g, String texto, Point point) { //Método para pintar texto
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2.5f));

        prevCounter = counter;
        prevCounter2 = counter2;
        prevCounter3 = counter3;

        while (counter > -1) {
            g.setColor(color.get(counter));
            g.drawLine((int) XPoint.get(counter).getX(), (int) XPoint.get(counter).getY(), (int) YPoint.get(counter).getX(), (int) YPoint.get(counter).getY());
            counter--;
        }
        while (counter2 > -1) {
            SwitchGate(type2.get(counter2));
            icon.paintIcon(this, g, (int) XimageCorner.get(counter2).getX(), (int) XimageCorner.get(counter2).getY());
            counter2--;
        }
        while (counter3 > -1) {
            g.setColor(color2.get(counter3));
            g2d.setFont(new Font("Verdana", Font.BOLD, 14));
            g.drawString(text.get(counter3), (int) YimageCorner.get(counter3).getX(), (int) YimageCorner.get(counter3).getY());
            counter3--;
        }

        counter3 = prevCounter3 + 1;
        counter2 = prevCounter2;
        counter = prevCounter;

        g2d.setFont(new Font("Verdana", Font.BOLD, 14));
        g2d.setColor(MyFrame.thiscolor);
        g.drawString(texto, (int) (point.getX() / zoomFactor),(int) (point.getY() / zoomFactor));

        color2.add(MyFrame.thiscolor);
        text.add(texto);
        YimageCorner.add(new Point((int) (point.getX() / zoomFactor),(int) (point.getY() / zoomFactor)));
        lastAction.add(3);

        if (!hasGraphics) hasGraphics = true;

        refreshRedo();
    }

    public void refreshRedo() { //Refresca los valores de memoria del redo
        nextXPoint.clear();
        nextYPoint.clear();
        nextXimageCorner.clear();
        nextYimageCorner.clear();
        nextType2.clear();
        nextText.clear();
        nextColor.clear();
        nextColor2.clear();

        nextGates.clear();
        nextLines.clear();

        nextAction.clear();
        nextAction.add(0);

        MyFrame.redo.setEnabled(false);
        MyFrame.back.setEnabled(true);
        MyFrame.del.setEnabled(true);
    }

    public void KeyAction() { //Acción del back
        MyFrame.redo.setEnabled(true);
        if (lastAction.size() - 2 == 0) MyFrame.back.setEnabled(false);
        switch (lastAction.get(lastAction.size() - 1)) {
            case 1:
                counter--;
                nextXPoint.add(XPoint.get(XPoint.size() - 1));
                nextYPoint.add(YPoint.get(YPoint.size() - 1));
                nextAction.add(lastAction.get(lastAction.size() - 1));
                nextColor.add(color.get(color.size() - 1));
                nextLines.add(lines.get(lines.size() - 1));

                XPoint.remove(XPoint.size() - 1);
                YPoint.remove(YPoint.size() - 1);
                lastAction.remove(lastAction.size() - 1);
                color.remove(color.size() - 1);
                lines.remove(lines.size() - 1);
                break;
            case 2:
                counter2--;
                nextXimageCorner.add(XimageCorner.get(XimageCorner.size() - 1));
                nextType2.add(type2.get(type2.size() - 1));
                nextAction.add(lastAction.get(lastAction.size() - 1));
                nextGates.add(gates.get(gates.size() - 1));

                XimageCorner.remove(XimageCorner.size() - 1);
                type2.remove(type2.size() - 1);
                lastAction.remove(lastAction.size() - 1);
                gates.remove(gates.size() - 1);
                break;
            case 3:
                counter3--;
                nextYimageCorner.add(YimageCorner.get(YimageCorner.size() - 1));
                nextText.add(text.get(text.size() - 1));
                nextAction.add(lastAction.get(lastAction.size() - 1));
                nextColor2.add(color2.get(color2.size() - 1));

                YimageCorner.remove(YimageCorner.size() - 1);
                text.remove(text.size() - 1);
                lastAction.remove(lastAction.size() - 1);
                color2.remove(color2.size() - 1);
                break;
            default:
                if (nextAction.size() - 1 == 0) MyFrame.redo.setEnabled(false);
                JOptionPane.showMessageDialog(null, Locales.ERR_NACC_PREV, Locales.TITULOS_DIALOGS, JOptionPane.WARNING_MESSAGE);
                break;
        }
        repintado = true;
    }

    public void KeyAction2() { //Acción del redo
        MyFrame.back.setEnabled(true);
        switch (nextAction.get(nextAction.size() - 1)) {
            case 1:
                counter++;
                XPoint.add(nextXPoint.get(nextXPoint.size() - 1));
                YPoint.add(nextYPoint.get(nextYPoint.size() - 1));
                lastAction.add(nextAction.get(nextAction.size() - 1));
                color.add(nextColor.get(nextColor.size() - 1));
                lines.add(nextLines.get(nextLines.size() - 1));

                nextXPoint.remove(nextXPoint.size() - 1);
                nextYPoint.remove(nextYPoint.size() - 1);
                nextAction.remove(nextAction.size() - 1);
                nextColor.remove(nextColor.size() - 1);
                nextLines.remove(nextLines.size() - 1);
                break;
            case 2:
                counter2++;
                XimageCorner.add(nextXimageCorner.get(nextXimageCorner.size() - 1));
                type2.add(nextType2.get(nextType2.size() - 1));
                lastAction.add(nextAction.get(nextAction.size() - 1));
                gates.add(nextGates.get(nextGates.size() - 1));

                nextXimageCorner.remove(nextXimageCorner.size() - 1);
                nextType2.remove(nextType2.size() - 1);
                nextAction.remove(nextAction.size() - 1);
                nextGates.remove(nextGates.size() - 1);
                break;
            case 3:
                counter3++;
                YimageCorner.add(nextYimageCorner.get(nextYimageCorner.size() - 1));
                text.add(nextText.get(nextText.size() - 1));
                lastAction.add(nextAction.get(nextAction.size() - 1));
                color2.add(nextColor2.get(nextColor2.size() - 1));

                nextYimageCorner.remove(nextYimageCorner.size() - 1);
                nextText.remove(nextText.size() - 1);
                nextAction.remove(nextAction.size() - 1);
                nextColor2.remove(nextColor2.size() - 1);
                break;
            default:
                MyFrame.back.setEnabled(false);
                JOptionPane.showMessageDialog(null, Locales.ERR_NACC_POSI, Locales.TITULOS_DIALOGS, JOptionPane.WARNING_MESSAGE);
                break;
        }
        if (nextAction.size() == 1) {
            MyFrame.redo.setEnabled(false);
        }
        repintado = true;
    }

    public void descargarArchivo() { //Creación del archivo
        if (counter == -1 && counter2 == -1 && counter3 == -1) {
            JOptionPane.showMessageDialog(null, Locales.INF_SCHEMPTY, Locales.TITULOS_DIALOGS, JOptionPane.WARNING_MESSAGE);
        } else {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(getUserDesktop()));
            int result = fileChooser.showSaveDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileChooser.getSelectedFile() + ".txt"))) {
                    int j = 0, q = 0, k = 0;
                    bufferedWriter.write("  <" + counter + "$" + counter2 + "$" + counter3 + ">");
                    for (Integer integer : lastAction) {
                        switch (integer) {
                            case 1:
                                bufferedWriter.write("<" + integer + "/" + (int) XPoint.get(j).getX() * zoomFactor + "$" + (int) XPoint.get(j).getY() * zoomFactor + "/" + (int) YPoint.get(j).getX() * zoomFactor + "$" + (int) YPoint.get(j).getY() * zoomFactor + "/" + color.get(j).getRed() + "$" + color.get(j).getGreen() + "$" + color.get(j).getBlue() + ">");
                                j++;
                                break;
                            case 2:
                                bufferedWriter.write("<" + integer + "/" + (int) XimageCorner.get(q).getX() * zoomFactor + "$" + (int) XimageCorner.get(q).getY() * zoomFactor + "/" + type2.get(q) + ">");
                                q++;
                                break;
                            case 3:
                                bufferedWriter.write("<" + integer + "/" + (int) YimageCorner.get(k).getX() * zoomFactor + "$" + (int) YimageCorner.get(k).getY() * zoomFactor + "/" + text.get(k) + "/" + color2.get(k).getRed() + "$" + color2.get(k).getGreen() + "$" + color2.get(k).getBlue() + ">");
                                k++;
                                break;
                        }
                        bufferedWriter.newLine();
                    }
                    bufferedWriter.close();
                    JOptionPane.showMessageDialog(null, Locales.SUCC_ARCH_CREAD, Locales.TITULOS_DIALOGS, JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, Locales.ERR_ARCH_CREAD, Locales.TITULOS_DIALOGS, JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, Locales.ERR_OP_CANCEL, Locales.TITULOS_DIALOGS, JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    @SuppressWarnings("StringConcatenationInLoop")
    public void leerArchivo() { //Lector de archivos
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("C:\\Users\\david\\Desktop"));
        int result = fileChooser.showSaveDialog(null);
        clear(1);

        if (result == JFileChooser.APPROVE_OPTION) {
            if (fileChooser.getSelectedFile().getAbsolutePath().endsWith(".txt")) {
                try {
                    Scanner fileReader = new Scanner(fileChooser.getSelectedFile());
                    ArrayList<String> lineas = new ArrayList<>();

                    if (confirmed) { //Comprueba si el usuario ha confirmado la sobrescritura
                        while(fileReader.hasNextLine()) lineas.add(fileReader.nextLine());
                        String contador, contador2, contador3;
                        int j, t;
                        for (int i = 0; i < lineas.size(); i++) {
                            j = 0;
                            if (i == 0) { //Crear contadores
                                contador = "";
                                while (lineas.get(i).charAt(j) != 60) {j++;} j++;
                                while (lineas.get(i).charAt(j) != 36) {contador += lineas.get(i).charAt(j); j++;} counter = Integer.parseInt(contador); contador = ""; j++;
                                while (lineas.get(i).charAt(j) != 36) {contador += lineas.get(i).charAt(j); j++;} counter2 = Integer.parseInt(contador); contador = ""; j++;
                                while (lineas.get(i).charAt(j) != 62) {contador += lineas.get(i).charAt(j); j++;} counter3 = Integer.parseInt(contador);
                            } else { //Crear objetos
                                while (lineas.get(i).charAt(j) != 60) {j++;} j++;
                                t = Integer.parseInt(String.valueOf(lineas.get(i).charAt(j))); j += 2;
                                lastAction.add(t);
                                switch (t) {
                                    case 1: contador = ""; contador2 = "";
                                        while (lineas.get(i).charAt(j) != 36) {
                                            contador += lineas.get(i).charAt(j); j++;
                                        } j++;
                                        while (lineas.get(i).charAt(j) != 47) {
                                            contador2 += lineas.get(i).charAt(j); j++;
                                        } j++; XPoint.add(new Point((int) (Integer.parseInt(contador) / zoomFactor), (int) (Integer.parseInt(contador2) / zoomFactor)));
                                        contador = ""; contador2 = "";
                                        while (lineas.get(i).charAt(j) != 36) {
                                            contador += lineas.get(i).charAt(j); j++;
                                        } j++;
                                        while (lineas.get(i).charAt(j) != 47) {
                                            contador2 += lineas.get(i).charAt(j); j++;
                                        } j++; YPoint.add(new Point((int) (Integer.parseInt(contador) / zoomFactor), (int) (Integer.parseInt(contador2) / zoomFactor)));
                                        for (int h = 0; i < XPoint.size(); i++) {
                                            lines.add(new Line2D.Double((int) (XPoint.get(h).getX() / zoomFactor), (int) (XPoint.get(h).getY() / zoomFactor), (int) (YPoint.get(h).getX() / zoomFactor), (int) (YPoint.get(h).getY() / zoomFactor)));
                                        }
                                        contador = ""; contador2 = ""; contador3 = "";
                                        while (lineas.get(i).charAt(j) != 36) {
                                            contador += lineas.get(i).charAt(j); j++;
                                        } j++;
                                        while (lineas.get(i).charAt(j) != 36) {
                                            contador2 += lineas.get(i).charAt(j); j++;
                                        } j++;
                                        while (lineas.get(i).charAt(j) != 62) {
                                            contador3 += lineas.get(i).charAt(j); j++;
                                        } color.add(new Color(Integer.parseInt(contador), Integer.parseInt(contador2), Integer.parseInt(contador3))); break;
                                    case 2: contador = ""; contador2 = ""; contador3 = "";
                                        while (lineas.get(i).charAt(j) != 36) {
                                            contador += lineas.get(i).charAt(j); j++;
                                        } j++;
                                        while (lineas.get(i).charAt(j) != 47) {
                                            contador2 += lineas.get(i).charAt(j); j++;
                                        } j++; XimageCorner.add(new Point((int) (Integer.parseInt(contador) / zoomFactor), (int) (Integer.parseInt(contador2) / zoomFactor)));
                                        while (lineas.get(i).charAt(j) != 62) {
                                            contador3 += lineas.get(i).charAt(j); j++;
                                        } type2.add(Integer.parseInt(contador3));
                                        SwitchGate(Integer.parseInt(contador3));
                                        Point corner = new Point((int) (Integer.parseInt(contador) / zoomFactor), (int) (Integer.parseInt(contador2) / zoomFactor));
                                        gates.add(new Gate(new Rectangle2D.Double(
                                                corner.getX(), corner.getY(),
                                                (int) (icon.getIconWidth() / zoomFactor),
                                                (int) (icon.getIconHeight() / zoomFactor)), Integer.parseInt(contador3)));
                                        if (Integer.parseInt(contador3) == 7 || Integer.parseInt(contador3) == 8) {
                                            gates.get(gates.size() - 1).addExit(new Rectangle2D.Double((int) ((corner.getX()) + ((icon.getIconWidth()) / 2)) - 5, (int) (corner.getY()) - 12,10 ,10));
                                        } else if (Integer.parseInt(contador3) == 6) {
                                            gates.get(gates.size() - 1).addEntry(new Rectangle2D.Double((int) (corner.getX()) - 7, (int) ((corner.getY()) + ((icon.getIconHeight()) / 2)) - 4,10 ,10));
                                            gates.get(gates.size() - 1).addExit(new Rectangle2D.Double((int) ((corner.getX()) - 10 + (icon.getIconWidth())), (int) ((corner.getY()) + ((icon.getIconHeight()) / 2)) - 4,10 ,10));
                                        } else {
                                            gates.get(gates.size() - 1).addEntry(new Rectangle2D.Double((int) (corner.getX()) - 7, (int) ((corner.getY()) + ((icon.getIconHeight()) / 3)) - 7,10 ,10));
                                            gates.get(gates.size() - 1).addEntry(new Rectangle2D.Double((int) (corner.getX()) - 7, (int) ((corner.getY()) + ((2 * (icon.getIconHeight())) / 3)) - 4,10 ,10));
                                            gates.get(gates.size() - 1).addExit(new Rectangle2D.Double((int) ((corner.getX()) - 10 + (icon.getIconWidth())), (int) ((corner.getY()) + ((icon.getIconHeight()) / 2)) - 4,10 ,10));
                                        }
                                        break;
                                    case 3: contador = ""; contador2 = ""; contador3 = "";
                                        while (lineas.get(i).charAt(j) != 36) {
                                            contador += lineas.get(i).charAt(j); j++;
                                        } j++;
                                        while (lineas.get(i).charAt(j) != 47) {
                                            contador2 += lineas.get(i).charAt(j); j++;
                                        } j++; YimageCorner.add(new Point((int) (Integer.parseInt(contador) / zoomFactor), (int) (Integer.parseInt(contador2) / zoomFactor)));
                                        while (lineas.get(i).charAt(j) != 47) {
                                            contador3 += lineas.get(i).charAt(j); j++;
                                        } j++; text.add(contador3);
                                        contador = ""; contador2 = ""; contador3 = "";
                                        while (lineas.get(i).charAt(j) != 36) {
                                            contador += lineas.get(i).charAt(j); j++;
                                        } j++;
                                        while (lineas.get(i).charAt(j) != 36) {
                                            contador2 += lineas.get(i).charAt(j); j++;
                                        } j++;
                                        while (lineas.get(i).charAt(j) != 62) {
                                            contador3 += lineas.get(i).charAt(j); j++;
                                        } color2.add(new Color(Integer.parseInt(contador), Integer.parseInt(contador2), Integer.parseInt(contador3))); break;
                                }
                            }
                        }
                        repintado = true;
                        hasGraphics = true;
                        MyFrame.back.setEnabled(true);
                        MyFrame.del.setEnabled(true);
                        repaint();
                        JOptionPane.showMessageDialog(null, Locales.SUCC_LEER_ARCH, Locales.TITULOS_DIALOGS, JOptionPane.INFORMATION_MESSAGE);
                    } else JOptionPane.showMessageDialog(null, Locales.ERR_OP_CANCEL, Locales.TITULOS_DIALOGS, JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, Locales.ERR_LEER_ARCH, Locales.TITULOS_DIALOGS, JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, Locales.ERR_EXT_NVALID, Locales.TITULOS_DIALOGS, JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, Locales.ERR_OP_CANCEL, Locales.TITULOS_DIALOGS, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void repintaOptions(ArrayList<Integer> ultimacion, ArrayList<Point> XPunto,  ArrayList<Point> YPunto,
                               ArrayList<Point> XesquinaImagen, ArrayList<Point> YesquinaImagen, ArrayList<String> texto, ArrayList<Integer> tipo,
                               ArrayList<Color> Colorc, ArrayList<Color> Colorc2) { //Leer la memoria al rehacer el frame
        XPoint = XPunto; YPoint = YPunto; type2 = tipo;
        XimageCorner = XesquinaImagen; YimageCorner = YesquinaImagen; text = texto;
        lastAction = ultimacion; color = Colorc; color2 = Colorc2;

        repintado = true; //Llama al repintado en paintComponent
    }

    public void repintaNextOptions(ArrayList<Integer> siguienteacion, ArrayList<Point> nextXPunto,  ArrayList<Point> nextYPunto,
                                   ArrayList<Point> nextXesquinaImagen, ArrayList<Point> nextYesquinaImagen, ArrayList<String> nextTexto, ArrayList<Integer> nextTipo,
                                   ArrayList<Color> nextColorc, ArrayList<Color> nextColorc2) { //Leer memoria de las acciones siguientes al rehacer el frame
        nextXPoint = nextXPunto; nextYPoint = nextYPunto; nextType2 = nextTipo;
        nextXimageCorner = nextXesquinaImagen; nextYimageCorner = nextYesquinaImagen; nextText = nextTexto;
        nextAction = siguienteacion; nextColor = nextColorc; nextColor2 = nextColorc2;
    }

    public void repintaUniqueOptions(int contador, int contador2, int contador3, boolean backmem, boolean redomem, boolean delmem, boolean hasGraphicsmem) { //Leer memoria de los valores únicos al rehacer el frame
        counter = contador; counter2 = contador2; counter3 = contador3;
        MyFrame.back.setEnabled(backmem); MyFrame.redo.setEnabled(redomem); MyFrame.del.setEnabled(delmem);
        hasGraphics = hasGraphicsmem;
    }

    public void SwitchGate(int pene) { //Detecta que gate está seleccionada para pintarla
        switch (pene) {
            case 0:
                icon = new ImageIcon("src/media/gates/AND.png");
                break;
            case 1:
                icon = new ImageIcon("src/media/gates/NAND.png");
                break;
            case 2:
                icon = new ImageIcon("src/media/gates/OR.png");
                break;
            case 3:
                icon = new ImageIcon("src/media/gates/NOR.png");
                break;
            case 4:
                icon = new ImageIcon("src/media/gates/XOR.png");
                break;
            case 5:
                icon = new ImageIcon("src/media/gates/XNOR.png");
                break;
            case 6:
                icon = new ImageIcon("src/media/gates/NOT.png");
                break;
            case 7:
                icon = new ImageIcon("src/media/gates/OFF.png");
                break;
            case 8:
                icon = new ImageIcon("src/media/gates/LEDOFF.png");
                break;
            case 9:
                icon = new ImageIcon("src/media/gates/ON.png");
                break;
            case 10:
                icon = new ImageIcon("src/media/gates/LEDON.png");
                break;
            //Añadir nuevos elementos aquí (procurar que sean todos del mismo tamaño)
            default:
                JOptionPane.showMessageDialog(null, Locales.ERR_CODIGO, "GateCraft: Error", JOptionPane.ERROR_MESSAGE);
                MyRadioPanel.setMode(0);
                break;
        }
    }

    public String getUserDesktop() { //Buscar el directorio del escritorio del usuario
        String directorioUsuario = System.getProperty("user.home");
        String separadorDeArchivo = System.getProperty("file.separator");

        // Componer la ruta completa al escritorio
        return directorioUsuario + separadorDeArchivo + "Desktop";
    }

    public boolean isTextValid(String textox) { //Detecta si la entrada de texto es válida (Para evitar problemas con la lectura)
        int pitoloco = 0;
        for (int i = 0; i < textox.length(); i++) {
            if (textox.charAt(i) >= 48 && textox.charAt(i) <= 57) {
            } else if (textox.charAt(i) >= 65 && textox.charAt(i) <= 90) {
            } else if (textox.charAt(i) >= 97 && textox.charAt(i) <= 122) {
            } else if (textox.charAt(i) == 'Ç' || textox.charAt(i) == 'ç' || textox.charAt(i) == 'Ñ'
                    || textox.charAt(i) == 'ñ' || textox.charAt(i) == ' ') {} else {
                pitoloco++;
            }
        }
        return pitoloco == 0;
    }

    public void runCircuito() {
        ArrayList<Gate> parentGates = new ArrayList<>();
        for (int i = 0; i < type2.size(); i++) { //Busca los interruptores
            if (type2.get(i) == 7 || type2.get(i) == 9) {
                gates.get(i).setType(type2.get(i));
                parentGates.add(gates.get(i));
            }
        }

        ArrayList<Cables> cables = new ArrayList<>();
        for (int i = 0; i < parentGates.size(); i++) {
            for (int j = 0; j < lines.size(); j++) { //Busca líneas conectadas
                if (parentGates.get(i).getExitPoints(0).intersectsLine(lines.get(j))) {
                    cables.add(new Cables(lines.get(j), parentGates.get(i).getType() == 9, parentGates.get(i)));
                    cables.get(cables.size() - 1).seguirCable(); //Las sigue
                }
            }
        }

        boolean hasNext = true; //cambiar generacion de líneas
        ArrayList<Cables> cables2 = new ArrayList<>();
        int iterations = 0, results = 0;
        while (hasNext) {
            if (iterations > 0) {
                for (int i = 1; i <= results; i++) {
                    cables2.add(cables.get(cables.size() - i));
                }
                cables.clear(); cables = cables2; cables2.clear();
                cables.forEach(Cables::seguirCable);
                results = 0;
            }
            for (int i = 0; i < gates.size(); i++) { //Busca puertas conectadas (AÑADIR SALIDA SI NO HAY + IMPLEMENTAR BUCLE)
                if (type2.get(i) == 7 || type2.get(i) == 9) continue; //Comprueba que no es un interruptor
                for (int j = 0; j < cables.size(); j++) {
                    for (int k = 0; k < gates.get(i).numOfEntries(); k++) {
                        if (gates.get(i).getEntryPoints(k).intersectsLine(cables.get(j).getCable())) {
                            if (gates.get(i).numOfEntries() == 1) {
                                if (type2.get(i) == 6) {
                                    hasNext = false;
                                    for (int l = 0; l < lines.size(); l++) {
                                        if (gates.get(i).getExitPoints(0).intersectsLine(lines.get(l))) {
                                            hasNext = true;
                                            results++;
                                            cables.add(new Cables(lines.get(l), !cables.get(j).getValue(), gates.get(i)));
                                        }
                                    }
                                } else {
                                    hasNext = false;
                                    for (int l = 0; l < lines.size(); l++) {
                                        if (gates.get(i).getExitPoints(0).intersectsLine(lines.get(l))) {
                                            hasNext = true;
                                            results++;
                                            cables.add(new Cables(lines.get(l), cables.get(j).getValue(), gates.get(i)));
                                        }
                                    }
                                    if (cables.get(j).getValue()) {
                                        type2.set(i, 10);
                                        gates.get(i).setType(10);
                                    } else {
                                        type2.set(i, 8);
                                        gates.get(i).setType(8);
                                    }
                                    repintado = true;
                                    repaint();
                                }
                            } else if (gates.get(i).numOfEntries() == 2) {
                                gates.get(i).addValues(cables.get(j).getValue());
                                if (gates.get(i).getValuesLength() == 2) {
                                    hasNext = false;
                                    for (int l = 0; l < lines.size(); l++) {
                                        if (gates.get(i).getExitPoints(0).intersectsLine(lines.get(l))) {
                                            hasNext = true;
                                            results++;
                                            cables.add(new Cables(lines.get(l), switch (type2.get(i)) {
                                                case 0 -> gates.get(i).getValues(0) && gates.get(i).getValues(1);
                                                case 1 -> !(gates.get(i).getValues(0) && gates.get(i).getValues(1));
                                                case 2 -> gates.get(i).getValues(0) || gates.get(i).getValues(1);
                                                case 3 -> !(gates.get(i).getValues(0) || gates.get(i).getValues(1));
                                                case 4 -> (gates.get(i).getValues(0) || gates.get(i).getValues(1))
                                                        && gates.get(i).getValues(0) != gates.get(i).getValues(1);
                                                case 5 -> !((gates.get(i).getValues(0) || gates.get(i).getValues(1))
                                                        && gates.get(i).getValues(0) != gates.get(i).getValues(1));
                                                default -> false;
                                            }, gates.get(i)));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            iterations++;
        }
    }

    private class ClickListener extends MouseAdapter { //Detectores de clic del ratón
        @Override
        public void mousePressed(MouseEvent e) {}

        @Override
        public void mouseReleased(MouseEvent e) {}

        @Override
        public void mouseClicked(MouseEvent e) { //Creación de Gates y cuadros de Texto
            if (MyRadioPanel.mode == 1) { //Comprueba modo gate y la pinta
                paintGate(getGraphics(), MyFrame.tipo, new Point(e.getPoint()));
                repaint();
            } else if (MyRadioPanel.mode == 2) { //Comprueba modo texto y lo pinta
                String textoloco;
                textoloco = JOptionPane.showInputDialog(null, Locales.DIALOG_TEXT, Locales.TITULOS_DIALOGS, JOptionPane.INFORMATION_MESSAGE);
                if (textoloco != null && !textoloco.isEmpty()) { //Comprueba que no se haya salido del input y que no esté vacío
                    if (isTextValid(textoloco)) { //Comprueba si el texto es válido
                        paintText(getGraphics(), textoloco, e.getPoint());
                        repaint();
                    } else {
                        JOptionPane.showMessageDialog(null, Locales.ERR_INV_CHAR, Locales.TITULOS_DIALOGS , JOptionPane.WARNING_MESSAGE);
                    }
                }
            } else if (MyRadioPanel.mode == -1) { //Método de cambiar de posición el interruptor
                for (int k = 0; k < gates.size(); k++) {
                    if (gates.get(k).getGateBounds().contains(e.getX() / zoomFactor, e.getY() / zoomFactor)) {
                        MyFrame.clip.setMicrosecondPosition(0);
                        MyFrame.clip.start();
                        if (type2.get(k) == 7) {
                            type2.set(k, 9);
                        } else if (type2.get(k) == 9) {
                            type2.set(k, 7);
                        }
                        repaint();
                        break;
                    }
                }
            } else if (MyRadioPanel.mode == 0) {
                if (lineSignal == 0 || lineSignal == 1) {
                    if (lineSignal == 0) {
                        bucleexterno: for (int i = 0; i < gates.size(); i++) {
                            for (int j = 0; j < gates.get(i).getEntryPoints().size(); j++) {
                                if ((e.getX() / zoomFactor + 20 > gates.get(i).getEntryPoints(j).getCenterX()
                                        && e.getX() / zoomFactor - 20 < gates.get(i).getEntryPoints(j).getCenterX()) ||
                                        (e.getY() / zoomFactor + 20 > gates.get(i).getEntryPoints(j).getCenterY()
                                                && e.getY() / zoomFactor - 20 < gates.get(i).getEntryPoints(j).getCenterY())) {
                                    prevPt = new Point((int) gates.get(i).getEntryPoints(j).getCenterX(),
                                            (int) gates.get(i).getEntryPoints(j).getCenterY());
                                    lineSignal = 2;
                                    break bucleexterno;
                                }
                            }
                            for (int j = 0; j < gates.get(i).getExitPoints().size(); j++) {
                                if ((e.getX() / zoomFactor + 20 > gates.get(i).getExitPoints(j).getCenterX()
                                        && e.getX() / zoomFactor - 20 < gates.get(i).getExitPoints(j).getCenterX()) ||
                                        (e.getY() / zoomFactor + 20 > gates.get(i).getExitPoints(j).getCenterY()
                                                && e.getY() / zoomFactor - 20 < gates.get(i).getExitPoints(j).getCenterY())) {
                                    prevPt = new Point((int) gates.get(i).getExitPoints(j).getCenterX(),
                                            (int) gates.get(i).getExitPoints(j).getCenterY());
                                    lineSignal = 2;
                                    break bucleexterno;
                                }
                            }
                        }
                    } else {
                        prevPt = new Point((int) (e.getX() / zoomFactor), (int) (e.getY() / zoomFactor));
                    }
                } else {
                    lineSignal = 1;
                    bucleexterno2: for (int i = 0; i < gates.size(); i++) {
                        for (int j = 0; j < gates.get(i).getEntryPoints().size(); j++) {
                            if ((e.getX() / zoomFactor + 20 > gates.get(i).getEntryPoints(j).getCenterX()
                                    && e.getX() / zoomFactor - 20 < gates.get(i).getEntryPoints(j).getCenterX()) ||
                                    (e.getY() / zoomFactor + 20 > gates.get(i).getEntryPoints(j).getCenterY()
                                    && e.getY() / zoomFactor - 20 < gates.get(i).getEntryPoints(j).getCenterY())) {
                                paintLine(getGraphics(), new Point((int) gates.get(i).getEntryPoints(j).getCenterX(),
                                        (int) gates.get(i).getEntryPoints(j).getCenterY()));
                                lineSignal = 0;
                                break bucleexterno2;
                            }
                        }
                        for (int j = 0; j < gates.get(i).getExitPoints().size(); j++) {
                            if ((e.getX() / zoomFactor + 20 > gates.get(i).getExitPoints(j).getCenterX()
                                    && e.getX() / zoomFactor - 20 < gates.get(i).getExitPoints(j).getCenterX()) ||
                                    (e.getY() / zoomFactor + 20 > gates.get(i).getExitPoints(j).getCenterY()
                                    && e.getY() / zoomFactor - 20 < gates.get(i).getExitPoints(j).getCenterY())) {
                                paintLine(getGraphics(), new Point((int) gates.get(i).getExitPoints(j).getCenterX(),
                                        (int) gates.get(i).getExitPoints(j).getCenterY()));
                                lineSignal = 0;
                                break bucleexterno2;
                            }
                        }
                    }
                    if (lineSignal == 1) paintLine(getGraphics(), e.getPoint());
                }
            }
        }

        //Comprobación de que el mouse está en el panel para evitar problemas con los previews

        @Override
        public void mouseEntered(MouseEvent e) { //Detecta cuando el mouse entra en el panel
            isInFrame = true;
            switch (MyRadioPanel.mode) {
                case -1: setCursor(new Cursor(Cursor.HAND_CURSOR)); break;
                case 0: setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR)); break;
                case 1: setCursor(new Cursor(Cursor.MOVE_CURSOR)); break;
                case 2: setCursor(new Cursor(Cursor.TEXT_CURSOR)); break;
                default: JOptionPane.showMessageDialog(null, Locales.ERR_CODIGO, Locales.TITULOS_DIALOGS, JOptionPane.ERROR_MESSAGE); break;
            }
        }

        @Override
        public void mouseExited(MouseEvent e) { //Detecta cuando el mouse sale del panel
            isInFrame = false;
        }
    }

    private class MotionListener implements MouseMotionListener { //Detectores de movimiento para los previews
        @Override
        public void mouseDragged(MouseEvent e) {
            imageCorner2.setLocation(e.getPoint());
            repaint();
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (MyRadioPanel.mode == 1 || MyRadioPanel.mode == 2 || MyRadioPanel.mode == 0) { //Comprueba para generar el preview
                imageCorner2.setLocation(new Point((int) (e.getX() / zoomFactor), (int) (e.getY() / zoomFactor)));
                repaint();
            }
        }
    }
}