package Model;

import User.User;
import Vacation.Vacation;
import javafx.scene.control.Alert;

public interface ModelInt {
    User user=null;
    Alert getExitMessage();

    /**
     * create a new user
     * @param userName
     * @param password
     * @param email
     * @param date users birthdate
     * @param firstName
     * @param lastName
     * @return true if user created
     */
    boolean createUser(String userName, String password, String email, String date, String firstName, String lastName);
    User getUser();
    void setUser(User user);
    Vacation getVacation();
    void setVacation(Vacation v);
    /**
     * login to a user with username and password
     */
    User login(String userName, String password);

    /**
     * notify the controller about succesful login
     * @param user the logged user
     */
    public void loginSucces(User user);

    /**
     * update user information
     * @param user
     * @param email
     * @param date
     * @param firstName
     * @param lastName
     * @return
     */
    boolean updateUser(User user,String email, String date, String firstName, String lastName);

    boolean updatePassword(User user, String oldPass, String newPass);
}
