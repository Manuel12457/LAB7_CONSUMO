package edu.pucp.gtics.lab5_gtics_20221.controller;

import edu.pucp.gtics.lab5_gtics_20221.daos.DistribuidorasDao;
import edu.pucp.gtics.lab5_gtics_20221.daos.JuegosDao;
import edu.pucp.gtics.lab5_gtics_20221.entity.*;
import edu.pucp.gtics.lab5_gtics_20221.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller

public class JuegosController {

    @Autowired
    JuegosDao juegosDao;

    @Autowired
    DistribuidorasDao distribuidorasDao;

    @Autowired
    JuegosRepository juegosRepository;

    @Autowired
    PlataformasRepository plataformasRepository;

//    @Autowired
//    DistribuidorasRepository distribuidorasRepository;

    @Autowired
    GenerosRepository generosRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping(value = {"/juegos/lista"})
    public String listaJuegos (Model model, HttpSession session){
        User user = (User) session.getAttribute("usuario");

        //Ambos deben retornar listas de juegos
        if (user.getAutorizacion().equals("ADMIN")) {
            model.addAttribute("listaJuegos", juegosDao.listarJuegos());
            return "juegos/lista";
        } else {
            model.addAttribute("listaJuegos", juegosDao.listarJuegos());
            return "juegos/comprado";
        }
    }

    @GetMapping(value = {"", "/", "/vista"})
    public String vistaJuegos (Model model, HttpSession session){
        User user = (User) session.getAttribute("usuario");
        //Ambos deben retornar listas de juegos
        if(user==null){
            model.addAttribute("listaJuegos", juegosDao.listarJuegos());
        } else {
            model.addAttribute("listaJuegos", juegosDao.listarJuegos());
        }
        return "juegos/vista";
    }


    @GetMapping("/juegos/nuevo")
    public String nuevoJuegos(Model model, @ModelAttribute("juego") Juegos juego){
        List<Plataformas> listaPlataformas = plataformasRepository.findAll();
//        List<Distribuidoras> listaDistribuidoras = distribuidorasDao.listarDistribuidoras(); //Colocar que lo saque del distribuidorasDao
        List<Generos> listaGeneros = generosRepository.findAll();
        model.addAttribute("listaPlataformas", listaPlataformas);
//        model.addAttribute("listaDistribuidoras", listaDistribuidoras);
        model.addAttribute("listaGeneros", listaGeneros);
        return "juegos/editarFrm";
    }

    @GetMapping("/juegos/editar")
    public String editarJuegos(@RequestParam("id") int id, Model model){
        Juegos juegoAEditar = juegosDao.obtenerJuegoPorId(id);
        List<Plataformas> listaPlataformas = plataformasRepository.findAll();
//        List<Distribuidoras> listaDistribuidoras = distribuidorasDao.listarDistribuidoras(); //Colocar que lo saque del distribuidorasDao
        List<Generos> listaGeneros = generosRepository.findAll();
        if (juegoAEditar != null){
            model.addAttribute("juego", juegoAEditar);
            model.addAttribute("listaPlataformas", listaPlataformas);
//            model.addAttribute("listaDistribuidoras", listaDistribuidoras);
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
//            List<Distribuidoras> listaDistribuidoras = distribuidorasDao.listarDistribuidoras(); //Colocar que lo saque del distribuidorasDao
            List<Generos> listaGeneros = generosRepository.findAll();
            model.addAttribute("juego", juego);
            model.addAttribute("listaPlataformas", listaPlataformas);
//            model.addAttribute("listaDistribuidoras", listaDistribuidoras);
            model.addAttribute("listaGeneros", listaGeneros);
            return "juegos/editarFrm";
        } else {
            if (juego.getIdjuego() == 0) {
                attr.addFlashAttribute("msg", "Juego creado exitosamente");
            } else {
                attr.addFlashAttribute("msg", "Juego actualizado exitosamente");
            }
            juegosDao.guardarJuego(juego);
            return "redirect:/juegos/lista";
        }


    }

    @GetMapping("/juegos/borrar")
    public String borrarDistribuidora(@RequestParam("id") int id){

        Juegos juegoABorrar = juegosDao.obtenerJuegoPorId(id);

        if (juegoABorrar != null) {
            juegosDao.borrarJuego(id);
        }
        return "redirect:/juegos/lista";
    }


}
