INSERT INTO public.meal (id, name, created_date, created_by, modified_date, modified_by) VALUES (1, 'test', '2022-08-27 00:13:54.746885', 2, '2022-08-27 00:13:54.746885', 2);
INSERT INTO public.meal (id, name, created_date, created_by, modified_date, modified_by) VALUES (2, 'pizza', '2022-08-27 00:25:47.674787', 2, '2022-08-27 00:25:47.674787', 2);
INSERT INTO public.meal (id, name, created_date, created_by, modified_date, modified_by) VALUES (3, 'taco', '2022-08-27 00:30:11.926064', 2, '2022-08-27 00:30:11.926064', 2);
INSERT INTO public.meal (id, name, created_date, created_by, modified_date, modified_by) VALUES (4, 'noe annet', '2022-08-27 00:30:15.253578', 2, '2022-08-27 00:30:15.253578', 2);
INSERT INTO public.meal (id, name, created_date, created_by, modified_date, modified_by) VALUES (5, 'hello', '2022-08-27 00:30:18.619135', 2, '2022-08-27 00:30:18.619135', 2);
INSERT INTO public.meal (id, name, created_date, created_by, modified_date, modified_by) VALUES (6, 'lll', '2022-08-27 00:30:23.803349', 2, '2022-08-27 00:30:23.803349', 2);
INSERT INTO public.meal (id, name, created_date, created_by, modified_date, modified_by) VALUES (7, 'l', '2022-08-27 00:30:25.687516', 2, '2022-08-27 00:30:25.687516', 2);
INSERT INTO public.meal (id, name, created_date, created_by, modified_date, modified_by) VALUES (8, 'tes11', '2022-08-27 00:31:54.211191', 2, '2022-08-27 00:31:54.211191', 2);
INSERT INTO public.meal (id, name, created_date, created_by, modified_date, modified_by) VALUES (9, 'tes2', '2022-08-27 00:31:56.388214', 2, '2022-08-27 00:31:56.388214', 2);
INSERT INTO public.meal (id, name, created_date, created_by, modified_date, modified_by) VALUES (10, 'indrefilet av biff', '2022-08-27 00:32:02.275706', 2, '2022-08-27 00:32:02.275706', 2);
INSERT INTO public.meal (id, name, created_date, created_by, modified_date, modified_by) VALUES (11, 'Kylling og ris', '2022-08-27 00:32:20.361772', 2, '2022-08-27 00:32:20.361772', 2);
INSERT INTO public.meal (id, name, created_date, created_by, modified_date, modified_by) VALUES (12, 'Wok med kylling', '2022-08-27 00:32:26.610742', 2, '2022-08-27 00:32:26.610742', 2);
INSERT INTO public.meal (id, name, created_date, created_by, modified_date, modified_by) VALUES (13, 'Lapskaus', '2022-08-27 00:32:31.400226', 2, '2022-08-27 00:32:31.400226', 2);
INSERT INTO public.meal (id, name, created_date, created_by, modified_date, modified_by) VALUES (14, 'Pølse med brød', '2022-08-27 00:32:50.907956', 2, '2022-08-27 00:32:50.907956', 2);
INSERT INTO public.meal (id, name, created_date, created_by, modified_date, modified_by) VALUES (15, 'Laks i wrap', '2022-08-27 21:00:46.283609', 2, '2022-08-27 21:00:46.283609', 2);

ALTER SEQUENCE meal_id_seq RESTART WITH 100;

INSERT INTO public.mealplan (id, weeknumber, year, created_date, created_by, modified_date, modified_by) VALUES (1, '1','2022', '2022-08-27 21:00:46.283609', 2, '2022-08-27 21:00:46.283609', 2);
ALTER SEQUENCE mealplan_id_seq RESTART WITH 100;
