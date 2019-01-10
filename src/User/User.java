package User;
import DataBase.*;
import Mail.Mailbox;

import java.util.ArrayList;

public class User {
    private String username="";
    private String pass="";
    private String email="";
    private String name="";
    private String lastname="";
    private String birthDate="";
    private dbTableUsers db=new dbTableUsers();
    private Mailbox mailbox;
    String[] columns={"username","password","email","name","last_name","birth_date"};

    /**
     * a constructor that get a user from the db by his username and password
     * @param userName
     * @param password
     */
    public User(String userName,String password){
        String[] values= {userName,password};
        ArrayList<String> user=db.selectWhereCommand( "SELECT username,password,email,name,last_name,birth_date,image FROM users WHERE username= ? AND password=?",columns,values );
        String selectedUser;
        if(user.size()>=1){
            selectedUser=user.get( 0 );
            addValues(selectedUser);
        }
    }

    /**
     * a constructor that get a user from the db by his username
     * @param userName
     */
    public User(String userName){
        String[] values={userName};
        ArrayList<String> user=db.selectWhereCommand( "SELECT username,password,email,name,last_name,birth_date,image FROM users WHERE username= ?",columns,values);
        String selectedUser;
        if(user.size()>=1){
            selectedUser=user.get( 0 );
            addValues(selectedUser);
        }
        //this.mailbox = Mailbox.recreateMailBox(this);
    }

    /**
     * a constructor that create a new user in the db
     * @param username
     * @param pass
     * @param email
     * @param name
     * @param lastname
     * @param birthDath
     */
    public User(String username, String pass, String email, String name, String lastname, String birthDath) {
        this.username = username;
        this.pass = pass;
        this.email = email;
        this.name = name;
        this.lastname = lastname;
        this.birthDate = birthDath;
        this.mailbox = new Mailbox(this);
    }

    /**
     * @return the user username
     */
    public String getUsername() {
        return username;
    }
    /**
     * @return the user password
     */
    public String getPass() {
        return pass;
    }
    /**
     * @return the user email
     */
    public String getEmail() {
        return email;
    }
    /**
     * @return the user first name
     */
    public String getName() {
        return name;
    }

    /**
     * get a user lastname
     */
    public String getLastname() {
        return lastname;
    }

    /**
     *
     * @return  a user birthdate
     */
    public String getBirthDath() {
        return birthDate;
    }

    /**
     * cahnge a values after an update
     * @param selectedUser
     */
    private void addValues(String selectedUser) {
        String[] values=selectedUser.split( "\t" );
        username=values[0];
        pass=values[1];
        email=values[2];
        name=values[3];
        lastname=values[4];
        birthDate=values[5];
    }

    /**
     * update a user information
     * @param email
     * @param name
     * @param lastname
     * @param birthDate
     * @return
     */
    public boolean updateUser(String email,String name,String lastname,String birthDate){
        String[] newValues={email,name,lastname,birthDate,username};
         if(db.update( "UPDATE users SET email = ?, name = ?, last_name= ?, birth_date = ? WHERE username = ?", newValues)){
             this.email=email;
             this.name=name;
             this.lastname=lastname;
             this.birthDate=birthDate;
             return true;
         }
         return false;
    }
    //create a new user in the DB
    public boolean createNewUser(){
        String[] insert={username,pass,email,name,lastname,birthDate};
        return db.InsertComand( "INSERT INTO users(username,password,email,name,last_name,birth_date) VALUES(?,?,?,?,?,?)",insert );
    }
    //select a users frim the users by some fields
    public ArrayList<String> selectUser(String query, String[] fields){
        return db.selectCommand( query,fields );
    }

    /**
     * check if all user fields are ok
     */
    public boolean checkUser(){
        return username.length()>=1&&pass.length()>=1&&email.length()>=1&&name.length()>=1&&lastname.length()>=1&&birthDate.length()>=1;
    }

    /**
     * update a username passwrord
     * @param oldPass the user old password
     * @param newPass the user new password
     * @return true if the password succesfuly changed
     */
    public boolean updatePW(String oldPass, String newPass) {
        if(pass.equals( oldPass )){
            String[] newVals={newPass,username};
            if(db.update( "UPDATE users SET password = ? WHERE username = ?",newVals )){
                pass=newPass;
                return true;
            }
        }
        return false;
    }

    public Mailbox getMailbox(){return this.mailbox;}
}
