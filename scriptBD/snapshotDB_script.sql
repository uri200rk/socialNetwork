-- phpMyAdmin SQL Dump
-- version 4.7.9
-- https://www.phpmyadmin.net/
--
-- Host: mysql-uri200rk.alwaysdata.net
-- Generation Time: Jun 01, 2020 at 05:01 PM
-- Server version: 10.4.13-MariaDB
-- PHP Version: 7.2.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `uri200rk_snapshot`
--

-- --------------------------------------------------------

--
-- Table structure for table `follow`
--

CREATE TABLE `follow` (
  `idUser` int(11) NOT NULL,
  `following` int(11) NOT NULL,
  `idFollow` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `follow`
--

INSERT INTO `follow` (`idUser`, `following`, `idFollow`) VALUES
(3, 1, 1),
(1, 2, 2),
(1, 3, 3),
(2, 1, 4),
(2, 3, 5);

-- --------------------------------------------------------

--
-- Table structure for table `publication`
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
-- Dumping data for table `publication`
--

INSERT INTO `publication` (`idPublication`, `idUser`, `nick`, `title`, `description`, `idMedia`, `likes`, `date`) VALUES
(1, 3, 'cMartinez1', 'alchol de manos', 'chicos aprobechad que esta barato!', '2020-06-01 165452', 4, '2020-06-01 165452'),
(2, 1, 'maria_02', 'lluvia', 'hoy llueve mucho!!', '2020-06-01 165616', 2, '2020-06-01 165616'),
(3, 2, 'josepp32', 'ensalada rica', 'ensalada de la mama!!', '2020-06-01 165837', 1, '2020-06-01 165837');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `idUser` int(11) NOT NULL,
  `fullName` varchar(30) NOT NULL,
  `nick` varchar(10) NOT NULL,
  `mail` varchar(30) NOT NULL,
  `password` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`idUser`, `fullName`, `nick`, `mail`, `password`) VALUES
(1, 'Maria garcia', 'maria_02', 'maria@gmail.com', '$2y$05$zHbCuOSU/2IcVXw.Tkb0BukVLrvouLkm/DUEYpjtq7ABiK/4rg0uC'),
(2, 'josep maria', 'josepp32', 'josep@hotmail.com', '$2y$05$I7mBKvo0SYWr6yXCELhjxep5TH5PY4YByMXaACTyp5lCyWHRtBXcW'),
(3, 'cristian martinez', 'cMartinez1', 'cristian@gmail.com', '$2y$05$HcWfB6vAlkZsXVdabP5qwe78oZrsTHgXb36EwKH5SxxeYShVSiEka');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `follow`
--
ALTER TABLE `follow`
  ADD PRIMARY KEY (`idFollow`),
  ADD KEY `foreignKey2` (`idUser`) USING BTREE,
  ADD KEY `foreignKeyFollowing` (`following`);

--
-- Indexes for table `publication`
--
ALTER TABLE `publication`
  ADD PRIMARY KEY (`idPublication`),
  ADD KEY `foreignKey` (`idUser`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`idUser`),
  ADD UNIQUE KEY `unique` (`nick`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `follow`
--
ALTER TABLE `follow`
  MODIFY `idFollow` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT for table `publication`
--
ALTER TABLE `publication`
  MODIFY `idPublication` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `idUser` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `follow`
--
ALTER TABLE `follow`
  ADD CONSTRAINT `foreignKeyFollowing` FOREIGN KEY (`following`) REFERENCES `user` (`idUser`) ON DELETE CASCADE,
  ADD CONSTRAINT `foreignKeyIdUser` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`) ON DELETE CASCADE;

--
-- Constraints for table `publication`
--
ALTER TABLE `publication`
  ADD CONSTRAINT `foreignKey` FOREIGN KEY (`idUser`) REFERENCES `user` (`idUser`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
