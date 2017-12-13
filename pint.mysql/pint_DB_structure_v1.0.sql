CREATE DATABASE  IF NOT EXISTS `interactome_db` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `interactome_db`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: interactome_db
-- ------------------------------------------------------
-- Server version	5.7.17-log

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
-- Table structure for table `amount_type`
--

DROP TABLE IF EXISTS `amount_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `amount_type` (
  `name` varchar(90) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='INTENSITY, NORMALIZED_INTENSITY, AREA, XIC, SPC...';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `annotation_type`
--

DROP TABLE IF EXISTS `annotation_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `annotation_type` (
  `name` varchar(90) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `combination_type`
--

DROP TABLE IF EXISTS `combination_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `combination_type` (
  `name` varchar(90) NOT NULL,
  `description` varchar(8000) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `condition`
--

DROP TABLE IF EXISTS `condition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `condition` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'This table stores values from sample conditions that have to be referred as numbers and units, such as, a temperature (C grades) shift or a timepoint (h)',
  `name` varchar(45) NOT NULL,
  `description` varchar(4000) DEFAULT NULL,
  `value` double DEFAULT NULL,
  `unit` varchar(45) DEFAULT NULL,
  `Sample_id` int(11) NOT NULL,
  `Project_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Experimental_Condition_Sample1_idx` (`Sample_id`),
  KEY `fk_Experimental_Condition_Project1_idx` (`Project_id`),
  CONSTRAINT `fk_Experimental_Condition_Project1` FOREIGN KEY (`Project_id`) REFERENCES `project` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Experimental_Condition_Sample1` FOREIGN KEY (`Sample_id`) REFERENCES `sample` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=542 DEFAULT CHARSET=utf8 COMMENT='This can represent a timepoint that is the condition in whic /* comment truncated */ /* /* comment truncated */ /* /* comment truncated */ /*h a certain sample has been analyzed*/*/*/';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `confidence_score_type`
--

DROP TABLE IF EXISTS `confidence_score_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `confidence_score_type` (
  `name` varchar(90) NOT NULL,
  `description` varchar(8000) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `gene`
--

DROP TABLE IF EXISTS `gene`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gene` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gene_id` varchar(100) NOT NULL,
  `geneType` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2458075 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `label`
--

DROP TABLE IF EXISTS `label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `label` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `massDiff` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=353 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ms_run`
--

DROP TABLE IF EXISTS `ms_run`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ms_run` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `runID` varchar(90) NOT NULL,
  `path` varchar(800) NOT NULL,
  `date` date DEFAULT NULL,
  `Project_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_MS_Run_Project1` (`Project_id`),
  CONSTRAINT `fk_MS_Run_Project1` FOREIGN KEY (`Project_id`) REFERENCES `project` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1378 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `operator_type`
--

DROP TABLE IF EXISTS `operator_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `operator_type` (
  `name` varchar(45) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `organism`
--

DROP TABLE IF EXISTS `organism`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `organism` (
  `taxonomyID` varchar(90) NOT NULL,
  `name` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`taxonomyID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `peptide`
--

DROP TABLE IF EXISTS `peptide`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `peptide` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sequence` varchar(400) NOT NULL,
  `MS_Run_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Peptide_MS_Run1_idx` (`MS_Run_id`),
  CONSTRAINT `fk_Peptide_MS_Run1` FOREIGN KEY (`MS_Run_id`) REFERENCES `ms_run` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=24181262 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `peptide_amount`
--

DROP TABLE IF EXISTS `peptide_amount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `peptide_amount` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Peptide_id` int(11) NOT NULL,
  `value` double NOT NULL,
  `Amount_Type_name` varchar(90) NOT NULL,
  `Combination_Type_name` varchar(90) DEFAULT NULL,
  `Condition_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Protein_Amount_Amount_Type1_idx` (`Amount_Type_name`),
  KEY `fk_Protein_Amount_Combination_Type1_idx` (`Combination_Type_name`),
  KEY `fk_Peptide_Amount_Peptide1_idx` (`Peptide_id`),
  KEY `fk_Peptide_Amount_Condition1_idx` (`Condition_id`),
  CONSTRAINT `fk_Peptide_Amount_Condition1` FOREIGN KEY (`Condition_id`) REFERENCES `condition` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Peptide_Amount_Peptide1` FOREIGN KEY (`Peptide_id`) REFERENCES `peptide` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Amount_Amount_Type100` FOREIGN KEY (`Amount_Type_name`) REFERENCES `amount_type` (`name`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Amount_Combination_Type100` FOREIGN KEY (`Combination_Type_name`) REFERENCES `combination_type` (`name`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2237 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `peptide_has_condition`
--

DROP TABLE IF EXISTS `peptide_has_condition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `peptide_has_condition` (
  `Peptide_id` int(11) NOT NULL,
  `Condition_id` int(11) NOT NULL,
  PRIMARY KEY (`Peptide_id`,`Condition_id`),
  KEY `fk_Peptide_has_Condition_Condition1_idx` (`Condition_id`),
  KEY `fk_Peptide_has_Condition_Peptide1_idx` (`Peptide_id`),
  CONSTRAINT `fk_Peptide_has_Condition_Condition1` FOREIGN KEY (`Condition_id`) REFERENCES `condition` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Peptide_has_Condition_Peptide1` FOREIGN KEY (`Peptide_id`) REFERENCES `peptide` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `peptide_ratio_value`
--

DROP TABLE IF EXISTS `peptide_ratio_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `peptide_ratio_value` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Peptide_id` int(11) NOT NULL,
  `Ratio_Descriptor_id` int(11) NOT NULL,
  `value` double NOT NULL,
  `Combination_Type_name` varchar(45) DEFAULT NULL,
  `confidence_score_value` double DEFAULT NULL,
  `confidence_score_name` varchar(500) DEFAULT NULL,
  `Confidence_Score_Type_name` varchar(90) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Protein_Ratio_Value_Protein_Ratio_Descriptor1_idx` (`Ratio_Descriptor_id`),
  KEY `fk_Protein_Ratio_Value_Combination_Type1_idx` (`Combination_Type_name`),
  KEY `fk_Protein_Ratio_Value_Confidence_Score_Type1_idx` (`Confidence_Score_Type_name`),
  KEY `fk_Peptide_Ratio_Value_Peptide1_idx` (`Peptide_id`),
  CONSTRAINT `fk_Peptide_Ratio_Value_Peptide1` FOREIGN KEY (`Peptide_id`) REFERENCES `peptide` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Ratio_Value_Combination_Type100` FOREIGN KEY (`Combination_Type_name`) REFERENCES `combination_type` (`name`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Ratio_Value_Confidence_Score_Type100` FOREIGN KEY (`Confidence_Score_Type_name`) REFERENCES `confidence_score_type` (`name`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Ratio_Value_Protein_Ratio_Descriptor100` FOREIGN KEY (`Ratio_Descriptor_id`) REFERENCES `ratio_descriptor` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3284 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `peptide_score`
--

DROP TABLE IF EXISTS `peptide_score`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `peptide_score` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Peptide_id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `value` double NOT NULL,
  `Confidence_Score_Type_name` varchar(90) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_PSM_Score_Confidence_Score_Type1_idx` (`Confidence_Score_Type_name`),
  KEY `fk_Peptide_Score_Peptide1_idx` (`Peptide_id`),
  CONSTRAINT `fk_PSM_Score_Confidence_Score_Type10` FOREIGN KEY (`Confidence_Score_Type_name`) REFERENCES `confidence_score_type` (`name`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Peptide_Score_Peptide1` FOREIGN KEY (`Peptide_id`) REFERENCES `peptide` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(150) NOT NULL,
  `description` varchar(8000) DEFAULT NULL,
  `releaseDate` date DEFAULT NULL,
  `pubmedLink` varchar(1000) DEFAULT NULL,
  `private` tinyint(1) NOT NULL DEFAULT '1',
  `uploadedDate` date NOT NULL,
  `tag` varchar(15) NOT NULL,
  `hidden` tinyint(1) DEFAULT '0',
  `big` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `protein`
--

DROP TABLE IF EXISTS `protein`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `protein` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `acc` varchar(40) NOT NULL,
  `length` int(11) DEFAULT NULL,
  `pi` double DEFAULT NULL,
  `mw` double DEFAULT NULL,
  `Organism_taxonomyID` varchar(90) NOT NULL,
  `MS_Run_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Protein_Organism1_idx` (`Organism_taxonomyID`),
  KEY `fk_Protein_MS_Run1_idx` (`MS_Run_id`),
  CONSTRAINT `fk_Protein_MS_Run1` FOREIGN KEY (`MS_Run_id`) REFERENCES `ms_run` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Organism1` FOREIGN KEY (`Organism_taxonomyID`) REFERENCES `organism` (`taxonomyID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=9398786 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `protein_accession`
--

DROP TABLE IF EXISTS `protein_accession`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `protein_accession` (
  `accessionType` varchar(45) NOT NULL,
  `accession` varchar(40) NOT NULL,
  `description` longtext,
  `isPrimary` tinyint(1) NOT NULL,
  `alternativeNames` longtext COMMENT 'separated by special character ´***´',
  PRIMARY KEY (`accession`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `protein_amount`
--

DROP TABLE IF EXISTS `protein_amount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `protein_amount` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` double NOT NULL,
  `Protein_id` int(11) NOT NULL,
  `Amount_Type_name` varchar(90) NOT NULL,
  `Combination_Type_name` varchar(90) DEFAULT NULL,
  `Condition_id` int(11) NOT NULL,
  `Manual_spc` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Protein_Amount_Protein1_idx` (`Protein_id`),
  KEY `fk_Protein_Amount_Amount_Type1_idx` (`Amount_Type_name`),
  KEY `fk_Protein_Amount_Combination_Type1_idx` (`Combination_Type_name`),
  KEY `fk_Protein_Amount_Condition1_idx` (`Condition_id`),
  CONSTRAINT `fk_Protein_Amount_Amount_Type1` FOREIGN KEY (`Amount_Type_name`) REFERENCES `amount_type` (`name`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Amount_Combination_Type1` FOREIGN KEY (`Combination_Type_name`) REFERENCES `combination_type` (`name`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Amount_Condition1` FOREIGN KEY (`Condition_id`) REFERENCES `condition` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Amount_Protein1` FOREIGN KEY (`Protein_id`) REFERENCES `protein` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1473647 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `protein_annotation`
--

DROP TABLE IF EXISTS `protein_annotation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `protein_annotation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Annotation_Type_name` varchar(90) NOT NULL,
  `name` varchar(500) NOT NULL,
  `value` longtext,
  `source` varchar(500) DEFAULT NULL COMMENT 'description of the source of the annotation, i.e. GO , genemania...manual annotation...',
  `Protein_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Protein_Annotation_AnnotationType1_idx` (`Annotation_Type_name`),
  KEY `fk_Protein_Annotation_Protein1_idx` (`Protein_id`),
  CONSTRAINT `fk_Protein_Annotation_AnnotationType1` FOREIGN KEY (`Annotation_Type_name`) REFERENCES `annotation_type` (`name`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Annotation_Protein1` FOREIGN KEY (`Protein_id`) REFERENCES `protein` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1049573 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `protein_has_condition`
--

DROP TABLE IF EXISTS `protein_has_condition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `protein_has_condition` (
  `Protein_id` int(11) NOT NULL,
  `Condition_id` int(11) NOT NULL,
  PRIMARY KEY (`Protein_id`,`Condition_id`),
  KEY `fk_Protein_has_Condition_Condition1_idx` (`Condition_id`),
  KEY `fk_Protein_has_Condition_Protein1_idx` (`Protein_id`),
  CONSTRAINT `fk_Protein_has_Condition_Condition1` FOREIGN KEY (`Condition_id`) REFERENCES `condition` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_has_Condition_Protein1` FOREIGN KEY (`Protein_id`) REFERENCES `protein` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `protein_has_gene`
--

DROP TABLE IF EXISTS `protein_has_gene`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `protein_has_gene` (
  `Protein_id` int(11) NOT NULL,
  `Gene_id` int(11) NOT NULL,
  PRIMARY KEY (`Protein_id`,`Gene_id`),
  KEY `fk_Protein_has_Gene_Gene1_idx` (`Gene_id`),
  KEY `fk_Protein_has_Gene_Protein1_idx` (`Protein_id`),
  CONSTRAINT `fk_Protein_has_Gene_Gene1` FOREIGN KEY (`Gene_id`) REFERENCES `gene` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_has_Gene_Protein1` FOREIGN KEY (`Protein_id`) REFERENCES `protein` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `protein_has_peptide`
--

DROP TABLE IF EXISTS `protein_has_peptide`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `protein_has_peptide` (
  `Protein_id` int(11) NOT NULL,
  `Peptide_id` int(11) NOT NULL,
  PRIMARY KEY (`Protein_id`,`Peptide_id`),
  KEY `fk_Protein_has_Peptide_Peptide1_idx` (`Peptide_id`),
  KEY `fk_Protein_has_Peptide_Protein1_idx` (`Protein_id`),
  CONSTRAINT `fk_Protein_has_Peptide_Peptide1` FOREIGN KEY (`Peptide_id`) REFERENCES `peptide` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_has_Peptide_Protein1` FOREIGN KEY (`Protein_id`) REFERENCES `protein` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `protein_has_protein_accession`
--

DROP TABLE IF EXISTS `protein_has_protein_accession`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `protein_has_protein_accession` (
  `Protein_id` int(11) NOT NULL,
  `Protein_Accession_accession` varchar(15) NOT NULL,
  PRIMARY KEY (`Protein_id`,`Protein_Accession_accession`),
  KEY `fk_Protein_has_Protein_Accession_Protein_Accession1_idx` (`Protein_Accession_accession`),
  KEY `fk_Protein_has_Protein_Accession_Protein1_idx` (`Protein_id`),
  CONSTRAINT `fk_Protein_has_Protein_Accession_Protein1` FOREIGN KEY (`Protein_id`) REFERENCES `protein` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_has_Protein_Accession_Protein_Accession1` FOREIGN KEY (`Protein_Accession_accession`) REFERENCES `protein_accession` (`accession`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `protein_has_psm`
--

DROP TABLE IF EXISTS `protein_has_psm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `protein_has_psm` (
  `Protein_id` int(11) NOT NULL,
  `PSM_id` int(11) NOT NULL,
  PRIMARY KEY (`Protein_id`,`PSM_id`),
  KEY `fk_Protein_has_PSM_PSM1_idx` (`PSM_id`),
  KEY `fk_Protein_has_PSM_Protein1_idx` (`Protein_id`),
  CONSTRAINT `fk_Protein_has_PSM_PSM1` FOREIGN KEY (`PSM_id`) REFERENCES `psm` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_has_PSM_Protein1` FOREIGN KEY (`Protein_id`) REFERENCES `protein` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `protein_ratio_value`
--

DROP TABLE IF EXISTS `protein_ratio_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `protein_ratio_value` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Protein_id` int(11) NOT NULL,
  `Ratio_Descriptor_id` int(11) NOT NULL,
  `value` double NOT NULL,
  `Combination_Type_name` varchar(45) DEFAULT NULL,
  `confidence_score_value` double DEFAULT NULL,
  `confidence_score_name` varchar(500) DEFAULT NULL,
  `Confidence_Score_Type_name` varchar(90) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Protein_Ratio_Value_Protein_Ratio_Descriptor1_idx` (`Ratio_Descriptor_id`),
  KEY `fk_Protein_Ratio_Value_Protein1_idx` (`Protein_id`),
  KEY `fk_Protein_Ratio_Value_Combination_Type1_idx` (`Combination_Type_name`),
  KEY `fk_Protein_Ratio_Value_Confidence_Score_Type1_idx` (`Confidence_Score_Type_name`),
  CONSTRAINT `fk_Protein_Ratio_Value_Combination_Type1` FOREIGN KEY (`Combination_Type_name`) REFERENCES `combination_type` (`name`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Ratio_Value_Confidence_Score_Type1` FOREIGN KEY (`Confidence_Score_Type_name`) REFERENCES `confidence_score_type` (`name`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Ratio_Value_Protein1` FOREIGN KEY (`Protein_id`) REFERENCES `protein` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Ratio_Value_Protein_Ratio_Descriptor1` FOREIGN KEY (`Ratio_Descriptor_id`) REFERENCES `ratio_descriptor` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6438098 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `protein_score`
--

DROP TABLE IF EXISTS `protein_score`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `protein_score` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Protein_id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `value` double NOT NULL,
  `Confidence_Score_Type_name` varchar(90) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_PSM_Score_Confidence_Score_Type1_idx` (`Confidence_Score_Type_name`),
  KEY `fk_Protein_Score_Protein1_idx` (`Protein_id`),
  CONSTRAINT `fk_PSM_Score_Confidence_Score_Type11` FOREIGN KEY (`Confidence_Score_Type_name`) REFERENCES `confidence_score_type` (`name`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Score_Protein1` FOREIGN KEY (`Protein_id`) REFERENCES `protein` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2233 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `protein_threshold`
--

DROP TABLE IF EXISTS `protein_threshold`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `protein_threshold` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `Protein_id` int(11) NOT NULL,
  `Threshold_id` int(11) NOT NULL,
  `pass_threshold` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Applied_Threshold_Protein1_idx` (`Protein_id`),
  KEY `fk_Applied_Threshold_Threshold1_idx` (`Threshold_id`),
  CONSTRAINT `fk_Applied_Threshold_Protein1` FOREIGN KEY (`Protein_id`) REFERENCES `protein` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Applied_Threshold_Threshold1` FOREIGN KEY (`Threshold_id`) REFERENCES `threshold` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=103189 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `psm`
--

DROP TABLE IF EXISTS `psm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `psm` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `psmID` varchar(200) DEFAULT NULL,
  `MH` double DEFAULT NULL,
  `cal_mh` double DEFAULT NULL,
  `total_intensity` double DEFAULT NULL,
  `ppm_error` double DEFAULT NULL,
  `spr` int(11) DEFAULT NULL,
  `ion_proportion` double DEFAULT NULL,
  `pi` double DEFAULT NULL,
  `sequence` varchar(300) NOT NULL,
  `full_sequence` varchar(300) NOT NULL,
  `Peptide_id` int(11) NOT NULL,
  `beforeSeq` varchar(10) DEFAULT NULL,
  `afterSeq` varchar(10) DEFAULT NULL,
  `MS_Run_id` int(11) NOT NULL,
  `chargeState` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_PSM_Peptide1_idx` (`Peptide_id`),
  KEY `fk_PSM_MS_Run1_idx` (`MS_Run_id`),
  CONSTRAINT `fk_PSM_MS_Run1` FOREIGN KEY (`MS_Run_id`) REFERENCES `ms_run` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_PSM_Peptide1` FOREIGN KEY (`Peptide_id`) REFERENCES `peptide` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=37326254 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `psm_amount`
--

DROP TABLE IF EXISTS `psm_amount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `psm_amount` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `value` double NOT NULL,
  `PSM_id` int(11) NOT NULL,
  `Amount_Type_name` varchar(90) NOT NULL,
  `Combination_Type_name` varchar(90) DEFAULT NULL,
  `Condition_id` int(11) NOT NULL,
  `singleton` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Protein_Amount_Amount_Type1_idx` (`Amount_Type_name`),
  KEY `fk_Protein_Amount_Combination_Type1_idx` (`Combination_Type_name`),
  KEY `fk_Peptide_Amount_PSM1_idx` (`PSM_id`),
  KEY `fk_PSM_Amount_Condition1_idx` (`Condition_id`),
  CONSTRAINT `fk_PSM_Amount_Condition1` FOREIGN KEY (`Condition_id`) REFERENCES `condition` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Peptide_Amount_PSM1` FOREIGN KEY (`PSM_id`) REFERENCES `psm` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Amount_Amount_Type10` FOREIGN KEY (`Amount_Type_name`) REFERENCES `amount_type` (`name`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Amount_Combination_Type10` FOREIGN KEY (`Combination_Type_name`) REFERENCES `combination_type` (`name`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=28187467 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `psm_has_condition`
--

DROP TABLE IF EXISTS `psm_has_condition`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `psm_has_condition` (
  `PSM_id` int(11) NOT NULL,
  `Condition_id` int(11) NOT NULL,
  PRIMARY KEY (`PSM_id`,`Condition_id`),
  KEY `fk_PSM_has_Condition_Condition1_idx` (`Condition_id`),
  KEY `fk_PSM_has_Condition_PSM1_idx` (`PSM_id`),
  CONSTRAINT `fk_PSM_has_Condition_Condition1` FOREIGN KEY (`Condition_id`) REFERENCES `condition` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_PSM_has_Condition_PSM1` FOREIGN KEY (`PSM_id`) REFERENCES `psm` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `psm_ratio_value`
--

DROP TABLE IF EXISTS `psm_ratio_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `psm_ratio_value` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `PSM_id` int(11) NOT NULL,
  `Ratio_Descriptor_id` int(11) NOT NULL,
  `value` double NOT NULL,
  `Combination_Type_name` varchar(45) DEFAULT NULL,
  `confidence_score_value` double DEFAULT NULL,
  `confidence_score_name` varchar(500) DEFAULT NULL,
  `Confidence_Score_Type_name` varchar(90) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Protein_Ratio_Value_Protein_Ratio_Descriptor1_idx` (`Ratio_Descriptor_id`),
  KEY `fk_Protein_Ratio_Value_Combination_Type1_idx` (`Combination_Type_name`),
  KEY `fk_Protein_Ratio_Value_Confidence_Score_Type1_idx` (`Confidence_Score_Type_name`),
  KEY `fk_Peptide_Ratio_Value_PSM1_idx` (`PSM_id`),
  CONSTRAINT `fk_Peptide_Ratio_Value_PSM1` FOREIGN KEY (`PSM_id`) REFERENCES `psm` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Ratio_Value_Combination_Type10` FOREIGN KEY (`Combination_Type_name`) REFERENCES `combination_type` (`name`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Ratio_Value_Confidence_Score_Type10` FOREIGN KEY (`Confidence_Score_Type_name`) REFERENCES `confidence_score_type` (`name`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Ratio_Value_Protein_Ratio_Descriptor10` FOREIGN KEY (`Ratio_Descriptor_id`) REFERENCES `ratio_descriptor` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=42971359 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `psm_score`
--

DROP TABLE IF EXISTS `psm_score`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `psm_score` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `PSM_id` int(11) NOT NULL,
  `name` varchar(45) NOT NULL,
  `value` double NOT NULL,
  `Confidence_Score_Type_name` varchar(90) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_PSM_Score_Confidence_Score_Type1_idx` (`Confidence_Score_Type_name`),
  KEY `fk_PSM_Score_PSM1` (`PSM_id`),
  CONSTRAINT `fk_PSM_Score_Confidence_Score_Type1` FOREIGN KEY (`Confidence_Score_Type_name`) REFERENCES `confidence_score_type` (`name`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_PSM_Score_PSM1` FOREIGN KEY (`PSM_id`) REFERENCES `psm` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=74026322 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ptm`
--

DROP TABLE IF EXISTS `ptm`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ptm` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `PSM_id` int(11) NOT NULL,
  `mass_shift` double NOT NULL,
  `name` varchar(200) NOT NULL,
  `cv_id` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_PTM_PSM1_idx` (`PSM_id`),
  CONSTRAINT `fk_PTM_PSM1` FOREIGN KEY (`PSM_id`) REFERENCES `psm` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=315035 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ptm_site`
--

DROP TABLE IF EXISTS `ptm_site`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ptm_site` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `PTM_id` int(11) NOT NULL,
  `aa` varchar(1) NOT NULL,
  `position` int(11) NOT NULL,
  `confidence_score_value` varchar(45) DEFAULT NULL,
  `confidence_score_name` varchar(45) DEFAULT NULL,
  `Confidence_Score_Type_name` varchar(90) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_PTM_site_PTM1_idx` (`PTM_id`),
  KEY `fk_PTM_site_Confidence_Score_Type1_idx` (`Confidence_Score_Type_name`),
  CONSTRAINT `fk_PTM_site_Confidence_Score_Type1` FOREIGN KEY (`Confidence_Score_Type_name`) REFERENCES `confidence_score_type` (`name`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_PTM_site_PTM1` FOREIGN KEY (`PTM_id`) REFERENCES `ptm` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=316136 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ratio_descriptor`
--

DROP TABLE IF EXISTS `ratio_descriptor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ratio_descriptor` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` varchar(8000) NOT NULL,
  `Experimental_Condition_1_id` int(11) NOT NULL,
  `Experimental_Condition_2_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Protein_Ratio_Descriptor_Experimental_Condition1_idx` (`Experimental_Condition_1_id`),
  KEY `fk_Protein_Ratio_Descriptor_Experimental_Condition2_idx` (`Experimental_Condition_2_id`),
  CONSTRAINT `fk_Protein_Ratio_Descriptor_Experimental_Condition1` FOREIGN KEY (`Experimental_Condition_1_id`) REFERENCES `condition` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Ratio_Descriptor_Experimental_Condition2` FOREIGN KEY (`Experimental_Condition_2_id`) REFERENCES `condition` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=586 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sample`
--

DROP TABLE IF EXISTS `sample`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sample` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(500) NOT NULL COMMENT 'This table stores the different samples or conditions',
  `description` varchar(8000) DEFAULT NULL,
  `wildtype` tinyint(1) DEFAULT NULL,
  `Tissue_tissueID` varchar(90) DEFAULT NULL,
  `Label_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_Sample_Tissue1_idx` (`Tissue_tissueID`),
  KEY `fk_Sample_Label1_idx` (`Label_id`),
  CONSTRAINT `fk_Sample_Label1` FOREIGN KEY (`Label_id`) REFERENCES `label` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Sample_Tissue1` FOREIGN KEY (`Tissue_tissueID`) REFERENCES `tissue` (`tissueID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=530 DEFAULT CHARSET=utf8 COMMENT='Description of a sample that can be then being analyzed in o /* comment truncated */ /* /* comment truncated */ /* /* comment truncated */ /*ne or more experiments and that can have some corresponding conditions, such as a timepoint or */*/*/';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sample_has_organism`
--

DROP TABLE IF EXISTS `sample_has_organism`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sample_has_organism` (
  `Sample_id` int(11) NOT NULL,
  `Organism_taxonomyID` varchar(90) NOT NULL,
  PRIMARY KEY (`Sample_id`,`Organism_taxonomyID`),
  KEY `fk_Sample_has_Organism_Organism1_idx` (`Organism_taxonomyID`),
  KEY `fk_Sample_has_Organism_Sample1_idx` (`Sample_id`),
  CONSTRAINT `fk_Sample_has_Organism_Organism1` FOREIGN KEY (`Organism_taxonomyID`) REFERENCES `organism` (`taxonomyID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_Sample_has_Organism_Sample1` FOREIGN KEY (`Sample_id`) REFERENCES `sample` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `threshold`
--

DROP TABLE IF EXISTS `threshold`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `threshold` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `description` varchar(4000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tissue`
--

DROP TABLE IF EXISTS `tissue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tissue` (
  `tissueID` varchar(90) NOT NULL,
  `name` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`tissueID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'interactome_db'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-12-04 17:45:35
