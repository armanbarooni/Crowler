/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.service.impl;

import com.atlas.crawler.dao.SettingRepository;
import com.atlas.crawler.entity.Setting;
import com.atlas.crawler.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SettingServiceImpl implements SettingService {

    @Autowired
    private SettingRepository settingRepository;

    @Override
    @Transactional
    public Setting getSetting() {
        return settingRepository.getOne(1);
    }

    @Override
    @Transactional
    public void update(Setting setting) {
        settingRepository.save(setting);

    }
}
