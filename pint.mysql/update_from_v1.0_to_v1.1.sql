ALTER TABLE `interactome_db`.`ptm`
	ADD COLUMN `Peptide_id` INT,
    ADD FOREIGN KEY `fk_PTM_Peptide1`(`Peptide_id`) REFERENCES `interactome_db`.`Peptide` (`id`) 
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
CREATE INDEX `fk_PTM_Peptide1_idx` ON `interactome_db`.`PTM` (`Peptide_id` ASC);

# after setting all peptide ids:
ALTER TABLE `interactome_db`.`PTM` MODIFY `Peptide_id` INT NOT NULL;


ALTER TABLE `interactome_db`.`peptide`
	ADD COLUMN `full_sequence` varchar(300);
    

ALTER TABLE `interactome_db`.`peptide`
	ADD COLUMN `num_psms` int;
    
DELIMITER //
drop procedure if exists load_num_psms_to_peptides //
DELIMITER //
create procedure load_num_psms_to_peptides()
BEGIN
	declare v_counter INT;
    declare v_max INT;
    declare result INT;
    declare num_psms2 INT;
    SET v_max = (select count(*) from peptide);
	SET v_counter = 0;
    mainloop:
    WHILE v_counter < v_max do
		set v_counter=v_counter+1;
        set num_psms2 = (select num_psms from peptide where id = v_counter);
        IF isnull(num_psms2) THEN
			start transaction;
			set result = (select count(*) as result from psm where psm.Peptide_id = v_counter);
			update peptide set num_psms = result where peptide.id=v_counter;
			commit;
        END IF;
    END WHILE mainloop;
END //
call load_num_psms_to_peptides()
DELIMITER ;

