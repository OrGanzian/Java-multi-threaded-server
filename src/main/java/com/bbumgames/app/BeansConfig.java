package com.bbumgames.app;

import com.bbumgames.server.TcpServer;
import org.springframework.context.annotation.*;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@ComponentScan({"com.bbumgames.server","com.bbumgames.app"})
//@PropertySource("classpath:beans-config.properties") //properties file config
public class BeansConfig {


//    @Bean
//    public TcpServer studentOr() {
//        TcpServer or = new TcpServer();
//        return or;
//    }


}
