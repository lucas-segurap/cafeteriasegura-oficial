package com.br.cafeteriasegura.Service;

import com.br.cafeteriasegura.Model.*;
import com.br.cafeteriasegura.Repository.PedidoRepository;
import com.br.cafeteriasegura.Repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final ClienteService clienteService;

    public PedidoService(
            PedidoRepository pedidoRepository,
            ProdutoRepository produtoRepository,
            ClienteService clienteService) {

        this.pedidoRepository = pedidoRepository;
        this.produtoRepository = produtoRepository;
        this.clienteService = clienteService;
    }

    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    public List<Pedido> listarPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    public List<Pedido> listarEmAtendimento() {
        return pedidoRepository.findByStatus(
                StatusPedido.EM_ATENDIMENTO
        );
    }

    public List<Pedido> listarFinalizados() {
        return pedidoRepository.findByStatus(
                StatusPedido.FINALIZADO
        );
    }

    public Pedido criarPedido(
            Long clienteId,
            Pedido pedidoRecebido) {

        Cliente cliente = clienteService.buscarPorId(clienteId);

        pedidoRecebido.setCliente(cliente);

        validarTipoAtendimento(pedidoRecebido);

        if (pedidoRecebido.getItens() == null ||
                pedidoRecebido.getItens().isEmpty()) {

            throw new RuntimeException(
                    "O pedido precisa ter pelo menos um produto."
            );
        }

        List<ItemPedido> itensDoPedido = new ArrayList<>();

        double total = 0.0;

        for (ItemPedido itemRecebido : pedidoRecebido.getItens()) {

            if (itemRecebido.getProduto() == null ||
                    itemRecebido.getProduto().getId() == null) {

                throw new RuntimeException(
                        "Produto inválido no pedido."
                );
            }

            Produto produto = produtoRepository
                    .findById(itemRecebido.getProduto().getId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Produto não encontrado."
                            )
                    );

            if (itemRecebido.getQuantidade() <= 0) {
                throw new RuntimeException(
                        "A quantidade deve ser maior que zero."
                );
            }

            ItemPedido item = new ItemPedido();

            item.setProduto(produto);
            item.setQuantidade(itemRecebido.getQuantidade());

            // O preço vem do banco, não do navegador.
            item.setPrecoUnitario(produto.getPreco());

            item.setPedido(pedidoRecebido);

            total += produto.getPreco()
                    * item.getQuantidade();

            itensDoPedido.add(item);
        }

        pedidoRecebido.setItens(itensDoPedido);
        pedidoRecebido.setTotal(total);

        pedidoRecebido.setStatus(
                StatusPedido.RECEBIDO
        );

        return pedidoRepository.save(pedidoRecebido);
    }

    private void validarTipoAtendimento(Pedido pedido) {

        if (pedido.getTipoAtendimento() == null) {

            throw new RuntimeException(
                    "Escolha o tipo de atendimento."
            );
        }

        switch (pedido.getTipoAtendimento()) {

            case MESA:

                if (pedido.getNumeroMesa() == null ||
                        pedido.getNumeroMesa() < 1 ||
                        pedido.getNumeroMesa() > 15) {

                    throw new RuntimeException(
                            "A mesa deve estar entre 1 e 15."
                    );
                }

                pedido.setEnderecoEntrega(null);
                break;

            case ENTREGA:

                if (pedido.getEnderecoEntrega() == null ||
                        pedido.getEnderecoEntrega().isBlank()) {

                    throw new RuntimeException(
                            "Informe o endereço para entrega."
                    );
                }

                pedido.setNumeroMesa(null);
                break;

            case BALCAO:

                pedido.setNumeroMesa(null);
                pedido.setEnderecoEntrega(null);
                break;
        }
    }

    public Pedido salvar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public void excluir(Long id) {

        if (!pedidoRepository.existsById(id)) {
            throw new RuntimeException(
                    "Pedido não encontrado."
            );
        }

        pedidoRepository.deleteById(id);
    }

    public void excluirDoCliente(
            Long pedidoId,
            Long clienteId) {

        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Pedido não encontrado."
                        )
                );

        if (pedido.getCliente() == null ||
                !pedido.getCliente()
                        .getId()
                        .equals(clienteId)) {

            throw new RuntimeException(
                    "Você não pode excluir este pedido."
            );
        }

        pedidoRepository.delete(pedido);
    }
}