package estateSystem;

public class Flat extends Estate {
	
	private boolean isBalcony;
	private boolean isParking;
	private boolean isCellar;
	
	Flat(String[] name, float cost, int roomCount, float area, boolean isBalcony, boolean isParking, boolean isCellar) throws EstateException {
		super(name, cost, roomCount, area);
		this.isBalcony = isBalcony;
		this.isParking = isParking;
		this.isCellar = isCellar;
	}

	Flat(int id, String[] name, float cost, int roomCount, float area, int agent, boolean isBalcony, boolean isParking, boolean isCellar) {
		super(id, name, cost, roomCount, area, agent);
		this.isBalcony = isBalcony;
		this.isParking = isParking;
		this.isCellar = isCellar;
	}

	public void setData(String[] name, String cost, String roomCount, String area, String isBalcony, String isParking, String isCellar) throws EstateException {
		super.setData(name, cost, roomCount, area);

		if (!isBalcony.isEmpty()) {
			this.isBalcony = Integer.parseInt(isBalcony) == 1;
		}

		if (!isParking.isEmpty()) {
			this.isParking = Integer.parseInt(isParking) == 1;
		}

		if (!isCellar.isEmpty()) {
			this.isCellar = Integer.parseInt(isCellar) == 1;
		}
	}

	public boolean getIsBalcony () {
		return this.isBalcony;
	}

	public boolean getIsParking () {
		return this.isParking;
	}

	public boolean getIsCellar () {
		return this.isCellar;
	}

}
