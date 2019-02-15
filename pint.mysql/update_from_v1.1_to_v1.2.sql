ALTER TABLE `interactome_db`.`ptm` 
DROP FOREIGN KEY `fk_PTM_PSM1`;
ALTER TABLE `interactome_db`.`ptm` 
CHANGE COLUMN `PSM_id` `PSM_id` INT(11) NULL ;
ALTER TABLE `interactome_db`.`ptm` 
ADD CONSTRAINT `fk_PTM_PSM1`
  FOREIGN KEY (`PSM_id`)
  REFERENCES `interactome_db`.`psm` (`id`);

-- -----------------------------------------------------
-- Table `interactome_db`.`Protein_has_MS_Run`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Protein_has_MS_Run` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Protein_has_MS_Run` (
  `Protein_id` INT NOT NULL,
  `MS_Run_id` INT NOT NULL,
  PRIMARY KEY (`Protein_id`, `MS_Run_id`),
  CONSTRAINT `fk_Protein_has_MS_Run_Protein1`
    FOREIGN KEY (`Protein_id`)
    REFERENCES `interactome_db`.`Protein` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Protein_has_MS_Run_MS_Run1`
    FOREIGN KEY (`MS_Run_id`)
    REFERENCES `interactome_db`.`MS_Run` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Protein_has_MS_Run_MS_Run1_idx` ON `interactome_db`.`Protein_has_MS_Run` (`MS_Run_id` ASC);

CREATE INDEX `fk_Protein_has_MS_Run_Protein1_idx` ON `interactome_db`.`Protein_has_MS_Run` (`Protein_id` ASC);



-- -----------------------------------------------------
-- Table `interactome_db`.`Peptide_has_MS_Run`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `interactome_db`.`Peptide_has_MS_Run` ;

CREATE TABLE IF NOT EXISTS `interactome_db`.`Peptide_has_MS_Run` (
  `Peptide_id` INT NOT NULL,
  `MS_Run_id` INT NOT NULL,
  PRIMARY KEY (`Peptide_id`, `MS_Run_id`),
  CONSTRAINT `fk_Peptide_has_MS_Run_Peptide1`
    FOREIGN KEY (`Peptide_id`)
    REFERENCES `interactome_db`.`Peptide` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Peptide_has_MS_Run_MS_Run1`
    FOREIGN KEY (`MS_Run_id`)
    REFERENCES `interactome_db`.`MS_Run` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `fk_Peptide_has_MS_Run_MS_Run1_idx` ON `interactome_db`.`Peptide_has_MS_Run` (`MS_Run_id` ASC);

CREATE INDEX `fk_Peptide_has_MS_Run_Peptide1_idx` ON `interactome_db`.`Peptide_has_MS_Run` (`Peptide_id` ASC);

DROP PROCEDURE IF EXISTS interactome_db.`insert_into_protein_has_ms_run` ;
DROP PROCEDURE IF EXISTS interactome_db.`my_proc_protein` ;

DELIMITER $$
CREATE PROCEDURE interactome_db.`insert_into_protein_has_ms_run`(arg_protein_id INT, arg_ms_run_id INT ) 
BEGIN
	INSERT into interactome_db.protein_has_ms_run values (arg_protein_id,arg_ms_run_id);
END $$

CREATE PROCEDURE interactome_db.`my_proc_protein`( ) 
BEGIN

DECLARE val1 INT DEFAULT NULL;
DECLARE val2 INT DEFAULT NULL;

DECLARE done TINYINT DEFAULT FALSE;

DECLARE cursor1 -- cursor1 is an arbitrary label, an identifier for the cursor
 CURSOR FOR
 SELECT t1.id, t1.MS_Run_id
   FROM interactome_db.protein t1;
   
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

-- open the cursor

OPEN cursor1;

my_loop: -- loops have to have an arbitrary label; it's used to leave the loop
LOOP

  -- read the values from the next row that is available in the cursor

  FETCH cursor1 INTO val1,val2;

  IF done THEN -- this will be true when we are out of rows to read, so we go to the statement after END LOOP.
    LEAVE my_loop; 
  ELSE -- val1 and val2 will be the next values from c1 and c2 in table t1, 
       -- so now we call the procedure with them for this "row"
    CALL insert_into_protein_has_ms_run(val1,val2);
    -- maybe do more stuff here
  END IF;
END LOOP;

-- execution continues here when LEAVE my_loop is encountered;
-- you might have more things you want to do here

-- the cursor is implicitly closed when it goes out of scope, or can be explicitly closed if desired

CLOSE cursor1;

END $$

DELIMITER ;

call my_proc_protein();

DROP PROCEDURE IF EXISTS interactome_db.`insert_into_peptide_has_ms_run` ;
DROP PROCEDURE IF EXISTS interactome_db.`my_proc_peptide` ;

DELIMITER $$
CREATE PROCEDURE interactome_db.`insert_into_peptide_has_ms_run`(arg_peptide_id INT, arg_ms_run_id INT ) 
BEGIN
	INSERT into interactome_db.peptide_has_ms_run values (arg_peptide_id,arg_ms_run_id);
END $$

DELIMITER ;

 
DELIMITER $$
CREATE PROCEDURE interactome_db.`my_proc_peptide`( ) 
BEGIN

DECLARE val1 INT DEFAULT NULL;
DECLARE val2 INT DEFAULT NULL;

DECLARE done TINYINT DEFAULT FALSE;

DECLARE cursor1 -- cursor1 is an arbitrary label, an identifier for the cursor
 CURSOR FOR
 SELECT t1.id, t1.MS_Run_id
   FROM interactome_db.peptide t1;
   
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

-- open the cursor

OPEN cursor1;

my_loop: -- loops have to have an arbitrary label; it's used to leave the loop
LOOP

  -- read the values from the next row that is available in the cursor

  FETCH cursor1 INTO val1,val2;

  IF done THEN -- this will be true when we are out of rows to read, so we go to the statement after END LOOP.
    LEAVE my_loop; 
  ELSE -- val1 and val2 will be the next values from c1 and c2 in table t1, 
       -- so now we call the procedure with them for this "row"
    CALL insert_into_peptide_has_ms_run(val1,val2);
    -- maybe do more stuff here
  END IF;
END LOOP;

-- execution continues here when LEAVE my_loop is encountered;
-- you might have more things you want to do here

-- the cursor is implicitly closed when it goes out of scope, or can be explicitly closed if desired

CLOSE cursor1;

END $$

DELIMITER ;

call my_proc_peptide();
