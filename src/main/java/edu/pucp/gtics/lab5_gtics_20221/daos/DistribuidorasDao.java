package edu.pucp.gtics.lab5_gtics_20221.daos;

import edu.pucp.gtics.lab5_gtics_20221.entity.Distribuidoras;
import edu.pucp.gtics.lab5_gtics_20221.entity.Paises;
import edu.pucp.gtics.lab5_gtics_20221.repository.PaisesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Sort;
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
import java.util.Optional;

@Component
@Controller
@RequestMapping("/distribuidoras")
public class DistribuidorasDao {

    @Autowired
    DistribuidorasDao distribuidorasDao;

    @Autowired
    PaisesRepository paisesRepository;

    @GetMapping(value = {"/lista"})
    public String  listarDistribuidoras(Model model) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication("elarios@pucp.pe","123456").build();

        ResponseEntity<Distribuidoras[]> response = restTemplate.getForEntity(
                "http://localhost:8080/distribuidoras", Distribuidoras[].class);

        model.addAttribute("listadistribuidoras", Arrays.asList(response.getBody()));
        return "distribuidoras/lista";
    }


    @GetMapping("/nuevo")
    public String nuevaDistribuidora(Model model, @ModelAttribute("distribuidora") Distribuidoras distribuidora){
        List<Paises> listaPaises = paisesRepository.findAll();
        model.addAttribute("listaPaises", listaPaises);
        return "distribuidoras/editarFrm";
    }

    @GetMapping("/editar")
    public String editarDistribuidoras(@RequestParam("id") int id, Model model) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication("elarios@pucp.pe","123456").build();

        String url = "http://localhost:8080/distribuidoras/" + id;

        ResponseEntity<Distribuidoras> responseMap = restTemplate.getForEntity(url, Distribuidoras.class);

        Distribuidoras distribuidora = responseMap.getBody();

        List<Paises> listaPaises = paisesRepository.findAll();
        if (distribuidora!=null){
            model.addAttribute("distribuidora", distribuidora);
            model.addAttribute("listaPaises", listaPaises);
            return "distribuidoras/editarFrm";
        }else {
            return "redirect:/distribuidoras/lista";
        }
    }



    @PostMapping("/guardar")
    public String guardarDistribuidora(Model model, RedirectAttributes attr, @ModelAttribute("distribuidora") @Valid Distribuidoras distribuidora , BindingResult bindingResult) {


        if(bindingResult.hasErrors()){
            List<Paises> listaPaises = paisesRepository.findAll();
            model.addAttribute("distribuidora", distribuidora);
            model.addAttribute("listaPaises", listaPaises);
            return "distribuidoras/editarFrm";
        } else {

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String url = "http://localhost:8080/distribuidoras";
            HttpEntity<Distribuidoras> httpEntity = new HttpEntity<>(distribuidora, headers);

            RestTemplate restTemplate = new RestTemplateBuilder()
                    .basicAuthentication("admin","admin").build();
            if (distribuidora.getIddistribuidora() > 0) {
                restTemplate.put(url, httpEntity, Distribuidoras.class);
                attr.addFlashAttribute("msg", "Distribuidora actualizada exitosamente");
            } else {
                attr.addFlashAttribute("msg", "Distribuidora creada exitosamente");
                restTemplate.postForEntity(url, httpEntity, Distribuidoras.class);

            }
            return "redirect:/distribuidoras/lista";
        }

    }



    @GetMapping("/borrar")
    public String borrarDistribuidora(Model model, @RequestParam("id") int id, RedirectAttributes attr) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication("elarios@pucp.pe","123456").build();

        String url = "http://localhost:8080/distribuidoras/" + id;

        ResponseEntity<Distribuidoras> responseMap = restTemplate.getForEntity(url, Distribuidoras.class);

        Distribuidoras distribuidora = responseMap.getBody();

        if (distribuidora!=null) {
            restTemplate.delete("http://localhost:8080/distribuidoras/" + id);
            attr.addFlashAttribute("msg", "Producto borrado exitosamente");
        }

        return "redirect:/distribuidoras/lista";


    }

}
