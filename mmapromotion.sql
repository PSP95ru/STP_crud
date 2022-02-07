-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Хост: 127.0.0.1
-- Время создания: Фев 07 2022 г., 08:46
-- Версия сервера: 10.4.22-MariaDB
-- Версия PHP: 8.1.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- База данных: `mmapromotion`
--
CREATE DATABASE IF NOT EXISTS `mmapromotion` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `mmapromotion`;

-- --------------------------------------------------------

--
-- Структура таблицы `cardpostion`
--

CREATE TABLE `cardpostion` (
  `id` bigint(20) NOT NULL,
  `PPV_id` bigint(20) DEFAULT NULL,
  `number_in_show` int(11) NOT NULL,
  `Length` int(11) NOT NULL,
  `title_name` varchar(60) NOT NULL,
  `Winner` varchar(60) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `cardpostion`
--

INSERT INTO `cardpostion` (`id`, `PPV_id`, `number_in_show`, `Length`, `title_name`, `Winner`) VALUES
(12, 8, 1, 5, 'wohooster', 'miller'),
(13, 8, 2, 23, 'grelik', 'asper'),
(14, 8, 12, 5, 'boec', 'Nevskius'),
(15, 8, 6, 4, 'tester', 'testirovshik'),
(16, 3, 1, 5, 'geratron', 'reflexer'),
(21, 2, 1, 3, 'test1', 'tester1'),
(22, 2, 2, 3, 'test112', 'tester1'),
(23, 62, 8, 3, 'WNC', 'Meruzinov');

-- --------------------------------------------------------

--
-- Структура таблицы `fighter`
--

CREATE TABLE `fighter` (
  `id` bigint(20) NOT NULL,
  `Card_id` bigint(20) DEFAULT NULL,
  `first_name` varchar(60) NOT NULL,
  `second_name` varchar(60) NOT NULL,
  `Nationality` varchar(60) NOT NULL,
  `Age` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `fighter`
--

INSERT INTO `fighter` (`id`, `Card_id`, `first_name`, `second_name`, `Nationality`, `Age`) VALUES
(3, 12, 'Habib', 'Burliev', 'Adygeec', 23),
(5, 21, 'Vasily', 'Murov', 'red', 5),
(6, 21, 'Petr', 'Grelin', 'Adygeec', 3),
(7, 15, 'Vasily', 'Grelin', 'Adygeec', 4);

-- --------------------------------------------------------

--
-- Структура таблицы `ppv`
--

CREATE TABLE `ppv` (
  `id` bigint(20) NOT NULL,
  `name` varchar(75) NOT NULL,
  `date` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Дамп данных таблицы `ppv`
--

INSERT INTO `ppv` (`id`, `name`, `date`) VALUES
(2, 'Galactic contest 2', '2025-06-05 05:00:00'),
(3, 'Local brawl', '2022-05-03 20:04:31'),
(8, 'Master League 7', '2021-12-25 05:00:00'),
(27, 'Boxcraft', '2024-09-05 05:00:00'),
(62, 'brawl league 4', '2024-06-04 05:00:00');

--
-- Индексы сохранённых таблиц
--

--
-- Индексы таблицы `cardpostion`
--
ALTER TABLE `cardpostion`
  ADD PRIMARY KEY (`id`),
  ADD KEY `Card_FK` (`PPV_id`);

--
-- Индексы таблицы `fighter`
--
ALTER TABLE `fighter`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fighter_cardpostion_id_fk` (`Card_id`);

--
-- Индексы таблицы `ppv`
--
ALTER TABLE `ppv`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- AUTO_INCREMENT для сохранённых таблиц
--

--
-- AUTO_INCREMENT для таблицы `cardpostion`
--
ALTER TABLE `cardpostion`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=24;

--
-- AUTO_INCREMENT для таблицы `fighter`
--
ALTER TABLE `fighter`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT для таблицы `ppv`
--
ALTER TABLE `ppv`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=63;

--
-- Ограничения внешнего ключа сохраненных таблиц
--

--
-- Ограничения внешнего ключа таблицы `cardpostion`
--
ALTER TABLE `cardpostion`
  ADD CONSTRAINT `Card_FK` FOREIGN KEY (`PPV_id`) REFERENCES `ppv` (`id`) ON DELETE CASCADE;

--
-- Ограничения внешнего ключа таблицы `fighter`
--
ALTER TABLE `fighter`
  ADD CONSTRAINT `fighter_cardpostion_id_fk` FOREIGN KEY (`Card_id`) REFERENCES `cardpostion` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
