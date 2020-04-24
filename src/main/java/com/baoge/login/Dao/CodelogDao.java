package com.baoge.login.Dao;

import com.baoge.login.PO.Codelog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodelogDao extends JpaRepository<Codelog, Integer> {

    Codelog queryTop1ByCodePhoneOrderByCodeIdDesc(String codePhone);

    Codelog queryTop1ByCodePhoneAndTypeOrderByCodeIdDesc(String codePhone,short type);
}
