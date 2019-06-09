package ru.wallet.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.io.IOException;
import java.util.logging.Logger;

@SpringBootApplication
public class WalletServerApplication {

    private static final Logger logger = Logger.getLogger(WalletServerApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(WalletServerApplication.class, args);
    }

}
