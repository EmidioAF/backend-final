package br.pucpr.restaurante.produtos

import org.springframework.data.domain.Sort
import org.springframework.data.jpa.repository.JpaRepository

interface ProdutoRepository : JpaRepository<Produto, Long> {
    fun findByCategoriaContainingIgnoreCase(categoria: String, sort: Sort): List<Produto>
}
