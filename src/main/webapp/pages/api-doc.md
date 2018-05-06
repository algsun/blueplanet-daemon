### <a name="modifyInterval"></a> 修改设备工作周期

    GET /servlet/DcoServlet?method=modifyInterval

**参数**

|   名称    |  类型  |   描述  |
|     -     |      - |    -    |
| method    | string | 操作，固定为 `modifyInterval`  |
| deviceId  | number | 设备ID    |
| interval  | number | 工作周期(秒), 范围在 6~7200  |

**返回**

    {
        "success": true,
        "operate": "modifyInterval"
    }

### <a name="patrol-check-open-site"></a> 开始巡检

    GET /servlet/DcoServlet?method=pollingOpen

站点下所有的网关进行巡检

**参数**

|   名称    |  类型  |   描述  |
|     -     |      - |    -    |
| method    | string | 操作，固定为 `pollingOpen`  |
| siteId    | number | 站点ID  |
| interval  | number | 工作周期(秒), 范围在 6~7200  |


**返回**

    {
        "success": true,
        "operate": "pollingOpen"
    }

### <a name="patrol-check-open-site"></a> 结束巡检

    GET /servlet/DcoServlet?method=pollingClose

站点下所有的网关结束巡检

**参数**

|   名称    |  类型  |   描述  |
|     -     |      - |    -    |
| method    | string | 操作，固定为 `pollingClose`  |
| siteId    | number | 站点ID  |


**返回**

    {
        "success": true,
        "operate": "pollingClose"
    }

### <a name="delete-device"></a> 删除设备

    GET /struts/deleteDevice

删除某个设备

**参数**

|   名称    |  类型  |   描述  |
|     -     |   -    |    -    |
| deviceId  | number | 设备ID  |

**返回**

    {
        "success": true
    }

### <a name="patrol-check-start-device"></a> 开始巡检(网关)

    GET /struts/devices/{deviceId}/patrol-check?method=start

指定某个网关开始巡检

**参数**

|   名称    |  类型  |   描述  |
|     -     |   -    |    -    |
|{deviceId} | number | 设备ID  |
| method    | string | 操作，固定为 `start`  |
| interval  | number | 工作周期(秒), 范围在 6~7200  |

**返回**

    {
        "success": false,
        "msg": "error"
    }

### <a name="patrol-check-end-device"></a> 结束巡检(网关)

    GET /struts/devices/{deviceId}/patrol-check?method=end

指定某个网关结束巡检

**参数**

|   名称    |  类型  |   描述  |
|     -     |   -    |    -    |
|{deviceId} | number | 设备ID  |
| method    | string | 操作，固定为 `end`  |

**返回**

    {
        "success": false,
        "msg": "error"
    }

### <a name="set-default-parent"></a> 设置设备默认父节点

    GET /struts/devices/{deviceId}/default-parent/{parentId}

设置设备默认父节点

**参数**

|   名称    |  类型  |   描述  |
|     -     |   -    |    -    |
|{deviceId} | number | 设备ID  |
| {parentId}| number | 默认父节点，取消默认父节点值为 65535(0xFFFF)|

**返回**

    {
        "success": false,
    }

### <a name="conditionRefl"></a> 设备条件反射(控制模块)

    GET /struts/devices/{deviceId}/condition-refl

设备重启

**参数**

|   名称    |  类型  |   描述  |
|     -     |   -    |    -    |
|{deviceId} | number | 设备ID  |
|  route    | number | 路数，从1开始  |
| subNodeId | string | 子节点 |
| sensorId  | number | 监测指标ID |
| low       | number | 低阈值, 结果值 |
| high       | number | 高阈值，结果值 |
| switchAction | number | 动作 |

**返回**

    {
        "success": true, // http 接口调用成功
        "sendSuccess": true, // 命令发送到网关
        "doSuccess": false // 命令执行成功
    }

### <a name="conditionReflOrigin"></a> 计算原始值(控制模块)

    GET /struts/condition-refl/origin

计算条件反射的原始值

**参数**

|   名称    |  类型  |   描述  |
|     -     |   -    |    -    |
| deviceId  | string | 子节点 |
| sensorId  | number | 监测指标ID |
| target    | number | 结果值 |

**返回**

    {
        "origin": 345, // 原始值
        "originLeft": 343, // 左值
        "originRight": 348 // 右值
    }

