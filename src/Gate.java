import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Gate {
    private int type;
    private Rectangle2D gateBounds;
    private ArrayList<Rectangle2D> entryPoints, exitPoints;
    private ArrayList<Boolean> values = new ArrayList<>();

    public Gate(Rectangle2D gateBounds, int type, ArrayList<Rectangle2D> entryPoints, ArrayList<Rectangle2D> exitPoints) {
        this.type = type;
        this.gateBounds = gateBounds;
        this.entryPoints = entryPoints;
        this.exitPoints = exitPoints;
        setDefaultBounds();
    }

    public Gate(Rectangle2D gateBounds, int type) {
        this.type = type;
        this.gateBounds = gateBounds;
        this.entryPoints = new ArrayList<>();
        this.exitPoints = new ArrayList<>();
        setDefaultBounds();
    }

    public void addValues(boolean b) {values.add(b);}
    public ArrayList<Boolean> getValues() {return values;}
    public boolean getValues(int pos) {return values.get(pos);}
    public int getValuesLength() {return values.size();}
    public void clearValues() {values.clear();}

    public void addEntry(Rectangle2D entry) {entryPoints.add(entry);}
    public void addExit(Rectangle2D exit) {exitPoints.add(exit);}

    public int numOfEntries() {return entryPoints.size();}
    public int numOfExits() {return exitPoints.size();}
    public int numOfPoints() {return (entryPoints.size() + exitPoints.size());}

    public Rectangle2D getGateBounds() {return gateBounds;}
    public ArrayList<Rectangle2D> getEntryPoints() {return entryPoints;}
    public ArrayList<Rectangle2D> getExitPoints() {return exitPoints;}
    public ArrayList<Rectangle2D> getAllPoints() {
        ArrayList<Rectangle2D> result = entryPoints; result.addAll(exitPoints);
        return result;
    }

    public Rectangle2D getEntryPoints(int pos) {return entryPoints.get(pos);}
    public Rectangle2D getExitPoints(int pos) {return exitPoints.get(pos);}

    public int getType() {return type;}
    public void setType(int type) {this.type = type;}

    public void setDefaultBounds() {
        if (type == 7 || type == 9) {
            addExit(new Rectangle2D.Double((int) (gateBounds.getX() + gateBounds.getWidth() / 2) - 6, (int) gateBounds.getY() - 4,10 ,10));
        } else if (type == 6 || type == 8 || type == 10) {
            addEntry(new Rectangle2D.Double(gateBounds.getX() - 5, gateBounds.getY() + (int) (gateBounds.getHeight() / 2) - 7,10 ,10));
            addExit(new Rectangle2D.Double(gateBounds.getX() + gateBounds.getWidth() - 15, gateBounds.getY() + (int) (gateBounds.getHeight() / 2) - 7,10 ,10));
        } else {
            addEntry(new Rectangle2D.Double(gateBounds.getX() - 5, gateBounds.getY() + (1 * gateBounds.getHeight() / 3) - 7,10 ,10));
            addEntry(new Rectangle2D.Double(gateBounds.getX() - 5, gateBounds.getY() + (2 * gateBounds.getHeight() / 3) - 7,10 ,10));
            addExit(new Rectangle2D.Double(gateBounds.getX() + gateBounds.getWidth() - 15, gateBounds.getY() + (int) (gateBounds.getHeight() / 2) - 8,10 ,10));
        }
    }
}
