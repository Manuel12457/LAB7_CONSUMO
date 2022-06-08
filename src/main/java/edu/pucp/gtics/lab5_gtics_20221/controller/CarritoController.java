package edu.pucp.gtics.lab5_gtics_20221.controller;

import edu.pucp.gtics.lab5_gtics_20221.daos.JuegosDao;
import edu.pucp.gtics.lab5_gtics_20221.entity.*;
import edu.pucp.gtics.lab5_gtics_20221.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class CarritoController {

    @Autowired
    JuegosDao juegosDao;

    @Autowired
    JuegosRepository juegosRepository;
    @Autowired
    JuegosxUsuarioRepository juegosxUsuarioRepository;

    @GetMapping("/carrito/lista")
    public String listaCarrito(Model model, HttpSession session) {
        return "carrito/lista";
    }

    @GetMapping("/carrito/comprar")
    public String comprarCarrito(HttpSession session, RedirectAttributes redirectAttributes) {
        List<Juegos> carrito = (List<Juegos>) session.getAttribute("carrito");
        User user = (User) session.getAttribute("usuario");
        List<JuegosxUsuario> listaComprar = new ArrayList<JuegosxUsuario>();
        if(carrito.size()==0){
            redirectAttributes.addFlashAttribute("msg", "No tiene ningún juego en el carrito");
            return "redirect:/carrito/lista";
        }

        for (Juegos i : carrito) {
            JuegosxUsuario juegosxUsuario = new JuegosxUsuario();
            juegosxUsuario.setIdjuego(i);
            juegosxUsuario.setIdusuario(user);
            listaComprar.add(juegosxUsuario);
        }
        juegosxUsuarioRepository.saveAll(listaComprar);
        session.setAttribute("carrito", new ArrayList<Juegos>());
        session.setAttribute("ncarrito", 0);
        redirectAttributes.addFlashAttribute("msg","juegos comprados exitosamente");
        return "redirect:/carrito/lista";
    }

    @GetMapping("/carrito/borrar")
    public String borrarCarrito(HttpSession session, @RequestParam("id") String idString, RedirectAttributes redirectAttributes) {
        int id;
        try {
            id = Integer.parseInt(idString);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/vista";
        }
        Juegos juegoABorrar = juegosDao.obtenerJuegoPorId(id);
        if (juegoABorrar == null) {
            return "redirect:/vista";
        }
        List<Juegos> carrito = (List<Juegos>) session.getAttribute("carrito");
        int ncarrito = (int) session.getAttribute("ncarrito");
        int index = 0;
        String juegoeliminado = "";
        if (carrito.size() == 1 && (carrito.get(0).getIdjuego()==id)) {
            carrito = new ArrayList<Juegos>();
            ncarrito=ncarrito-1;
        } else {
            for (Juegos juego : carrito) {
                if (juego.getIdjuego() == juegoABorrar.getIdjuego()) {
                    ncarrito=ncarrito-1;
                    juegoeliminado= carrito.get(index).getNombre();
                    carrito.remove(index);

                    break;
                }
                index++;
            }
        }
        session.setAttribute("ncarrito", ncarrito);
        session.setAttribute("carrito", carrito);
        redirectAttributes.addFlashAttribute("msg","Juego borrado exitosamente "+ juegoeliminado );
        return "redirect:/carrito/lista";
    }

    @GetMapping("/carrito/anadir")
    public String anadirCarrito(HttpSession session, @RequestParam("id") String idString, RedirectAttributes redirectAttributes) {
        int id;
        try {
            id = Integer.parseInt(idString);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/vista";
        }

        Juegos juegoAAnadir = juegosDao.obtenerJuegoPorId(id);
        if (juegoAAnadir == null) {
            return "redirect:/vista";
        }
        List<Juegos> carrito= (List<Juegos>) session.getAttribute("carrito");

        int index=0;
        for(Juegos i : carrito){
            if(i.getIdjuego()==juegoAAnadir.getIdjuego()){
                redirectAttributes.addFlashAttribute("msg","Ya tiene el juego en su carrito");
                return "redirect:/carrito/lista";
            }
        }

        int ncarrito = (int) session.getAttribute("ncarrito");
        carrito.add(juegoAAnadir);
        session.setAttribute("ncarrito", ncarrito+1);
        session.setAttribute("carrito", carrito);
        redirectAttributes.addFlashAttribute("msg", "se ha añadido el juego " +juegoAAnadir.getNombre() + " al carrito");
        return "redirect:/carrito/lista";
    }

//    public String nuevoCarrito(@RequestParam("id") int id, ...){
//
//        return "redirect:/vista";
//    }
//
//    public String editarCarrito( ... ){
//
//        return "redirect:/juegos/lista";
//    }
//
//    public String borrarCarrito(...){
//
//        return "redirect:/carrito/lista";
//    }


}
