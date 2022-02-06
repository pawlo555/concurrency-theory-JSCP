package utils;

import java.util.Collection;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

public class BufferObserver {

    private final Map<Integer, Integer> successfulActionMap = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> failActionMap = new ConcurrentHashMap<>();

    public BufferObserver(int period) {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                showStatistics();
            }
        }, period, period);
    }

    public void addBuffer(int id) {
        successfulActionMap.put(id, 0);
        failActionMap.put(id, 0);
    }

    public void actionSuccess(int id) {
        int value = successfulActionMap.get(id);
        successfulActionMap.put(id, value+1);
    }

    public void actionFail(int id) {
        int value = failActionMap.get(id);
        failActionMap.put(id, value+1);
    }

    private void showStatistics() {
        System.out.println("Successful actions:");
        System.out.println(successfulActionMap);
        System.out.println(getTotal(successfulActionMap.values()));
        System.out.println("Failed actions:");
        System.out.println(failActionMap);
        System.out.println(getTotal(failActionMap.values()));
    }

    private int getTotal(Collection<Integer> values) {
        int total = 0;
        for (Integer i: values) {
            total += i;
        }
        return total;
    }
}
