package helpers;

import java.util.Date;

public class Refrigerator {

    private short code;
    private int pol_expand;
    private int iso_expand;
    private String date;
    private String time;

    public Refrigerator(/*int code, int pol_expand, int iso_expand, String date, String time*/) {
        this.code = code;
        this.pol_expand = pol_expand;
        this.iso_expand = iso_expand;
        this.date = date;
        this.time = time;
    }

    public short getCode() {
        return code;
    }

    public void setCode(short code) {
        this.code = code;
    }

    public int getPol_expand() {
        return pol_expand;
    }

    public void setPol_expand(int pol_expand) {
        this.pol_expand = pol_expand;
    }

    public int getIso_expand() {
        return iso_expand;
    }

    public void setIso_expand(int iso_expand) {
        this.iso_expand = iso_expand;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Refrigerator{" +
                "code=" + code +
                ", pol_expand=" + pol_expand +
                ", iso_expand=" + iso_expand +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
