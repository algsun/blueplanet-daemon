package com.microwise.msp.hardware.businessbean.rabbitmq;

import lombok.Data;

import java.util.List;

/**
 * 银河的区域/终结者的文物
 *
 * @author sun.cong
 * @create 2017-11-20 13:07
 **/
@Data
public class Target extends Base {
    private List<Location> locations;
}
