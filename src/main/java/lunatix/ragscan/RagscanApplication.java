package lunatix.ragscan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.command.annotation.CommandScan;

@SpringBootApplication
@CommandScan
public class RagscanApplication {

    public static void main(String[] args) {
        SpringApplication.run(RagscanApplication.class, args);
    }

}
