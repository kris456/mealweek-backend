create table shopping_list
(
    id            serial  not null
        constraint shopping_list_pk primary key,
    week_number   integer not null,
    year          integer not null,
    created_date  TIMESTAMP,
    created_by    integer,
    modified_date TIMESTAMP,
    modified_by   integer,
    constraint shopping_list_pk_2 unique (week_number, year)
);

create table shopping_item
(
    id               serial not null
        constraint item_pk primary key,
    name             varchar(255),
    shopping_list_id int    not null
        constraint FK_list_id references shopping_list (id),
    created_date     TIMESTAMP,
    created_by       integer,
    modified_date    TIMESTAMP,
    modified_by      integer,
    constraint shopping_item_pk unique (name)
);

create table meal
(
    id            serial not null
        constraint meal_pk primary key,
    name          varchar(255),
    created_date  TIMESTAMP,
    created_by    integer,
    modified_date TIMESTAMP,
    modified_by   integer,
    constraint meal_name_constraint unique (name)
);

create table mealPlan
(
    id            serial not null
        constraint meal_plan_pk primary key,
    weekNumber    int    not null,
    year          int    not null,
    created_date  TIMESTAMP,
    created_by    integer,
    modified_date TIMESTAMP,
    modified_by   integer
);

create table mealPlan_meals
(
    mealPlanId    int not null
        constraint FK_meal_plan references mealPlan (id),
    mealId        int not null
        constraint FK_meal references meal (id),
    created_date  TIMESTAMP,
    created_by    integer,
    modified_date TIMESTAMP,
    modified_by   integer,
    constraint mealPlan_meals_unique unique (mealId, mealPlanId)
)
