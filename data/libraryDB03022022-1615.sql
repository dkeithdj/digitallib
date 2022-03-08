-- MySQL dump 10.13  Distrib 8.0.27, for Win64 (x86_64)
--
-- Host: localhost    Database: library
-- ------------------------------------------------------
-- Server version	8.0.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `b_id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) NOT NULL,
  `author` varchar(100) NOT NULL,
  `genre` varchar(100) NOT NULL,
  `quantity` int NOT NULL,
  `issued` int NOT NULL,
  `publishYear` varchar(50) NOT NULL,
  PRIMARY KEY (`b_id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (3,'Legend','Marie Luaa','Thriller',5,1,'2011'),(29,'qaa','w','faa',6,0,'433'),(30,'3aa','3','3',2,1,'3'),(31,'12rw','1','1222',10,3,'1'),(32,'1a','1','1aaaa',9,2,'1'),(39,'q','waa','f',1,3,'433'),(46,'p','p','p',4,0,'5555'),(47,'f','u','f',4,0,'3993'),(48,'pp','p','pp',5,0,'5'),(49,'bb','bb','tt',5,0,'6w2'),(51,'tt','tt','t',2,0,'2344'),(55,'a','a','a',2,1,'3322'),(56,'tt','ttt','ttt',53,1,'5'),(58,'t','t','t',4,0,'22'),(59,'wwww','tttttt','ttt',3,0,'2222');
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issuedbooks`
--

DROP TABLE IF EXISTS `issuedbooks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `issuedbooks` (
  `ib_id` int NOT NULL AUTO_INCREMENT,
  `m_id` int NOT NULL,
  `b_id` int NOT NULL,
  `brwrLName` varchar(50) NOT NULL,
  `bookTitle` varchar(50) NOT NULL,
  `issuedDate` varchar(50) NOT NULL,
  `borrowPeriod` int NOT NULL,
  `returnDate` varchar(50) NOT NULL,
  `overdued` varchar(50) NOT NULL,
  PRIMARY KEY (`ib_id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issuedbooks`
--

LOCK TABLES `issuedbooks` WRITE;
/*!40000 ALTER TABLE `issuedbooks` DISABLE KEYS */;
INSERT INTO `issuedbooks` VALUES (1,1,5,'De Jesus','Guinness World Records: Gamer\'s Edition 2020','12/12/2021',0,'-','YES'),(2,3,5,'Nomus','Guinness World Records: Gamer\'s Edition 2020','12/13/2021',1,'-','YES'),(3,2,3,'Andoque','Legend','12/13/2021',1,'02/24/2022','YES'),(4,1,8,'De Jesus','w','02/24/2022',5,'-','-'),(6,3,32,'Nomus','4','02/24/2022',4,'-','-'),(7,1,29,'De Jesus','22','02/24/2022',2,'02/28/2022','YES'),(8,2,30,'Andoque','3','02/24/2022',6,'02/24/2022','NO'),(11,3,31,'Nomus','12rw','02/26/2022',2,'02/28/2022','NO'),(13,23,39,'g','q','02/28/2022',4,'-','-'),(14,29,46,'zz','p','02/28/2022',2,'02/28/2022','NO'),(15,24,39,'gggaa','q','02/28/2022',6,'-','-'),(16,28,56,'gmz','tt','02/28/2022',3,'-','-'),(17,29,39,'zz','q','02/28/2022',1,'-','-'),(18,28,55,'gmz','a','02/28/2022',2,'02/28/2022','NO'),(19,28,55,'gmz','a','02/28/2022',2,'-','-'),(20,29,31,'zz','12rw','02/28/2022',1,'-','-'),(21,22,31,'bb','12rw','02/28/2022',1,'-','-'),(23,29,32,'zz','1a','02/28/2022',2,'-','-'),(24,28,31,'gmz','12rw','02/28/2022',2,'-','-'),(25,24,30,'aaaaa','30','02/28/2022',1,'-','-'),(26,24,31,'gggaa','31','02/28/2022',1,'-','-'),(27,29,51,'zz','tt','02/28/2022',3,'-','-'),(28,1,39,'De Jesus','q','02/28/2022',2,'-','-'),(29,3,47,'Nomus','f','02/28/2022',2,'-','-'),(30,3,3,'Joshua Roi','Marie Luaa','02/28/2022',3,'-','-'),(31,3,3,'Joshua Roi','Marie Luaa','02/28/2022',5,'-','-'),(32,3,3,'Joshua Roi','Marie Luaa','02/28/2022',6,'-','-'),(33,3,30,'Joshua Roi','3','03/02/2022',2,'-','-');
/*!40000 ALTER TABLE `issuedbooks` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `members`
--

DROP TABLE IF EXISTS `members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `members` (
  `m_id` int NOT NULL AUTO_INCREMENT,
  `firstName` varchar(150) NOT NULL,
  `lastName` varchar(150) NOT NULL,
  `address` varchar(150) NOT NULL,
  `contact` varchar(100) NOT NULL,
  PRIMARY KEY (`m_id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `members`
--

LOCK TABLES `members` WRITE;
/*!40000 ALTER TABLE `members` DISABLE KEYS */;
INSERT INTO `members` VALUES (1,'Denrei Keith','De Jesus','Nabunturan','091734328'),(2,'Karl','Andoque','Davao','09873871'),(3,'Joshua Roi','Nomus','Davao','093452896'),(22,'bb','bb','b','bbb'),(23,'gg','g','g','g'),(24,'gg','gggaa','aaaaa','g'),(28,'aw','gmz','mkm','m'),(29,'z','zz','zz','zzz'),(32,'tt','ttf','ft','ft'),(35,'ars','t','t','ff');
/*!40000 ALTER TABLE `members` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-03-02 16:15:04
