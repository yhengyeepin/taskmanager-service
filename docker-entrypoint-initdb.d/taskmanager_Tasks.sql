-- MySQL dump 10.13  Distrib 5.7.17, for macos10.12 (x86_64)
--
-- Host: localhost    Database: taskmanager
-- ------------------------------------------------------
-- Server version	8.0.2-dmr

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Tasks`
--

DROP TABLE IF EXISTS `Tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Tasks` (
  `TaskID` int(11) NOT NULL AUTO_INCREMENT,
  `ProjectID` int(11) DEFAULT NULL,
  `Description` text,
  `PriorityID` int(11) DEFAULT NULL,
  `StatusID` int(11) DEFAULT NULL,
  `DueDate` datetime DEFAULT NULL,
  `ModifiedBy` int(11) DEFAULT NULL,
  `ModifiedOn` datetime DEFAULT NULL,
  `OwnedBy` int(11) NOT NULL,
  PRIMARY KEY (`TaskID`),
  KEY `FK_PRIORITY_idx` (`PriorityID`),
  KEY `FK_STATUS_idx` (`StatusID`),
  KEY `FK_OWNER_idx` (`OwnedBy`),
  KEY `FK_MODIFIED_BY_idx` (`ModifiedBy`),
  CONSTRAINT `FK_MODIFIED_BY` FOREIGN KEY (`ModifiedBy`) REFERENCES `users` (`userid`),
  CONSTRAINT `FK_OWNER` FOREIGN KEY (`OwnedBy`) REFERENCES `users` (`userid`),
  CONSTRAINT `FK_PRIORITY` FOREIGN KEY (`PriorityID`) REFERENCES `priorities` (`priorityid`),
  CONSTRAINT `FK_STATUS` FOREIGN KEY (`StatusID`) REFERENCES `statuses` (`statusid`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Tasks`
--

LOCK TABLES `Tasks` WRITE;
/*!40000 ALTER TABLE `Tasks` DISABLE KEYS */;
INSERT INTO `Tasks` VALUES (12,0,'new task',323,336,'2017-12-11 16:00:00',142,'2017-09-05 15:10:38',142);
/*!40000 ALTER TABLE `Tasks` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-09-08  7:54:06
