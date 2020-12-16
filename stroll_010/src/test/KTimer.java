package test;

public class KTimer {
    private final long startTime;

    public KTimer() {
        this.startTime = System.currentTimeMillis();
    }

    public KTimer print(String s) {
        long diff = (System.currentTimeMillis() - this.startTime);
        System.out.println(s + ": " + diff);
        return new KTimer();
    }
}
