-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Erstellungszeit: 10. Jan 2020 um 09:25
-- Server-Version: 10.4.10-MariaDB
-- PHP-Version: 7.3.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Datenbank: `school`
--
CREATE DATABASE IF NOT EXISTS `school` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `school`;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `class`
--

DROP TABLE IF EXISTS `class`;
CREATE TABLE IF NOT EXISTS `class` (
  `class_name` varchar(4) NOT NULL,
  `class_id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`class_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `class`
--

INSERT INTO `class` (`class_name`, `class_id`) VALUES
('1a', 1),
('1b', 2),
('1c', 3),
('2a', 4);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `student`
--

DROP TABLE IF EXISTS `student`;
CREATE TABLE IF NOT EXISTS `student` (
  `firstname` varchar(75) NOT NULL,
  `surname` varchar(75) NOT NULL,
  `email` varchar(75) NOT NULL,
  `student_id` int(11) NOT NULL AUTO_INCREMENT,
  `fk_class_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`student_id`),
  KEY `fk_class_id` (`fk_class_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `student`
--

INSERT INTO `student` (`firstname`, `surname`, `email`, `student_id`, `fk_class_id`) VALUES
('Dan', 'Phelps', 'danny@yahoo.com', 1, 2),
('Kara ', 'Harper', 'harper@gmail.com', 2, 3),
('Cesar ', 'Curry', 'cc@com.com', 3, 4),
('Eula ', 'Quinn', 'QuinnE@gmail.com', 4, 3),
('Tabitha ', 'Holmes', 'TabiHolmes@gmx.at', 5, 1),
('Bridget ', 'Freeman', 'freeman123@gmx.at', 6, 4),
('Claude ', 'Hanson', 'ch123@spoi.com', 7, 1),
('Rogelio ', 'Vega', 'rv@gmail.com', 8, 1),
('Benny ', 'Garcia', 'msGarcia@gmail.com', 9, 2),
('Monique', 'Moran', 'MandM@gmx.at', 10, 3);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `teacher`
--

DROP TABLE IF EXISTS `teacher`;
CREATE TABLE IF NOT EXISTS `teacher` (
  `name` varchar(75) NOT NULL,
  `surname` varchar(75) DEFAULT NULL,
  `email` varchar(75) DEFAULT NULL,
  `teacher_id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`teacher_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `teacher`
--

INSERT INTO `teacher` (`name`, `surname`, `email`, `teacher_id`) VALUES
('James', 'Bond', 'jamesbond@gmx.at', 1),
('Jonny', 'Depp', 'jd@gmx.at', 2),
('Captain', 'Marvel', 'cM@gmail.com', 3),
('Mister', 'Meeseeks', 'existencemeanspain@gmail.com', 4);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `teaching`
--

DROP TABLE IF EXISTS `teaching`;
CREATE TABLE IF NOT EXISTS `teaching` (
  `fk_teacher_id` int(11) DEFAULT NULL,
  `fk_class_id` int(11) DEFAULT NULL,
  KEY `fk_teacher_id` (`fk_teacher_id`),
  KEY `fk_class_id` (`fk_class_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Daten für Tabelle `teaching`
--

INSERT INTO `teaching` (`fk_teacher_id`, `fk_class_id`) VALUES
(3, 2),
(3, 1),
(2, 3),
(4, 3),
(1, 4),
(1, 3);

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `student`
--
ALTER TABLE `student`
  ADD CONSTRAINT `student_ibfk_1` FOREIGN KEY (`fk_class_id`) REFERENCES `class` (`class_id`);

--
-- Constraints der Tabelle `teaching`
--
ALTER TABLE `teaching`
  ADD CONSTRAINT `teaching_ibfk_1` FOREIGN KEY (`fk_teacher_id`) REFERENCES `teacher` (`teacher_id`),
  ADD CONSTRAINT `teaching_ibfk_2` FOREIGN KEY (`fk_class_id`) REFERENCES `class` (`class_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
