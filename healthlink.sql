-- MySQL dump 10.13  Distrib 8.0.42, for macos15 (arm64)
--
-- Host: localhost    Database: healthlink
-- ------------------------------------------------------
-- Server version	9.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `assigned_to`
--

DROP TABLE IF EXISTS `assigned_to`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assigned_to` (
  `assigned_id` int NOT NULL AUTO_INCREMENT,
  `patient_id` int NOT NULL,
  `doctor_id` int NOT NULL,
  PRIMARY KEY (`assigned_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assigned_to`
--

LOCK TABLES `assigned_to` WRITE;
/*!40000 ALTER TABLE `assigned_to` DISABLE KEYS */;
INSERT INTO `assigned_to` VALUES (1,1,1),(2,2,2),(3,3,3),(4,4,4),(5,5,5),(6,6,6),(7,7,7),(8,8,8),(9,9,9),(10,10,10),(11,10,9),(12,10,42);
/*!40000 ALTER TABLE `assigned_to` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor`
--

DROP TABLE IF EXISTS `doctor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor` (
  `doctor_id` int NOT NULL AUTO_INCREMENT,
  `department` varchar(32) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`doctor_id`),
  KEY `user_id_fk` (`user_id`),
  CONSTRAINT `user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor`
--

LOCK TABLES `doctor` WRITE;
/*!40000 ALTER TABLE `doctor` DISABLE KEYS */;
INSERT INTO `doctor` VALUES (1,'Cardiology',11),(2,'Dermatology',12),(3,'Endocrinology',13),(4,'Gastroenterology',14),(5,'Pediatrics',15),(6,'General Medicine',16),(7,'Oncology',17),(8,'Nephrology',18),(9,'Urology',19),(10,'Ophthalmology',20),(11,'ENT',42),(15,'Cardio',53);
/*!40000 ALTER TABLE `doctor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medication`
--

DROP TABLE IF EXISTS `medication`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medication` (
  `medication_id` int NOT NULL AUTO_INCREMENT,
  `patient_id` int DEFAULT NULL,
  `doctor_id` int DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `dosage` varchar(45) DEFAULT NULL,
  `frequency` varchar(45) DEFAULT NULL,
  `notes` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`medication_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medication`
--

LOCK TABLES `medication` WRITE;
/*!40000 ALTER TABLE `medication` DISABLE KEYS */;
INSERT INTO `medication` VALUES (1,1,1,'Lisinopril','10mg','Once daily','For blood pressure control.'),(2,2,2,'Metformin','500mg','Twice daily','For type 2 diabetes management.'),(3,3,3,'Atorvastatin','20mg','Once daily at night','For cholesterol reduction.'),(4,4,4,'Amoxicillin','250mg','Three times daily','Antibiotic for bacterial infection.'),(5,5,5,'Levothyroxine','75mcg','Once daily in the morning','For hypothyroidism.'),(6,6,6,'Omeprazole','20mg','Once daily','For acid reflux.'),(7,7,7,'Sertraline','50mg','Once daily','For depression/anxiety.'),(8,8,8,'Warfarin','5mg','Once daily','Anticoagulant, monitor INR closely.'),(9,9,42,'Albuterol','2 puffs','As needed for shortness of breath','Rescue inhaler for asthma.'),(10,10,10,'Hydrochlorothiazide','25mg','Once daily','Diuretic for fluid retention.'),(18,10,11,'doctor','doctor','doctor','doctor\n');
/*!40000 ALTER TABLE `medication` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medication_log`
--

DROP TABLE IF EXISTS `medication_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medication_log` (
  `medication_log_id` int NOT NULL,
  `medication_id` int DEFAULT NULL,
  `patient_id` int DEFAULT NULL,
  `taken_at` varchar(45) DEFAULT NULL,
  `dosage_taken` varchar(45) DEFAULT NULL,
  `created_at` date DEFAULT NULL,
  `note` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`medication_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medication_log`
--

LOCK TABLES `medication_log` WRITE;
/*!40000 ALTER TABLE `medication_log` DISABLE KEYS */;
INSERT INTO `medication_log` VALUES (1,1,10,'2025-07-07 09:00:00','10mg','2025-07-07','Taken as prescribed.'),(2,2,10,'2025-07-07 08:30:00','500mg','2025-07-07','Taken with breakfast.'),(3,3,10,'2025-07-06 21:00:00','20mg','2025-07-06','Taken before bed.'),(4,4,10,'2025-07-07 10:00:00','250mg','2025-07-07','Completed full course.'),(5,5,10,'2025-07-07 07:00:00','75mcg','2025-07-07','Taken on an empty stomach.'),(6,6,10,'2025-07-07 08:00:00','20mg','2025-07-07','Taken before first meal.'),(7,7,10,'2025-07-07 09:30:00','50mg','2025-07-07','No side effects reported.'),(8,8,10,'2025-07-07 18:00:00','5mg','2025-07-07','INR check scheduled next week.'),(9,9,10,'2025-07-07 14:15:00','2 puffs','2025-07-07','Used during mild exertion.'),(10,10,10,'2025-07-07 08:45:00','25mg','2025-07-07','Reduced swelling.');
/*!40000 ALTER TABLE `medication_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `message`
--

DROP TABLE IF EXISTS `message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `message` (
  `message_id` int NOT NULL,
  `sender_id` int DEFAULT NULL,
  `receiver_id` int DEFAULT NULL,
  `content` varchar(256) DEFAULT NULL,
  `timestamp` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `message`
--

LOCK TABLES `message` WRITE;
/*!40000 ALTER TABLE `message` DISABLE KEYS */;
INSERT INTO `message` VALUES (1,1,11,'Dr. Chen, I have a question about my medication dosage.','2025-07-08 09:00:00'),(2,11,1,'Hi Emily, please clarify your question. Which medication are you referring to?','2025-07-08 09:05:00'),(3,2,12,'Dr. Williams, my test results are available. Can we discuss them?','2025-07-08 09:10:00'),(4,13,3,'Fnu, your MRI results look good. No immediate concerns.','2025-07-08 09:15:00'),(5,4,14,'Dr. Nguyen, I need to reschedule my next appointment.','2025-07-08 09:20:00'),(6,15,5,'Mike, please ensure you complete your daily dosage of Levothyroxine.','2025-07-08 09:25:00'),(7,6,7,'Hi Deborah, how are you feeling today?','2025-07-08 09:30:00'),(8,16,17,'Dr. Wilson, patient 6 needs a follow-up on their CT scan.','2025-07-08 09:35:00'),(9,9,19,'Dr. Thompson, my asthma symptoms have improved significantly.','2025-07-08 09:40:00'),(10,20,10,'James, remember your next colonoscopy is due in 5 years.','2025-07-08 09:45:00');
/*!40000 ALTER TABLE `message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `note`
--

DROP TABLE IF EXISTS `note`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `note` (
  `note_id` int NOT NULL AUTO_INCREMENT,
  `doctor_id` int DEFAULT NULL,
  `patient_id` int DEFAULT NULL,
  `content` varchar(256) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`note_id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `note`
--

LOCK TABLES `note` WRITE;
/*!40000 ALTER TABLE `note` DISABLE KEYS */;
INSERT INTO `note` VALUES (5,NULL,10,'1234567890','2025-07-21 21:08:34'),(6,NULL,10,'asdad','2025-07-21 21:10:38'),(7,NULL,10,'asdasd','2025-07-21 21:11:18'),(8,NULL,10,'asdad','2025-07-21 21:11:27'),(9,NULL,10,'sdasd','2025-07-21 21:12:43'),(10,NULL,10,'tommy was here','2025-07-21 21:14:42'),(11,19,10,'asdasdasd','2025-07-21 22:17:55'),(12,20,10,'asdasdasd','2025-07-22 17:24:15');
/*!40000 ALTER TABLE `note` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notification` (
  `notification_id` int NOT NULL,
  `patient_id` int DEFAULT NULL,
  `message` varchar(256) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  PRIMARY KEY (`notification_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES (1,1,'Your blood work results are now available. Please review them.','2025-07-08 10:00:00'),(2,2,'Reminder: Your next appointment is scheduled for July 15th at 10 AM.','2025-07-08 10:05:00'),(3,3,'New medication prescription has been sent to your pharmacy.','2025-07-08 10:10:00'),(4,4,'Please complete the pre-visit questionnaire before your next appointment.','2025-07-08 10:15:00'),(5,5,'Your recent ECG results have been reviewed by Dr. Rodriguez.','2025-07-08 10:20:00'),(6,6,'Action required: Please confirm your medication intake for today.','2025-07-08 10:25:00'),(7,7,'Your doctor has sent you a new message. Check your inbox.','2025-07-08 10:30:00'),(8,8,'Important update regarding your recent lab tests.','2025-07-08 10:35:00'),(9,9,'Your feedback on the app has been received. Thank you!','2025-07-08 10:40:00'),(10,10,'A new educational resource about your condition is available.','2025-07-08 10:45:00');
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient` (
  `patient_id` int NOT NULL AUTO_INCREMENT,
  `date_of_birth` date DEFAULT NULL,
  `phone_number` varchar(45) DEFAULT NULL,
  `address` varchar(45) DEFAULT NULL,
  `emergency_contact_name` varchar(45) DEFAULT NULL,
  `emergency_contact_phone_number` varchar(45) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`patient_id`),
  KEY `fk_user_id` (`user_id`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` VALUES (1,'1990-05-15','(408) 555-1234','123 Main St, San Jose, CA 95112','Jane Liu','(408) 555-1235',2),(2,'1985-11-22','(408) 555-2345','456 Oak Ave, Santa Clara, CA 95050','Ali Hasham','(408) 555-2346',3),(3,'1992-07-01','(408) 555-3456','789 Pine Ln, Milpitas, CA 95035','Omar Hasan','(408) 555-3457',4),(4,'1978-03-20','(408) 555-4567','101 Elm Rd, Sunnyvale, CA 94087','John Madden','(408) 555-4568',5),(5,'1965-09-10','(408) 555-5678','202 Birch Ct, Cupertino, CA 95014','Robert Sanders','(408) 555-5679',6),(6,'1995-01-25','(408) 555-6789','303 Cedar Dr, Campbell, CA 95008','Maria Shaffer','(408) 555-6790',7),(7,'1980-04-03','(408) 555-7890','404 Dogwood Pl, Los Gatos, CA 95032','Sarah Stubbs','(408) 555-7891',8),(8,'1972-12-18','(408) 555-8901','505 Fir St, Saratoga, CA 95070','Lisa Louis','(408) 555-8902',9),(9,'2000-06-08','(408) 555-9012','606 Spruce Way, Mountain View, CA 94043','Robert Smith','(408) 555-9013',10),(10,'1975-08-01','(408) 555-1111','808 Maple Ave, San Jose, CA 95123','Susan Davis','(408) 555-1112',43),(15,'2003-10-05','(650) 878-7788','(650) 878-7788','(650) 878-7788','(650) 878-7788',45),(16,'2003-10-05','(650) 878-7788','(650) 878-7788','(650) 878-7788','(650) 878-7788',45);
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `system_log`
--

DROP TABLE IF EXISTS `system_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `system_log` (
  `log_id` int NOT NULL,
  `user_id` int DEFAULT NULL,
  `action` varchar(45) DEFAULT NULL,
  `detail` varchar(128) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  PRIMARY KEY (`log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `system_log`
--

LOCK TABLES `system_log` WRITE;
/*!40000 ALTER TABLE `system_log` DISABLE KEYS */;
INSERT INTO `system_log` VALUES (1,5,'User Login','User Tommy Liu logged in successfully.','2025-07-08 08:00:10'),(2,11,'Medication Prescribed','Dr. Michael Chen prescribed Lisinopril to Patient 1.','2025-07-08 08:05:22'),(3,1,'Profile Update','Patient Emily White updated their phone number.','2025-07-08 08:10:45'),(4,15,'Appointment Scheduled','Dr. James Rodriguez scheduled an appointment with Patient 5.','2025-07-08 08:15:30'),(5,2,'Medication Logged','Patient John Smith logged Metformin intake.','2025-07-08 08:20:15'),(6,18,'Data Access','Dr. Olivia Martinez accessed Patient 8 records.','2025-07-08 08:25:05'),(7,7,'Password Reset','Patient Sarah Stubbs initiated password reset.','2025-07-08 08:30:50'),(8,13,'System Alert','Low stock alert for Atorvastatin.','2025-07-08 08:35:10'),(9,9,'Feedback Submitted','Patient Robert Smith submitted feedback on app usability.','2025-07-08 08:40:00'),(10,20,'User Logout','Dr. Sophia Garcia logged out.','2025-07-08 08:45:20');
/*!40000 ALTER TABLE `system_log` ENABLE KEYS */;
UNLOCK TABLES;



