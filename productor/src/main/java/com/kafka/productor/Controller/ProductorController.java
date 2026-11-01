package com.kafka.productor.Controller;

import java.util.Random;
import java.util.UUID;

import org.apache.kafka.common.Uuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kafka.productor.Model.TelemetriaCamion;

import tools.jackson.databind.ObjectMapper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController 
@RequestMapping ("/api/kafka")
public class ProductorController {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired 
    private ObjectMapper objectMapper;
    @Autowired 
    public ProductorController(KafkaTemplate<String, String> kafkaTemplate){
        this.kafkaTemplate= kafkaTemplate;
    }

    @GetMapping("/send")
    public String getMethodName(
        @RequestParam String message,
        @RequestParam (required = false ) String key
    ) {
        String topicName= "tutorial-particiones";
        if(key!= null && !key.isEmpty()){
            kafkaTemplate.send(topicName, key, message);
            return "Mensaje enviado al topic: "+ topicName
            + "con key: "+ key
            + "mensaje: " + message;
        }else{
            kafkaTemplate.send(topicName, message);
              return "Mensaje enviado al topic: "+ topicName
            + "con key: sin key"
            + "mensaje: " + message;
        }
    }
    @PostMapping("/simular-camiones")
    public String postMethodName() {
        String topicName= "telemetria-camion";
        String[] camiones= {"CAMION-01", "CAMION-02", "CAMION-03"};
        String[] estados= {"EN_RUTA", "DETENIDO", "REFRIGERANDO","ALERTA_TEMPERATURA"};

        Random random = new Random();
        try {
            for(int i= 1;i<50; i++){
                String id = UUID.randomUUID().toString().substring(0,8);
                String camionId = camiones[random.nextInt(camiones.length)];
                Double temperatura = -25.0+(random.nextDouble()*15.0);
                Double humedad= 50.0+(random.nextDouble()*30.0); 
                String estado = estados[random.nextInt(estados.length)];
                Long timestamp= System.currentTimeMillis()+(i*1000L); 
                TelemetriaCamion telemetriaCamion= new TelemetriaCamion();
                telemetriaCamion.setCamionId(camionId);
                telemetriaCamion.setEstado(estado);
                telemetriaCamion.setHumedad(humedad);
                telemetriaCamion.setTimestamp(timestamp);
                telemetriaCamion.setTemperatura(temperatura);
                telemetriaCamion.setId(id);

                String jsonMesage = objectMapper.writeValueAsString(telemetriaCamion); 
                kafkaTemplate.send(topicName, camionId, jsonMesage); 
            }
            return "Informacion enviada correctamente";
        } catch (Exception e) {
            return "error al enviar informacion: "+e.getMessage();
        }
    }

    
}
