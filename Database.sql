-- phpMyAdmin SQL Dump
-- version 4.6.5.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Dec 20, 2016 at 06:12 PM
-- Server version: 10.1.18-MariaDB
-- PHP Version: 7.0.8

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id293084_connectingdots`
--

-- --------------------------------------------------------

--
-- Table structure for table `Employees`
--

CREATE TABLE `Employees` (
  `IMEI` varchar(40) NOT NULL,
  `Profession` varchar(40) NOT NULL,
  `image_skill` longblob NOT NULL,
  `Image_One` longtext NOT NULL,
  `Image_Two` longtext NOT NULL,
  `Image_Three` longtext NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Employers`
--

CREATE TABLE `Employers` (
  `IMEI` varchar(40) NOT NULL,
  `Profession` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ImeiZip`
--

CREATE TABLE `ImeiZip` (
  `IMEI` varchar(40) NOT NULL,
  `ZipCode` int(11) NOT NULL,
  `Latitude` double NOT NULL,
  `Longitude` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `JobList`
--

CREATE TABLE `JobList` (
  `jid` int(11) NOT NULL,
  `jName` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `JobList`
--

INSERT INTO `JobList` (`jid`, `jName`) VALUES
(0, 'Potter'),
(1, 'Labourer'),
(2, 'Mason'),
(3, 'Plumber'),
(4, 'Electrician'),
(5, 'Embroider'),
(6, 'Barber'),
(7, 'Maid'),
(8, 'Blacksmith'),
(9, 'Cobbler'),
(10, 'BabySitter'),
(11, 'Gardener'),
(12, 'Carpenter'),
(13, 'Tailor'),
(14, 'Washerman'),
(15, 'Sweeper'),
(16, 'Painter'),
(17, 'Coolie');

-- --------------------------------------------------------

--
-- Table structure for table `Login_Information`
--

CREATE TABLE `Login_Information` (
  `id` int(11) NOT NULL,
  `Username` varchar(40) NOT NULL,
  `IMEI` varchar(40) NOT NULL,
  `Profile_Pic` longtext NOT NULL,
  `Type` varchar(8) NOT NULL,
  `PhoneNumber` varchar(15) NOT NULL,
  `Compressed_image` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `Requests`
--

CREATE TABLE `Requests` (
  `IMEI` varchar(40) NOT NULL,
  `Jobs` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Employees`
--
ALTER TABLE `Employees`
  ADD PRIMARY KEY (`IMEI`);

--
-- Indexes for table `Employers`
--
ALTER TABLE `Employers`
  ADD PRIMARY KEY (`IMEI`);

--
-- Indexes for table `JobList`
--
ALTER TABLE `JobList`
  ADD PRIMARY KEY (`jid`);

--
-- Indexes for table `Login_Information`
--
ALTER TABLE `Login_Information`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `JobList`
--
ALTER TABLE `JobList`
  MODIFY `jid` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;
--
-- AUTO_INCREMENT for table `Login_Information`
--
ALTER TABLE `Login_Information`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
