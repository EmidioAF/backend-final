package br.pucpr.restaurante.pedidos

import org.springframework.data.jpa.repository.JpaRepository

interface PedidoRepository : JpaRepository<Pedido, Long>
