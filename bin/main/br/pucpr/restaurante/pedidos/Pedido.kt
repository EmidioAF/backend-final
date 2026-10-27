package br.pucpr.restaurante.pedidos

import br.pucpr.restaurante.produtos.Produto
import br.pucpr.restaurante.usuarios.Usuario
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "pedidos")
class Pedido(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    
    var dataCriacao: LocalDateTime = LocalDateTime.now(),
    
    var status: String = "NOVO",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    var usuario: Usuario,

    @ManyToMany
    @JoinTable(
        name = "pedido_produto",
        joinColumns = [JoinColumn(name = "pedido_id")],
        inverseJoinColumns = [JoinColumn(name = "produto_id")]
    )
    var produtos: MutableList<Produto> = mutableListOf()
)
