package cl.pedidos360.pedidos_service.service;

import cl.pedidos360.pedidos_service.client.InventarioClient;
import cl.pedidos360.pedidos_service.client.ProductoClient;
import cl.pedidos360.pedidos_service.client.ProductoResponse;
import cl.pedidos360.pedidos_service.model.Pedido;
import cl.pedidos360.pedidos_service.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProductoClient productoClient;
    private final InventarioClient inventarioClient;

    public PedidoService(
            PedidoRepository pedidoRepository,
            ProductoClient productoClient,
            InventarioClient inventarioClient) {

        this.pedidoRepository = pedidoRepository;
        this.productoClient = productoClient;
        this.inventarioClient = inventarioClient;
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

        // 1. Consultar el producto y obtener el precio oficial.
        ProductoResponse producto =
                productoClient.obtenerProducto(pedido.getProductoId());

        pedido.setPrecioUnitario(producto.precio());

        // 2. Reservar el stock solicitado.
        inventarioClient.reservarStock(
                pedido.getProductoId(),
                pedido.getCantidad()
        );

        // 3. Guardar el pedido solamente después de reservar stock.
        return pedidoRepository.save(pedido);
    }

    public Pedido actualizar(Long id, Pedido datos) {

        Pedido existente = buscarPorId(id);

        // Consultar el precio oficial del nuevo producto.
        ProductoResponse producto =
                productoClient.obtenerProducto(datos.getProductoId());

        existente.setClienteEmail(datos.getClienteEmail());
        existente.setProductoId(datos.getProductoId());
        existente.setCantidad(datos.getCantidad());
        existente.setPrecioUnitario(producto.precio());

        return pedidoRepository.save(existente);
    }

    public void eliminar(Long id) {

        Pedido pedido = buscarPorId(id);

        pedidoRepository.delete(pedido);
    }
}