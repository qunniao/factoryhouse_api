package com.baoge.information.Dao;

import com.baoge.information.PO.DelegationInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DelegationInformationDao extends JpaRepository<DelegationInformation, Integer>, JpaSpecificationExecutor<DelegationInformation> {
}
