package br.pucpr.restaurante.config

import br.pucpr.restaurante.produtos.Produto
import br.pucpr.restaurante.produtos.ProdutoRepository
import br.pucpr.restaurante.usuarios.PerfilCliente
import br.pucpr.restaurante.usuarios.Usuario
import br.pucpr.restaurante.usuarios.UsuarioRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class DataInitializer(
    private val usuarioRepository: UsuarioRepository,
    private val produtoRepository: ProdutoRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        if (usuarioRepository.count() == 0L) {
            val admin = Usuario(
                nome = "Admin Supremo",
                email = "admin@restaurante.com",
                senha = passwordEncoder.encode("senha123"),
                role = "ADMIN"
            )
            val perfil = PerfilCliente(
                telefone = "41999998888",
                endereco = "Rua Imaculada Conceição, 1155 - Prado Velho, Curitiba",
                usuario = admin
            )
            admin.perfil = perfil
            usuarioRepository.save(admin)
        }

        if (produtoRepository.count() == 0L) {
            produtoRepository.saveAll(listOf(
                Produto(nome = "X-Bacon Brutal", preco = 35.50, categoria = "LANCHE"),
                Produto(nome = "Coca-Cola Geladaça", preco = 8.00, categoria = "BEBIDA"),
                Produto(nome = "Batata Frita Monstra", preco = 20.00, categoria = "PORCAO")
            ))
        }
    }
}
