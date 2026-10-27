package br.pucpr.restaurante.produtos

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/produtos")
class ProdutoController(private val service: ProdutoService) {

    @GetMapping
    fun listar(
        @RequestParam(required = false) categoria: String?,
        @RequestParam(defaultValue = "ASC") sortDir: String
    ) = service.listar(categoria, sortDir)

    @GetMapping("/{id}")
    fun buscarPorId(@PathVariable id: Long) = service.buscarPorId(id)

    @PostMapping
    fun salvar(@RequestBody @Valid dto: ProdutoDTO) = service.salvar(dto)

    @PutMapping("/{id}")
    fun atualizar(@PathVariable id: Long, @RequestBody @Valid dto: ProdutoDTO) = service.atualizar(id, dto)

    @DeleteMapping("/{id}")
    fun deletar(@PathVariable id: Long) = service.deletar(id)
}
