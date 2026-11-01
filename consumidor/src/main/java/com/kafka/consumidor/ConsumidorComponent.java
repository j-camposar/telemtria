package com.kafka.consumidor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.kafka.consumidor.Model.TelemetriaCamion;
import com.kafka.consumidor.Repository.TelemetriaRepository;

import tools.jackson.databind.ObjectMapper;

@Component 
public class ConsumidorComponent {
    @Autowired 
    private ObjectMapper objectMapper;
    @Autowired 
    private TelemetriaRepository telemetriaRepository;
    
    
    @KafkaListener (topics="tutorial-particiones", groupId= "prueba-group-1")
    public void listen(
        @Payload String message,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset, 
        @Header(name= KafkaHeaders.RECEIVED_KEY, required = false) String key 
    ){
        System.out.println("\n"+"=".repeat(100));
        System.out.printf("Mensaje Recibido %n");
        System.out.printf("Particion %d%n", partition);
        System.out.printf("offset: %d%n", offset);
        System.out.printf("key: %s%n", key!=null? key:"sin key");
        System.out.printf("mensaje: %s%n", message);
        System.out.println("=".repeat(100)+"\n");
    }
    @KafkaListener (topics= "telemetria-camion", groupId= "prueba-group-1")
    public void telemetria(
        @Payload String message,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
        @Header(KafkaHeaders.OFFSET) long offset, 
        @Header(name= KafkaHeaders.RECEIVED_KEY, required = false) String key 
    ){
        try {
            TelemetriaCamion telemetriaCamion = objectMapper.readValue(message, TelemetriaCamion.class);
            System.out.println("Camion recibido :" + telemetriaCamion.getCamionId());
            System.out.println("Temperatura camion: "+ telemetriaCamion.getTemperatura());
            telemetriaRepository.guardar(telemetriaCamion);
        } catch (Exception e) {
            System.err.println("error al guardar informacion :" +e.getMessage());
        }
    }

}
