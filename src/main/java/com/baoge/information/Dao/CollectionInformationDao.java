package com.baoge.information.Dao;

import com.baoge.information.PO.CollectionInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CollectionInformationDao extends JpaRepository<CollectionInformation, Integer>, JpaSpecificationExecutor<CollectionInformation> {
    List<CollectionInformation> findAllByUid(int uid);

    List<CollectionInformation> findAllByUidAndPrimaryid(int uid, int primaryid);
//    List<CollectionInformation> findAllByUidPrimaryid(int uid, int primaryid);
}
