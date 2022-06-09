package edu.pucp.gtics.lab5_gtics_20221.controller;

import edu.pucp.gtics.lab5_gtics_20221.dtos.DistribuidorasDto;
import edu.pucp.gtics.lab5_gtics_20221.entity.*;
import edu.pucp.gtics.lab5_gtics_20221.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller

public class JuegosController {

    @Autowired
    JuegosRepository juegosRepository;

    @Autowired
    PlataformasRepository plataformasRepository;


    @Autowired
    GenerosRepository generosRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping(value = {"/juegos/lista"})
    public String listaJuegos (Model model, HttpSession session){


        User user= (User) session.getAttribute("usuario");

        if(user.getAutorizacion().equals("USER")){
            //Si es usuario
            model.addAttribute("listaPorUsuario",juegosRepository.obtenerJuegosPorUser(user.getIdusuario()));
            return "juegos/comprado";
        }else{
            //Si es admin
            model.addAttribute("lista",juegosRepository.findAll(Sort.by("precio")));
            return "juegos/lista";
        }

    }

    @GetMapping(value = {"", "/", "/vista"})
    public String vistaJuegos (Model model, HttpSession session){
        if(session.getAttribute("usuario")==null){
            //no esta loguqeado
            model.addAttribute("listaJuegos",juegosRepository.obtenerJuegosDesc());
        }else{
            //Si está logueado
            User user_logueado = (User) session.getAttribute("usuario");
            int id = user_logueado.getIdusuario();
            model.addAttribute("listaJuegos",juegosRepository.obtenerJuegosNotInUsuario(id));
        }
        return "juegos/vista";
    }

    @GetMapping("/juegos/nuevo")
    public String nuevoJuegos(Model model, @ModelAttribute("juego") Juegos juego){
        List<Plataformas> listaPlataformas = plataformasRepository.findAll();
        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication("elarios@pucp.pe","123456").build();

        ResponseEntity<DistribuidorasDto> response = restTemplate.getForEntity(
                "http://localhost:8080/distribuidora/listar", DistribuidorasDto.class);

        Distribuidoras[] array=response.getBody().getDistribuidoras();
        List<Generos> listaGeneros = generosRepository.findAll();
        model.addAttribute("listaPlataformas", listaPlataformas);
        model.addAttribute("listaDistribuidoras", Arrays.asList(response.getBody().getDistribuidoras()));
        model.addAttribute("listaGeneros", listaGeneros);
        return "juegos/editarFrm";
    }

    @GetMapping("/juegos/editar")
    public String editarJuegos(@RequestParam("id") int id, Model model){
        Optional<Juegos> opt = juegosRepository.findById(id);
        List<Plataformas> listaPlataformas = plataformasRepository.findAll();
        RestTemplate restTemplate = new RestTemplateBuilder()
                .basicAuthentication("elarios@pucp.pe","123456").build();

        ResponseEntity<DistribuidorasDto> response = restTemplate.getForEntity(
                "http://localhost:8080/distribuidora/listar", DistribuidorasDto.class);

        Distribuidoras[] array=response.getBody().getDistribuidoras();
        List<Generos> listaGeneros = generosRepository.findAll();
        if (opt.isPresent()){
            Juegos juego = opt.get();
            model.addAttribute("juego", juego);
            model.addAttribute("listaPlataformas", listaPlataformas);
            model.addAttribute("listaDistribuidoras", Arrays.asList(response.getBody().getDistribuidoras()));
            model.addAttribute("listaGeneros", listaGeneros);
            return "juegos/editarFrm";
        }else {
            return "redirect:/juegos/lista";
        }
    }

    @PostMapping("/juegos/guardar")
    public String guardarJuegos(Model model, RedirectAttributes attr, @ModelAttribute("juego") @Valid Juegos juego, BindingResult bindingResult ){
        if(bindingResult.hasErrors( )){
            List<Plataformas> listaPlataformas = plataformasRepository.findAll();
            RestTemplate restTemplate = new RestTemplateBuilder()
                    .basicAuthentication("elarios@pucp.pe","123456").build();

            ResponseEntity<DistribuidorasDto> response = restTemplate.getForEntity(
                    "http://localhost:8080/distribuidora/listar", DistribuidorasDto.class);

            Distribuidoras[] array=response.getBody().getDistribuidoras();
            List<Generos> listaGeneros = generosRepository.findAll();
            model.addAttribute("juego", juego);
            model.addAttribute("listaPlataformas", listaPlataformas);
            model.addAttribute("listaDistribuidoras", Arrays.asList(response.getBody().getDistribuidoras()));
            model.addAttribute("listaGeneros", listaGeneros);
            return "juegos/editarFrm";
        } else {
            if (juego.getIdjuego() == 0) {
                attr.addFlashAttribute("msg", "Juego creado exitosamente");
            } else {
                attr.addFlashAttribute("msg", "Juego actualizado exitosamente");
            }
            juegosRepository.save(juego);
            return "redirect:/juegos/lista";
        }


    }

    @GetMapping("/juegos/borrar")
    public String borrarDistribuidora(@RequestParam("id") int id){
        Optional<Juegos> opt = juegosRepository.findById(id);
        if (opt.isPresent()) {
            juegosRepository.deleteById(id);
        }
        return "redirect:/juegos/lista";
    }





}
