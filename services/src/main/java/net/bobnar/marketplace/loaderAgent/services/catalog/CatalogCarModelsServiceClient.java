package net.bobnar.marketplace.loaderAgent.services.catalog;

import com.kumuluz.ee.grpc.client.GrpcClient;
import com.kumuluz.ee.grpc.client.GrpcChannelConfig;
import com.kumuluz.ee.grpc.client.GrpcChannels;
import net.bobnar.marketplace.common.dtos.catalog.v1.carBrands.CarBrand;
import net.bobnar.marketplace.common.dtos.catalog.v1.carModels.CarModel;
import net.bobnar.marketplace.common.grpc.catalog.CarModelsGrpc;
import net.bobnar.marketplace.common.grpc.catalog.CarModelsService;
import org.parboiled.common.Tuple2;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.net.ssl.SSLException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class CatalogCarModelsServiceClient {

//    private final static Logger logger = Logger.getLogger(CatalogCarModelsServiceClient.class.getName());

    private CarModelsGrpc.CarModelsBlockingStub stub;

    @PostConstruct
    public void init() {
        try {
            GrpcChannels clientPool = GrpcChannels.getInstance();
            GrpcChannelConfig config = clientPool.getGrpcClientConfig("catalog");
            GrpcClient client = new GrpcClient(config);
            stub = CarModelsGrpc.newBlockingStub(client.getChannel());//.withCallCredentials(new JWTClientCredentials(JWT_TOKEN));
        } catch (SSLException e) {
//            logger.warning(e.getMessage());
        }
    }

    public CarBrand resolveBrand(String brandName, String title) {
        CarModelsService.BrandResolveResponse response = stub.resolveBrand(CarModelsService.BrandResolveRequest.newBuilder()
                .setBrand(brandName != null ? brandName : "")
                .setTitle(title != null ? title : "")
                .build());

        if (response.hasResolvedBrand()) {
            CarModelsService.CarBrand item = response.getResolvedBrand();
            return new CarBrand(
                    item.getId(),
                    item.getName(),
                    item.getPrimaryIdentifier(),
                    String.join(",", item.getIdentifiersList()),
                    0L
            );
        }

        return null;
    }

    public CarModel resolveModel(String brandPrimaryIdentifier, String modelName, String title) {
        CarModelsService.ModelResolveResponse response = stub.resolveModel(CarModelsService.ModelResolveRequest.newBuilder()
                .setBrandPrimaryIdentifier(brandPrimaryIdentifier != null ? brandPrimaryIdentifier : "")
                .setModel(modelName != null ? modelName : "")
                .setTitle(title != null ? title : "")
                .build());

        if (response.hasResolvedModel()) {
            CarModelsService.CarModel item = response.getResolvedModel();
            return new CarModel(
                    item.getId(),
                    item.getName(),
                    item.getBrandId(),
                    item.getPrimaryIdentifier(),
                    String.join(",", item.getIdentifiersList())
            );
        }

        return null;
    }


    public Tuple2<Integer, Integer> findModelByIdentifiers(String brandIdentifier, String modelIdentifier) {
        CarModelsService.FindModelByIdentifierRequest request = CarModelsService.FindModelByIdentifierRequest.newBuilder()
                .setBrandIdentifier(brandIdentifier)
                .setModelIdentifier(modelIdentifier)
                .build();
        CarModelsService.FindModelByIdentifierResponse response = stub.findModelByIdentifiers(request);

        if (response == null) {
            return null;
        }
        if (!response.hasCandidate()) {
            return null;
        }
        if (!response.getCandidate().hasBrand()) {
            return null;
        }

        Integer brandId = response.getCandidate().getBrand().getId();
        Integer modelId = response.getCandidate().getId();

        return new Tuple2<>(brandId, modelId);
    }

    public List<Tuple2<Integer, Integer>> findMultipleModelsByIdentifiers(List<Tuple2<String, String>> identifiers) {
        CarModelsService.FindMultipleModelsByIdentifiersRequest request = CarModelsService.FindMultipleModelsByIdentifiersRequest.newBuilder()
                .addAllRequests(identifiers.stream().map(i -> CarModelsService.FindModelByIdentifierRequest.newBuilder()
                                .setBrandIdentifier(i.a)
                                .setModelIdentifier(i.b)
                                .build()
                ).toList())
                .build();
        CarModelsService.FindMultipleModelsByIdentifiersResponse response = stub.findMultipleModelsByIdentifiers(request);

        ArrayList<Tuple2<Integer, Integer>> results = new ArrayList<>();
        if (response == null) {
            return results;
        }

        for (CarModelsService.FindModelByIdentifierResponse result : response.getResultsList()) {
            Integer brandId = null, modelId = null;
            if (result.hasCandidate()) {
                if (result.getCandidate().hasBrand()) {
                    brandId = result.getCandidate().getBrand().getId();
                }

                modelId = result.getCandidate().getId();
            }

            results.add(new Tuple2<>(brandId, modelId));
        }

        return results;
    }
}
