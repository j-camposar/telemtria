package com.kafka.consumidor.Repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.kafka.consumidor.Model.TelemetriaCamion;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

@Repository 
public class TelemetriaRepository {
    private final DynamoDbTable<TelemetriaCamion> table;

    public TelemetriaRepository(DynamoDbEnhancedClient enhancedClient){
        this.table = enhancedClient.table("TelemetriaCamion", 
        TableSchema.fromBean(TelemetriaCamion.class));
    }
    public void guardar(TelemetriaCamion telemetriaCamion){
        table.putItem(telemetriaCamion);
    }

    public List<TelemetriaCamion> historial(String camionId){
        
        QueryConditional queryConditional = QueryConditional.keyEqualTo(
            Key.builder().partitionValue(camionId).build()
        );
        return table.query(queryConditional)
        .items()
        .stream()
        .collect(Collectors.toList());
    }
}
