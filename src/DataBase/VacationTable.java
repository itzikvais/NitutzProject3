package DataBase;

import Vacation.Vacation;

import java.sql.*;
import java.util.ArrayList;

public class VacationTable extends Adb {

    public ArrayList<Vacation> selectByDatesWithBackFlights(Date departureTime, Date backLaunchTime) {
        ArrayList<Vacation> vacationsArrayList = new ArrayList<>();
        String sql;
        Date[] values;

        sql = "SELECT vacationID,seller,aviationCompany,departureTime,launchTime,backDepartureTime,backLaunchTime,baggage,tickets,fromCountry,destinationCountry,ticketType,price,avalible FROM vacations WHERE departureTime > ? AND backLaunchTime < ? AND avalible = 1;";
        values = new Date[2];
        values[0] = departureTime;
        values[1] = backLaunchTime;

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            for (int i = 1; i <= values.length; i++) {
                pstmt.setDate(i, values[i - 1]);
            }
            //
            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                Vacation currRecord = new Vacation(rs.getInt("vacationID"),
                        rs.getString("seller"),
                        rs.getString("aviationCompany"),
                        rs.getDate("departureTime"),
                        rs.getDate("launchTime"),
                        rs.getDate("backDepartureTime"),
                        rs.getDate("backLaunchTime"),
                        rs.getInt("baggage"),
                        rs.getInt("tickets"),
                        rs.getString("fromCountry"),
                        rs.getString("destinationCountry"),
                        rs.getString("ticketType"),
                        rs.getDouble("price"),
                        rs.getInt("avalible"));
                vacationsArrayList.add(currRecord);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return vacationsArrayList;
    }
    public boolean InsertCommand(Vacation vacationToDB) {
        boolean success = false;
        String sqlCommand = "INSERT INTO vacations(vacationID,seller,aviationCompany,departureTime,launchTime," +
                "backDepartureTime,backLaunchTime,baggage,tickets,fromCountry,destinationCountry,ticketType," +
                "price,avalible) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sqlCommand)) {
            pstmt.setInt(1, vacationToDB.getVacationID());
            pstmt.setString(2, vacationToDB.getSeller());
            pstmt.setString(3, vacationToDB.getAviationCompany());
            pstmt.setDate(4, vacationToDB.getDepartureTime());
            pstmt.setDate(5, vacationToDB.getLaunchTime());
            if (vacationToDB.getBackDepartureTime()!= null)
                pstmt.setDate(6, vacationToDB.getBackDepartureTime());
            else  pstmt.setNull(6, Types.DATE);
            if (vacationToDB.getBackLaunchTime()!= null)
                pstmt.setDate(7, vacationToDB.getBackLaunchTime());
            else  pstmt.setNull(7, Types.DATE);
            pstmt.setInt(8, vacationToDB.getBaggage());
            pstmt.setInt(9, vacationToDB.getTickets());
            pstmt.setString(10, vacationToDB.getFromCountry());
            pstmt.setString(11, vacationToDB.getDestinationCountry());
            pstmt.setString(12, vacationToDB.getTicketType());
            pstmt.setDouble(13, vacationToDB.getPrice());
            int isAvalibleInt = 0;
            if (vacationToDB.isAvalible()) isAvalibleInt = 1;
            pstmt.setInt(14, isAvalibleInt);

            pstmt.executeUpdate();
            success = true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return success;
    }
    public boolean updateAvailable(int vacID, int update) {
        String sqlCommand = "UPDATE vacations SET avalible = ? WHERE vacationID = ?;";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sqlCommand)) {
            pstmt.setInt(1,update);
            pstmt.setInt(2,vacID);
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }
    public boolean deletCommand(int vacID) {
        String sqlCommand = "DELETE FROM vacations WHERE vacationID = ?;";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sqlCommand)) {
            pstmt.setInt(1,vacID);
            // delete
            pstmt.executeUpdate();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }
    public ArrayList<Vacation> getAllAvailableVacations(){
        ArrayList<Vacation>  res = new ArrayList<Vacation>();
        String sql;
        sql = "SELECT vacationID,seller,aviationCompany,departureTime,launchTime,backDepartureTime,backLaunchTime,baggage,tickets,fromCountry,destinationCountry,ticketType,price,avalible FROM vacations WHERE avalible = 1 ;";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the value
            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                Vacation currRecord = new Vacation(rs.getInt("vacationID"),
                        rs.getString("seller"),
                        rs.getString("aviationCompany"),
                        rs.getDate("departureTime"),
                        rs.getDate("launchTime"),
                        rs.getDate("backDepartureTime"),
                        rs.getDate("backLaunchTime"),
                        rs.getInt("baggage"),
                        rs.getInt("tickets"),
                        rs.getString("fromCountry"),
                        rs.getString("destinationCountry"),
                        rs.getString("ticketType"),
                        rs.getDouble("price"),
                        rs.getInt("avalible"));
                res.add(currRecord);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return res;
    }
    public ArrayList<Vacation> getAllAvailableVacations(String registeredUerName){
        ArrayList<Vacation>  res = new ArrayList<Vacation>();
        String sql;
        sql = "SELECT vacationID,seller,aviationCompany,departureTime,launchTime,backDepartureTime,backLaunchTime,baggage,tickets,fromCountry,destinationCountry,ticketType,price,avalible FROM vacations WHERE seller !=  ? AND avalible = 1 ;";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,registeredUerName);
            // set the value
            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                Vacation currRecord = new Vacation(rs.getInt("vacationID"),
                        rs.getString("seller"),
                        rs.getString("aviationCompany"),
                        rs.getDate("departureTime"),
                        rs.getDate("launchTime"),
                        rs.getDate("backDepartureTime"),
                        rs.getDate("backLaunchTime"),
                        rs.getInt("baggage"),
                        rs.getInt("tickets"),
                        rs.getString("fromCountry"),
                        rs.getString("destinationCountry"),
                        rs.getString("ticketType"),
                        rs.getDouble("price"),
                        rs.getInt("avalible"));
                res.add(currRecord);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return res;
    }
    public ArrayList<Vacation> getMyVacations(String userName){
        ArrayList<Vacation>  res = new ArrayList<Vacation>();
        String sql;
        sql = "SELECT vacationID,seller,aviationCompany,departureTime,launchTime,backDepartureTime,backLaunchTime,baggage,tickets,fromCountry,destinationCountry,ticketType,price,avalible FROM vacations WHERE avalible = 1 AND seller = ?;";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,userName);
            // set the value
            ResultSet rs = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                Vacation currRecord = new Vacation(rs.getInt("vacationID"),
                        rs.getString("seller"),
                        rs.getString("aviationCompany"),
                        rs.getDate("departureTime"),
                        rs.getDate("launchTime"),
                        rs.getDate("backDepartureTime"),
                        rs.getDate("backLaunchTime"),
                        rs.getInt("baggage"),
                        rs.getInt("tickets"),
                        rs.getString("fromCountry"),
                        rs.getString("destinationCountry"),
                        rs.getString("ticketType"),
                        rs.getDouble("price"),
                        rs.getInt("avalible"));
                res.add(currRecord);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return res;
    }

}