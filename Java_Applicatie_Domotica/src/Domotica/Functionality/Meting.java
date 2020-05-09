package Domotica.Functionality;

public class Meting {
    private float waarde;
    private final String naam;

    public Meting(String naam) {
        this.naam = naam;
    }

    public void setWaarde(float waarde) {
        this.waarde = waarde;
    }

    public float getWaarde() {
        return waarde;
    }

    public String getNaam() {
        return naam;
    }
}
