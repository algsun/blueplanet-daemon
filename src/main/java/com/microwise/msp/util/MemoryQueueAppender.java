package com.microwise.msp.util;

import com.google.common.base.Charsets;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;

import java.io.Serializable;

/**
 * 将日志放在有界队列中
 *
 */
@Plugin(name = "MemoryQueue", category = "Core", elementType = "appender", printObject = true)
public final class MemoryQueueAppender extends AbstractAppender {

    public MemoryQueueAppender(String name, Filter filter, Layout<? extends Serializable> layout) {
        super(name, filter, layout);
    }

    @PluginFactory
    public static MemoryQueueAppender createAppender(@PluginAttribute("name") String name,
                                              @PluginElement("Layout") Layout<? extends Serializable> layout,
                                              @PluginElement("Filter") Filter filter) {
        return new MemoryQueueAppender(name, filter, layout);
    }

    @Override
    public void append(LogEvent event) {
        String log = new String(getLayout().toByteArray(event), Charsets.UTF_8);
        LogQueue.getInstance().append(event.getLevel().intLevel(), log);
    }
}