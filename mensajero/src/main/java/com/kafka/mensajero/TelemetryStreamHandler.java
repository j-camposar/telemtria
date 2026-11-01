package main.java.com.kafka.mensajero;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamoDBEvent;
import com.amazonaws.services.lambda.runtime.events.models.dynamodb.AttributeValue;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TelemetryStreamHandler implements RequestHandler<DynamoDBEvent, Void> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Void handleRequest(DynamoDBEvent event, Context context) {
        context.getLogger().log("Procesando " + event.getRecords().size() + " eventos desde DynamoDB Stream...");

        // 1. Creamos una lista para acumular el lote (micro-batching)
        List<CamionTelemetryDTO> batchTelemetry = new ArrayList<>();

        for (DynamoDBEvent.DynamoDBStreamRecord record : event.getRecords()) {
            // Nos interesa solo cuando se inserta (INSERT) o modifica (MODIFY) un registro
            if ("INSERT".equals(record.getEventName()) || "MODIFY".equals(record.getEventName())) {
                
                Map<String, AttributeValue> newImage = record.getDynamodb().getNewImage();
                
                if (newImage != null && !newImage.isEmpty()) {
                    // Mapeamos el DynamoDB JSON plano a tu clase de modelo
                    CamionTelemetryDTO telemetry = mapToDTO(newImage);
                    
                    // Añadimos el objeto a la lista del lote
                    batchTelemetry.add(telemetry);
                }
            }
        }

        // 2. Si hay datos en el lote, los enviamos de un solo golpe al frontend
        if (!batchTelemetry.isEmpty()) {
            broadcastBatchToFrontend(batchTelemetry, context);
        }

        return null;
    }

    private CamionTelemetryDTO mapToDTO(Map<String, AttributeValue> image) {
        CamionTelemetryDTO dto = new CamionTelemetryDTO();
        
        // Mapeo seguro extrayendo el tipo de dato correspondiente
        if (image.get("id") != null) dto.setId(image.get("id").getS());
        if (image.get("id_camion") != null) dto.setCamionId(image.get("id_camion").getS());
        if (image.get("timestamp") != null) dto.setTimestamp(Long.valueOf(image.get("timestamp").getN()));
        if (image.get("temperatura") != null) dto.setTemperatura(Double.valueOf(image.get("temperatura").getN()));
        if (image.get("humedad") != null) dto.setHumedad(Double.valueOf(image.get("humedad").getN()));
        if (image.get("estado") != null) dto.setEstado(image.get("estado").getS());
        
        return dto;
    }

    private void broadcastBatchToFrontend(List<CamionTelemetryDTO> telemetryBatch, Context context) {
        try {
            // Serializamos toda la lista en un único JSON compacto de golpe
            String jsonPayload = objectMapper.writeValueAsString(telemetryBatch);
            context.getLogger().log("Enviando ráfaga de " + telemetryBatch.size() + " elementos al dashboard: " + jsonPayload);
            
    
        } catch (Exception e) {
            context.getLogger().log("Error serializando lote de telemetría: " + e.getMessage());
        }
    }
}