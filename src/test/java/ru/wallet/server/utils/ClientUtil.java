package ru.wallet.server.utils;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.wallet.server.WalletServiceGrpc;

import javax.annotation.PostConstruct;

@Component
public class ClientUtil {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(ClientUtil.class);

    private WalletServiceGrpc.WalletServiceBlockingStub walletServiceBlockingStub;

    @PostConstruct
    private void init() {
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress("localhost", 6565).usePlaintext().build();

        walletServiceBlockingStub =
                WalletServiceGrpc.newBlockingStub(managedChannel);
    }

    public WalletServiceGrpc.WalletServiceBlockingStub getWalletServiceBlockingStub() {
        return walletServiceBlockingStub;
    }
}
