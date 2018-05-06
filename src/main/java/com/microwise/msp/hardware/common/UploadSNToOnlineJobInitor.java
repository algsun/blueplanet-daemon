package com.microwise.msp.hardware.common;

import com.google.gson.Gson;
import com.microwise.msp.hardware.businessbean.DeviceBean;
import com.microwise.msp.hardware.businessbean.DeviceFormulaBean;
import com.microwise.msp.hardware.businessbean.Formula;
import com.microwise.msp.hardware.businessbean.Product;
import com.microwise.msp.hardware.businessservice.FormulaService;
import com.microwise.msp.hardware.cache.AppCache;
import com.microwise.msp.hardware.dao.DeviceDao;
import com.microwise.msp.hardware.vo.NodeSensor;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 上传产品序列号至online
 *
 * @author liuzhu
 * @date 15-1-12
 */
@Deprecated
public class UploadSNToOnlineJobInitor {

    private static final Logger log = LoggerFactory.getLogger(UploadSNToOnlineJobInitor.class);

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private DeviceDao deviceDao;

    public void initSendCommandTrigger(DeviceBean deviceBean) {

        // 组织JobDataMap
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("deviceBean", deviceBean);
        dataMap.put("appContext", appContext);
        dataMap.put("deviceDao", deviceDao);

        try {

            JobDetail jobDetail = JobBuilder.newJob(UploadSNToOnlineJob.class)
                    .withIdentity(String.format("%s", deviceBean))
                    .storeDurably()
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .forJob(String.format("%s", deviceBean))
                    .withIdentity(String.format("%s", deviceBean))
                    .withSchedule(SimpleScheduleBuilder.repeatSecondlyForTotalCount(1))
                    .usingJobData(dataMap)
                    .startNow()
                    .build();

            // 调度
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (UnableToInterruptJobException e) {
            log.error("CommandSendJobInitor", e);
        } catch (SchedulerException e) {
            log.error("CommandSendJobInitor", e);
        }
    }

    public static class UploadSNToOnlineJob implements InterruptableJob {

        /**
         * 是否中断
         */
        private boolean interrupted;

        @Override
        public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

            ApplicationContext appContext = (ApplicationContext) jobExecutionContext.getTrigger().getJobDataMap().get("appContext");
            DeviceBean deviceBean = (DeviceBean) jobExecutionContext.getTrigger().getJobDataMap().get("deviceBean");
            DeviceDao dao = (DeviceDao)jobExecutionContext.getTrigger().getJobDataMap().get("deviceDao");
            AppCache appCache = appContext.getBean(AppCache.class);

            FormulaService formulaService = appContext.getBean(FormulaService.class);
            OnlineHttpApiClient onlineHttpApiClient = new OnlineHttpApiClient();

            try {

                List<DeviceFormulaBean> deviceFormulaBeans = onlineHttpApiClient.downloadFormula(deviceBean.getSn());
                //如果云端有数据，将设备公式更新至数据库
                if (!deviceFormulaBeans.isEmpty()) {
                    for (DeviceFormulaBean deviceFormulaBean : deviceFormulaBeans) {
                        formulaService.saveOrUpdateParamsByDeviceId(deviceBean.deviceid, deviceFormulaBean.getSensorId(), deviceFormulaBean.getFormulaParams());
                    }
                } else {
                    //如果云端没有数据，那么将设备的默认公式上传至云端

                    List<DeviceFormulaBean> formulaBeans = new ArrayList<DeviceFormulaBean>();
                    List<NodeSensor> nodeSensors = appCache.loadDeviceSensors(deviceBean.deviceid);

                    //组织formulaBeans
                    getFormulaBeans(deviceBean, appCache, formulaBeans, nodeSensors);

                    if (!formulaBeans.isEmpty()) {
                            //上传设备公式
                            onlineHttpApiClient.post(SysConfig.galaxyOnLineUrl + "products/" + deviceBean.getSn() + "/formula", new Gson().toJson(formulaBeans));
                            //上传站点信息
                            uploadSite(deviceBean, appCache);
                    }
                }
                dao.updateUploadState(deviceBean.deviceid);
                appCache.evictUploadState(deviceBean.deviceid);
            } catch (Exception e) {
                log.error("网络不通，连接云端失败，上传数据失败", e);
            }

        }

        /**
         * 上传站点信息
         *
         * @param deviceBean 设备bean
         * @param appCache   数据缓存
         */
        private void uploadSite(DeviceBean deviceBean, AppCache appCache) {
            RestTemplate restTemplate = new RestTemplate();
            List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
            converters.add(new MappingJackson2HttpMessageConverter());
            restTemplate.setMessageConverters(converters);
            Product product = new Product();
            String siteId = deviceBean.deviceid.substring(0, 8);
            product.setSiteId(siteId);
            product.setSiteName(appCache.loadSite(siteId).getName());
            HttpEntity<Product> entity = new HttpEntity<Product>(product);
            restTemplate.put(SysConfig.galaxyOnLineUrl + "products/" + deviceBean.getSn() + "/info", entity);
        }

        /**
         * 组织 formulaBeans数据
         *
         * @param deviceBean   设备bean
         * @param appCache     缓存
         * @param formulaBeans 设备公式
         * @param nodeSensors  实时数据
         */
        private void getFormulaBeans(DeviceBean deviceBean, AppCache appCache, List<DeviceFormulaBean> formulaBeans, List<NodeSensor> nodeSensors) {
            for (NodeSensor sensor : nodeSensors) {
                Formula formula = appCache.loadFormula(sensor.getSensorPhysicalid());
                DeviceFormulaBean formulaBean = new DeviceFormulaBean();
                formulaBean.setSerialNumber(String.valueOf(deviceBean.getSn()));
                formulaBean.setFormulaName(formula.getName());
                formulaBean.setSensorId(sensor.getSensorPhysicalid());
                formulaBean.setSensorName(appCache.loadSensor(sensor.getSensorPhysicalid()).getCnName());
                formulaBean.setFormulaParams(formula.getFormulaParams());
                formulaBeans.add(formulaBean);
            }
        }

        @Override
        public void interrupt() throws UnableToInterruptJobException {
            interrupted = true;
        }
    }
}
