package ru.wallet.server.services;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.wallet.server.BalanceMap;
import ru.wallet.server.Deposit;
import ru.wallet.server.User;
import ru.wallet.server.WalletServiceGrpc;
import ru.wallet.server.entities.Currency;
import ru.wallet.server.entities.UserEntity;
import ru.wallet.server.exception.InvalidDataException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;


@GRpcService
public class RpcWalletServiceImpl extends WalletServiceGrpc.WalletServiceImplBase {
    private static final Logger logger =
            LoggerFactory.getLogger(RpcWalletServiceImpl.class);

    @Autowired
    private DbWalletService dbWalletService;

    @Override
    public void putDeposit(Deposit request, StreamObserver<Empty> responseObserver) {
        logger.debug("Method {}. Server received {}", "putDeposit", request);
        try {
            dbWalletService.putDeposit(request.getUser().getUserId(), new BigDecimal(request.getAmount()), Currency.valueOf(request.getCurrency()));
            responseObserver.onNext(Empty.newBuilder().build());
        } catch (InvalidDataException invalidData) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription(invalidData.getMessage()).asRuntimeException());
        } catch (NullPointerException nullPointer) {
            throw new InvalidDataException("Data is wrong. NPE. ");
        } catch (NumberFormatException numberFormat) {
            throw new InvalidDataException("Amount is wrong");
        }
        responseObserver.onCompleted();
    }

    @Override
    public void createUser(Empty request, StreamObserver<User> responseObserver) {
        logger.debug("Method {}. Server received {}", "createUser", request);
        try {
            UserEntity user = dbWalletService.createUser(null);
            responseObserver.onNext(User.newBuilder().setUserId(user.getId()).build());
        } catch (InvalidDataException invalidData) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription(invalidData.getMessage()).asRuntimeException());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void takeDeposit(Deposit request, StreamObserver<Empty> responseObserver) {
        logger.debug("Method {}. Server received {}", "takeDeposit", request);
        try {
            dbWalletService.takeDeposit(request.getUser().getUserId(), new BigDecimal(request.getAmount()), Currency.valueOf(request.getCurrency()));
            responseObserver.onNext(Empty.newBuilder().build());
            responseObserver.onCompleted();

        } catch (InvalidDataException invalidData) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription(invalidData.getMessage()).asRuntimeException());
        } catch (NullPointerException nullPointer) {
            throw new InvalidDataException("Data is wrong. NPE. ");
        } catch (NumberFormatException numberFormat) {
            throw new InvalidDataException("Amount is wrong");
        }
    }

    @Override
    public void getBalance(User request, StreamObserver<BalanceMap> responseObserver) {
        logger.debug("Method {}. server received {}", "getBalance", request);
        try {
            Map<String, String> collect = dbWalletService.getBalance(request.getUserId()).stream()
                    .collect(Collectors.toMap(e -> e.getType().toString(), e -> e.getBalance().toString()));
            responseObserver.onNext(BalanceMap.newBuilder().putAllBalance(collect).build());

        } catch (InvalidDataException invalidData) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription(invalidData.getMessage()).asRuntimeException());
        } catch (NullPointerException nullPointer) {
            throw new InvalidDataException("Data is wrong. NPE. ");
        } catch (NumberFormatException numberFormat) {
            throw new InvalidDataException("Amount is wrong");
        }
        responseObserver.onCompleted();
    }
}
