-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 27-05-2020 a las 20:01:23
-- Versión del servidor: 10.4.11-MariaDB
-- Versión de PHP: 7.2.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `snapshot_db`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `follow`
--

CREATE TABLE `follow` (
  `idUser` int(11) NOT NULL,
  `following` int(11) NOT NULL,
  `idFollow` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `follow`
--

INSERT INTO `follow` (`idUser`, `following`, `idFollow`) VALUES
(39, 30, 2),
(39, 40, 4);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `publication`
--

CREATE TABLE `publication` (
  `idPublication` int(11) NOT NULL,
  `idUser` int(11) NOT NULL,
  `nick` varchar(10) NOT NULL,
  `title` varchar(20) NOT NULL,
  `description` text NOT NULL,
  `idMedia` varchar(20) NOT NULL,
  `likes` int(11) NOT NULL,
  `date` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `publication`
--

INSERT INTO `publication` (`idPublication`, `idUser`, `nick`, `title`, `description`, `idMedia`, `likes`, `date`) VALUES
(81, 30, 'uri200rk', 'publicacionn', 'primera publicacion', '2020-05-26 014958', 0, '2020-05-26 014958');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user`
--

CREATE TABLE `user` (
  `idUser` int(11) NOT NULL,
  `fullName` varchar(30) NOT NULL,
  `nick` varchar(10) NOT NULL,
  `mail` varchar(30) NOT NULL,
  `password` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Volcado de datos para la tabla `user`
--

INSERT INTO `user` (`idUser`, `fullName`, `nick`, `mail`, `password`) VALUES
(30, 'Oriol Martinez', 'uri200rk', 'oriolmartinez2@gmail.com', '$2y$05$QcdUGs4MRk02UFUtb2AvaOnM5ztNCh5ZYHZKjZuU/lusD4RoFfH2S'),
(39, '1', '1', '1@gmail.com', '$2y$05$brZLI.wbgHSzxh7KoklWHemLW8zbsO.xG.o6MFNESWJketMxYGQhG'),
(40, 'prueba', 'prueba', 'prueba@gmail.com', '$2y$05$SgSuZBeVW3KAOVQiMsHR/OUVRtaWTlrqESj4uPE.ph0PkRbZj5Iie');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `follow`
--
ALTER TABLE `follow`
  ADD PRIMARY KEY (`idFollow`),
  ADD KEY `foreignKey2` (`idUser`);

--
-- Indices de la tabla `publication`
--
ALTER TABLE `publication`
  ADD PRIMARY KEY (`idPublication`),
  ADD KEY `foreignKey` (`idUser`);

--
-- Indices de la tabla `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`idUser`),
  ADD UNIQUE KEY `unique` (`nick`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `follow`
--
ALTER TABLE `follow`
  MODIFY `idFollow` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT de la tabla `publication`
--
ALTER TABLE `publication`
  MODIFY `idPublication` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=82;

--
-- AUTO_INCREMENT de la tabla `user`
--
ALTER TABLE `user`
  MODIFY `idUser` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=41;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `follow`
--
ALTER TABLE `follow`
  ADD CONSTRAINT `foreignKey2` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`) ON DELETE CASCADE;

--
-- Filtros para la tabla `publication`
--
ALTER TABLE `publication`
  ADD CONSTRAINT `foreignKey` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
