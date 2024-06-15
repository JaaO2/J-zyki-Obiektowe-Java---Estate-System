package estateSystem;


import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class User {
	private final int id;
	private final String username;
	private final String email;
	private final int role;
	private final String passwordHash;
	private static User[] users;

	public User (int id, String username, String email, int role, String passwordHash) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.role = role;
		this.passwordHash = passwordHash;

		User[] newUsersArray = (User.users == null) ? new User[1] : Arrays.copyOf(User.users, User.users.length + 1);
		newUsersArray[(User.users == null) ? 0 : User.users.length] = this;
		User.users = newUsersArray;
	}

	public User(int id, String username, String email, int role, String password, boolean saveFlag) throws UserException {

		if (username == null || username.isEmpty()) {
			throw new UserException(0xA1);
		}

		if (username.length() < 5) {
			throw new UserException(0xA2);
		}

		if (username.length() > 25 ) {
			throw new UserException(0xA3);
		}

		User user = User.find(username);

		if (user != null) {
			throw new UserException(0xA11);
		}


		if (email == null || email.isEmpty()) {
			throw new UserException(0xA4);
		}

		if (email.length() < 5) {
			throw new UserException(0xA5);
		}

		if (!email.contains("@") || !email.contains(".")) {
			throw new UserException(0xA5);
		}

		User userEmail = User.findByEmail(email);

		if (userEmail != null) {
			throw new UserException(0xA12);
		}

		if (password == null || password.isEmpty()) {
			throw new UserException(0xA6);
		}

		if (password.length() < 5) {
			throw new UserException(0xA7);
		}

		if (password.length() > 25) {
			throw new UserException(0xA8);
		}

		try {
			String passwordHash = User.hashPassword(password);

			this.id = id;
			this.username = username;
			this.email = email;
			this.role = role;
			this.passwordHash = passwordHash;
			User[] newUsersArray = (User.users == null) ? new User[1] : Arrays.copyOf(User.users, User.users.length + 1);
			newUsersArray[(User.users == null) ? 0 : User.users.length] = this;
			User.users = newUsersArray;

			if (saveFlag) User.saveUsersToFile();

		} catch (NoSuchAlgorithmException e) {
			throw new UserException(0xA9);
		}
	}

	public String getUsername() {
		return this.username;
	}

	public String getEmail () {
		return this.email;
	}

	public int getRole () {
		return this.role;
	}

	public int getID () { return this.id; }

	public static User find (String username) {
		if (User.getAll() == null) return null;

		User[] users = User.users;
		for (User user : users) {
			if (user.username.equals(username)) {
				return user;
			}
		}

		return null;
	}

	public static User findByEmail (String email)  {
		if (User.getAll() == null) return null;

		User[] users = User.users;
		for (User user : users) {
			if (user.email != null && user.email.equals(email)) {
				return user;
			}
		}

		return null;
	}

	public static User findByID (int id) {
		if (User.getAll() == null) return null;

		User[] users = User.users;
		for (User user : users) {
			if (user.id == id) {
				return user;
			}
		}
		return null;
	}

	public static void getUsersFromFile() throws FileNotFoundException, UserException {
		String filename = "user.bin";
		byte encryptionKey = (byte) 0xAA;

		try {
			DataInputStream dataInputStream = new DataInputStream(new FileInputStream(filename));
			while (dataInputStream.available() > 0) {
				int length = dataInputStream.readInt();
				byte[] encryptedData = new byte[length];
				dataInputStream.readFully(encryptedData);

				byte[] decryptedData = new byte[length];
				for (int i = 0; i < length; i++) {
					decryptedData[i] = (byte) (encryptedData[i] ^ encryptionKey);
				}

				String data = new String(decryptedData);
				String[] parts = data.split(" ");
				User user = new User(Integer.parseInt(parts[0]), parts[1], parts[2], Integer.parseInt(parts[3]), parts[4]);
			}
			dataInputStream.close();

		} catch (IOException e) {
			throw new FileNotFoundException(e.getMessage());
		}
	}

	public static void saveUsersToFile() throws UserException {
		String filename = "user.bin";
		try (DataOutputStream dataOutputStream = new DataOutputStream(new FileOutputStream(filename))) {
			User[] users = User.users;

			byte decryptionKey = (byte) 0xAA;

			for (User user : users) {
				if (user.id != 0) {
					String data = user.id + " " + user.username + " " + user.email + " " + user.role + " " + user.passwordHash;
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
			throw new UserException(0xA10);
		}
	}




	public static int getNextID() {
		if (User.users == null) {
			return 1;
		}
		return User.users.length+1;
	}

	public static String hashPassword(String password) throws NoSuchAlgorithmException {
		if (password != null) {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] hashedPassword = md.digest(password.getBytes());
			return Base64.getEncoder().encodeToString(hashedPassword);
		}

		return null;
	}

	public boolean checkPassword(String password) throws UserException {
		try {
			String hashPasswordEntered = User.hashPassword(password);
			if (!hashPasswordEntered.equals(this.passwordHash)) {
				throw new UserException(0xB2);
			}
		} catch (NoSuchAlgorithmException e) {
			throw new UserException(0xB3);
		}
		return true;
	}

	public static User[] getAll () {
		return User.users;
	}
}
