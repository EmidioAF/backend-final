package br.pucpr.restaurante.produtos

import jakarta.persistence.*

@Entity
@Table(name = "produtos")
class Produto(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    
    var nome: String = "",
    
    var preco: Double = 0.0,
    
    var categoria: String = ""
)
