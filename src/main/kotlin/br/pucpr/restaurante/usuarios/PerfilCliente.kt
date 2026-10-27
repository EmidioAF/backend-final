package br.pucpr.restaurante.usuarios

import jakarta.persistence.*

@Entity
@Table(name = "perfis_clientes")
class PerfilCliente(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    
    var telefone: String = "",
    
    var endereco: String = "",

    @OneToOne
    @JoinColumn(name = "usuario_id")
    var usuario: Usuario
)
