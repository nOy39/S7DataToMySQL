package app;

import com.sourceforge.snap7.moka7.IntByRef;
import com.sourceforge.snap7.moka7.S7;
import s7connector.S7Connect;

public class DispatcherPLC {

    private int                 dbAddres;
    private int                 dataRead;
    private static S7Connect    s7Connect;
    private static byte[]       buffer          = new byte[65536]; // 64K buffer (maximum for S7400 systems)
    private static int          dataToMove;

    public short                shortValue;
    public int                  intValue;
    public boolean              booleanValue;

    public DispatcherPLC(S7Connect s7Connect) {

        this.s7Connect = s7Connect;
        this.dbAddres = dbAddres;
        this.dataRead = dataRead;
    }



    public static boolean getDB(S7Connect s7Connect, int dbSample, byte[] buffer, int dataToMove) throws NullPointerException{
        IntByRef SizeRead = new IntByRef(0);

        int result = s7Connect.client.DBGet(dbSample, buffer, SizeRead);

        if (result==0)
        {
            dataToMove = SizeRead.Value; // Stores DB size for next test
//            System.out.println("DB "+dbSample+" - Size read "+dataToMove+" bytes");
            hexDump(buffer, dataToMove);
            return true;
        } else
        return false;
    }

    public int getIntData(int dbAdd, int bitAddres) {
        int value = 0;
        if (resultConnection(dbAdd) == 0) {
            return  S7.GetDIntAt(buffer,bitAddres);
        }

        return value = resultConnection(dbAdd);
    }

    public static short getShortData(int dbAdd, int bitAddres) {
        short value = 0;
        if (resultConnection(dbAdd) == 0) {
            value = (short) S7.GetShortAt(buffer,bitAddres);
        }
        return value;
    }


    public static int resultConnection(int dbAdd) {
        if (getDB(s7Connect, dbAdd, buffer, dataToMove)) {
            return s7Connect.client.ReadArea(S7.S7AreaDB, dbAdd, 0, dataToMove, buffer);
        } else {
            return -1;
        }

    }
    public static void hexDump(byte[] buffer, int size) {
        int r=0;
        String Hex = "";

        for (int i=0; i<size; i++)
        {
            int v = (buffer[i] & 0x0FF);
            String hv = Integer.toHexString(v);

            if (hv.length()==1)
                hv="0"+hv+" ";
            else
                hv=hv+" ";

            Hex=Hex+hv;

            r++;
            if (r==16)
            {
//                System.out.print(Hex+" ");
//                System.out.println(S7.GetPrintableStringAt(buffer, i-15, 16));
                Hex="";
                r=0;
            }
        }
        int L=Hex.length();
        if (L>0)
        {
            while (Hex.length()<49)
                Hex=Hex+" ";
//            System.out.print(Hex);
//            System.out.println(S7.GetPrintableStringAt(buffer, size-r, r));
        }
        else
            System.out.println();
    }






}
