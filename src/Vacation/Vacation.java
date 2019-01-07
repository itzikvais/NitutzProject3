package Vacation;


import DataBase.VacationTable;

import java.sql.*;

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

    public Vacation(String sellerName, String aviationCompany, Date departureTime, Date launchTime,
                    Date backDepartureTime, Date backLaunchTime, int baggage, int tickets,
                    String fromCountry, String destinationCountry, String ticketType, double price) {
        VacationTable vte = new VacationTable();
        this.vacationID = (Math.random()*Integer.MAX_VALUE+ "").hashCode();
        vte.updateInt("UPDATE ids SET vacationID = ?",vacationID+1);
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

    /* getters */
    public int getVacationID() {
        return vacationID;
    }

    public String getSeller() {
        return sellerName;
    }

    public String getAviationCompany() {
        return aviationCompany;
    }

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

    public String getFromCountry() {
        return fromCountry;
    }

    public String getDestinationCountry() {
        return destinationCountry;
    }

    public String getTicketType() {
        return ticketType;
    }

    public double getPrice() {
        return price;
    }

    public boolean isAvalible() {
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
        sb.append("seller: ");
        sb.append(sellerName);
        sb.append(", ");
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
