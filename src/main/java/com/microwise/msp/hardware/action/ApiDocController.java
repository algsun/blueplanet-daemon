package com.microwise.msp.hardware.action;

import com.bastengao.struts2.freeroute.Results;
import com.bastengao.struts2.freeroute.annotation.Route;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author gaohui
 * @date 13-12-30 12:55
 */
@Component
@Scope("prototype")
@Route("/struts")
public class ApiDocController {
    public static final PegDownProcessor pegDownProcessor = new PegDownProcessor(Extensions.ALL);

    @Route("/api-doc")
    public String index(){
        return Results.ftl("/pages/api-doc");
    }

    /**
     * 将 markdown 转化为 html
     *
     * @param markdown
     * @return
     */
    public String markdown2html(String markdown) {
        return pegDownProcessor.markdownToHtml(markdown);
    }
}
