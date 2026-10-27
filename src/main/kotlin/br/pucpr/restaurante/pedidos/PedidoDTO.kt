package br.pucpr.restaurante.pedidos

import br.pucpr.restaurante.produtos.ProdutoDTO
import br.pucpr.restaurante.produtos.toDTO
import jakarta.validation.constraints.NotNull

data class PedidoReqDTO(
    @field:NotNull(message = "ID do usuário é obrigatório")
    val usuarioId: Long
)

data class PedidoResDTO(
    val id: Long?,
    val status: String,
    val produtos: List<ProdutoDTO>
)

fun Pedido.toResDTO() = PedidoResDTO(id, status, produtos.map { it.toDTO() })
