package app;

import dbconnector.DataBaseHandler;
import helpers.Refrigerator;
import s7connector.S7Connect;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main {

    private static S7Connect s7Connect;

    private static ArrayList<Refrigerator> refList = new ArrayList<>();

    private static DataBaseHandler dbHand = new DataBaseHandler();
    private static boolean isRunning = true;
    private static boolean s7FaultConnection = false;

    public static void main(String[] args) throws InterruptedException {

        if (args.length!=3) {
            consoleMessageHowToStart();
        } else {
                startApp(args[0], args[1], args[2]);
        }

    }

    private static void startApp(String arg, String arg1, String arg2) throws InterruptedException {

            s7Connect = new S7Connect(arg, Integer.parseInt(arg1), Integer.parseInt(arg2));
            System.out.println(s7Connect);
            if (!s7Connect.faultConnection) {
                dispatcherPLC(s7Connect);
            } else {
                Thread.sleep(10000);
                startApp(arg, arg1, arg2);
            }


        }

    private static void dispatcherPLC(S7Connect s7Connect) {

        Refrigerator ref = new Refrigerator();
        long workingStatus = 0;

        DispatcherPLC plcReadDB = new DispatcherPLC(s7Connect);
        DispatcherPLC plcReadDB10 = new DispatcherPLC(s7Connect);


        while (isRunning && !s7Connect.faultConnection) {
            if (workingStatus > plcReadDB.getIntData(9,82)
                    && plcReadDB.getIntData(9,82) == 0) {
                workingStatus = 0;
            }

            if (workingStatus < plcReadDB.getIntData(9,82) ) {
                ref = buildReffObj(plcReadDB.getShortData(10,102),
                        plcReadDB.getIntData(9,12),
                        plcReadDB.getIntData(9,0));
                dbHand.setRefToBase(ref);

                workingStatus = plcReadDB.getIntData(9,82);
            }

            try {
                Thread.sleep(500);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void consoleMessageHowToStart() {

        System.out.println("Usage:");
        System.out.println("    <APPLICATIONNAME> <PLC_IP> <RACK> <SLOT>");
        System.out.println("Example:");
        System.out.println("    application.java 10.10.0.1 0 2");
        System.out.println("    +--------------+ +-------+ + +");
        System.out.println("                   |         | | |");
        System.out.println("                   |         | | +--<Slot>");
        System.out.println("                   |         | +----<Rack>");
        System.out.println("                   |         +------<PLC_IP>");
        System.out.println("                   +----------------<Application name>");
    }


    private static Refrigerator buildReffObj (int code, int expand_pol, int expand_iso) {

        SimpleDateFormat dateFormatYMD = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormatHMS = new SimpleDateFormat("HH:mm:ss");

        Refrigerator refCabin = new Refrigerator();

        refCabin.setCode((short) code);
        refCabin.setPol_expand(expand_pol);
        refCabin.setIso_expand(expand_iso);

        refCabin.setDate(dateFormatYMD.format(new Date()));
        refCabin.setTime(dateFormatHMS.format(new Date()));

        System.out.println(refCabin);

        DataBaseHandler dbHandler = new DataBaseHandler();

        return refCabin;
    }
}