### <a name="turnSwitch"></a> 控制开关(控制模块)

    GET /struts/devices/{deviceId}/turnSwitch

设备重启

**参数**

|   名称    |  类型  |   描述  |
|     -     |   -    |    -    |
|{deviceId} | number | 设备ID  |
|  route    | number | 路数，从1开始  |
|  onOrOff  | boolean| true 开，false 关|

**返回**

    {
        "success": false,
        "sendSuccess": true, // 命令发送到网关
        "doSuccess": false // 命令执行成功
    }


### <a name="switch-action-time-changed"></a> 自动控制时间类改变(控制模块)

    PUT /struts/devices/{deviceId}/switch-action-time

自动控制时间类改变，包括 每日执行和周期执行. 有对应的添加，删除，修改。

**参数**

|   名称    |  类型  |   描述  |
|     -     |   -    |    -    |
|{deviceId} | number | 设备ID  |
| switchActionId | string | 动作ID  |
| type | number | 动作类型: 1 每日，2 周期  |

**返回**

    {
        "success": false
    }

### <a name="restart"></a> 设备重启

    GET /struts/devices/{deviceId}/restart

设备重启

**参数**

|   名称    |  类型  |   描述  |
|     -     |   -    |    -    |
|{deviceId} | string | 设备ID  |

**返回**

    {
        "success": false,
    }

### <a name="suspend"></a> 中继待机

    GET /struts/devices/{deviceId}/suspend

中继待机

**参数**

|   名称    |  类型  |   描述  |
|     -     |   -    |    -    |
|{deviceId} | number | 设备ID  |
| inOrOut   | boolean| 进入待机 true, 退出待机 false |

**返回**

    {
        "success": false,
    }

### <a name="available-parents"></a> 查询可询父节点

    GET /struts/devices/{deviceId}/available-parents

查询可询父节点

**参数**

|   名称    |  类型  |   描述  |
|     -     |   -    |    -    |
|{deviceId} | number | 设备ID  |

**返回**

    {
        "success": false,
    }

### <a name="rf-alive"></a> 节点RF不休眠

    GET /struts/devices/{deviceId}/rf-alive

设置节点休眠状态

**参数**

|   名称    |  类型  |   描述  |
|     -     |   -    |    -    |
|{deviceId} | number | 设备ID  |
| enable    | boolean| 开启、关闭 |

**返回**

    {
        "success": false,
        "sendSuccess": true, // 命令发送到网关
        "doSuccess": false // 命令执行成功
    }

### <a name="threshold-alarm-state"></a> 设置阈值报警状态

    GET /struts/device/{deviceId}/threshold-alarm-state

设置阈值报警状态

**参数**

|   名称    |  类型  |   描述  |
|     -     |   -    |    -    |
|{deviceId} | number | 设备ID  |
| enable    | boolean| 启用、禁用 |

**返回**

    {
        "success": false,
        "sendSuccess": true, // 命令发送到网关
        "doSuccess": false // 命令执行成功
    }
    
### <a name="sensor-threshold"></a> 设置监测指标阈值

    GET /struts/device/{deviceId}/sensor-threshold

设置设备监测指标阈值范围

**参数**

|   名称    |  类型  |   描述  |
|     -     |   -    |    -    |
|{deviceId} | number | 设备ID  |
| sensorId  | number | 监测指标ID |
| high      | number | 高阈值  |
| low       | number | 低阈值  |

**返回**

    {
        "success": false,
        "sendSuccess": true, // 命令发送到网关
        "doSuccess": false // 命令执行成功
    }
    
### <a name="evict-threshold-alarm-cache"></a> 清除阈值报警缓存

    GET /thresholdCacheServlet

通常是在阈值报警改变的时候调用

**参数**

|   名称    |  类型  |   描述  |
|     -     |   -    |    -    |
| zoneId    | number | 区域ID  |

**返回**

    {
        "success": true
    }

### <a name="evict-custom-formula-cache"></a> 清除设备自定义公式

    GET /caches/device-custom-formula?_method=delete

通常是在设备自定义公式改变的时候调用

**参数**

|   名称    |  类型  |   描述  |
|     -     |   -    |    -    |
| deviceId  | number | 设备ID  |

**返回**

    {
        "success": true
    }


### <a name="other"></a> 其他

<li>/fetchData</li>
<li>/daemonServlet</li>
