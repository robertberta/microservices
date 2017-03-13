CREATE TABLE `approvaldb`.`amountlimit` (
  `id` BIGINT(20) NOT NULL,
  `idCompany` BIGINT(20) NOT NULL,
  `currency` VARCHAR(4) NOT NULL,
  `amount` BIGINT(20) NOT NULL,
  `idUser` BIGINT(20) NOT NULL,
  PRIMARY KEY (`id`));
INSERT INTO `approvaldb`.`amountlimit` (`id`, `idCompany`, `currency`, `amount`, `idUser`) VALUES ('1', '10001', 'EUR', '5000', '1001');
INSERT INTO `approvaldb`.`amountlimit` (`id`, `idCompany`, `currency`, `amount`, `idUser`) VALUES ('1', '10001', 'USD', '2000', '1003');

