package com.bbumgames.app;

import com.bbumgames.server.TcpServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Server start point
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeansConfig.class);
        Thread t1 = new Thread(applicationContext.getBean("tcpServer", TcpServer.class));
        t1.start();
    }
}
