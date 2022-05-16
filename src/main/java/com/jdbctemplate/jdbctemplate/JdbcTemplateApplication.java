package com.jdbctemplate.jdbctemplate;

import com.jdbctemplate.jdbctemplate.model.Car;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

@SpringBootApplication
public class JdbcTemplateApplication {


    public static Connection connection = null;
    public static String databaseName = "jdbcproject";

    @Value("${spring.datasource.url}")
    public static String DB_URL = "jdbc:mysql://localhost:3306/" + databaseName;

    @Value("${spring.datasource.username}")
    public static String DB_USERNAME = "root";

    @Value("${spring.datasource.password}")
    public static String DB_PASSWORD = "C9ww933te4ryxj";

    public static void main(String[] args) throws SQLException {
        SpringApplication.run(JdbcTemplateApplication.class, args);

        // connect
        connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        Statement statement = connection.createStatement();

        Scanner input = new Scanner(System.in);

        int choice;
        while (true) {
            System.out.println("Options\n");
            System.out.print("0.) Create database \n");
            System.out.print("1.) Add car\n");
            System.out.print("2.) Select all cars\n");
            System.out.print("3.) Exit and close connection\n");

            choice = input.nextInt();

            switch (choice) {

                case 0:
                    // define query
                    String query = "create table car(id int primary key, model varchar(55))";
                    statement.execute(query);
                    break;
                case 1:
                    Random r = new Random();
                    int low = 10;
                    int high = 100;
                    int result = r.nextInt(high-low) + low;
                    String insert = "insert into car(id,model) values(" + result + ",'honda civic')";
                    statement.execute(insert);
                    System.out.println("Car inserted with id " + result);
                    break;
                case 2:
                    ResultSet resultSet = statement.executeQuery("select * from car");
                    List<Car> cars = new ArrayList<>();

                    while (resultSet.next()) {
                        System.out.println("Selecting all cars...");

                        int id = resultSet.getInt("id");
                        String model = resultSet.getString("model");
                        Car car = new Car(id, model);
                        cars.add(car);
                    }
                    System.out.println(cars);
                    break;
                case 3:
                    System.out.println("Exiting Program...");
                    try {
                        if (connection != null) {
                            connection.close();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    statement.close();
                    System.out.println("<----------FINISHED---------->");
                    System.exit(0);
                    break;
                default:
                    System.out.println("This is not a valid Menu Option! Please Select Another");
                    break;
            }
        }
    }
}
