package Test;

import DataBase.VacationTable;
import Vacation.Vacation;

import java.sql.Date;
import java.util.ArrayList;

public class VacationTest {
public static boolean firstTest = true;
//"YYYY-MM-DD HH:MM"
    public static void main(String[] args) {
        VacationTable vacationTableEntry = new VacationTable();
        if(firstTest) firstTest=false;
        else {
            vacationTableEntry.deletCommand(1);
            vacationTableEntry.deletCommand(2);
            vacationTableEntry.deletCommand(3);
        }

        Date dateFirst = Date.valueOf("2015-12-30");

        //1 Liad
        Date d1departureTime = Date.valueOf("2018-12-30");
        Date d1launchTime = Date.valueOf("2018-12-30");
        Date d1backDepartureTime = Date.valueOf("2019-01-10");
        Date d1backLaunchTime = Date.valueOf("2019-01-10");
        //2 Itzik
        Date d2departureTime = Date.valueOf("2019-07-10");
        Date d2launchTime = Date.valueOf("2019-07-10");
        Date d2backDepartureTime = Date.valueOf("2019-08-10");
        Date d2backLaunchTime = Date.valueOf("2019-08-10");

        Date dateMiddle = Date.valueOf("2020-02-15");

        //3 Itzik
        Date d3departureTime = Date.valueOf("2020-12-30");
        Date d3launchTime = Date.valueOf("2020-12-30");
        Date d3backDepartureTime = Date.valueOf("2021-12-30");
        Date d3backLaunchTime = Date.valueOf("2021-12-30");

        Date dateLast = Date.valueOf("2023-12-30");

        //prices for vacations
        double p1 = 120;
        double p2 = 150;
        double p3 = 70;

        //vacations
        Vacation v1 = new Vacation("Liad","El-Al",d1departureTime,d1launchTime,d1backDepartureTime,d1backLaunchTime,
                                    20,2,"Israel","Colombia","adult",p1);
        Vacation v2 = new Vacation("Itzik","SwissAirLine",d2departureTime,d2launchTime,d2backDepartureTime,d2backLaunchTime,
                40,2,"Israel","Swiss","adult",p2);
        Vacation v3 = new Vacation("Itzik","Avianka",d3departureTime,d3launchTime,d3backDepartureTime,d3backLaunchTime,
                15,1,"Israel","Brazil","adult",p3);

        firstToMiddle(vacationTableEntry, dateFirst, dateMiddle);
        middleToLast(vacationTableEntry, dateMiddle, dateLast);
        firstToLast(vacationTableEntry, dateFirst, dateLast);
        itzikVacations(vacationTableEntry);
        System.out.println("**** Itzik 1 vacations update NOT avalible ****");
        boolean itzik1 = vacationTableEntry.updateAvailable(2,1);
        if(itzik1) System.out.println("update itsik1 success!");

        System.out.println("**** Itzik 2 vacations update NOT avalible ****");
        boolean itzik2 = vacationTableEntry.updateAvailable(3,1);
        if(itzik2) System.out.println("update itsik2 success!");

        System.out.println("**** Itzik 2 vacations update avalible ****");
        boolean itzik3 = vacationTableEntry.updateAvailable(3,1);
        if(itzik3) System.out.printf("update itsik2 success!");

        Vacation v = new Vacation("Liad",1);
        System.out.println();
        System.out.println(v);

        printAllAvalibleVacations(vacationTableEntry);
        printAllAvalibleVacationsWithoutRegisteredUser(vacationTableEntry);
    }

    private static void itzikVacations(VacationTable vacationTableEntry) {
        System.out.println();
        System.out.println("**** Itzik vacations ****");
        ArrayList<Vacation> zinaVacations = vacationTableEntry.getMyVacations("Itzik");
        if (zinaVacations.size() != 0){
            for (Vacation v : zinaVacations)
                System.out.println(v.toString());
        }
    }

    private static void firstToLast(VacationTable vacationTableEntry, Date dateFirst, Date dateLast) {
        System.out.println();
        System.out.println("**** vacations First To Last ****");
        ArrayList<Vacation> vacationsFirstToLast = vacationTableEntry.selectByDatesWithBackFlights(dateFirst,dateLast);
        if (vacationsFirstToLast.size() != 0){
            for (Vacation v : vacationsFirstToLast)
                System.out.println(v.toString());
        }
    }

    private static void middleToLast(VacationTable vacationTableEntry, Date dateMiddle, Date dateLast) {
        System.out.println("**** vacations Middle To Last ****");
        ArrayList<Vacation> vacationsMiddleToLast = vacationTableEntry.selectByDatesWithBackFlights(dateMiddle,dateLast);
        if (vacationsMiddleToLast.size() != 0){
            for (Vacation v : vacationsMiddleToLast)
                System.out.println(v.toString());
        }
    }

    private static void firstToMiddle(VacationTable vacationTableEntry, Date dateFirst, Date dateMiddle) {
        System.out.println();
        System.out.println("**** vacations First To Middle ****");
        ArrayList<Vacation> vacationsFirstToMiddle = vacationTableEntry.selectByDatesWithBackFlights(dateFirst,dateMiddle);
        if (vacationsFirstToMiddle.size() != 0){
            for (Vacation v : vacationsFirstToMiddle)
                System.out.println(v.toString());
        }
    }
    private static void printAllAvalibleVacations(VacationTable vacationTableEntry) {
        System.out.println();
        System.out.println("**** print All Avalible Vacations ****");
        ArrayList<Vacation> allAvalibleVacations = vacationTableEntry.getAllAvailableVacations();
        if (allAvalibleVacations.size() != 0){
            for (Vacation v : allAvalibleVacations)
                System.out.println(v.toString());
        }
    }
    private static void printAllAvalibleVacationsWithoutRegisteredUser(VacationTable vacationTableEntry) {
        System.out.println();
        System.out.println("**** print All Avalible Vacations Without Registered User ****");
        String registeredUser = "Liad";
        ArrayList<Vacation> allAvalibleVacations = vacationTableEntry.getAllAvailableVacations("Liad");
        if (allAvalibleVacations.size() != 0){
            for (Vacation v : allAvalibleVacations)
                System.out.println(v.toString());
        }
    }
}
