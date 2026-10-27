package br.pucpr.restaurante.produtos

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class ProdutoDTO(
    val id: Long? = null,
    
    @field:NotBlank(message = "Nome do produto é obrigatório")
    val nome: String,
    
    @field:NotNull(message = "Preço é obrigatório")
    @field:Min(value = 0, message = "Preço não pode ser negativo")
    val preco: Double,
    
    @field:NotBlank(message = "Categoria é obrigatória")
    val categoria: String
)

fun Produto.toDTO() = ProdutoDTO(id, nome, preco, categoria)
