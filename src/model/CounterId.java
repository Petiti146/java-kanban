package model;

public  class CounterId {
    private static int id;
    public static int getId () {
        return ++id;
    }
}
