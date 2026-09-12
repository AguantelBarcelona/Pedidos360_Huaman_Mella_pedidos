package cl.pedidos360.pedidos_service.service;

import cl.pedidos360.pedidos_service.model.Pedido;
import cl.pedidos360.pedidos_service.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNoEncontradoException(id));
    }

    public List<Pedido> buscarPorCliente(String email) {
        return pedidoRepository.findByClienteEmail(email);
    }

    public Pedido crear(Pedido pedido) {

        pedido.setId(null);
        pedido.setEstado("CREADO");

        return pedidoRepository.save(pedido);
    }

    public Pedido actualizar(Long id, Pedido datos) {

        Pedido existente = buscarPorId(id);

        existente.setClienteEmail(datos.getClienteEmail());
        existente.setProductoId(datos.getProductoId());
        existente.setCantidad(datos.getCantidad());
        existente.setPrecioUnitario(datos.getPrecioUnitario());

        return pedidoRepository.save(existente);
    }

    public void eliminar(Long id) {

        Pedido pedido = buscarPorId(id);

        pedidoRepository.delete(pedido);
    }
}