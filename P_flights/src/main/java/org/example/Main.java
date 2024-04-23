package org.example;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Main {
    public static void main(String[] args) throws FileNotFoundException, ParseException {
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

                String departureDate = tick.get("departure_date").getAsString();
                String arrivalDate = tick.get("arrival_date").getAsString();

                LocalTime depTime = LocalTime.parse(tick.get("departure_time").getAsString());
                LocalTime arrTime = LocalTime.parse(tick.get("arrival_time").getAsString());

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

                LocalDateTime departureDateTime = LocalDateTime.parse(departureDate + " " + depTime, formatter);
                LocalDateTime arrivalDateTime = LocalDateTime.parse(arrivalDate + " " + arrTime, formatter);

                Duration duration = Duration.between(departureDateTime, arrivalDateTime);
                long hours = duration.toHours();
                long minutes = duration.toMinutes() % 60;
                int min = (int) (hours * 60 + minutes);

                if(!minFlights.containsKey(carrier)|| min<minFlights.get(carrier)){
                    minFlights.put(carrier, min);
                }}
        }
            System.out.println("Минимальное время полета для каждого авиаперевозчика:");
            for (Map.Entry<String, Integer> entry : minFlights.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue() + " мин");
                //System.out.println(entry.getKey() + ": " +"час:"+entry.getValue()/60+" мин:" +entry.getValue()%60);

            }
        prices.sort(Comparator.naturalOrder());
        for(int i=0;i<prices.size();i++){
            if(i == prices.size() / 2-1){
                median_price = prices.get(i)+prices.get(i+1);

            }
            av_price+=prices.get(i);
            }
        System.out.println("Средняя цена:"+av_price/prices.size());
        System.out.println("Медианная цена:"+median_price/2);
        System.out.println("Разница между ними:"+(av_price/prices.size()-median_price/2));

    }
}
