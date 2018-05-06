安装部署说明：

1.将msp文件夹拷贝至tomcat/webapps/目录下
2.修改数据配置文件（msp\WEB-INF\classes\config.properties）
3.配置UDP监听端口（数据库m_netinfo表）
4.如使用旧网关，需在t_siteinfo表中配置当前站点信息（isCurrentSite=1为当前站点）。
5.启动tomcat，中间件即启动。