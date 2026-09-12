package cl.pedidos360.pedidos_service.controller;

import cl.pedidos360.pedidos_service.model.Pedido;
import cl.pedidos360.pedidos_service.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<Pedido> listar() {
        return pedidoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Pedido obtener(@PathVariable Long id) {
        return pedidoService.buscarPorId(id);
    }

    @GetMapping("/cliente/{email}")
    public List<Pedido> buscarPorCliente(
            @PathVariable String email) {

        return pedidoService.buscarPorCliente(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido crear(
            @Valid @RequestBody Pedido pedido) {

        return pedidoService.crear(pedido);
    }

    @PutMapping("/{id}")
    public Pedido actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Pedido pedido) {

        return pedidoService.actualizar(id, pedido);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {

        pedidoService.eliminar(id);
    }
}