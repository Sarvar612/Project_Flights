package org.example;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        JsonElement jsonElement= JsonParser.parseReader(new FileReader("src/main/resources/tickets.json"));
        JsonObject jsonObject=jsonElement.getAsJsonObject();
        Map<String,Integer> minFlights= new HashMap<>();
        JsonArray tickets=jsonObject.getAsJsonArray("tickets");
        ArrayList<Integer> prices=new ArrayList<>();

        int av_price=0;
        int median_price=0;
        for(JsonElement ticket:tickets){
            JsonObject tick=ticket.getAsJsonObject();
            String carrier = tick.get("carrier").getAsString();
            String origin = tick.get("origin").getAsString();
            String dest=tick.get("destination").getAsString();
            int price = tick.get("price").getAsInt();
            if(origin.equals("VVO")&&dest.equals("TLV")){
                prices.add(price);
                LocalTime depTime =LocalTime.parse(tick.get("departure_time").getAsString());
                LocalTime arrTime=LocalTime.parse(tick.get("arrival_time").getAsString());
                Duration duration = Duration.between(depTime,arrTime);
                int min = (int) duration.toMinutes();
                if(!minFlights.containsKey(carrier)|| min<minFlights.get(carrier)){
                    minFlights.put(carrier, min);
                }}
        }
            System.out.println("Минимальное время полета для каждого авиаперевозчика:");
            for (Map.Entry<String, Integer> entry : minFlights.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue() + " мин");
                //System.out.println(entry.getKey() + ": " +"час:"+entry.getValue()%3600+" мин:" +entry.getValue()%60);

            }
        prices.sort(Comparator.naturalOrder());
        for(int i=0;i<prices.size();i++){
            if(i == prices.size() / 2){
                median_price = prices.get(i)+prices.get(i+1);
            }
            av_price+=prices.get(i);
            }
        System.out.println("Средняя цена:"+av_price/prices.size());
        System.out.println("Медианная цена:"+median_price/2);
        System.out.println("Разница между ними:"+(av_price/prices.size()-median_price/2));

    }
}
