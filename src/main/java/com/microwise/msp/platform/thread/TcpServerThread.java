package com.microwise.msp.platform.thread;

import com.microwise.msp.hardware.common.SysConfig;
import com.microwise.msp.platform.handler.ServerHandler;
import com.microwise.msp.util.AppContext;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * tcpServerThread【tcp服务端启动线程】
 * 
 * @author heming
 * @since 2011-11-01
 */
public class TcpServerThread implements Runnable {
	private static Logger log = LoggerFactory.getLogger(TcpServerThread.class);

	/** 创建一个非阻塞的Server端Socket */
	SocketAcceptor acceptor = new NioSocketAcceptor();

	private final Thread th = new Thread(this);

	/**
	 * 启动tcp监听端口
	 * 
	 * @param port
	 *            监听端口
	 * @return
	 */
	public boolean listener(int port) {
		boolean isTrue = false;
		// NioSocketAcceptor acceptor = new NioSocketAcceptor();

		// 创建接收数据的过滤器,添加过滤器到Acceptor
		DefaultIoFilterChainBuilder filterChain = acceptor.getFilterChain();

		// 建立线程池 Executors.newCachedThreadPool()
		// Executor threadPool = Executors.newFixedThreadPool(500);
		// filterChain.addLast("exector", new ExecutorFilter(threadPool));
		// filterChain.addLast("exector", new ExecutorFilter());

		// ExecutorService executorService = Executors.newCachedThreadPool();
		// filterChain.addFirst("exector", new ExecutorFilter(executorService));

		// 添加日志过滤器
		filterChain.addLast("logger", new LoggingFilter());
		// filterChain.addLast("textFilter", new ProtocolCodecFilter(
		// new TextLineCodecFactory(Charset.forName("GBK"))));

		// 添加对象过滤器
		filterChain.addLast("objectFilter", new ProtocolCodecFilter(
				new ObjectSerializationCodecFactory()));

		// 设置主服务监听的端口可以重用
		acceptor.setReuseAddress(true);
		// 设置每一个非主监听连接的端口可以重用
		acceptor.getSessionConfig().setReuseAddress(true);
		// 设置空闲时间为180s(2013-5-15)
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 60 * 3);
		// 设置输入缓冲区大小
		// acceptor.getSessionConfig().setReceiveBufferSize(1024);
		// 设置输出缓冲区大小
		// acceptor.getSessionConfig().setSendBufferSize(10240);
		// 设置为非延迟发送，为true则不组装成大包发送，收到东西马上发出
		// acceptor.getSessionConfig().setTcpNoDelay(true);
		// 设置主服务监听端口的监听队列的最大值为100，如果当前已经有100个连接，再有新连接来将被服务器拒绝
		acceptor.setBacklog(100);

		ServerHandler handler = (ServerHandler) AppContext.getInstance()
				.getAppContext().getBean("ServerHandler");
		acceptor.setHandler(handler);
		try {
			acceptor.bind(new InetSocketAddress(port));
			isTrue = true;
			log.info("sync listening on [{}]", port);
		} catch (IOException e) {
			isTrue = false;
			log.error(e.getMessage());
		}
		return isTrue;
	}

	/**
	 * 启动tcp监听，如果监听失败，1分钟后再次尝试启动监听服务
	 */
	@Override
	public void run() {
		try {
			boolean isTrue = listener(SysConfig.getInstance().getSynchronizePort());
			if (!isTrue) {
				Thread.sleep(1000 * 60); // 休眠1分钟
				th.interrupt();
				th.start();
			}
		} catch (Exception e) {
			log.error("\n\n --tcpServerThread 异常 " + e.getMessage());
			if (th.isInterrupted()) {
				th.start();
			}
		}
	}

    /**
     * 停止监听
     */
    public void shutdown(){
        acceptor.dispose();
    }

}
