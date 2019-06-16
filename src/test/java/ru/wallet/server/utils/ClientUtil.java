package ru.wallet.server.utils;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.wallet.server.WalletServiceGrpc;
import ru.wallet.server.repositories.AccountRepository;
import ru.wallet.server.repositories.UserRepository;

import javax.annotation.PostConstruct;

@Component
public class ClientUtil {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;


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

    public void clearDb() {
        userRepository.deleteAll();
    }
}
