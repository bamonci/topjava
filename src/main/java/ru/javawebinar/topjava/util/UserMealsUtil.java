package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        LocalTime dateStart = LocalTime.of(7, 0);
        LocalTime dateEnd = LocalTime.of(12, 0);
        Integer caloriesPerDay = 2000;

        filteredByCycles(meals, dateStart, dateEnd, caloriesPerDay).forEach(System.out::println);

        System.out.println("=============");

        filteredByStreams(meals, dateStart, dateEnd, caloriesPerDay).forEach(System.out::println);

        System.out.println("=============");

        filteredByCycles2(meals, dateStart, dateEnd, caloriesPerDay).forEach(System.out::println);
    }

    public static List<MealTo> filteredByCycles(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> excessList = new HashMap<>();
        meals.forEach(mealItem -> {
            excessList.merge(mealItem.getDateTime().toLocalDate(), mealItem.getCalories(), Integer::sum);
        });

        List<MealTo> resultMeals = new ArrayList<>();
        meals.forEach(mealItem -> {
            if(TimeUtil.isBetweenInclusive(mealItem.getDateTime().toLocalTime(), startTime, endTime)){
                resultMeals.add(new MealTo(mealItem.getDateTime(), mealItem.getDescription(), mealItem.getCalories(), excessList.get(mealItem.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        });

        return resultMeals;
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
         Map<LocalDate, Integer> caloriesSumByDate = meals.stream()
             .collect(Collectors.groupingBy(um -> um.getDateTime().toLocalDate(), Collectors.summingInt(Meal::getCalories)));

        return meals.stream()
            .filter(mealItem -> TimeUtil.isBetweenInclusive(mealItem.getDateTime().toLocalTime(), startTime, endTime))
            .map(mealItem -> new MealTo(mealItem.getDateTime(), mealItem.getDescription(), mealItem.getCalories(),
                caloriesSumByDate.get(mealItem.getDateTime().toLocalDate()) > caloriesPerDay
            ))
            .collect(Collectors.toList());
    }

    public static List<MealTo> filteredByCycles2(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> excessList = new HashMap<>();
        for (Meal meal : meals){
            LocalDate lDate= meal.getDateTime().toLocalDate();
            excessList.put(lDate, excessList.getOrDefault(lDate, 0) + meal.getCalories());
        }

        List<MealTo> resultMeals = new ArrayList<>();
        for (Meal mealItem : meals){
            if(TimeUtil.isBetweenInclusive(mealItem.getDateTime().toLocalTime(), startTime, endTime)) {
                resultMeals.add(new MealTo(mealItem.getDateTime(), mealItem.getDescription(), mealItem.getCalories(), excessList.getOrDefault(mealItem.getDateTime().toLocalDate(), 0) > caloriesPerDay));
            }
        }

        return resultMeals;
    }
}
