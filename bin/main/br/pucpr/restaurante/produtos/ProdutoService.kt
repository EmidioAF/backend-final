package br.pucpr.restaurante.produtos

import br.pucpr.restaurante.config.ElementoNaoEncontradoException
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProdutoService(private val repository: ProdutoRepository) {
    private val log = LoggerFactory.getLogger(ProdutoService::class.java)

    fun listar(categoria: String?, sortDir: String): List<ProdutoDTO> {
        val direction = if (sortDir.equals("DESC", true)) Sort.Direction.DESC else Sort.Direction.ASC
        val sort = Sort.by(direction, "nome")
        
        val produtos = if (categoria.isNullOrBlank()) {
            repository.findAll(sort)
        } else {
            repository.findByCategoriaContainingIgnoreCase(categoria, sort)
        }
        return produtos.map { it.toDTO() }
    }

    fun buscarPorId(id: Long): ProdutoDTO = repository.findById(id)
        .orElseThrow { ElementoNaoEncontradoException("Produto ID $id não encontrado") }.toDTO()

    @Transactional
    fun salvar(dto: ProdutoDTO): ProdutoDTO {
        val produto = Produto(nome = dto.nome, preco = dto.preco, categoria = dto.categoria.uppercase())
        val salvo = repository.save(produto)
        log.info("Novo produto criado na base: ${salvo.nome} (ID: ${salvo.id})")
        return salvo.toDTO()
    }

    @Transactional
    fun atualizar(id: Long, dto: ProdutoDTO): ProdutoDTO {
        val produto = repository.findById(id).orElseThrow { ElementoNaoEncontradoException("Produto ID $id não encontrado") }
        produto.nome = dto.nome
        produto.preco = dto.preco
        produto.categoria = dto.categoria.uppercase()
        log.info("Produto atualizado: ID $id")
        return repository.save(produto).toDTO()
    }

    @Transactional
    fun deletar(id: Long) {
        if (!repository.existsById(id)) throw ElementoNaoEncontradoException("Produto ID $id não existe")
        repository.deleteById(id)
        log.info("Produto apagado com sucesso: ID $id")
    }
}
