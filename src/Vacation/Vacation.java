package Vacation;


import DataBase.VacationTable;

import java.sql.*;
import java.time.LocalDate;

public class Vacation implements Comparable<Vacation> {
    private int vacationID;
    private String sellerName;
    private String aviationCompany;

    private Date departureTime; //"YYYY-MM-DD HH:MM"
    private Date launchTime;
    private Date backDepartureTime;
    private Date backLaunchTime;

    private int baggage;
    private int tickets;
    private String fromCountry;
    private String destinationCountry;
    private String ticketType;
    private double price;

    private boolean isAvalible;

    /**
     * create a new vacation on the DB with the user inputs
     * @param sellerName
     * @param aviationCompany
     * @param departureTime
     * @param launchTime
     * @param backDepartureTime
     * @param backLaunchTime
     * @param baggage
     * @param tickets
     * @param fromCountry
     * @param destinationCountry
     * @param ticketType
     * @param price
     */
    public Vacation(String sellerName, String aviationCompany, Date departureTime, Date launchTime,
                    Date backDepartureTime, Date backLaunchTime, int baggage, int tickets,
                    String fromCountry, String destinationCountry, String ticketType, double price) {
        VacationTable vte = new VacationTable();
        this.vacationID = (Math.random()*Integer.MAX_VALUE+ "").hashCode();
        if(this.vacationID<0)
            this.vacationID=this.vacationID*-1;
        this.sellerName = sellerName;
        this.aviationCompany = aviationCompany;
        this.departureTime = departureTime;
        this.launchTime = launchTime;
        this.backDepartureTime = backDepartureTime;
        this.backLaunchTime = backLaunchTime;
        this.baggage = baggage;
        this.tickets = tickets;
        this.fromCountry = fromCountry;
        this.destinationCountry = destinationCountry;
        this.ticketType = ticketType;
        this.price = price;
        isAvalible = true;

        //add to dataBase
        vte.InsertCommand(this);
    }

    /**
     * create a new vacation on the DB with the user inputs
     * @param vacationID
     * @param sellerName
     * @param aviationCompany
     * @param departureTime
     * @param launchTime
     * @param backDepartureTime
     * @param backLaunchTime
     * @param baggage
     * @param tickets
     * @param fromCountry
     * @param destinationCountry
     * @param ticketType
     * @param price
     * @param isAvalible
     */
    public Vacation(int vacationID, String sellerName, String aviationCompany, Date departureTime, Date launchTime,
                    Date backDepartureTime, Date backLaunchTime, int baggage, int tickets,
                    String fromCountry, String destinationCountry, String ticketType, double price, int isAvalible){
        //create object from record in DB
        // NO NEED TO ADD TO DB!

        this.vacationID = vacationID;
        this.sellerName = sellerName;
        this.aviationCompany = aviationCompany;
        this.departureTime = departureTime;
        this.launchTime = launchTime;
        if (backDepartureTime != null) this.backDepartureTime = backDepartureTime;
        if (backLaunchTime != null) this.backLaunchTime = backLaunchTime;
        this.baggage = baggage;
        this.tickets = tickets;
        this.fromCountry = fromCountry;
        this.destinationCountry = destinationCountry;
        this.ticketType = ticketType;
        this.price = price;
        this.isAvalible = isAvalible != 0;
    }

