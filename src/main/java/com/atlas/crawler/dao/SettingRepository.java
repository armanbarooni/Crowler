/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.dao;

import com.atlas.crawler.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingRepository extends JpaRepository<Setting,Integer> {

}
