ALTER TABLE meal DROP CONSTRAINT meal_name_constraint;

CREATE UNIQUE INDEX meal_name_user_constraint
    ON meal(name, created_by);
