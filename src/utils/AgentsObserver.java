package utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AgentsObserver {
    private final Map<Integer, Integer> normalWorkMap = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> asksToBuffer = new ConcurrentHashMap<>();
    private final Map<Integer, Integer> asksToSplitter = new ConcurrentHashMap<>();

    public AgentsObserver(int period) {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                showStatistics();
            }
        }, period, period);
    }

    public void addUser(int userId) {
        normalWorkMap.put(userId, 0);
        asksToSplitter.put(userId, 0);
        asksToBuffer.put(userId, 0);
    }

    public void normalWorkDone(int userId) {
        int prevValue = normalWorkMap.get(userId);
        normalWorkMap.put(userId, prevValue+1);
    }

    public void askToSplitter(int userId) {
        int prevValue = asksToSplitter.get(userId);
        asksToSplitter.put(userId, prevValue+1);
    }

    public void askToBuffer(int userId) {
        int prevValue = asksToBuffer.get(userId);
        asksToBuffer.put(userId, prevValue+1);
    }

    private void showStatistics() {
        System.out.println("Work stats:");
        printStatisticsAboutHashMap(normalWorkMap);
        System.out.println("Communication with splitter stats:");
        printStatisticsAboutHashMap(asksToSplitter);
        System.out.println("Communication with buffer stats:");
        printStatisticsAboutHashMap(asksToBuffer);
        System.out.println();
    }

    private void printStatisticsAboutHashMap(Map<Integer, Integer> map) {
        Collection<Integer> values = map.values();
        int min = Collections.min(values);
        int max = Collections.max(values);
        int average = getAverageExtraWork(values);
        System.out.println("Average: " + average);
        System.out.println("Min: " + min);
        System.out.println("Max: " + max);
        System.out.println(values);
    }

    private int getAverageExtraWork(Collection<Integer> values) {
        int total = 0;
        for (Integer i: values) {
            total += i;
        }
        return total/values.size();
    }
}
