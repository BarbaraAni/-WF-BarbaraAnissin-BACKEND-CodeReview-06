--shows all
SELECT surname, class_name
FROM `student`
INNER  JOIN `class` on fk_class_id =  class_id;

--shows by class_id and class_name filter
SELECT student.surname, class.class_name
FROM `student`
INNER  JOIN `class` on fk_class_id =  class_id
WHERE class_name = "1b" AND class_id = 2

--shows by class_name filter
SELECT student.surname, class.class_name
FROM `student`
INNER  JOIN `class` on fk_class_id =  class_id
WHERE class_name = "1b"

--shows by class_id filter
SELECT student.surname, class.class_name
FROM `student`
INNER  JOIN `class` on fk_class_id =  class_id
WHERE class_id = 2



--(20) JavaFX: Display the list of the teachers in a ListView, showing the teacher’s data as Surname,
--Name in the ListView . Once a teacher’s name is selected, you need to populate teacher’s data in the field
--to the left (see the mockup below)

--Note: All the data are fetched from the database on the start of the application.
--Additional  Points:(20) Add a 2nd ListView.  Once a teachers name/surname is selected in the list of teachers, you need to DYNAMICALLY (i.e send an SQL to the database) query all the classes that that teacher teaches and display their names in the 2nd list  (see the mock-up below)
