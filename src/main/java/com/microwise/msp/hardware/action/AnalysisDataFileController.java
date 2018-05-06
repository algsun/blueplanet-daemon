package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.microwise.msp.hardware.service.AnalysisDataFileService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiedeng
 * @date 14-8-1
 */
@Component
@Scope("prototype")
@Route("/struts")
public class AnalysisDataFileController {

    private static Logger log = LoggerFactory.getLogger(AnalysisDataFileController.class);

    @Autowired
    private AnalysisDataFileService analysisDataFileService;

    private String locationId;

    @Route("/analysisDataFile")
    public String analysisDataFile() {
        Map<String, Object> result = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(locationId)) {
            try {
                analysisDataFileService.handleFile(locationId);
                result.put("success", true);
            } catch (Exception e) {
                result.put("success", false);
                log.error("解析木卫一数据文件失败",e);
            }
        }
        return Results.json().asRoot(result).done();
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
}
