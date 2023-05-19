-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: k8b305.p.ssafy.io    Database: vuddy
-- ------------------------------------------------------
-- Server version	8.0.33

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
-- Table structure for table `feed_pictures`
--

DROP TABLE IF EXISTS `feed_pictures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feed_pictures` (
  `feed_picture_id` bigint NOT NULL AUTO_INCREMENT,
  `feed_id` bigint NOT NULL,
  `fp_img_url` varchar(255) NOT NULL,
  PRIMARY KEY (`feed_picture_id`),
  KEY `FK_feed_TO_feed_pictures_2` (`feed_id`),
  CONSTRAINT `FK_feed_TO_feed_pictures_cascade` FOREIGN KEY (`feed_id`) REFERENCES `feed` (`feed_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=211 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feed_pictures`
--

LOCK TABLES `feed_pictures` WRITE;
/*!40000 ALTER TABLE `feed_pictures` DISABLE KEYS */;
INSERT INTO `feed_pictures` VALUES (172,107,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/f31115a5-b239-47a1-8dbe-760e6ba4389b_Screenshot_20230511_145450_Mattermost.jpg'),(173,108,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/740dd4de-8f75-48a0-9f08-52e2606a9c98_Screenshot_20230511011605_Vuddy.jpg'),(174,113,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/791a8189-fcdc-4171-aba7-56317f1f53b5_20201121_183241.jpg'),(175,113,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/ad86f633-8b06-4755-ae6e-a0b3d304bedb_20201121_183237.jpg'),(176,114,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/d5409050-9386-47a1-94a6-ec78938c06cd_16819148075481.jpg'),(177,114,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/169181be-71ab-4adb-aa5d-6db815b7222d_16819148075482.jpg'),(178,114,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/31bb3833-39a9-468c-8dd5-2771d15998d4_16819148075483.jpg'),(179,115,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/4850af56-7b4e-42b4-96a0-7a965bcc0e58_20230514_135033.jpg'),(180,116,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/1d42b512-168c-4f8b-9249-6a62c0512b28_Screenshot_20230511011605_Vuddy.jpg'),(181,117,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/97544bdd-dc92-4cc8-b1de-38aadc8c9659_16819148075482.jpg'),(182,117,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/268ee008-2fed-4e6c-9cde-652c1e0d1a82_16819148075483.jpg'),(183,118,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/7dad8c0c-456d-49c6-9ad0-1be5555ba34d_20200919_215953.jpg'),(184,119,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/565313f7-ef0b-490b-8650-37542a6ac348_20201205_161936.jpg'),(185,120,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/1902323d-4692-4ed9-9cb9-b379aff773af_20200919_215953.jpg'),(186,121,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/9b020beb-d11b-4c2d-a889-f1b709a1d2ff_1684144651675.jpg'),(187,122,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/9b97196a-5942-4796-aad9-5884bfd81f9c_Internet_20230322_081554_11.jpeg'),(188,123,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/d5f8c07b-7506-44c9-8d23-c4a9c107dece_20201205_161956.jpg'),(189,124,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/b8cbbd2c-3087-4016-8d56-e0f73459a95a_Screenshot_20230518130347_NaverMap.jpg'),(190,125,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/2f54ebfe-df6a-4512-9340-3d118312827a_20201115_195527.jpg'),(191,126,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/e41695e4-aa84-4e21-97bc-c5648138c0eb_Screenshot_20230518130347_NaverMap.jpg'),(192,127,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/bf12e158-71dc-4797-bb07-8a8bbdca1819_Screenshot_20230518_131930_Instagram.jpg'),(195,145,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/206447bc-5e43-4918-9433-b45d20171f96_KakaoTalk_Photo_20230518222510007.jpeg'),(197,147,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/494914c4-224e-40b7-b2da-578bd920d3b6_KakaoTalk_Photo_20230518222510006.jpeg'),(198,148,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/9c79f584-6ca5-4282-90e4-66ed35169bc5_KakaoTalk_Photo_20230518204742001.jpeg'),(199,149,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/b1157779-5deb-4cae-a16b-3b28bda66f90_KakaoTalk_Photo_20230518204742003.jpeg'),(200,150,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/7dcac6d4-d7c9-48de-bd3a-78ec394410d5_KakaoTalk_Photo_20230518205244008.jpeg'),(202,152,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/165f3e5b-83e8-4e17-996c-4cfa726f275e_KakaoTalk_Photo_20230518222509001.jpeg'),(203,152,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/4cff0e7e-07a6-4d0f-8dc2-2279100f4c01_KakaoTalk_Photo_20230518222509002.jpeg'),(204,153,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/161844d9-3e79-4eb3-9db5-3c4d02fb0238_KakaoTalk_Photo_20230518205243001.jpeg'),(205,153,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/0c27a519-4459-4947-baec-f200a3b00319_KakaoTalk_Photo_20230518205243002.jpeg'),(206,154,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/f3c7b23e-d58e-44b2-b079-603e05e380ad_KakaoTalk_Photo_20230518222509003.jpeg'),(207,155,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/762f2833-c06a-404c-910b-ff3624df922e_KakaoTalk_Photo_20230518222509004.jpeg'),(208,156,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/bf1067d2-3440-4fb4-8c4a-c0f4faceba90_KakaoTalk_Photo_20230518222510005.jpeg'),(209,157,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/c5b0e2c8-9db1-45c1-9c81-d1ba0615143e_KakaoTalk_Photo_20230518205243004.jpeg'),(210,158,'https://vuddy-s3-bucket1.s3.amazonaws.com/images/4a071239-3726-422a-bd38-d4fe4db8f1f0_KakaoTalk_Photo_20230518205243005.jpeg');
/*!40000 ALTER TABLE `feed_pictures` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-05-19  9:03:53
