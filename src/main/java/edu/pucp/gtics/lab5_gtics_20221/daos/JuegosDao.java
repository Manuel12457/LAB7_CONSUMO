package edu.pucp.gtics.lab5_gtics_20221.daos;

import edu.pucp.gtics.lab5_gtics_20221.dtos.JuegosDto;
import edu.pucp.gtics.lab5_gtics_20221.entity.Juegos;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class JuegosDao {

    public void guardarJuego(Juegos juego) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String url = "http://localhost:8080/juego"; //Cambiar por la direccion del WebService
        HttpEntity<Juegos> httpEntity = new HttpEntity<>(juego, headers);

        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication("elarios@pucp.pe","123456").build(); //CAMBIAR
        if (juego.getIdjuego() > 0) {
            restTemplate.put(url, httpEntity, Juegos.class);
        } else {
            restTemplate.postForEntity(url, httpEntity, Juegos.class);
        }

    }

    public void borrarJuego(int id) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication("elarios@pucp.pe","123456").build(); //CAMBIAR
        restTemplate.delete("http://localhost:8080/juego/eliminar/" + id); //Cambiar por la direccion del WebService

    }

    public Juegos obtenerJuegoPorId(int id) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication("elarios@pucp.pe","123456").build();

        String url = "http://localhost:8080/juego/obtener/" + id; //Cambiar por la direccion del WebService

        ResponseEntity<JuegosDto> responseMap = restTemplate.getForEntity(url, JuegosDto.class);

        JuegosDto juegosDto = responseMap.getBody();

        return juegosDto.getJuegos();
    }

    public List<Juegos> listarJuegos() {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication("elarios@pucp.pe","123456").build();
        ResponseEntity<Juegos[]> response = restTemplate.getForEntity(
                "http://localhost:8080/juegos", Juegos[].class); //Cambiar por la direccion del WebService

        return Arrays.asList(response.getBody());
    }

}
