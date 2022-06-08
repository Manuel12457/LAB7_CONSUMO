package edu.pucp.gtics.lab5_gtics_20221.daos;

import edu.pucp.gtics.lab5_gtics_20221.dtos.DistrDto2;
import edu.pucp.gtics.lab5_gtics_20221.dtos.DistribuidorasDto;
import edu.pucp.gtics.lab5_gtics_20221.entity.Distribuidoras;
import edu.pucp.gtics.lab5_gtics_20221.entity.Paises;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Component
@Controller
@RequestMapping("/distribuidoras")
public class DistribuidorasDao {

    @Autowired
    DistribuidorasDao distribuidorasDao;


    @GetMapping(value = {"/lista"})
    public String  listarDistribuidoras(Model model) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication("elarios@pucp.pe","123456").build();

        ResponseEntity<DistribuidorasDto> response = restTemplate.getForEntity(
                "http://localhost:8080/distribuidora/listar", DistribuidorasDto.class);

        Distribuidoras[] array=response.getBody().getDistribuidoras();
        System.out.println(array.length);
        model.addAttribute("listadistribuidoras", Arrays.asList(response.getBody().getDistribuidoras()));
        return "distribuidoras/lista";
    }


    @GetMapping("/nuevo")
    public String nuevaDistribuidora(Model model, @ModelAttribute("distribuidora") Distribuidoras distribuidora){

        model.addAttribute("listaPaises",this.listarPaises());
        return "distribuidoras/editarFrm";
    }

    @GetMapping("/editar")
    public String editarDistribuidoras(@RequestParam("id") int id, Model model) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication("elarios@pucp.pe","123456").build();

        String url = "http://localhost:8080/distribuidora/obtener/" + id;

        ResponseEntity<DistrDto2> responseMap = restTemplate.getForEntity(url, DistrDto2.class);

        DistrDto2 distrDto2 = responseMap.getBody();

        if (distrDto2.getEstado().equals("ok")){
            model.addAttribute("distribuidora", distrDto2.getDistribuidora());
            model.addAttribute("listaPaises", this.listarPaises());
            return "distribuidoras/editarFrm";
        }else {
            return "redirect:/distribuidoras/lista";
        }
    }



    @PostMapping("/guardar")
    public String guardarDistribuidora(Model model, RedirectAttributes attr, @ModelAttribute("distribuidora") @Valid Distribuidoras distribuidora , BindingResult bindingResult) {


        if(bindingResult.hasErrors()){
            model.addAttribute("distribuidora", distribuidora);
            model.addAttribute("listaPaises", this.listarPaises());
            return "distribuidoras/editarFrm";
        } else {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);


            HttpEntity<Distribuidoras> httpEntity = new HttpEntity<>(distribuidora, headers);

            RestTemplate restTemplate = new RestTemplateBuilder()
                    .basicAuthentication("elarios@pucp.pe","123456").build();
            if (distribuidora.getId() !=null) {
                String url = "http://localhost:8080/distribuidora/editar";
                restTemplate.put(url, httpEntity, Distribuidoras.class);
                attr.addFlashAttribute("msg", "Distribuidora actualizada exitosamente");
            } else {

                String url = "http://localhost:8080/distribuidora/nueva";
                restTemplate.postForEntity(url, httpEntity, Distribuidoras.class);
                attr.addFlashAttribute("msg", "Distribuidora creada exitosamente");
            }
            return "redirect:/distribuidoras/lista";
        }

    }



    @GetMapping("/borrar")
    public String borrarDistribuidora(Model model, @RequestParam("id") int id, RedirectAttributes attr) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication("elarios@pucp.pe","123456").build();

        String url = "http://localhost:8080/distribuidora/obtener/" + id;

        ResponseEntity<DistrDto2> responseMap = restTemplate.getForEntity(url, DistrDto2.class);

        DistrDto2 distrDto2 = responseMap.getBody();

        if (distrDto2.getDistribuidora().getId()!=null) {
            restTemplate.delete("http://localhost:8080/distribuidora/borrar/" + id);
            attr.addFlashAttribute("msg", "Producto borrado exitosamente");
        }


        return "redirect:/distribuidoras/lista";


    }

    public List<Paises> listarPaises() {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication("elarios@pucp.pe","123456").build();
        ResponseEntity<Paises[]> response = restTemplate.getForEntity(
                "http://localhost:8080/paises", Paises[].class);

        return Arrays.asList(response.getBody());
    }



}
