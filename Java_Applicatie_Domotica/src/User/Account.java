package User;

import Communication.Database;

import java.util.ArrayList;

public class Account {
    private static int accountid;
    private static float lichtVK;
    private static float temperatureVK;
    private static String username;

    public static void setAccountid(int accountid) {
        Account.accountid = accountid;
    }

    public static int getAccountid() {
        return accountid;
    }

    public static float getLichtVK() {
        return lichtVK;
    }

    public static float getTemperatureVK() {
        return temperatureVK;
    }

    public static boolean updatePref(String field, float waarde) {
        String query = "UPDATE account SET " + field + "=" + waarde + " WHERE id=" + accountid + ";";
        int rows = Database.executeUpdate(query);
        if (rows == 1) {
            if (field.equals("lichtVK")) {
                Account.lichtVK = waarde;
                return true;
            } else if (field.equals("temperatuurVK")) {
                Account.temperatureVK = waarde;
                return true;
            }
        }
        return false;
    }

    public static void getPref() {
        String query = "SELECT temperatuurVK, lichtVK FROM account WHERE id=" + accountid + ";";
        ArrayList<ArrayList<String>> result = Database.executeQuery(query);

        try {
            Account.temperatureVK = Float.valueOf(result.get(0).get(0));
            Account.lichtVK = Float.valueOf(result.get(0).get(1));
        } catch (NumberFormatException nfe) {
            System.out.println(nfe.getMessage());
        } catch (IndexOutOfBoundsException ioobe) {
            System.out.println(ioobe.getMessage());
        }
    }

    public static void setIdentity(String username) {
        Account.username = username;
    }

    public static String getUsername() {
        return username;
    }
}
