package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;
import com.opensymphony.xwork2.ActionContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author gaohui
 * @date 13-10-23 18:01
 */
@Route("/struts")
public class HelloController {
    @Route("hello.json")
    public String hello(){
        Map<String, Object> data = new HashMap<String,Object>();
        data.put("name", "blueplanet-daemon");
        ActionContext.getContext().put("data", data);

        return Results.json().root("data").done();
    }
}
