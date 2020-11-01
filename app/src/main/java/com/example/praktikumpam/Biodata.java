package com.example.praktikumpam;
public class Biodata {
    private String nama;
    private String jeniskelamin;

    public Biodata(String nama, String jeniskelamin){
        this.nama = nama;
        this.jeniskelamin = jeniskelamin;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJeniskelamin() {
        return jeniskelamin;
    }

    public void setJeniskelamin(String jeniskelamin) {
        this.jeniskelamin = jeniskelamin;
    }
}




