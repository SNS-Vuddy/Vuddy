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
-- Table structure for table `feed`
--

DROP TABLE IF EXISTS `feed`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feed` (
  `feed_id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `f_nickname` varchar(30) NOT NULL,
  `f_content` varchar(255) NOT NULL,
  `f_location` varchar(50) NOT NULL,
  `f_main_img` varchar(255) DEFAULT NULL,
  `f_created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `f_updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `f_is_deleted` tinyint(1) DEFAULT '0',
  `f_title` varchar(100) NOT NULL,
  PRIMARY KEY (`feed_id`),
  KEY `FK_user_TO_feed_1` (`user_id`),
  KEY `idx_c_is_deleted` (`f_is_deleted`),
  CONSTRAINT `FK_user_TO_feed_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=159 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feed`
--

LOCK TABLES `feed` WRITE;
/*!40000 ALTER TABLE `feed` DISABLE KEYS */;
INSERT INTO `feed` VALUES (107,44,'admin1','1','36.3664146, 127.3490451','https://vuddy-s3-bucket1.s3.amazonaws.com/images/f31115a5-b239-47a1-8dbe-760e6ba4389b_Screenshot_20230511_145450_Mattermost.jpg','2023-05-11 02:48:38','2023-05-13 05:40:58',0,'11'),(108,54,'kira','확인용','36.353171, 127.3380294','https://vuddy-s3-bucket1.s3.amazonaws.com/images/740dd4de-8f75-48a0-9f08-52e2606a9c98_Screenshot_20230511011605_Vuddy.jpg','2023-05-12 20:32:15','2023-05-12 20:32:15',0,'장소'),(109,44,'admin1','2','36.3649113,127.3596667','https://vuddy-s3-bucket1.s3.amazonaws.com/images/f31115a5-b239-47a1-8dbe-760e6ba4389b_Screenshot_20230511_145450_Mattermost.jpg','2023-05-11 02:48:38','2023-05-13 05:17:44',0,'11'),(110,44,'admin1','3','36.3621466,127.3606108','https://vuddy-s3-bucket1.s3.amazonaws.com/images/f31115a5-b239-47a1-8dbe-760e6ba4389b_Screenshot_20230511_145450_Mattermost.jpg','2023-05-11 02:48:38','2023-05-13 05:17:44',0,'11'),(111,45,'admin2','3','36.3680388,127.3589157','https://vuddy-s3-bucket1.s3.amazonaws.com/images/f31115a5-b239-47a1-8dbe-760e6ba4389b_Screenshot_20230511_145450_Mattermost.jpg','2023-05-11 02:48:38','2023-05-13 05:17:44',0,'11'),(112,45,'admin2','확인용','36.353171, 127.3380294','https://vuddy-s3-bucket1.s3.amazonaws.com/images/740dd4de-8f75-48a0-9f08-52e2606a9c98_Screenshot_20230511011605_Vuddy.jpg','2023-05-12 20:32:15','2023-05-12 20:32:15',0,'장소'),(113,54,'kira','안녕하세요 ','36.3531324, 127.3379968','https://vuddy-s3-bucket1.s3.amazonaws.com/images/791a8189-fcdc-4171-aba7-56317f1f53b5_20201121_183241.jpg','2023-05-13 04:08:42','2023-05-13 04:08:42',0,'테스트'),(114,1,'test1','12345','36.3531686, 127.3380199','https://vuddy-s3-bucket1.s3.amazonaws.com/images/d5409050-9386-47a1-94a6-ec78938c06cd_16819148075481.jpg','2023-05-13 08:49:20','2023-05-13 08:49:20',0,'test'),(115,71,'busbus','ㅌㅅㅌ','36.35526374610644, 127.29809907497933','https://vuddy-s3-bucket1.s3.amazonaws.com/images/4850af56-7b4e-42b4-96a0-7a965bcc0e58_20230514_135033.jpg','2023-05-17 17:00:26','2023-05-17 17:00:26',0,'ㅌㅅㅌ'),(116,54,'kira','1234','36.35531720579465, 127.29804378479294','https://vuddy-s3-bucket1.s3.amazonaws.com/images/1d42b512-168c-4f8b-9249-6a62c0512b28_Screenshot_20230511011605_Vuddy.jpg','2023-05-17 17:24:07','2023-05-17 17:24:07',0,'qwer'),(117,54,'kira','0518','36.35552743268009, 127.29796100743137','https://vuddy-s3-bucket1.s3.amazonaws.com/images/97544bdd-dc92-4cc8-b1de-38aadc8c9659_16819148075482.jpg','2023-05-17 17:57:10','2023-05-17 17:57:10',0,'test'),(118,54,'kira','12345','36.35544524080312, 127.29817422745533','https://vuddy-s3-bucket1.s3.amazonaws.com/images/7dad8c0c-456d-49c6-9ad0-1be5555ba34d_20200919_215953.jpg','2023-05-17 17:58:17','2023-05-17 17:58:17',0,'test'),(119,54,'kira','12345','36.355402334162626, 127.29813112516142','https://vuddy-s3-bucket1.s3.amazonaws.com/images/565313f7-ef0b-490b-8650-37542a6ac348_20201205_161936.jpg','2023-05-17 17:58:42','2023-05-17 17:58:42',0,'test'),(120,54,'kira','ㄱㄴㄷ','36.355402334162626, 127.29813112516142','https://vuddy-s3-bucket1.s3.amazonaws.com/images/1902323d-4692-4ed9-9cb9-b379aff773af_20200919_215953.jpg','2023-05-17 17:59:50','2023-05-17 17:59:50',0,'확인'),(121,57,'admin4','졸귀','36.35492427, 127.29774862','https://vuddy-s3-bucket1.s3.amazonaws.com/images/9b020beb-d11b-4c2d-a889-f1b709a1d2ff_1684144651675.jpg','2023-05-17 18:42:51','2023-05-17 18:42:51',0,'귀엽다'),(122,54,'kira','','36.35506560172811, 127.29803450208898','https://vuddy-s3-bucket1.s3.amazonaws.com/images/9b97196a-5942-4796-aad9-5884bfd81f9c_Internet_20230322_081554_11.jpeg','2023-05-17 18:57:46','2023-05-17 18:57:46',0,''),(123,54,'kira','asdf','36.355440492674965, 127.29826876617524','https://vuddy-s3-bucket1.s3.amazonaws.com/images/d5f8c07b-7506-44c9-8d23-c4a9c107dece_20201205_161956.jpg','2023-05-17 20:25:32','2023-05-17 20:25:32',0,'1234'),(124,54,'kira','aaaaa','36.355210486181676, 127.29807196560584','https://vuddy-s3-bucket1.s3.amazonaws.com/images/b8cbbd2c-3087-4016-8d56-e0f73459a95a_Screenshot_20230518130347_NaverMap.jpg','2023-05-17 22:01:20','2023-05-17 22:01:20',0,'12345'),(125,54,'kira','11111','36.35528532140432, 127.2980077526465','https://vuddy-s3-bucket1.s3.amazonaws.com/images/2f54ebfe-df6a-4512-9340-3d118312827a_20201115_195527.jpg','2023-05-17 22:02:06','2023-05-17 22:02:06',0,'12344'),(126,54,'kira','asdf','36.355359771398845, 127.29804390031042','https://vuddy-s3-bucket1.s3.amazonaws.com/images/e41695e4-aa84-4e21-97bc-c5648138c0eb_Screenshot_20230518130347_NaverMap.jpg','2023-05-17 22:23:16','2023-05-17 22:23:16',0,'12344'),(127,45,'admin2','고양이','36.35534005, 127.29807556','https://vuddy-s3-bucket1.s3.amazonaws.com/images/bf12e158-71dc-4797-bb07-8a8bbdca1819_Screenshot_20230518_131930_Instagram.jpg','2023-05-17 23:03:36','2023-05-17 23:03:36',0,'고양이'),(145,27,'정현석','무접점 폼 미쳤다','36.3654653,127.3552473','https://vuddy-s3-bucket1.s3.amazonaws.com/images/206447bc-5e43-4918-9433-b45d20171f96_KakaoTalk_Photo_20230518222510007.jpeg','2023-05-18 04:35:00','2023-05-18 13:46:02',0,'해피해킹 직구 해버렸습니다,,,'),(147,27,'정현석','주유 6만원 이상 무료 세차 오졌다,,','36.3477001,127.3484141','https://vuddy-s3-bucket1.s3.amazonaws.com/images/494914c4-224e-40b7-b2da-578bd920d3b6_KakaoTalk_Photo_20230518222510006.jpeg','2023-05-18 04:38:16','2023-05-18 04:38:16',0,'세차 했습니다.'),(148,28,'이진욱','밥 먹을때만 사이 좋아','36.358291,127.3496738','https://vuddy-s3-bucket1.s3.amazonaws.com/images/9c79f584-6ca5-4282-90e4-66ed35169bc5_KakaoTalk_Photo_20230518204742001.jpeg','2023-05-18 04:40:31','2023-05-18 04:40:31',0,'우리랑 제리랑,,,'),(149,28,'이진욱','미세먼지도 없고 너무 행복했어요~~','36.3560177,127.3525192','https://vuddy-s3-bucket1.s3.amazonaws.com/images/b1157779-5deb-4cae-a16b-3b28bda66f90_KakaoTalk_Photo_20230518204742003.jpeg','2023-05-18 04:42:02','2023-05-18 04:42:02',0,'벚꽃놀이 즐겼습니다.'),(150,28,'이진욱','나도 이제 스벅 입장 가능?','36.3500079,127.2981896','https://vuddy-s3-bucket1.s3.amazonaws.com/images/7dcac6d4-d7c9-48de-bd3a-78ec394410d5_KakaoTalk_Photo_20230518205244008.jpeg','2023-05-18 04:44:14','2023-05-18 13:45:54',0,'맥북 샀습니다,,'),(152,29,'김채원','사장님이 맛있고 해장국이 친절해요.','36.3493994,127.2975358','https://vuddy-s3-bucket1.s3.amazonaws.com/images/165f3e5b-83e8-4e17-996c-4cfa726f275e_KakaoTalk_Photo_20230518222509001.jpeg','2023-05-18 04:48:25','2023-05-18 13:57:51',0,'양평 해장국 옴뇸뇸뇸'),(153,30,'사쿠라','나도 사쿠라 너도 사쿠라','36.3459324,127.2939758','https://vuddy-s3-bucket1.s3.amazonaws.com/images/161844d9-3e79-4eb3-9db5-3c4d02fb0238_KakaoTalk_Photo_20230518205243001.jpeg','2023-05-18 04:49:44','2023-05-18 13:57:51',0,'밤에 본 벚꽃'),(154,31,'허윤진','짜파게티요리사 ','36.3459511,127.3023291','https://vuddy-s3-bucket1.s3.amazonaws.com/images/f3c7b23e-d58e-44b2-b079-603e05e380ad_KakaoTalk_Photo_20230518222509003.jpeg','2023-05-18 04:50:41','2023-05-18 04:50:41',0,'오늘은 내가 '),(155,32,'카즈하','야옹~','36.3579779,127.3068379','https://vuddy-s3-bucket1.s3.amazonaws.com/images/762f2833-c06a-404c-910b-ff3624df922e_KakaoTalk_Photo_20230518222509004.jpeg','2023-05-18 04:51:55','2023-05-18 04:51:55',0,'저는 냥냥이파예요'),(156,33,'홍은채','멀캠에 온듯한 기분~~','36.3630422,127.3568083','https://vuddy-s3-bucket1.s3.amazonaws.com/images/bf1067d2-3440-4fb4-8c4a-c0f4faceba90_KakaoTalk_Photo_20230518222510005.jpeg','2023-05-18 04:52:51','2023-05-18 04:52:51',0,'집앞에 바나프레소가 생겼어요.'),(157,34,'최예나','피최몇?','36.3537196,127.3439164','https://vuddy-s3-bucket1.s3.amazonaws.com/images/c5b0e2c8-9db1-45c1-9c81-d1ba0615143e_KakaoTalk_Photo_20230518205243004.jpeg','2023-05-18 04:53:43','2023-05-18 04:53:43',0,'엔씨 백화점에 피자몰 가봤어요~~'),(158,35,'송우기',',,,','36.3424343,127.3179902','https://vuddy-s3-bucket1.s3.amazonaws.com/images/4a071239-3726-422a-bd38-d4fe4db8f1f0_KakaoTalk_Photo_20230518205243005.jpeg','2023-05-18 04:54:43','2023-05-18 04:54:43',0,'꽃구경,,');
/*!40000 ALTER TABLE `feed` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-05-19  9:03:42
