package dipdip.android.dip.com.hanun.Model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class HomeModel {
    public String nama;
    public String usia;
    public String token;
    public String uid;

    public HomeModel() {
    }

    public HomeModel(String nama, String usia, String token, String uid) {
        this.nama = nama;
        this.usia = usia;
        this.token = token;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUsia() {
        return usia;
    }

    public void setUsia(String usia) {
        this.usia = usia;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}