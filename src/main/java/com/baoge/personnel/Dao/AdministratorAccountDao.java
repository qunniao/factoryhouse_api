package com.baoge.personnel.Dao;

import com.baoge.personnel.PO.AdministratorAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdministratorAccountDao extends JpaRepository<AdministratorAccount, Integer> , JpaSpecificationExecutor<AdministratorAccount> {
    AdministratorAccount queryByAccountNumber(String userPhone);
    AdministratorAccount queryByAccountNumberAndUserPassword(String accountNumber,String userPassword);
}
