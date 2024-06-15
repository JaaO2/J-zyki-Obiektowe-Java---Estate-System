package estateSystem;

public class Session {
    private static Session currentSession;
    private final static User guest = new User(0, "Guest", null, 0, "");
    private final User user;

    Session() {
            this.user = Session.guest;
    }

    Session(User user) {
        this.user = user;
        Session.currentSession = this;
    }

    public static void createNew (User user) {
        Session.currentSession = new Session(user);
    }

    public static void clear () {
        Session.createNew(guest);
    }

    public static Session getCurrent () {
        if (Session.currentSession == null) {
            Session.currentSession = new Session();
        }

        return Session.currentSession;
    }

    public User getUser() {
        return this.user;
    }
}
