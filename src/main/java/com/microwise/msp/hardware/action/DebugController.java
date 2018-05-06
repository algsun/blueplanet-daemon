package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by basten on 14-2-28.
 */
@Component
@Scope("prototype")
@Route("/struts")
public class DebugController {

    @Route("/debug")
    public String debug(){
        return Results.ftl("/pages/dco");
    }
}
