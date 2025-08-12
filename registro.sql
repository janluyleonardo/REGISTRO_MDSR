-- phpMyAdmin SQL Dump
-- version 4.8.3
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 27-10-2018 a las 18:30:30
-- Versión del servidor: 10.1.36-MariaDB
-- Versión de PHP: 7.2.11

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `registro`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clientes`
--

CREATE TABLE `clientes` (
  `Codigo` int(30) NOT NULL,
  `Nombre` varchar(40) NOT NULL,
  `Apellido` varchar(40) NOT NULL,
  `Telefono` varchar(40) NOT NULL,
  `Cédula` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `clientes`
--

INSERT INTO `clientes` (`Codigo`, `Nombre`, `Apellido`, `Telefono`, `Cédula`) VALUES
(36, 'maria ', 'gonzalez', '00000', '00000'),
(37, 'juen', 'duarte', '741852963', '852456'),
(38, 'gabriel ', 'gonzales', '753159', '123987'),
(39, 'katerine ', 'valderrama', '4561230', '0321654'),
(40, 'YEFERSON GABRIEL', 'CHAPARRO MARROQUIN', '1234567', '1012365987'),
(41, 'YEFERSON GABRIEL', 'CHAPARRO MONAZAL', '1234567', '1012365987'),
(42, 'maria YAKELINE', 'gonzalez', '00000', '00000'),
(43, 'ROBERTO', 'MORENO', '1111111', '2222222222');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `productos`
--

CREATE TABLE `productos` (
  `Codigo` int(30) NOT NULL,
  `Nombre` varchar(40) NOT NULL,
  `Marca` varchar(40) NOT NULL,
  `Valor` varchar(40) NOT NULL,
  `Cantidad` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `productos`
--

INSERT INTO `productos` (`Codigo`, `Nombre`, `Marca`, `Valor`, `Cantidad`) VALUES
(15, 'fuente de poder', 'debian', '200000', '1'),
(16, 'torre para pc de escritorio', 'hp', '500000', '1'),
(17, 'camara web', 'tecnomith', '45000', '2'),
(18, 'celular ', 'hwawei', '80000', '1'),
(19, 'CELULAR', 'SONY', '900000', '1'),
(20, 'CELULAR', 'SONY', '900000', '1'),
(21, 'fuente de poder', 'debian', '200000', '1'),
(22, 'PROTECTOR DE PORTATIL', 'HP', '90000', '1');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `clientes`
--
ALTER TABLE `clientes`
  ADD PRIMARY KEY (`Codigo`);

--
-- Indices de la tabla `productos`
--
ALTER TABLE `productos`
  ADD PRIMARY KEY (`Codigo`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `clientes`
--
ALTER TABLE `clientes`
  MODIFY `Codigo` int(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=44;

--
-- AUTO_INCREMENT de la tabla `productos`
--
ALTER TABLE `productos`
  MODIFY `Codigo` int(30) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
