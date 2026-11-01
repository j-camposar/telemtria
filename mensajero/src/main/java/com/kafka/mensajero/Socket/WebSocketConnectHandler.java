package main.java.com.kafka.mensajero.Socket;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2WebSocketEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2WebSocketResponse;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.Map;

public class WebSocketConnectHandler implements RequestHandler<APIGatewayV2We2bSocketEvent, APIGatewayV2WebSocketResponse> {

    private final DynamoDbClient dynamoDb = DynamoDbClient.create();
    private final String tableName = "ConexionesWebSocket"; // Tu tabla DynamoDB para guardar clientes

    @Override
    public APIGatewayV2WebSocketResponse handleRequest(APIGatewayV2WebSocketEvent event, Context context) {
        String connectionId = event.getRequestContext().getConnectionId();
        context.getLogger().log("Cliente conectado: " + connectionId);

        try {
            // Guardamos el connectionId en DynamoDB
            dynamoDb.putItem(PutItemRequest.builder()
                    .tableName(tableName)
                    .item(Map.of("connectionId", AttributeValue.builder().s(connectionId).build()))
                    .build());
        } catch (Exception e) {
            context.getLogger().log("Error guardando conexión: " + e.getMessage());
            return new APIGatewayV2WebSocketResponse(500, "Error interno");
        }

        APIGatewayV2WebSocketResponse response = new APIGatewayV2WebSocketResponse();
        response.setStatusCode(200);
        return response;
    }
}