--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `user_id` int NOT NULL AUTO_INCREMENT,
  `email_address` varchar(45) DEFAULT NULL,
  `password_hashed` varchar(60) DEFAULT NULL,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `role` enum('patient','doctor','admin') DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin@admin.com','$2a$12$0VcLk7uyA97dflWvdKDoCeyj6T1oIqJomz3k3pGHsYeeo8hIG8DkC','admin','admin','admin','2025-07-08 07:54:32','2025-07-08 07:54:32'),(2,'tommy.liu@sjsu.edu','$2b$12$wA4PrHZ5tcVHZJAxHckLj.e5cvkFL5qDDBtHY1','Tommy','Liu','patient','2025-07-08 07:54:32','2025-07-08 07:54:32'),(3,'fnu.hasham@sjsu.edu','$2b$12$SLjahlduI49oXt6Piah8Q.WD/ZUwsEicYHaQX/','Fnu','Hasham','patient','2025-07-08 07:54:32','2025-07-08 07:54:32'),(4,'hiba.hasan@sjsu.edu','$2b$12$Xa1FKaAmoKddgOrgIelCEONF5WhU03UsKNJHqP','Hiba','Hasan','patient','2025-07-08 07:54:32','2025-07-08 07:54:32'),(5,'Ching-seh.Wu@sjsu.edu','$2b$12$PN4AXNSW4p6wDr2Idv6hwevd0wbIgPmgz5/WVo','Mike','Wu','patient','2025-07-08 07:54:32','2025-07-08 07:54:32'),(6,'heidi-madden39@gmail.com','$2b$12$MaDAgKIzg38M.RORvTU94eQnD70XKUFjs8XtS3','Madden','Heidi','patient','2025-07-08 07:54:32','2025-07-08 07:54:32'),(7,'sanders-deborah96@gmail.com','$2b$12$VqpDO3ZMexdXrQBzK3tWo.vFo.k7SQ3WtfxpLA','Deborah','Sanders','patient','2025-07-08 07:54:32','2025-07-08 07:54:32'),(8,'shaffer-seamus6@gmail.com','$2b$12$CDFdd2XVNVaHSnxvKHrhaeuYmHXWnfnydbEtmG','Shaffer','Seamus','patient','2025-07-08 07:54:32','2025-07-08 07:54:32'),(9,'hamilton-stubbs65@gmail.com','$2b$12$ZyXqQl3gAju7SVcvkmq.Hur1y6HJi5DNQDSPiJ','Hamilton','Stubbs','patient','2025-07-08 07:54:32','2025-07-08 07:54:32'),(10,'louis_kade16@gmail.com','$2a$12$lyyE4gkgByBPQA8GI00Fge6H/7wTYzFz4HTaxMpAeUusnfncalrru','Kade','Louis','patient','2025-07-08 07:54:32','2025-07-08 07:54:32'),(11,'dr.michael.chen@gmail.com','$2b$12$5BgU5wZHN9vLk8mR2dDp3fFtNc4gKq1SvLbYm','Michael','Chen','doctor','2025-07-08 08:15:58','2025-07-08 08:15:58'),(12,'dr.sarah.williams@gmail.com','$2b$12$QhOeX1J9vLk8mR2dDp3fFtNc4gKq1SvLbYm','Sarah','Williams','doctor','2025-07-08 08:15:58','2025-07-08 08:15:58'),(13,'dr.david.patel@gmail.com','$2b$12$5BgU5wDp3fFtNc4gKq1SvLbYm','David','Patel','doctor','2025-07-08 08:15:58','2025-07-08 08:15:58'),(14,'dr.emily.nguyen@gmail.com','$2b$12$51J9vLk8mR2dDp3fFtNc4gKq1SvLbYm','Emily','Nguyen','doctor','2025-07-08 08:15:58','2025-07-08 08:15:58'),(15,'dr.james.rodriguez@gmail.com','$2b$12k8mR2dDp3fFtNc4gKq1SvLbYm','James','Rodriguez','doctor','2025-07-08 08:15:58','2025-07-08 08:15:58'),(16,'dr.jessica.kim@gmail.com','$2b$12$5BJ9vLk8mR2dDp3fFtNc4gKq1SvLbYm','Jessica','Kim','doctor','2025-07-08 08:15:58','2025-07-08 08:15:58'),(17,'dr.robert.wilson@gmail.com','$2b$12$5BgU2dDp3fFtNc4gKq1SvLbYm','Robert','Wilson','doctor','2025-07-08 08:15:58','2025-07-08 08:15:58'),(18,'dr.olivia.martinez@gmail.com','$2b$12$5BgU5wZHN6VZQ7zYw','Olivia','Martinez','doctor','2025-07-08 08:15:58','2025-07-08 08:15:58'),(19,'dr.daniel.thompson@gmail.com','$2b$12$5BgU5wZHN6VZQ7zYwWQhOe','Daniel','Thompson','doctor','2025-07-08 08:15:58','2025-07-08 08:15:58'),(20,'dr.sophia.garcia@gmail.com','$2b$12$5BgU5wZHN6VZQ7zYwWQhOeXm','Sophia','Garcia','doctor','2025-07-08 08:15:58','2025-07-08 08:15:58'),(21,'johnny-smith@gmail.com','$2b$12$5BgU5wZHYwWQhOeXm','Johnny','Smith','patient','2025-07-08 08:17:55','2025-07-08 08:17:55'),(42,'doctor@doctor.com','$2a$12$ncILeSlK1fzxp4pl2mT9Fe2hwEasOOiqWOrjSuUGca0IaB8EmrLfm','tommy','liu','doctor','2025-07-21 00:00:00','2025-07-21 00:00:00'),(43,'patient@patient.com','$2a$12$lyyE4gkgByBPQA8GI00Fge6H/7wTYzFz4HTaxMpAeUusnfncalrru','tommy','liu','patient','2025-07-21 00:00:00','2025-07-21 00:00:00'),(45,'test@test','$2a$12$sJkLI6uBJOseBXElJDCrl.V1kM.44Pv5UjV4vGaVjSBCaClnOPkXS','test','test','patient','2025-07-22 00:00:00','2025-07-22 00:00:00'),(46,'admin@admin.com','$2a$12$0VcLk7uyA97dflWvdKDoCeyj6T1oIqJomz3k3pGHsYeeo8hIG8DkC','admin','admin','patient','2025-07-29 00:00:00','2025-07-29 00:00:00'),(53,'Email@email.com','$2a$12$eU0Ddd7rIuz3QFfBfb2CMO4/8bagyOx/WKjY..0dPK.lPTm94ETc2','First','Name','doctor','2025-07-31 00:00:00','2025-07-31 00:00:00');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

-- Test Results Table
CREATE TABLE `test_results` (
  `result_id` int NOT NULL AUTO_INCREMENT,
  `patient_id` int NOT NULL,
  `doctor_id` int NOT NULL,
  `file_name` varchar(255) NOT NULL,
  `file_data` longblob NOT NULL,
  `file_type` varchar(50) NOT NULL,
  `upload_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `description` text,
  PRIMARY KEY (`result_id`),
  KEY `fk_test_results_patient` (`patient_id`),
  KEY `fk_test_results_doctor` (`doctor_id`),
  CONSTRAINT `fk_test_results_patient` FOREIGN KEY (`patient_id`) REFERENCES `patients` (`patient_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_test_results_doctor` FOREIGN KEY (`doctor_id`) REFERENCES `doctors` (`doctor_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-31 15:53:54
