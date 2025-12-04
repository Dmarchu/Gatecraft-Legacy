-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 28-03-2025 a las 09:33:11
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `bd_gatecraft`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `chats`
--

CREATE TABLE `chats` (
  `chat_ID` int(255) NOT NULL,
  `chat_title` varchar(200) NOT NULL,
  `chat_description` longtext DEFAULT NULL,
  `chat_author` varchar(50) NOT NULL,
  `chat_pinned` int(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `chats`
--

INSERT INTO `chats` (`chat_ID`, `chat_title`, `chat_description`, `chat_author`, `chat_pinned`) VALUES
(1, 'Chat General de GateCraft Forum', 'En este chat libre, podréis discutir y hablar de lo que queráis, desde dudas y recomendaciones a otros temas que no tengan que ver con la aplicación.', 'Apachu', 1),
(2, 'Chat de Soporte de GateCraft Forum', 'Este chat solo podrá ser utilizado para dudas ayuda o soporte, de lo contrario esto supondrá una sanción administrativa.', 'Apachu', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `messages`
--

CREATE TABLE `messages` (
  `message_ID` int(255) NOT NULL,
  `chat_ID` int(255) NOT NULL,
  `message_author` varchar(50) NOT NULL,
  `message_text` longtext NOT NULL,
  `message_date` date NOT NULL,
  `message_hour` time(6) NOT NULL,
  `message_pinned` int(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `messages`
--

INSERT INTO `messages` (`message_ID`, `chat_ID`, `message_author`, `message_text`, `message_date`, `message_hour`, `message_pinned`) VALUES
(1, 1, 'Apachu', 'Bienvenidos a Gatecraft Social Forum, donde podréis discutir de varios temas así como pedir ayuda en el respectivo canal de soporte. En el apartado de proyectos encontraréis proyectos realizados por otros usuarios que esperamos sean útiles para vosotros.', '2024-02-23', '09:22:10.000000', 1),
(2, 1, 'Incapaz', 'Hola soy Ángel', '2024-02-23', '10:18:49.000000', 0),
(51, 1, 'Apachu', 'A\n', '2024-03-01', '08:54:38.000000', 0),
(52, 1, 'Apachu', 'A\n', '2024-03-01', '08:54:38.000000', 0),
(53, 1, 'Apachu', 'A\n', '2024-03-01', '08:54:39.000000', 0),
(54, 1, 'Apachu', 'A\n', '2024-03-01', '08:54:39.000000', 0),
(55, 1, 'Apachu', 'A\n', '2024-03-01', '08:54:39.000000', 0),
(56, 1, 'Apachu', 'A\n', '2024-03-01', '08:54:39.000000', 0),
(57, 1, 'Apachu', 'A\n', '2024-03-01', '08:54:39.000000', 0),
(58, 1, 'Apachu', 'A\n', '2024-03-01', '08:54:44.000000', 0),
(59, 1, 'Apachu', 'Pollita sexy\n', '2024-03-27', '09:51:57.000000', 0),
(60, 1, 'Apachu', 'A tu mami me ña beneficio\n', '2024-03-27', '09:52:05.000000', 0),
(61, 1, 'Apachu', 'domingo es gay\n', '2024-03-27', '09:52:09.000000', 0),
(62, 1, 'Apachu', 'el y davex se dan por detrás\n', '2024-03-27', '09:52:20.000000', 0),
(63, 2, 'Apachu', 'pollita\n', '2025-02-06', '12:01:13.000000', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `projects`
--

CREATE TABLE `projects` (
  `ID` int(255) NOT NULL,
  `project_userID` int(255) NOT NULL,
  `project_title` varchar(50) NOT NULL DEFAULT 'Proyecto sin título',
  `project_description` varchar(255) NOT NULL DEFAULT 'Proyecto sin descripción',
  `project_date` date NOT NULL,
  `project_likes` varchar(255) DEFAULT '{}',
  `project_downloads` int(255) NOT NULL DEFAULT 0,
  `project_code` longtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `ID` int(255) NOT NULL,
  `user_name` varchar(50) NOT NULL,
  `user_password` varchar(100) NOT NULL,
  `user_permission` varchar(5) NOT NULL DEFAULT 'user',
  `user_mail` varchar(100) NOT NULL,
  `user_logged` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='Tabla de almacenamiento de users.';

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`ID`, `user_name`, `user_password`, `user_permission`, `user_mail`, `user_logged`) VALUES
(1, 'Apachu', '1234', 'admin', 'apachusko@gmail.com', 1),
(3, 'FCOMilf', '0000', 'user', 'milffco@outlook.es', 0),
(4, 'Davexx', '0000', 'user', 'davex@incapaz.es', 0),
(5, 'Incapaz', '0000', 'user', 'incapacesfc@gmail.com', 0),
(6, 'AngelGay', '0000', 'user', 'angel@incapaz.com', 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users_sanciones`
--

CREATE TABLE `users_sanciones` (
  `ID` int(255) NOT NULL,
  `banned` tinyint(1) NOT NULL,
  `muted` tinyint(1) NOT NULL,
  `warnings` int(1) NOT NULL,
  `notes` longtext DEFAULT '{}',
  `bannedby` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Volcado de datos para la tabla `users_sanciones`
--

INSERT INTO `users_sanciones` (`ID`, `banned`, `muted`, `warnings`, `notes`, `bannedby`) VALUES
(1, 0, 0, 0, '{Merece sexo,Problemática pero sensual,}', NULL),
(3, 0, 0, 0, '{}', NULL),
(4, 1, 1, 3, '{}', 'Apachu'),
(5, 0, 0, 0, '{}', NULL),
(6, 0, 1, 0, '{Es muy gay ayudale porfavor,Y iincapaz,deivid maricón}', NULL);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `chats`
--
ALTER TABLE `chats`
  ADD PRIMARY KEY (`chat_ID`);

--
-- Indices de la tabla `messages`
--
ALTER TABLE `messages`
  ADD PRIMARY KEY (`message_ID`);

--
-- Indices de la tabla `projects`
--
ALTER TABLE `projects`
  ADD PRIMARY KEY (`ID`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`ID`);

--
-- Indices de la tabla `users_sanciones`
--
ALTER TABLE `users_sanciones`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `chats`
--
ALTER TABLE `chats`
  MODIFY `chat_ID` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `messages`
--
ALTER TABLE `messages`
  MODIFY `message_ID` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=64;

--
-- AUTO_INCREMENT de la tabla `projects`
--
ALTER TABLE `projects`
  MODIFY `ID` int(255) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `ID` int(255) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
