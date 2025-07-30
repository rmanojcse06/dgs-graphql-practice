package edu.man.dgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;


@SpringBootApplication
@Slf4j
public class Application {
    public static void main(String[] args) {
        try {
            log.info("Starting edu.man.dgs.Application...");
            SpringApplication.run(Application.class, args);
        } catch (Throwable t) {
            log.error("Error starting application: " + t.getMessage(), t);
        }
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            log.info("\n\n\nCommandLineRunner executed with arguments: {}", (Object[]) args);
            try {
                Arrays.stream(ctx.getBeanDefinitionNames())
                        .sorted()
                        .forEach(bean -> log.info("Bean: {}", bean));
            }catch(Exception e) {
                log.error("Error listing beans: {}", e.getMessage(), e);
            }
            log.info("\n\n\n\n\n");
        };
    }

    @Bean
    public ApplicationRunner applicationLineRunner(ApplicationContext ctx) {
        return args -> {
            log.info("\n\n\nApplicationRunner executed with arguments: {}", args.getOptionNames());
            try {
                args.getOptionNames().forEach(option -> {
                    log.info("Option: {} = {}", option, args.getOptionValues(option));
                });
            } catch (Exception e) {
                log.error("Error in ApplicationLineRunner: {}", e.getMessage(), e);
            }
        };
    }
}
