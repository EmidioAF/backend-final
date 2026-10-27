package br.pucpr.restaurante.pedidos

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/pedidos")
class PedidoController(private val service: PedidoService) {

    @PostMapping
    fun criar(@RequestBody @Valid dto: PedidoReqDTO) = service.criar(dto)

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long) = service.buscarPorId(id)

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long) = service.deletar(id)

    @PostMapping("/{id}/produtos/{produtoId}")
    fun adicionarProduto(@PathVariable id: Long, @PathVariable produtoId: Long) = service.adicionarProduto(id, produtoId)

    @DeleteMapping("/{id}/produtos/{produtoId}")
    fun removerProduto(@PathVariable id: Long, @PathVariable produtoId: Long) = service.removerProduto(id, produtoId)
}
