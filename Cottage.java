package estateSystem;

public class Cottage extends Estate {
    private boolean isGarden;
    private boolean isFireplace;

    Cottage (String[] name, float cost, int roomCount, float area, boolean isGarden, boolean isFireplace) throws EstateException {
        super(name, cost, roomCount, area);
        this.isGarden = isGarden;
        this.isFireplace = isFireplace;
    }

    Cottage (int id, String[] name, float cost, int roomCount, float area, int agent, boolean isGarden, boolean isFireplace)  {
        super(id, name, cost, roomCount, area, agent);
        this.isGarden = isGarden;
        this.isFireplace = isFireplace;
    }

    public void setData(String[] name, String cost, String roomCount, String area, String isGarden, String isFireplace) throws EstateException {
        super.setData(name, cost, roomCount, area);

        if (!isGarden.isEmpty()) {
            this.isGarden = (Integer.parseInt(isGarden) == 1);
        }

        if (!isFireplace.isEmpty()) {
            this.isFireplace = (Integer.parseInt(isFireplace) == 1);
        }

    }

    public boolean getIsGarden() {
        return this.isGarden;
    }

    public boolean getIsFireplace () {
        return this.isFireplace;
    }

}
