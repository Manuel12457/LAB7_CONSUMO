package edu.pucp.gtics.lab5_gtics_20221.controller;

import edu.pucp.gtics.lab5_gtics_20221.entity.*;
import edu.pucp.gtics.lab5_gtics_20221.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    JuegosRepository juegosRepository;

    @Autowired
    JuegosxUsuarioRepository juegosxUsuarioRepository;

    @GetMapping(value = {"", "/","/lista"})
    public String listaCarrito (Model model, HttpSession session){
        List<Juegos> juegoscarrito= (List<Juegos>) session.getAttribute("carrito");
        model.addAttribute("listjuegoscarrito",juegoscarrito);

        return "carrito/lista";
    }

    //añadir juego al carrito de compras
    @GetMapping(value = "/compras")
    public String nuevoCarrito(@RequestParam("id") String id, HttpSession httpSession){
        if(httpSession.getAttribute("carrito")==null){
            //Se crea
            List<Juegos> lista = new ArrayList<>();
            lista.add(juegosRepository.findById(Integer.parseInt(id)).get());
            httpSession.setAttribute("carrito",lista);
            httpSession.setAttribute("ncarrito",1);
        }else{
            //se añade
            List<Juegos> lista =(List<Juegos>) httpSession.getAttribute("carrito");
            lista.add(juegosRepository.findById(Integer.parseInt(id)).get());
            httpSession.setAttribute("carrito",lista);
            httpSession.setAttribute("ncarrito",lista.size());
        }
        return "redirect:/vista";
    }

    /*
    public String editarCarrito( ... ){

        return "redirect:/juegos/lista";
    }

    public String borrarCarrito(...){

        return "redirect:/carrito/lista";
    }
    */


    //Comprar juegos del carrito de compras
    @GetMapping("/comprar")
    public String comprarCarrito(HttpSession session) {
        List<Juegos> carrito = (List<Juegos>) session.getAttribute("carrito");
        User user = (User) session.getAttribute("usuario");
        //asignacion
        for (Juegos juego : carrito) {
            //si existe el juego comprado, se aumenta la cantidad
            if (juegosxUsuarioRepository.obtenerJuegoPorUser(juego.getId(), user.getIdusuario()) != null){
                JuegosxUsuario juegosxUsuario=juegosxUsuarioRepository.obtenerJuegoPorUser(juego.getId(), user.getIdusuario());
                juegosxUsuario.setCantidad(juegosxUsuario.getCantidad()+1);
                juegosxUsuarioRepository.save(juegosxUsuario);
            }else{
                JuegosxUsuario juegosxUsuario = new JuegosxUsuario();
                juegosxUsuario.setIdjuego(juego);
                juegosxUsuario.setIdusuario(user);
                juegosxUsuario.setCantidad(1);
                juegosxUsuarioRepository.save(juegosxUsuario);
            }

        }
        carrito.clear();
        session.setAttribute("carrito",carrito);
        session.setAttribute("ncarrito", 0);
        return "redirect:/carrito/lista";

    }


    @GetMapping(value = "/borrar")
    public String borrarCarrito(@RequestParam("id") String id, HttpSession httpSession){

        List<Juegos> listacarr =(List<Juegos>) httpSession.getAttribute("carrito");

        //busqueda
        int i =0;
        for (Juegos juego : listacarr){
            if (juego.getId()==listacarr.get(i).getId()){
                listacarr.remove(i);
                break;
            }
            i++;
        }
        httpSession.setAttribute("carrito",listacarr);
        httpSession.setAttribute("ncarrito",listacarr.size());
        return "redirect:/carrito/lista";
    }

}
