import java.awt.geom.Line2D;

public class Cables {
    private Line2D cableInicial;
    private Gate parent;
    private boolean valor;

    public Cables(Line2D cableInicial, boolean valor, Gate parent) {
        this.cableInicial = cableInicial;
        this.valor = valor;
        this.parent = parent;
    }

    public void seguirCable() {
        for (int i = 0; i < MyPanel.lines.size(); i++) {
            if ((!cableInicial.equals(MyPanel.lines.get(i)) && cableInicial.intersectsLine(MyPanel.lines.get(i)))
                || (cableInicial.getX2() + 5 > MyPanel.lines.get(i).getX1() && cableInicial.getX2() - 5 < MyPanel.lines.get(i).getX1())
                 && cableInicial.getY2() + 5 > MyPanel.lines.get(i).getY1() && cableInicial.getY2() - 5 < MyPanel.lines.get(i).getY1()) {
                    this.cableInicial = new Line2D.Double((int) (MyPanel.lines.get(i).getX1()), (int) (MyPanel.lines.get(i).getY1()),
                        (int) (MyPanel.lines.get(i).getX2()), (int) (MyPanel.lines.get(i).getY2()));
            }
        }
    }

    public boolean getValue() {return valor;}
    public Gate getParent() {return parent;}
    public Line2D getCable() {return this.cableInicial;}
}
