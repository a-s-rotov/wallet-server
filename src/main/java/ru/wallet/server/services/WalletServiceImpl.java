package ru.wallet.server.services;

import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.wallet.server.*;


@GRpcService
public class WalletServiceImpl extends WalletServiceGrpc.WalletServiceImplBase {
    private static final Logger LOGGER =
            LoggerFactory.getLogger(WalletServiceImpl.class);


    @Override
    public void putDeposit(Deposit request, StreamObserver<Empty> responseObserver) {
        LOGGER.debug("Method {}. Server received {}", "putDeposit", request);

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void takeDeposit(Deposit request, StreamObserver<Empty> responseObserver) {
        LOGGER.debug("Method {}. Server received {}", "takeDeposit", request);

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void getBalance(User request, StreamObserver<Balance> responseObserver) {
        LOGGER.debug("Method {}. server received {}", "getBalance", request);

        responseObserver.onNext(Balance.newBuilder().build());
        responseObserver.onCompleted();
    }
}
