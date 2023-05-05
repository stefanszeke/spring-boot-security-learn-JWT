package com.stevo.bankbackend2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.stevo.bankbackend2.model.Notice;

@Repository
public interface NoticeRepository extends CrudRepository<Notice, Long> {
	
	@Query(value = "from Notice n where CURRENT_DATE BETWEEN noticBegDt AND noticEndDt")
	List<Notice> findAllActiveNotices();

}
