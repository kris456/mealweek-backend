ALTER TABLE mealplan_meals
    ADD COLUMN weekday_iso int not null default 1;

ALTER TABLE mealplan_meals
    DROP CONSTRAINT mealplan_meals_unique;

ALTER TABLE mealplan_meals
    RENAME COLUMN mealplanid to mealplan;

ALTER TABLE mealplan_meals
    RENAME COLUMN mealid to meal;


CREATE UNIQUE INDEX mealplan_meals_day_unique
    ON mealplan_meals(mealplan, meal, weekday_iso);
