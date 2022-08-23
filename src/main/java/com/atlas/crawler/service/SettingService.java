/**
 * @author Reza Dehghani
 */

package com.atlas.crawler.service;

import com.atlas.crawler.entity.Setting;

public interface SettingService {

    Setting getSetting();

    void update(Setting setting);

}
