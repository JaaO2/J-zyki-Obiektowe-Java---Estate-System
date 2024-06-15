package estateSystem;

import java.io.*;
import java.lang.*;
import java.util.*;

public abstract class Estate {
	
	private static Estate[] estateBase = {};
	
	private final int id;
	private String[] name;
	private float cost;
	private int agent;
	private int RoomCount;
	private float area;
	
	
	public Estate(String[] name, float cost, int roomCount, float area) throws EstateException {

		if (name == null) {
			throw new EstateException(0xE1);
		}

		String joinName = String.join(" ", name);

		if (joinName.length() < 5) {
			throw new EstateException(0xE2);
		}

		if (joinName.length() > 45) {
			throw new EstateException(0xE3);
		}

		if (cost < 1500) {
			throw new EstateException(0xE4);
		}

		if (roomCount < 1) {
			throw new EstateException(0xE5);
		}

		if (area < 25) {
			throw new EstateException(0xE6);
		}

		this.name = name;
		this.cost = cost;
		this.RoomCount = roomCount;
		this.area = area;
		this.agent = Session.getCurrent().getUser().getID();

		this.id = (Estate.estateBase.length > 0) ? Estate.estateBase[Estate.estateBase.length-1].id + 1 : 1;
		Estate[] newEstateBase =  Arrays.copyOf(Estate.estateBase, Estate.estateBase.length +1);
		newEstateBase[Estate.estateBase.length] = this;
		Estate.estateBase = newEstateBase;
	}

	protected Estate (int id, String [] name, float cost, int roomCount, float area, int agent) {
		this.id = id;
		this.name = name;
		this.cost = cost;
		this.RoomCount = roomCount;
		this.area = area;
		this.agent = agent;

		Estate[] newEstateBase =  Arrays.copyOf(Estate.estateBase, Estate.estateBase.length +1);
		newEstateBase[Estate.estateBase.length] = this;
		Estate.estateBase = newEstateBase;
	}

	public void setData(String [] name, String cost, String roomCount, String area) throws EstateException {
		String joinName = String.join(" ", name);

		if (!joinName.isEmpty()) {
			if (joinName.length() < 5) {
				throw new EstateException(0xE2);
			}

			if (joinName.length() > 45) {
				throw new EstateException(0xE3);
			}

			this.name = name;
		}

		if (!cost.isEmpty()) {
			float parsedCost = Float.parseFloat(cost);
			if (parsedCost < 1500) {
				throw new EstateException(0xE4);
			}

			this.cost = parsedCost;
		}

		if (!roomCount.isEmpty()) {
			int parsedRoomCount = Integer.parseInt(roomCount);
			if (parsedRoomCount < 1) {
				throw new EstateException(0xE5);
			}
			this.RoomCount = parsedRoomCount;

		}

		if (!area.isEmpty()) {
			float parsedArea = Float.parseFloat(area);
			if (parsedArea < 25) {
				throw new EstateException(0xE6);
			}
			this.area = parsedArea;
		}

	}

	public int getID() {
		return this.id;
	}

	public String getName() {
		return String.join(" ", this.name);
	}

	public float getCost() {
		return this.cost;
	}

	public String getAgent() {
		User agent = User.findByID(this.agent);
		if (agent != null) {
			return agent.getUsername();
		}
		return null;
	}

	public float getArea () {
		return this.area;
	}

	public int getRoomCount () {
		return this.RoomCount;
	}
	
	public static Estate[] getEstates() {
		return Estate.estateBase;
	}
	
	public static Estate getById(int id) {
		for (int i = 0; i < Estate.estateBase.length; i++) {
			if (Estate.estateBase[i].id == id) return Estate.estateBase[i];
		}
		
		return null;
	}

	public void delete () {
		int foundIndex = -1;
		Estate[] newEstateBase = new Estate[estateBase.length-1];
		for (int i = 0; i < Estate.estateBase.length; i++) {
			if (Estate.estateBase[i] == this) {
				foundIndex = i;
				if (i+1 < Estate.estateBase.length) {
					newEstateBase[i] = Estate.estateBase[i + 1];
				}
			} else if (i > foundIndex  && foundIndex != -1) {
				if ((i+1) < Estate.estateBase.length && estateBase[i+1] != null) {
					newEstateBase[i] = Estate.estateBase[i + 1];
				}
			} else {
				newEstateBase[i] = Estate.estateBase[i];
			}
		}

		Estate.estateBase = newEstateBase;
	}



