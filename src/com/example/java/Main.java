package com.example.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) {
	// write your code here
        Scanner scanner = new Scanner(System.in);
        List<String> trades = new ArrayList();
        System.out.println("Print information about trades. To start calculation, print 'calculate'");
        String nextTradeInfo = scanner.next();
        List<Time> timeList = new ArrayList<>();
        List<String> exchangeList = new ArrayList<>();
        do {
            String[] tradeInfo = nextTradeInfo.split(",");
            String[] timeData = tradeInfo[0].split(":");
            String[] secondMillisecond = timeData[2].split("\\.");
            timeList.add(new Time(Integer.parseInt(timeData[0]), Integer.parseInt(timeData[1]),
            Integer.parseInt(secondMillisecond[0]), Integer.parseInt(secondMillisecond[1])));
            exchangeList.add(tradeInfo[3]);
            nextTradeInfo = scanner.next();
        } while (!nextTradeInfo.equals("calculate"));

        LinkedList<HashMap<String, Integer>> amountExchangeStack = new LinkedList();
        LinkedList<Time> timeStack = new LinkedList();
        Time lessMinute = timeList.get(0);
        HashMap<String, Integer> amountOfExchanges = new HashMap();
        Map<String, Integer> result = new TreeMap();
        for (int i = 0; i < timeList.size(); i++) {
            if (timeList.get(i).compareTo(lessMinute) >= 60*1000) {
                for (Map.Entry<String, Integer> entry : amountOfExchanges.entrySet()) {
                    String exchange = entry.getKey();
                    Integer amount = entry.getValue();
                    if (!result.containsKey(exchange) || amountOfExchanges.get(exchange) > result.get(exchange)) {
                        result.put(exchange, amount);
                    }
                }
                lessMinute = timeStack.poll();
                amountOfExchanges = amountExchangeStack.poll();
            }
            String exchange = exchangeList.get(i);
            if (!amountOfExchanges.containsKey(exchange)) {
                amountOfExchanges.put(exchange, 1);
            } else {
                amountOfExchanges.put(exchange, amountOfExchanges.get(exchange)+1);
            }
            amountExchangeStack.add((HashMap) amountOfExchanges.clone());
            timeStack.add(timeList.get(i));
        }
        for (Map.Entry<String, Integer> entry : amountOfExchanges.entrySet()) {
            String exchange = entry.getKey();
            Integer amount = entry.getValue();
            if (!result.containsKey(exchange) || amountOfExchanges.get(exchange) > result.get(exchange)) {
                result.put(exchange, amount);
            }
        }
        result.forEach((exchange, amount) -> {
            System.out.println(amount);
        });
    }
}

class Time implements Comparable<Time> {
    private int hour;
    private int minute;
    private int second;
    private int millisecond;

    public Time(int hour, int minute, int second, int millisecond) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
        this.millisecond = millisecond;
    }

    @Override
    public int compareTo(Time time) {
        return (this.millisecond-time.millisecond)+(this.second-time.second)*1000+
                (this.minute-time.minute)*60*1000+(this.hour-time.hour)*60*60*1000;
    }
}
