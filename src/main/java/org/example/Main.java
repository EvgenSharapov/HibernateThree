package org.example;

import com.javarush.domain.City;
import com.javarush.redis.CityCountry;
import com.javarush.repository.RepositoryConnections;
import com.javarush.service.DataService;
import java.util.List;



public class Main {


    public static void main(String[] args) {
        DataService dataService = new DataService();
        List<City> allCities = dataService.fetchData();
        List<CityCountry> preparedData = dataService.transformData(allCities);
        dataService.pushToRedis(preparedData);
        RepositoryConnections.getInstance().getSessionFactory().getCurrentSession().close();
        List<Integer> ids = List.of(3, 2545, 123, 4, 189, 89, 3458, 1189, 10, 102);

        long startRedis = System.currentTimeMillis();
        dataService.testRedisData(ids);
        long stopRedis = System.currentTimeMillis();

        long startMysql = System.currentTimeMillis();
        dataService.testMysqlData(ids);
        long stopMysql = System.currentTimeMillis();

        System.out.printf("%s:\t%d ms\n", "Redis", (stopRedis - startRedis));
        System.out.printf("%s:\t%d ms\n", "MySQL", (stopMysql - startMysql));

        dataService.shutdown();


    }









}