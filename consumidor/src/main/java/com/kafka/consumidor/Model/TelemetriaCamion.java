package com.kafka.consumidor.Model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean 
public class TelemetriaCamion {
    private String id;
    private String camionId;
    private Long timestamp;
    private Double temperatura;
    private Double humedad;
    private String estado;

    public void setId(String id) {
        this.id= id;
    }
    public String getId(){
        return this.id;
    }
    @DynamoDbPartitionKey 
    @DynamoDbAttribute("id_camion")
    public void setCamionId(String camionId){
        this.camionId=camionId;
    }
    public String getCamionId(){
        return this.camionId;
    }
    public void setTimestamp(Long timestamp){
        this.timestamp= timestamp;
    }
    public Long getTimestamp(){
        return this.timestamp;
    }
    public void setTemperatura(Double temperatura){
        this.temperatura= temperatura;
    }
    public Double getTemperatura(){
        return this.temperatura;
    }
    public void setHumedad(Double humedad){
        this.humedad= humedad;
    }
    public Double getHumedad(){
        return this.humedad;
    }
    public void setEstado(String estado){
        this.estado= estado;
    }
    public String getEstado(){
        return this.estado;
    }
}