	public static void saveToFile(String name) throws EstateException {
		String filename = name + ".bin";
		try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(filename))) {
			Estate[] estates = Estate.estateBase;

			byte decryptionKey = (byte) 0xAA;

			for (Estate estate : estates) {
				if (estate.id != 0) {
					String data = estate.id + "," + String.join(" ", estate.name) + "," + estate.cost + "," + estate.RoomCount + "," + estate.area + "," + estate.agent;

					if (estate instanceof Flat flat) {
						data += "," + "Flat" + "," + flat.getIsBalcony() + "," + flat.getIsCellar() + "," + flat.getIsParking();
					}

					if (estate instanceof Cottage cottage) {
						data += "," + "Cottage" + "," + cottage.getIsGarden() + "," + cottage.getIsFireplace();
					}

					if (estate instanceof MultiFamily multiFamily) {
						data += "," + "MultiFamily" + "," + multiFamily.getFlatCount() + "," + multiFamily.getIsParking() + "," + multiFamily.getIsGarage();
					}

					byte[] byteData = data.getBytes();
					byte[] encryptedData = new byte[byteData.length];

					for (int j = 0; j < byteData.length; j++) {
						encryptedData[j] = (byte) (byteData[j] ^ decryptionKey);
					}

					dataOutputStream.writeInt(encryptedData.length);
					dataOutputStream.write(encryptedData);
				}
			}
			dataOutputStream.close();
		} catch (IOException e) {
			throw new EstateException(0xEF1);
		}
	}

	public static void loadFromFile (String name) throws EstateException {
		String filename = name + ".bin";
		byte encryptionKey = (byte) 0xAA;

		try {
			DataInputStream dataInputStream = new DataInputStream(new FileInputStream(filename));
			Estate.estateBase = new Estate[0];
			while (dataInputStream.available() > 0) {
				int length = dataInputStream.readInt();
				byte[] encryptedData = new byte[length];
				dataInputStream.readFully(encryptedData);

				byte[] decryptedData = new byte[length];
				for (int i = 0; i < length; i++) {
					decryptedData[i] = (byte) (encryptedData[i] ^ encryptionKey);
				}

				String data = new String(decryptedData);
				String[] parts = data.split(",");

				if (parts[6].equals("Flat")) {
					Flat flat = new Flat(Integer.parseInt(parts[0]), parts[1].split(" "), Float.parseFloat(parts[2]), Integer.parseInt(parts[3]), Float.parseFloat(parts[4]), Integer.parseInt(parts[5]), Boolean.parseBoolean(parts[7]), Boolean.parseBoolean(parts[8]), Boolean.parseBoolean(parts[9]));
				}

				if (parts[6].equals("Cottage")) {
					Cottage cottage = new Cottage(Integer.parseInt(parts[0]), parts[1].split(" "), Float.parseFloat(parts[2]), Integer.parseInt(parts[3]), Float.parseFloat(parts[4]), Integer.parseInt(parts[5]), Boolean.parseBoolean(parts[7]), Boolean.parseBoolean(parts[8]));
				}

				if (parts[6].equals("MultiFamily")) {
					MultiFamily multiFamily = new MultiFamily(Integer.parseInt(parts[0]), parts[1].split(" "), Float.parseFloat(parts[2]), Integer.parseInt(parts[3]), Float.parseFloat(parts[4]),Integer.parseInt(parts[5]), Integer.parseInt(parts[7]), Boolean.parseBoolean(parts[8]), Boolean.parseBoolean(parts[9]));
				}
			}
			dataInputStream.close();

			System.out.println(Estate.estateBase.length);

		} catch (IOException e) {
			throw new EstateException(0xEF2);
		}
	}

	public static void flush () {
		Estate.estateBase = new Estate[0];
	}

	public static void sortByName (boolean asc) {
		if (asc) {
			Arrays.sort(Estate.estateBase, Comparator.comparing(Estate::getName));
		} else {
			Arrays.sort(Estate.estateBase, Comparator.comparing(Estate::getName).reversed());
		}
	}

	public static void sortByCost (boolean asc) {
		if (asc) {
			Arrays.sort(Estate.estateBase, Comparator.comparing(Estate::getCost));
		} else {
			Arrays.sort(Estate.estateBase, Comparator.comparing(Estate::getCost).reversed());
		}
	}

	public static Estate[] searchByName(String[] name) {
		String joinName = String.join(" ", name);

		List<Estate> matchingEstates = new ArrayList<>();

		for (Estate estate : Estate.estateBase) {
			if (joinName.equals(estate.getName())) {
				matchingEstates.add(estate);
			}
		}

		return matchingEstates.toArray(new Estate[0]);
	}

	public static Estate[] searchByCost (float min, float max) {

		List<Estate> matchingEstates = new ArrayList<>();

		for (Estate estate : Estate.estateBase) {
			if (estate.cost >=  min && estate.cost <= max) {
				matchingEstates.add(estate);
			}
		}

		return matchingEstates.toArray(new Estate[0]);
	}
}

