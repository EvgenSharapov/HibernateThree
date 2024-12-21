package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.dao.CityDAO;
import com.javarush.dao.CountryDAO;
import com.javarush.domain.City;
import com.javarush.repository.RepositoryConnections;
import com.mysql.cj.xdevapi.SessionFactory;
import io.lettuce.core.RedisClient;
import org.hibernate.Session;
import java.util.ArrayList;
import java.util.List;


import static java.util.Objects.nonNull;

public class Main {

    private final RedisClient redisClient;

    private final ObjectMapper mapper;

    public Main() {
        redisClient = prepareRedisClient();
        mapper = new ObjectMapper();
    }

    public static void main(String[] args) {

        Main main = new Main();
        List<City> allCities = main.fetchData();
        main.shutdown();;
    }


    private List<City> fetchData() {
        try (Session session = RepositoryConnections.getInstance().getSessionFactory().getCurrentSession()) {
            List<City> allCities = new ArrayList<>();
            session.beginTransaction();
            CityDAO cityDAO = RepositoryConnections.getInstance().getCityDAO();

            int totalCount = cityDAO.getTotalCount();
            int step = 500;
            for (int i = 0; i < totalCount; i += step) {
                allCities.addAll(cityDAO.getItems(i, step));
            }
            session.getTransaction().commit();
            return allCities;
        }
    }

    private void shutdown() {
        if (nonNull(RepositoryConnections.getInstance().getSessionFactory())) {
            RepositoryConnections.getInstance().getSessionFactory().close();
        }
        if (nonNull(redisClient)) {
            redisClient.shutdown();
        }
    }

    private RedisClient prepareRedisClient(){
        return null;
    }



}