SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `interactome_db` ;
CREATE SCHEMA IF NOT EXISTS `interactome_db` DEFAULT CHARACTER SET utf8 ;
USE `interactome_db` ;

-- -----------------------------------------------------
-- Table `interactome_db`.`Tissue`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Tissue` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Tissue` (
  `tissueID` VARCHAR(90) NOT NULL,
  `name` VARCHAR(500) NULL,
  PRIMARY KEY (`tissueID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Organism`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Organism` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Organism` (
  `taxonomyID` VARCHAR(90) NOT NULL,
  `name` VARCHAR(500) NULL,
  PRIMARY KEY (`taxonomyID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Label`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Label` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Label` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `massDiff` DOUBLE NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Sample`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Sample` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Sample` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(500) NOT NULL COMMENT 'This table stores the different samples or conditions',
  `description` VARCHAR(8000) NULL DEFAULT NULL,
  `wildtype` TINYINT(1) NULL,
  `Tissue_tissueID` VARCHAR(90) NULL,
  `Label_id` INT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Sample_Tissue1_idx` (`Tissue_tissueID` ASC),
  INDEX `fk_Sample_Label1_idx` (`Label_id` ASC),
  CONSTRAINT `fk_Sample_Tissue1`
    FOREIGN KEY (`Tissue_tissueID`)
    REFERENCES `interactome_db`.`Tissue` (`tissueID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Sample_Label1`
    FOREIGN KEY (`Label_id`)
    REFERENCES `interactome_db`.`Label` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'Description of a sample that can be then being analyzed in o /* comment truncated */ /* /* comment truncated */ /* /* comment truncated */ /*ne or more experiments and that can have some corresponding conditions, such as a timepoint or */*/*/';


-- -----------------------------------------------------
-- Table `interactome_db`.`Project`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Project` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Project` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(150) NOT NULL,
  `description` VARCHAR(8000) NULL DEFAULT NULL,
  `releaseDate` DATE NULL,
  `pubmedLink` VARCHAR(1000) NULL,
  `private` TINYINT(1) NOT NULL DEFAULT true,
  `uploadedDate` DATE NOT NULL,
  `tag` VARCHAR(15) NOT NULL,
  `hidden` TINYINT(1) NOT NULL DEFAULT false,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Condition`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Condition` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Condition` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT 'This table stores values from sample conditions that have to be referred as numbers and units, such as, a temperature (C grades) shift or a timepoint (h)',
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(4000) NULL DEFAULT NULL,
  `value` DOUBLE NULL DEFAULT NULL,
  `unit` VARCHAR(45) NULL DEFAULT NULL,
  `Sample_id` INT NOT NULL,
  `Project_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Experimental_Condition_Sample1_idx` (`Sample_id` ASC),
  INDEX `fk_Experimental_Condition_Project1_idx` (`Project_id` ASC),
  CONSTRAINT `fk_Experimental_Condition_Sample1`
    FOREIGN KEY (`Sample_id`)
    REFERENCES `interactome_db`.`Sample` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Experimental_Condition_Project1`
    FOREIGN KEY (`Project_id`)
    REFERENCES `interactome_db`.`Project` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = 'This can represent a timepoint that is the condition in whic /* comment truncated */ /* /* comment truncated */ /* /* comment truncated */ /*h a certain sample has been analyzed*/*/*/';


-- -----------------------------------------------------
-- Table `interactome_db`.`MS_Run`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`MS_Run` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`MS_Run` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `runID` VARCHAR(90) NOT NULL,
  `path` VARCHAR(800) NOT NULL,
  `date` DATE NULL DEFAULT NULL,
  `Project_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_MS_Run_Project1_idx` (`Project_id` ASC),
  CONSTRAINT `fk_MS_Run_Project1`
    FOREIGN KEY (`Project_id`)
    REFERENCES `interactome_db`.`Project` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Protein`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Protein` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Protein` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `acc` VARCHAR(20) NOT NULL,
  `length` INT NULL,
  `pi` DOUBLE NULL,
  `mw` DOUBLE NULL,
  `Organism_taxonomyID` VARCHAR(90) NOT NULL,
  `MS_Run_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Protein_Organism1_idx` (`Organism_taxonomyID` ASC),
  INDEX `fk_Protein_MS_Run1_idx` (`MS_Run_id` ASC),
  CONSTRAINT `fk_Protein_Organism1`
    FOREIGN KEY (`Organism_taxonomyID`)
    REFERENCES `interactome_db`.`Organism` (`taxonomyID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_MS_Run1`
    FOREIGN KEY (`MS_Run_id`)
    REFERENCES `interactome_db`.`MS_Run` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Protein_Accession`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Protein_Accession` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Protein_Accession` (
  `accessionType` VARCHAR(45) NOT NULL,
  `accession` VARCHAR(15) NOT NULL,
  `description` LONGTEXT NULL DEFAULT NULL,
  `isPrimary` TINYINT(1) NOT NULL,
  `alternativeNames` LONGTEXT NULL COMMENT 'separated by special character ´***´',
  PRIMARY KEY (`accession`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Amount_Type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Amount_Type` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Amount_Type` (
  `name` VARCHAR(90) NOT NULL,
  PRIMARY KEY (`name`))
ENGINE = InnoDB
COMMENT = 'INTENSITY, NORMALIZED_INTENSITY, AREA, XIC, SPC...';


-- -----------------------------------------------------
-- Table `interactome_db`.`Combination_Type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Combination_Type` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Combination_Type` (
  `name` VARCHAR(90) NOT NULL,
  `description` VARCHAR(8000) NULL,
  PRIMARY KEY (`name`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Protein_Amount`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Protein_Amount` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Protein_Amount` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `value` DOUBLE NOT NULL,
  `Protein_id` INT NOT NULL,
  `Amount_Type_name` VARCHAR(90) NOT NULL,
  `Combination_Type_name` VARCHAR(90) NULL,
  `Condition_id` INT NOT NULL,
  `Manual_spc` TINYINT(1) NULL COMMENT 'if Manual_spc is true, means that that protein amount has been calculated somehow manually or by any custom method, so it will be treated in a different way by the interface\n',
  PRIMARY KEY (`id`),
  INDEX `fk_Protein_Amount_Protein1_idx` (`Protein_id` ASC),
  INDEX `fk_Protein_Amount_Amount_Type1_idx` (`Amount_Type_name` ASC),
  INDEX `fk_Protein_Amount_Combination_Type1_idx` (`Combination_Type_name` ASC),
  INDEX `fk_Protein_Amount_Condition1_idx` (`Condition_id` ASC),
  CONSTRAINT `fk_Protein_Amount_Protein1`
    FOREIGN KEY (`Protein_id`)
    REFERENCES `interactome_db`.`Protein` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Amount_Amount_Type1`
    FOREIGN KEY (`Amount_Type_name`)
    REFERENCES `interactome_db`.`Amount_Type` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Amount_Combination_Type1`
    FOREIGN KEY (`Combination_Type_name`)
    REFERENCES `interactome_db`.`Combination_Type` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Amount_Condition1`
    FOREIGN KEY (`Condition_id`)
    REFERENCES `interactome_db`.`Condition` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Annotation_Type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Annotation_Type` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Annotation_Type` (
  `name` VARCHAR(90) NOT NULL,
  PRIMARY KEY (`name`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Protein_Annotation`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Protein_Annotation` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Protein_Annotation` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Annotation_Type_name` VARCHAR(90) NOT NULL,
  `name` VARCHAR(500) NOT NULL,
  `value` LONGTEXT NULL,
  `source` VARCHAR(500) NULL COMMENT 'description of the source of the annotation, i.e. GO , genemania...manual annotation...',
  `Protein_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Protein_Annotation_AnnotationType1_idx` (`Annotation_Type_name` ASC),
  INDEX `fk_Protein_Annotation_Protein1_idx` (`Protein_id` ASC),
  CONSTRAINT `fk_Protein_Annotation_AnnotationType1`
    FOREIGN KEY (`Annotation_Type_name`)
    REFERENCES `interactome_db`.`Annotation_Type` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Annotation_Protein1`
    FOREIGN KEY (`Protein_id`)
    REFERENCES `interactome_db`.`Protein` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Confidence_Score_Type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Confidence_Score_Type` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Confidence_Score_Type` (
  `name` VARCHAR(90) NOT NULL,
  `description` VARCHAR(8000) NULL,
  PRIMARY KEY (`name`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Ratio_Descriptor`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Ratio_Descriptor` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Ratio_Descriptor` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `description` VARCHAR(8000) NOT NULL,
  `Experimental_Condition_1_id` INT NOT NULL,
  `Experimental_Condition_2_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Protein_Ratio_Descriptor_Experimental_Condition1_idx` (`Experimental_Condition_1_id` ASC),
  INDEX `fk_Protein_Ratio_Descriptor_Experimental_Condition2_idx` (`Experimental_Condition_2_id` ASC),
  CONSTRAINT `fk_Protein_Ratio_Descriptor_Experimental_Condition1`
    FOREIGN KEY (`Experimental_Condition_1_id`)
    REFERENCES `interactome_db`.`Condition` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Ratio_Descriptor_Experimental_Condition2`
    FOREIGN KEY (`Experimental_Condition_2_id`)
    REFERENCES `interactome_db`.`Condition` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Protein_Ratio_Value`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Protein_Ratio_Value` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Protein_Ratio_Value` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Protein_id` INT NOT NULL,
  `Ratio_Descriptor_id` INT NOT NULL,
  `value` DOUBLE NOT NULL,
  `Combination_Type_name` VARCHAR(45) NULL,
  `confidence_score_value` DOUBLE NULL,
  `confidence_score_name` VARCHAR(500) NULL,
  `Confidence_Score_Type_name` VARCHAR(90) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Protein_Ratio_Value_Protein_Ratio_Descriptor1_idx` (`Ratio_Descriptor_id` ASC),
  INDEX `fk_Protein_Ratio_Value_Protein1_idx` (`Protein_id` ASC),
  INDEX `fk_Protein_Ratio_Value_Combination_Type1_idx` (`Combination_Type_name` ASC),
  INDEX `fk_Protein_Ratio_Value_Confidence_Score_Type1_idx` (`Confidence_Score_Type_name` ASC),
  CONSTRAINT `fk_Protein_Ratio_Value_Protein_Ratio_Descriptor1`
    FOREIGN KEY (`Ratio_Descriptor_id`)
    REFERENCES `interactome_db`.`Ratio_Descriptor` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Ratio_Value_Protein1`
    FOREIGN KEY (`Protein_id`)
    REFERENCES `interactome_db`.`Protein` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Ratio_Value_Combination_Type1`
    FOREIGN KEY (`Combination_Type_name`)
    REFERENCES `interactome_db`.`Combination_Type` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Ratio_Value_Confidence_Score_Type1`
    FOREIGN KEY (`Confidence_Score_Type_name`)
    REFERENCES `interactome_db`.`Confidence_Score_Type` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Threshold`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Threshold` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Threshold` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(4000) NULL DEFAULT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Protein_Threshold`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Protein_Threshold` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Protein_Threshold` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Protein_id` INT NOT NULL,
  `Threshold_id` INT NOT NULL,
  `pass_threshold` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Applied_Threshold_Protein1_idx` (`Protein_id` ASC),
  INDEX `fk_Applied_Threshold_Threshold1_idx` (`Threshold_id` ASC),
  CONSTRAINT `fk_Applied_Threshold_Protein1`
    FOREIGN KEY (`Protein_id`)
    REFERENCES `interactome_db`.`Protein` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Applied_Threshold_Threshold1`
    FOREIGN KEY (`Threshold_id`)
    REFERENCES `interactome_db`.`Threshold` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Operator_Type`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Operator_Type` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Operator_Type` (
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`name`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Gene`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Gene` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Gene` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `gene_id` VARCHAR(100) NOT NULL,
  `geneType` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Peptide`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Peptide` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Peptide` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `sequence` VARCHAR(400) NOT NULL,
  `MS_Run_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Peptide_MS_Run1_idx` (`MS_Run_id` ASC),
  CONSTRAINT `fk_Peptide_MS_Run1`
    FOREIGN KEY (`MS_Run_id`)
    REFERENCES `interactome_db`.`MS_Run` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`PSM`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`PSM` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`PSM` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `psmID` VARCHAR(200) NULL,
  `MH` DOUBLE NULL,
  `cal_mh` DOUBLE NULL,
  `total_intensity` DOUBLE NULL,
  `ppm_error` DOUBLE NULL,
  `spr` INT NULL,
  `ion_proportion` DOUBLE NULL,
  `pi` DOUBLE NULL,
  `sequence` VARCHAR(300) NOT NULL,
  `full_sequence` VARCHAR(300) NOT NULL,
  `Peptide_id` INT NOT NULL,
  `beforeSeq` VARCHAR(10) NULL,
  `afterSeq` VARCHAR(10) NULL,
  `MS_Run_id` INT NOT NULL,
  `chargeState` VARCHAR(10) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_PSM_Peptide1_idx` (`Peptide_id` ASC),
  INDEX `fk_PSM_MS_Run1_idx` (`MS_Run_id` ASC),
  CONSTRAINT `fk_PSM_Peptide1`
    FOREIGN KEY (`Peptide_id`)
    REFERENCES `interactome_db`.`Peptide` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PSM_MS_Run1`
    FOREIGN KEY (`MS_Run_id`)
    REFERENCES `interactome_db`.`MS_Run` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`PTM`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`PTM` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`PTM` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `PSM_id` INT NOT NULL,
  `mass_shift` DOUBLE NOT NULL,
  `name` VARCHAR(200) NOT NULL,
  `cv_id` VARCHAR(10) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_PTM_PSM1_idx` (`PSM_id` ASC),
  CONSTRAINT `fk_PTM_PSM1`
    FOREIGN KEY (`PSM_id`)
    REFERENCES `interactome_db`.`PSM` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`PTM_site`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`PTM_site` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`PTM_site` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `PTM_id` INT NOT NULL,
  `aa` VARCHAR(1) NOT NULL,
  `position` INT NOT NULL,
  `confidence_score_value` VARCHAR(45) NULL,
  `confidence_score_name` VARCHAR(45) NULL,
  `Confidence_Score_Type_name` VARCHAR(90) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_PTM_site_PTM1_idx` (`PTM_id` ASC),
  INDEX `fk_PTM_site_Confidence_Score_Type1_idx` (`Confidence_Score_Type_name` ASC),
  CONSTRAINT `fk_PTM_site_PTM1`
    FOREIGN KEY (`PTM_id`)
    REFERENCES `interactome_db`.`PTM` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PTM_site_Confidence_Score_Type1`
    FOREIGN KEY (`Confidence_Score_Type_name`)
    REFERENCES `interactome_db`.`Confidence_Score_Type` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`PSM_Score`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`PSM_Score` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`PSM_Score` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `PSM_id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `value` DOUBLE NOT NULL,
  `Confidence_Score_Type_name` VARCHAR(90) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_PSM_Score_Confidence_Score_Type1_idx` (`Confidence_Score_Type_name` ASC),
  CONSTRAINT `fk_PSM_Score_PSM1`
    FOREIGN KEY (`PSM_id`)
    REFERENCES `interactome_db`.`PSM` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PSM_Score_Confidence_Score_Type1`
    FOREIGN KEY (`Confidence_Score_Type_name`)
    REFERENCES `interactome_db`.`Confidence_Score_Type` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Protein_has_Gene`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Protein_has_Gene` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Protein_has_Gene` (
  `Protein_id` INT NOT NULL,
  `Gene_id` INT NOT NULL,
  PRIMARY KEY (`Protein_id`, `Gene_id`),
  INDEX `fk_Protein_has_Gene_Gene1_idx` (`Gene_id` ASC),
  INDEX `fk_Protein_has_Gene_Protein1_idx` (`Protein_id` ASC),
  CONSTRAINT `fk_Protein_has_Gene_Protein1`
    FOREIGN KEY (`Protein_id`)
    REFERENCES `interactome_db`.`Protein` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_has_Gene_Gene1`
    FOREIGN KEY (`Gene_id`)
    REFERENCES `interactome_db`.`Gene` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Protein_has_PSM`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Protein_has_PSM` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Protein_has_PSM` (
  `Protein_id` INT NOT NULL,
  `PSM_id` INT NOT NULL,
  PRIMARY KEY (`Protein_id`, `PSM_id`),
  INDEX `fk_Protein_has_PSM_PSM1_idx` (`PSM_id` ASC),
  INDEX `fk_Protein_has_PSM_Protein1_idx` (`Protein_id` ASC),
  CONSTRAINT `fk_Protein_has_PSM_Protein1`
    FOREIGN KEY (`Protein_id`)
    REFERENCES `interactome_db`.`Protein` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_has_PSM_PSM1`
    FOREIGN KEY (`PSM_id`)
    REFERENCES `interactome_db`.`PSM` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Protein_has_Protein_Accession`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Protein_has_Protein_Accession` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Protein_has_Protein_Accession` (
  `Protein_id` INT NOT NULL,
  `Protein_Accession_accession` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`Protein_id`, `Protein_Accession_accession`),
  INDEX `fk_Protein_has_Protein_Accession_Protein_Accession1_idx` (`Protein_Accession_accession` ASC),
  INDEX `fk_Protein_has_Protein_Accession_Protein1_idx` (`Protein_id` ASC),
  CONSTRAINT `fk_Protein_has_Protein_Accession_Protein1`
    FOREIGN KEY (`Protein_id`)
    REFERENCES `interactome_db`.`Protein` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_has_Protein_Accession_Protein_Accession1`
    FOREIGN KEY (`Protein_Accession_accession`)
    REFERENCES `interactome_db`.`Protein_Accession` (`accession`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`PSM_Amount`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`PSM_Amount` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`PSM_Amount` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `value` DOUBLE NOT NULL,
  `PSM_id` INT NOT NULL,
  `Amount_Type_name` VARCHAR(90) NOT NULL,
  `Combination_Type_name` VARCHAR(90) NULL,
  `Condition_id` INT NOT NULL,
  `Singleton` TINYINT(1) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Protein_Amount_Amount_Type1_idx` (`Amount_Type_name` ASC),
  INDEX `fk_Protein_Amount_Combination_Type1_idx` (`Combination_Type_name` ASC),
  INDEX `fk_Peptide_Amount_PSM1_idx` (`PSM_id` ASC),
  INDEX `fk_PSM_Amount_Condition1_idx` (`Condition_id` ASC),
  CONSTRAINT `fk_Protein_Amount_Amount_Type10`
    FOREIGN KEY (`Amount_Type_name`)
    REFERENCES `interactome_db`.`Amount_Type` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Amount_Combination_Type10`
    FOREIGN KEY (`Combination_Type_name`)
    REFERENCES `interactome_db`.`Combination_Type` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Peptide_Amount_PSM1`
    FOREIGN KEY (`PSM_id`)
    REFERENCES `interactome_db`.`PSM` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PSM_Amount_Condition1`
    FOREIGN KEY (`Condition_id`)
    REFERENCES `interactome_db`.`Condition` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`PSM_Ratio_Value`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`PSM_Ratio_Value` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`PSM_Ratio_Value` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `PSM_id` INT NOT NULL,
  `Ratio_Descriptor_id` INT NOT NULL,
  `value` DOUBLE NOT NULL,
  `Combination_Type_name` VARCHAR(45) NULL,
  `confidence_score_value` DOUBLE NULL,
  `confidence_score_name` VARCHAR(500) NULL,
  `Confidence_Score_Type_name` VARCHAR(90) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Protein_Ratio_Value_Protein_Ratio_Descriptor1_idx` (`Ratio_Descriptor_id` ASC),
  INDEX `fk_Protein_Ratio_Value_Combination_Type1_idx` (`Combination_Type_name` ASC),
  INDEX `fk_Protein_Ratio_Value_Confidence_Score_Type1_idx` (`Confidence_Score_Type_name` ASC),
  INDEX `fk_Peptide_Ratio_Value_PSM1_idx` (`PSM_id` ASC),
  CONSTRAINT `fk_Protein_Ratio_Value_Protein_Ratio_Descriptor10`
    FOREIGN KEY (`Ratio_Descriptor_id`)
    REFERENCES `interactome_db`.`Ratio_Descriptor` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Ratio_Value_Combination_Type10`
    FOREIGN KEY (`Combination_Type_name`)
    REFERENCES `interactome_db`.`Combination_Type` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Ratio_Value_Confidence_Score_Type10`
    FOREIGN KEY (`Confidence_Score_Type_name`)
    REFERENCES `interactome_db`.`Confidence_Score_Type` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Peptide_Ratio_Value_PSM1`
    FOREIGN KEY (`PSM_id`)
    REFERENCES `interactome_db`.`PSM` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Protein_has_Peptide`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Protein_has_Peptide` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Protein_has_Peptide` (
  `Protein_id` INT NOT NULL,
  `Peptide_id` INT NOT NULL,
  PRIMARY KEY (`Protein_id`, `Peptide_id`),
  INDEX `fk_Protein_has_Peptide_Peptide1_idx` (`Peptide_id` ASC),
  INDEX `fk_Protein_has_Peptide_Protein1_idx` (`Protein_id` ASC),
  CONSTRAINT `fk_Protein_has_Peptide_Protein1`
    FOREIGN KEY (`Protein_id`)
    REFERENCES `interactome_db`.`Protein` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_has_Peptide_Peptide1`
    FOREIGN KEY (`Peptide_id`)
    REFERENCES `interactome_db`.`Peptide` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`PSM_has_Condition`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`PSM_has_Condition` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`PSM_has_Condition` (
  `PSM_id` INT NOT NULL,
  `Condition_id` INT NOT NULL,
  PRIMARY KEY (`PSM_id`, `Condition_id`),
  INDEX `fk_PSM_has_Condition_Condition1_idx` (`Condition_id` ASC),
  INDEX `fk_PSM_has_Condition_PSM1_idx` (`PSM_id` ASC),
  CONSTRAINT `fk_PSM_has_Condition_PSM1`
    FOREIGN KEY (`PSM_id`)
    REFERENCES `interactome_db`.`PSM` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PSM_has_Condition_Condition1`
    FOREIGN KEY (`Condition_id`)
    REFERENCES `interactome_db`.`Condition` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Protein_has_Condition`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Protein_has_Condition` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Protein_has_Condition` (
  `Protein_id` INT NOT NULL,
  `Condition_id` INT NOT NULL,
  PRIMARY KEY (`Protein_id`, `Condition_id`),
  INDEX `fk_Protein_has_Condition_Condition1_idx` (`Condition_id` ASC),
  INDEX `fk_Protein_has_Condition_Protein1_idx` (`Protein_id` ASC),
  CONSTRAINT `fk_Protein_has_Condition_Protein1`
    FOREIGN KEY (`Protein_id`)
    REFERENCES `interactome_db`.`Protein` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_has_Condition_Condition1`
    FOREIGN KEY (`Condition_id`)
    REFERENCES `interactome_db`.`Condition` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Peptide_Ratio_Value`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Peptide_Ratio_Value` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Peptide_Ratio_Value` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Peptide_id` INT NOT NULL,
  `Ratio_Descriptor_id` INT NOT NULL,
  `value` DOUBLE NOT NULL,
  `Combination_Type_name` VARCHAR(45) NULL,
  `confidence_score_value` DOUBLE NULL,
  `confidence_score_name` VARCHAR(500) NULL,
  `Confidence_Score_Type_name` VARCHAR(90) NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Protein_Ratio_Value_Protein_Ratio_Descriptor1_idx` (`Ratio_Descriptor_id` ASC),
  INDEX `fk_Protein_Ratio_Value_Combination_Type1_idx` (`Combination_Type_name` ASC),
  INDEX `fk_Protein_Ratio_Value_Confidence_Score_Type1_idx` (`Confidence_Score_Type_name` ASC),
  INDEX `fk_Peptide_Ratio_Value_Peptide1_idx` (`Peptide_id` ASC),
  CONSTRAINT `fk_Protein_Ratio_Value_Protein_Ratio_Descriptor100`
    FOREIGN KEY (`Ratio_Descriptor_id`)
    REFERENCES `interactome_db`.`Ratio_Descriptor` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Ratio_Value_Combination_Type100`
    FOREIGN KEY (`Combination_Type_name`)
    REFERENCES `interactome_db`.`Combination_Type` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Ratio_Value_Confidence_Score_Type100`
    FOREIGN KEY (`Confidence_Score_Type_name`)
    REFERENCES `interactome_db`.`Confidence_Score_Type` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Peptide_Ratio_Value_Peptide1`
    FOREIGN KEY (`Peptide_id`)
    REFERENCES `interactome_db`.`Peptide` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Peptide_Score`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Peptide_Score` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Peptide_Score` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Peptide_id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `value` DOUBLE NOT NULL,
  `Confidence_Score_Type_name` VARCHAR(90) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_PSM_Score_Confidence_Score_Type1_idx` (`Confidence_Score_Type_name` ASC),
  INDEX `fk_Peptide_Score_Peptide1_idx` (`Peptide_id` ASC),
  CONSTRAINT `fk_PSM_Score_Confidence_Score_Type10`
    FOREIGN KEY (`Confidence_Score_Type_name`)
    REFERENCES `interactome_db`.`Confidence_Score_Type` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Peptide_Score_Peptide1`
    FOREIGN KEY (`Peptide_id`)
    REFERENCES `interactome_db`.`Peptide` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Peptide_Amount`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Peptide_Amount` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Peptide_Amount` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Peptide_id` INT NOT NULL,
  `value` DOUBLE NOT NULL,
  `Amount_Type_name` VARCHAR(90) NOT NULL,
  `Combination_Type_name` VARCHAR(90) NULL,
  `Condition_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_Protein_Amount_Amount_Type1_idx` (`Amount_Type_name` ASC),
  INDEX `fk_Protein_Amount_Combination_Type1_idx` (`Combination_Type_name` ASC),
  INDEX `fk_Peptide_Amount_Peptide1_idx` (`Peptide_id` ASC),
  INDEX `fk_Peptide_Amount_Condition1_idx` (`Condition_id` ASC),
  CONSTRAINT `fk_Protein_Amount_Amount_Type100`
    FOREIGN KEY (`Amount_Type_name`)
    REFERENCES `interactome_db`.`Amount_Type` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Amount_Combination_Type100`
    FOREIGN KEY (`Combination_Type_name`)
    REFERENCES `interactome_db`.`Combination_Type` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Peptide_Amount_Peptide1`
    FOREIGN KEY (`Peptide_id`)
    REFERENCES `interactome_db`.`Peptide` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Peptide_Amount_Condition1`
    FOREIGN KEY (`Condition_id`)
    REFERENCES `interactome_db`.`Condition` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Protein_Score`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Protein_Score` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Protein_Score` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `Protein_id` INT NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `value` DOUBLE NOT NULL,
  `Confidence_Score_Type_name` VARCHAR(90) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_PSM_Score_Confidence_Score_Type1_idx` (`Confidence_Score_Type_name` ASC),
  INDEX `fk_Protein_Score_Protein1_idx` (`Protein_id` ASC),
  CONSTRAINT `fk_PSM_Score_Confidence_Score_Type11`
    FOREIGN KEY (`Confidence_Score_Type_name`)
    REFERENCES `interactome_db`.`Confidence_Score_Type` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_Score_Protein1`
    FOREIGN KEY (`Protein_id`)
    REFERENCES `interactome_db`.`Protein` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Sample_has_Organism`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Sample_has_Organism` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Sample_has_Organism` (
  `Sample_id` INT NOT NULL,
  `Organism_taxonomyID` VARCHAR(90) NOT NULL,
  PRIMARY KEY (`Sample_id`, `Organism_taxonomyID`),
  INDEX `fk_Sample_has_Organism_Organism1_idx` (`Organism_taxonomyID` ASC),
  INDEX `fk_Sample_has_Organism_Sample1_idx` (`Sample_id` ASC),
  CONSTRAINT `fk_Sample_has_Organism_Sample1`
    FOREIGN KEY (`Sample_id`)
    REFERENCES `interactome_db`.`Sample` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Sample_has_Organism_Organism1`
    FOREIGN KEY (`Organism_taxonomyID`)
    REFERENCES `interactome_db`.`Organism` (`taxonomyID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `interactome_db`.`Peptide_has_Condition`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Peptide_has_Condition` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Peptide_has_Condition` (
  `Peptide_id` INT NOT NULL,
  `Condition_id` INT NOT NULL,
  PRIMARY KEY (`Peptide_id`, `Condition_id`),
  INDEX `fk_Peptide_has_Condition_Condition1_idx` (`Condition_id` ASC),
  INDEX `fk_Peptide_has_Condition_Peptide1_idx` (`Peptide_id` ASC),
  CONSTRAINT `fk_Peptide_has_Condition_Peptide1`
    FOREIGN KEY (`Peptide_id`)
    REFERENCES `interactome_db`.`Peptide` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Peptide_has_Condition_Condition1`
    FOREIGN KEY (`Condition_id`)
    REFERENCES `interactome_db`.`Condition` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
