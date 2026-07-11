package com.cognizant.springlearn;
import org.slf4j.*;import org.springframework.boot.*;import org.springframework.boot.autoconfigure.*;import org.springframework.context.*;import org.springframework.context.support.ClassPathXmlApplicationContext;
@SpringBootApplication public class SpringLearnApplication{static Logger LOGGER=LoggerFactory.getLogger(SpringLearnApplication.class);
public static void main(String[]a){LOGGER.info("START");SpringApplication.run(SpringLearnApplication.class,a);displayCountry();LOGGER.info("END");}
static void displayCountry(){ApplicationContext c=new ClassPathXmlApplicationContext("country.xml"); Country country=c.getBean("country",Country.class);LOGGER.debug("Country : {}",country);}}