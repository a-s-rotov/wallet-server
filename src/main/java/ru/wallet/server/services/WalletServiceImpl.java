package ru.wallet.server.services;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.wallet.server.WalletServiceGrpc;


@GRpcService
public class WalletServiceImpl extends WalletServiceGrpc.WalletServiceImplBase {

}
