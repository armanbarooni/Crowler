/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.dao;

import com.atlas.crawler.entity.Log;
import com.atlas.crawler.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;

public interface LogRepository extends JpaRepository<Log , Integer> {


    List<Log> findTop5LogsByUserAndEventResultAndEventTypeOrderByIdDesc(User user,Integer eventResult,String eventType);

    @Query("select count(l) from Log l where l.user=?1 and l.eventType = 'Login Unsuccessful' and" +
            " l.eventResult = 0 and l.id > (select max(lo.id) from Log lo where lo.eventType = 'Login Successful' and lo.eventResult = 1 and lo.user = ?1 and" +
            " lo.id < (select max(lo.id) from Log lo where lo.eventType = 'Login Successful' and lo.eventResult = 1 and lo.user = ?1))")
    Integer countLogsFailLogin(User user);

    @Query("select log from Log log order by log.timeStampFinishDate desc ")
    List<Log> findAllByOrderByTimeStampStartDateDesc();

    @Query("select log from Log log where log.eventType in (?1) or log.formName in (?1) order by log.timeStampFinishDate desc ")
    List<Log> findLogsByEventTypeInOrFormNameInOrderByTimeStampStartDateDesc(Collection<String> event);

    @Query("select log from Log log where log.timeStampFinishDate <= ?2 and log.id in(select lg from Log lg where lg.eventType in (?1) or lg.formName in (?1) ) order by log.timeStampFinishDate desc ")
    List<Log> findLogsByEventTypeInOrFormNameInAndTimeStampStartDateLessThanEqualOrderByTimeStampStartDateDesc(Collection<String> event,Long endDate);

    @Query("select log from Log log where log.timeStampFinishDate <= ?1 order by log.timeStampFinishDate desc ")
    List<Log> findLogsByTimeStampStartDateLessThanEqualOrderByTimeStampStartDateDesc(Long endDate);

    @Query("select log from Log log where log.timeStampFinishDate >= ?2 and log.id in(select lg from Log lg where lg.eventType in (?1) or lg.formName in (?1) ) order by log.timeStampFinishDate desc ")
    List<Log> findLogsByEventTypeInOrFormNameInAndTimeStampStartDateGreaterThanEqualOrderByTimeStampStartDateDesc(Collection<String> event,Long startDate);

    @Query("select log from Log log where log.timeStampFinishDate >= ?1 order by log.timeStampFinishDate desc ")
    List<Log> findLogsByTimeStampStartDateGreaterThanEqualOrderByTimeStampStartDateDesc(Long startDate);

    @Query("select log from Log log where log.timeStampFinishDate between ?2 and ?3 and log.id in(select lg from Log lg where lg.eventType in (?1) or lg.formName in (?1) ) order by log.timeStampFinishDate desc ")
    List<Log> findLogsByEventTypeInOrFormNameInAndTimeStampStartDateBetweenOrderByTimeStampStartDateDesc(Collection<String> event,Long startDate,Long endDate);

    @Query("select log from Log log where log.timeStampFinishDate between ?1 and ?2 order by log.timeStampFinishDate desc ")
    List<Log> findLogsByTimeStampStartDateBetweenOrderByTimeStampStartDateDesc(Long startDate,Long endDate);


}
