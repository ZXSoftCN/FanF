package com.zxsoft.fanfanfamily.mort.repository;

import com.zxsoft.fanfanfamily.base.domain.TraceLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraceLogDao extends JpaRepository<TraceLog,String> {

//    @Transactional(propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
//    public TraceLog saveLog()

}