    /**
     * constractor that create a Vacation that
     * @param sellerUserName
     * @param vacationID
     */
    public Vacation(String sellerUserName, int vacationID) {
        VacationTable vacationTableEntry = new VacationTable();
        String querry = "SELECT vacationID,seller,aviationCompany,departureTime,launchTime,backDepartureTime,backLaunchTime,baggage,tickets,fromCountry,destinationCountry,ticketType,price,avalible FROM vacations WHERE  seller = ? AND vacationID = ?;";
        try (Connection conn = vacationTableEntry.connect();
             PreparedStatement pstmt  = conn.prepareStatement(querry)){
            pstmt.setString(1,sellerUserName);
            pstmt.setInt(2,vacationID);
            //
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                this.vacationID= rs.getInt("vacationID");
                this.sellerName=rs.getString("seller");
                this.aviationCompany=rs.getString("aviationCompany");
                this.departureTime=rs.getDate("departureTime");
                this.launchTime=rs.getDate("launchTime");
                if (backDepartureTime != null) this.backDepartureTime=rs.getDate("backDepartureTime");
                if (backLaunchTime != null) this.backLaunchTime=rs.getDate("backLaunchTime");
                this.baggage=rs.getInt("baggage");
                this.tickets=rs.getInt("tickets");
                this.fromCountry=rs.getString("fromCountry");
                this.destinationCountry=rs.getString("destinationCountry");
                this.ticketType=rs.getString("ticketType");
                this.price=rs.getDouble("price");
                if(rs.getInt("avalible") == 0) this.isAvalible = false;
                else this.isAvalible = true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * get a vacation from the db by the vacation id
     * @param vacationID
     */
    public Vacation(int vacationID) {
        VacationTable vacationTableEntry = new VacationTable();
        String querry = "SELECT vacationID,seller,aviationCompany,departureTime,launchTime,backDepartureTime,backLaunchTime,baggage,tickets,fromCountry,destinationCountry,ticketType,price,avalible FROM vacations WHERE  vacationID = ?;";
        try (Connection conn = vacationTableEntry.connect();
             PreparedStatement pstmt  = conn.prepareStatement(querry)){
            pstmt.setInt(1,vacationID);
            //
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                this.vacationID= rs.getInt("vacationID");
                this.sellerName=rs.getString("seller");
                this.aviationCompany=rs.getString("aviationCompany");
                this.departureTime=rs.getDate("departureTime");
                this.launchTime=rs.getDate("launchTime");
                if (backDepartureTime != null) this.backDepartureTime=rs.getDate("backDepartureTime");
                if (backLaunchTime != null) this.backLaunchTime=rs.getDate("backLaunchTime");
                this.baggage=rs.getInt("baggage");
                this.tickets=rs.getInt("tickets");
                this.fromCountry=rs.getString("fromCountry");
                this.destinationCountry=rs.getString("destinationCountry");
                this.ticketType=rs.getString("ticketType");
                this.price=rs.getDouble("price");
                if(rs.getInt("avalible") == 0) this.isAvalible = false;
                else this.isAvalible = true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /* getters */
    public int getVacationID() {
        return vacationID;
    }

    /**
     * get the vacation seller
     * @return
     */
    public String getSeller() {
        return sellerName;
    }
    //get the vacation avaition company
    public String getAviationCompany() {
        return aviationCompany;
    }
    //get the vacation departure time
    public Date getDepartureTime() {
        return departureTime;
    }

    public Date getLaunchTime() {
        return launchTime;
    }

    public Date getBackDepartureTime() {
        return backDepartureTime;
    }

    public Date getBackLaunchTime() {
        return backLaunchTime;
    }

    public int getBaggage() {
        return baggage;
    }

    public int getTickets() {
        return tickets;
    }

    /**
     * get flight country source
     * @return
     */
    public String getFromCountry() {
        return fromCountry;
    }

    /**
     * get flight country destination
     * @return
     */
    public String getDestinationCountry() {
        return destinationCountry;
    }

    /**
     * get vacation tickets type
     * @return
     */
    public String getTicketType() {
        return ticketType;
    }

    /**
     * get vacation price
     * @return
     */
    public double getPrice() {
        return price;
    }

    /**
     * @return true of a vacation is available
     */
    public boolean isAvalible() {
        Date now = Date.valueOf(LocalDate.now());
        isAvalible = (isAvalible && !(now.after(departureTime)));
        setAvalible(isAvalible);
        return isAvalible;
    }

    /* setters */
    public void changePrice(double price) {
        this.price = price;
    }

    public void setAvalible(boolean avalible) {
        isAvalible = avalible;
        //UPDATE IN DB!!!!
        VacationTable vacationTableEntry = new VacationTable();
        int boolInt = 0;
        if(avalible) boolInt = 1;
        vacationTableEntry.updateAvailable(getVacationID(),boolInt);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append( "vacationID: " );
        sb.append( vacationID );
        sb.append(", ");
        sb.append("seller: ");
        sb.append(sellerName);
        sb.append("\n");
        sb.append("country of origin: ");
        sb.append(fromCountry);
        sb.append(",  ");
        sb.append("country destination: ");
        sb.append(destinationCountry);
        sb.append(",  ");
        sb.append("price: ");
        sb.append(price);
        sb.append("\n");
        sb.append("departureTime: ");
        sb.append(departureTime.toString());
        sb.append(", ");
        sb.append("launchTime: ");
        sb.append(launchTime.toString());
        sb.append("\n");
        if (backDepartureTime != null) {
            sb.append("backDepartureTime: ");
            sb.append(backDepartureTime.toString());
            sb.append(", ");
        }
        if (backLaunchTime != null) {
            sb.append("backLaunchTime: ");
            sb.append(backLaunchTime.toString());
        }
        return sb.toString();
    }

    @Override
    public int compareTo(Vacation o) {
        return this.departureTime.compareTo(o.getDepartureTime());
    }


}
