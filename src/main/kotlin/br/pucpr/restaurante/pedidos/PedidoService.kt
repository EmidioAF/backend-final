package br.pucpr.restaurante.pedidos

import br.pucpr.restaurante.config.ElementoNaoEncontradoException
import br.pucpr.restaurante.config.RegraDeNegocioException
import br.pucpr.restaurante.produtos.ProdutoRepository
import br.pucpr.restaurante.usuarios.UsuarioRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PedidoService(
    private val pedidoRepository: PedidoRepository,
    private val produtoRepository: ProdutoRepository,
    private val usuarioRepository: UsuarioRepository
) {
    private val log = LoggerFactory.getLogger(PedidoService::class.java)

    fun buscarPorId(id: Long): PedidoResDTO = pedidoRepository.findById(id)
        .orElseThrow { ElementoNaoEncontradoException("Pedido $id não encontrado") }.toResDTO()

    @Transactional
    fun criar(dto: PedidoReqDTO): PedidoResDTO {
        val usuario = usuarioRepository.findById(dto.usuarioId)
            .orElseThrow { ElementoNaoEncontradoException("Usuário não existe") }
        val pedido = pedidoRepository.save(Pedido(usuario = usuario))
        log.info("Pedido criado: ID ${pedido.id} pelo usuário ${usuario.email}")
        return pedido.toResDTO()
    }

    @Transactional
    fun deletar(id: Long) {
        if (!pedidoRepository.existsById(id)) throw ElementoNaoEncontradoException("Pedido não encontrado")
        pedidoRepository.deleteById(id)
        log.info("Pedido exterminado: ID $id")
    }

    @Transactional
    fun adicionarProduto(pedidoId: Long, produtoId: Long): PedidoResDTO {
        val pedido = pedidoRepository.findById(pedidoId).orElseThrow { ElementoNaoEncontradoException("Pedido $pedidoId não encontrado") }
        val produto = produtoRepository.findById(produtoId).orElseThrow { ElementoNaoEncontradoException("Produto $produtoId não encontrado") }
        
        pedido.produtos.add(produto)
        log.info("Produto [${produto.nome}] adicionado ao pedido [$pedidoId]")
        return pedidoRepository.save(pedido).toResDTO()
    }

    @Transactional
    fun removerProduto(pedidoId: Long, produtoId: Long): PedidoResDTO {
        val pedido = pedidoRepository.findById(pedidoId).orElseThrow { ElementoNaoEncontradoException("Pedido $pedidoId não encontrado") }
        val produto = produtoRepository.findById(produtoId).orElseThrow { ElementoNaoEncontradoException("Produto $produtoId não encontrado") }
        
        if (!pedido.produtos.remove(produto)) {
            throw RegraDeNegocioException("Esse produto não pertence a este pedido.")
        }
        
        log.info("Produto [${produto.nome}] removido do pedido [$pedidoId]")
        return pedidoRepository.save(pedido).toResDTO()
    }
}
