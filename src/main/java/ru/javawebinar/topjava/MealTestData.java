package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {
    public static final int USER_MEAL_ID = 100_002;
    public static final int NOT_EXISTING_USER_MEAL_ID = 100_001;
    public static final int ADMIN_MEAL_ID = 100_006;

    public static final Meal MEAL1 = new Meal(USER_MEAL_ID, LocalDateTime.of(2020, 1, 30, 7, 0), "User Breakfast", 700);
    public static final Meal MEAL2 = new Meal(USER_MEAL_ID + 1, LocalDateTime.of(2020, 1, 30, 12, 0), "User lunch", 1000);
    public static final Meal MEAL3 = new Meal(USER_MEAL_ID + 2, LocalDateTime.of(2020, 1, 30, 19, 0), "User dinner", 300);
    public static final Meal MEAL4 = new Meal(USER_MEAL_ID + 3, LocalDateTime.of(2020, 1, 31, 0, 0), "User border meal", 300);
    public static final Meal MEAL5 = new Meal(ADMIN_MEAL_ID, LocalDateTime.of(2020, 1, 31, 7, 0), "Admin breakfast", 300);
    public static final Meal MEAL6 = new Meal(ADMIN_MEAL_ID + 1, LocalDateTime.of(2020, 1, 31, 14, 0), "Admin lunch", 600);
    public static final Meal MEAL7 = new Meal(ADMIN_MEAL_ID + 2, LocalDateTime.of(2020, 1, 31, 22, 0), "Admin dinner", 500);

    public static final List<Meal> USER_MEALS = Arrays.asList(MEAL7, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2021, 1, 30, 7, 0), "New Meal", 777);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(MEAL1);
        updated.setDateTime(LocalDateTime.of(2021, 1, 30, 7, 0));
        updated.setDescription("updated meal");
        updated.setCalories(555);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

}
