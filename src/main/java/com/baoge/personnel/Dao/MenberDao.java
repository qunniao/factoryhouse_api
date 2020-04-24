package com.baoge.personnel.Dao;

import com.baoge.personnel.PO.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

public interface MenberDao extends JpaRepository<MemberEntity, Integer> {

    int countByUidAndExpiryDateGreaterThan(int uid,Date expiryDate);

    MemberEntity queryByUid(int uid);
}
