package s7connector;

import com.sourceforge.snap7.moka7.S7;
import com.sourceforge.snap7.moka7.S7Client;
import com.sourceforge.snap7.moka7.S7CpuInfo;
import com.sourceforge.snap7.moka7.S7OrderCode;

public class S7Connect {

    public boolean faultConnection = true;
    public static final S7Client client = new S7Client();

    private static long Elapsed;
    private int faultCount;
    private int workCount;


    public S7Connect(String plcIp, int rack, int slot) {

        int result = -1;

        testBegin("Connection To()");

            client.SetConnectionType(S7.OP);

        if (client.ConnectTo(plcIp, rack, slot)==0) {
            faultConnection = false;
            System.out.println("Connected to   : " + plcIp + " (Rack=" + rack + ", Slot=" + slot + ")");
            System.out.println("PDU negotiated : " + client.PDULength() + " bytes");
            testEnd(result);
            plcInfo();
        } else {

            error(result);
        }


    }

    static void testBegin(String functionName) {
        {
            System.out.println();
            System.out.println("+===============================================================+");
            System.out.println("| "+functionName);
            System.out.println("+===============================================================+");
            Elapsed = System.currentTimeMillis();
        }
    }

    private void testEnd(int result) {
        if (result!=0)
        {
            faultCount++;
            error(result);
        }
        else
            workCount++;
        System.out.println("Execution time "+(System.currentTimeMillis()-Elapsed)+" ms");
    }

    private void error(int code) {
        System.out.println(S7Client.ErrorText(code));
    }

    public void plcInfo(){
        testBegin("OrderCode()");
        S7OrderCode s7OrderCode = new S7OrderCode();
        int orderCode = client.GetOrderCode(s7OrderCode);
        if (orderCode == 0) {
            System.out.println("Order Code        : "+s7OrderCode.Code());
            System.out.println("Firmware version  : "+s7OrderCode.V1+"."+s7OrderCode.V2+"."+s7OrderCode.V3);
        }
        testEnd(orderCode);

        testBegin("CpuInfo()");
        S7CpuInfo s7CpuInfo = new S7CpuInfo();
        int cpuInfo = client.GetCpuInfo(s7CpuInfo);
        if (cpuInfo==0) {
            System.out.println("Module Type Name  : "+s7CpuInfo.ModuleTypeName());
            System.out.println("Serial Number     : "+s7CpuInfo.SerialNumber());
            System.out.println("AS Name           : "+s7CpuInfo.ASName());
            System.out.println("CopyRight         : "+s7CpuInfo.Copyright());
            System.out.println("Module Name       : "+s7CpuInfo.ModuleName());
        }
        testEnd(cpuInfo);

    }

    @Override
    public String toString() {
        return "S7Connect{" +
                "faultConnection=" + faultConnection +
                ", faultCount=" + faultCount +
                ", workCount=" + workCount +
                '}';
    }
}
