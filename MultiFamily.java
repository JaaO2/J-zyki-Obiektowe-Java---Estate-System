package estateSystem;

public class MultiFamily extends Estate {

    private int FlatCount;
    private boolean isParking;
    private boolean isGarage;

    MultiFamily (String[] name, float cost, int roomCount, float area, int flatCount, boolean isParking, boolean isGarage) throws EstateException {
        super(name, cost, roomCount, area);

        if (flatCount < 1) {
            throw new EstateException(0xE7);
        }

        this.FlatCount = flatCount;
        this.isParking = isParking;
        this.isGarage = isGarage;
    }

    MultiFamily (int id, String [] name, float cost, int roomCount, float area, int agent, int flatCount, boolean isParking, boolean isGarage) {
        super(id, name, cost, roomCount,area, agent);
        this.FlatCount = flatCount;
        this.isParking = isParking;
        this.isGarage = isGarage;
    }

    public void setData(String[] name, String cost, String roomCount, String area, String flatCount, String isParking, String isGarage) throws EstateException {
        super.setData(name, cost, roomCount, area);

        if (!flatCount.isEmpty()) {
            int parsedFlatCount = Integer.parseInt(flatCount);
            if (parsedFlatCount < 1) {
                throw new EstateException(0xE7);
            }

            this.FlatCount = parsedFlatCount;
        }

        if (!isParking.isEmpty()) {
            this.isParking = Integer.parseInt(isParking) == 1;
        }

        if (!isGarage.isEmpty()) {
            this.isGarage = Integer.parseInt(isGarage) == 1;
        }
    }

    public int getFlatCount () {
        return this.FlatCount;
    }

    public boolean getIsParking() {
        return this.isParking;
    }

    public boolean getIsGarage () {
        return this.isGarage;
    }
}
