package ru.javawebinar.topjava.repository.inmemory;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepository implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(x -> save(InMemoryUserRepository.USER_ID, x));
        save(InMemoryUserRepository.ADMIN_ID, new Meal(LocalDateTime.now(), "Current meal", 500));
    }

    @Override
    public Meal save(int userId, Meal meal) {
        Map<Integer, Meal> userMeal = repository.computeIfAbsent(userId, ConcurrentHashMap::new);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            userMeal.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return userMeal.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int userId, int id) {
        Map<Integer, Meal> userMeal = repository.get(userId);
        return userMeal != null && userMeal.remove(id) != null;
    }

    @Override
    public Meal get(int userId, int id) {
        Map<Integer, Meal> userMeal = repository.get(userId);
        if (userMeal == null) return null;
        return userMeal.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        Map<Integer, Meal> userMeal = repository.get(userId);
        return userMeal == null ? Collections.emptyList() : userMeal.values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}

