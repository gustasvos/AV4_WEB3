package com.autobots.automanager;

import java.time.LocalDateTime;

import com.autobots.automanager.entidades.*;
import com.autobots.automanager.enumeracoes.PerfilAcesso;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class AutomanagerApplication implements CommandLineRunner {

	@Autowired
	private RepositorioUsuario repositorio;

	public static void main(String[] args) {
		SpringApplication.run(AutomanagerApplication.class, args);
	}

	// @Component
	// public static class Runner implements ApplicationRunner {

		// @Autowired
		// private EmpresaRepositorio empresaRepositorio;

		// @Override
		// public void run(ApplicationArguments args) throws Exception {

		// 	Empresa empresa = new Empresa();
		// 	empresa.setRazaoSocial("Car service toyota ltda");
		// 	empresa.setNomeFantasia("Car service manutenção veicular");
		// 	empresa.setDataCadastro(LocalDateTime.now());

		// 	Endereco enderecoEmpresa = new Endereco();
		// 	enderecoEmpresa.setEstado("São Paulo");
		// 	enderecoEmpresa.setCidade("São Paulo");
		// 	enderecoEmpresa.setBairro("Centro");
		// 	enderecoEmpresa.setRua("Av. São João");
		// 	enderecoEmpresa.setNumero("00");
		// 	enderecoEmpresa.setCodigoPostal("01035-000");

		// 	empresa.setEndereco(enderecoEmpresa);

		// 	Telefone telefoneEmpresa = new Telefone();
		// 	telefoneEmpresa.setDdd("011");
		// 	telefoneEmpresa.setNumero("986454527");

		// 	empresa.getTelefones().add(telefoneEmpresa);

		// 	Usuario funcionario = new Usuario();
		// 	funcionario.setNome("Pedro Alcântara de Bragança e Bourbon");
		// 	funcionario.setNomeSocial("Dom Pedro");
		// 	funcionario.getPerfis().add(PerfilUsuario.FUNCIONARIO);

		// 	Email emailFuncionario = new Email();
		// 	emailFuncionario.setEndereco("a@a.com");

		// 	funcionario.getEmails().add(emailFuncionario);

		// 	Endereco enderecoFuncionario = new Endereco();
		// 	enderecoFuncionario.setEstado("São Paulo");
		// 	enderecoFuncionario.setCidade("São Paulo");
		// 	enderecoFuncionario.setBairro("Jardins");
		// 	enderecoFuncionario.setRua("Av. São Gabriel");
		// 	enderecoFuncionario.setNumero("00");
		// 	enderecoFuncionario.setCodigoPostal("01435-001");

		// 	funcionario.setEndereco(enderecoFuncionario);

		// 	empresa.getUsuarios().add(funcionario);

		// 	Telefone telefoneFuncionario = new Telefone();
		// 	telefoneFuncionario.setDdd("011");
		// 	telefoneFuncionario.setNumero("9854633728");

		// 	funcionario.getTelefones().add(telefoneFuncionario);

		// 	Documento cpf = new Documento();
		// 	cpf.setDataEmissao(new Date());
		// 	cpf.setNumero("856473819229");
		// 	cpf.setTipo(TipoDocumento.CPF);

		// 	funcionario.getDocumentos().add(cpf);

		// 	CredencialUsuarioSenha credencialFuncionario = new CredencialUsuarioSenha();
		// 	credencialFuncionario.setInativo(false);
		// 	credencialFuncionario.setNomeUsuario("dompedrofuncionario");
		// 	credencialFuncionario.setSenha("123456");
		// 	credencialFuncionario.setCriacao(LocalDateTime.now());
		// 	credencialFuncionario.setUltimoAcesso(LocalDateTime.now().minusHours(1));

		// 	funcionario.getCredenciais().add(credencialFuncionario);

		// 	Usuario fornecedor = new Usuario();
		// 	fornecedor.setNome("Componentes varejo de partes automotivas ltda");
		// 	fornecedor.setNomeSocial("Loja do carro, vendas de componentes automotivos");
		// 	fornecedor.getPerfis().add(PerfilUsuario.FORNECEDOR);

		// 	Email emailFornecedor = new Email();
		// 	emailFornecedor.setEndereco("f@f.com");

		// 	fornecedor.getEmails().add(emailFornecedor);

		// 	CredencialUsuarioSenha credencialFornecedor = new CredencialUsuarioSenha();
		// 	credencialFornecedor.setInativo(false);
		// 	credencialFornecedor.setNomeUsuario("dompedrofornecedor");
		// 	credencialFornecedor.setSenha("123456");
		// 	credencialFornecedor.setCriacao(LocalDateTime.now());
		// 	credencialFornecedor.setUltimoAcesso(LocalDateTime.now().minusHours(1));

		// 	fornecedor.getCredenciais().add(credencialFornecedor);

		// 	Documento cnpj = new Documento();
		// 	cnpj.setDataEmissao(new Date());
		// 	cnpj.setNumero("00014556000100");
		// 	cnpj.setTipo(TipoDocumento.CNPJ);

		// 	fornecedor.getDocumentos().add(cnpj);

		// 	Endereco enderecoFornecedor = new Endereco();
		// 	enderecoFornecedor.setEstado("Rio de Janeiro");
		// 	enderecoFornecedor.setCidade("Rio de Janeiro");
		// 	enderecoFornecedor.setBairro("Centro");
		// 	enderecoFornecedor.setRua("Av. República do chile");
		// 	enderecoFornecedor.setNumero("00");
		// 	enderecoFornecedor.setCodigoPostal("20031-170");

		// 	fornecedor.setEndereco(enderecoFornecedor);

		// 	empresa.getUsuarios().add(fornecedor);

		// 	Mercadoria rodaLigaLeve = new Mercadoria();
		// 	rodaLigaLeve.setDataCadastro(LocalDate.now());
		// 	rodaLigaLeve.setDataFabricacao(LocalDate.now().minusWeeks(2));
		// 	rodaLigaLeve.setNome("Roda de liga leva modelo toyota etios");
		// 	rodaLigaLeve.setDataValidade(LocalDate.now().plusYears(2));
		// 	rodaLigaLeve.setQuantidade(30);
		// 	rodaLigaLeve.setValor(300.0);
		// 	rodaLigaLeve.setDescricao("Roda de liga leve original de fábrica da toyta para modelos do tipo hatch");

		// 	empresa.getMercadorias().add(rodaLigaLeve);

		// 	fornecedor.getMercadorias().add(rodaLigaLeve);

		// 	Usuario cliente = new Usuario();
		// 	cliente.setNome("Pedro Alcântara de Bragança e Bourbon");
		// 	cliente.setNomeSocial("Dom pedro cliente");
		// 	cliente.getPerfis().add(PerfilUsuario.CLIENTE);

		// 	Email emailCliente = new Email();
		// 	emailCliente.setEndereco("c@c.com");

		// 	cliente.getEmails().add(emailCliente);

		// 	Documento cpfCliente = new Documento();
		// 	cpfCliente.setDataEmissao(new Date());
		// 	cpfCliente.setNumero("12584698533");
		// 	cpfCliente.setTipo(TipoDocumento.CPF);

		// 	cliente.getDocumentos().add(cpfCliente);

		// 	CredencialUsuarioSenha credencialCliente = new CredencialUsuarioSenha();
		// 	credencialCliente.setInativo(false);
		// 	credencialCliente.setNomeUsuario("dompedrocliente");
		// 	credencialCliente.setSenha("123456");
		// 	credencialCliente.setCriacao(LocalDateTime.now());
		// 	credencialCliente.setUltimoAcesso(LocalDateTime.now().minusHours(1));

		// 	cliente.getCredenciais().add(credencialCliente);

		// 	Endereco enderecoCliente = new Endereco();
		// 	enderecoCliente.setEstado("São Paulo");
		// 	enderecoCliente.setCidade("São José dos Campos");
		// 	enderecoCliente.setBairro("Centro");
		// 	enderecoCliente.setRua("Av. Dr. Nelson D'Ávila");
		// 	enderecoCliente.setNumero("00");
		// 	enderecoCliente.setCodigoPostal("12245-070");

		// 	cliente.setEndereco(enderecoCliente);

		// 	Veiculo veiculo = new Veiculo();
		// 	veiculo.setPlaca("ABC-0000");
		// 	veiculo.setModelo("corolla-cross");
		// 	veiculo.setTipo(TipoVeiculo.SUV);
		// 	veiculo.setProprietario(cliente);

		// 	cliente.getVeiculos().add(veiculo);

		// 	empresa.getUsuarios().add(cliente);

		// 	Servico trocaRodas = new Servico();
		// 	trocaRodas.setDescricao("Troca das rodas do carro por novas");
		// 	trocaRodas.setNome("Troca de rodas");
		// 	trocaRodas.setValor(50.0);

		// 	Servico alinhamento = new Servico();
		// 	alinhamento.setDescricao("Alinhamento das rodas do carro");
		// 	alinhamento.setNome("Alinhamento de rodas");
		// 	alinhamento.setValor(50.0);

		// 	empresa.getServicos().add(trocaRodas);
		// 	empresa.getServicos().add(alinhamento);

		// 	Venda venda = new Venda();
		// 	venda.setDataCadastro(LocalDateTime.now());
		// 	venda.setCliente(cliente);
		// 	venda.getMercadorias().add(rodaLigaLeve);
		// 	venda.setIdentificacao("1234698745");
		// 	venda.setFuncionario(funcionario);
		// 	venda.getServicos().add(trocaRodas);
		// 	venda.getServicos().add(alinhamento);
		// 	venda.setVeiculo(veiculo);
		// 	veiculo.getVendas().add(venda);

		// 	empresa.getVendas().add(venda);

		// 	empresaRepositorio.save(empresa);

		// 	Mercadoria rodaLigaLeve2 = new Mercadoria();
		// 	rodaLigaLeve2.setDataCadastro(LocalDate.now());
		// 	rodaLigaLeve2.setDataFabricacao(LocalDate.now().minusMonths(2));
		// 	rodaLigaLeve2.setNome("Roda de liga leva modelo toyota etios");
		// 	rodaLigaLeve2.setDataValidade(LocalDate.now().plusYears(2));
		// 	rodaLigaLeve2.setQuantidade(30);
		// 	rodaLigaLeve2.setValor(300.0);
		// 	rodaLigaLeve2.setDescricao("Roda de liga leve original de fábrica da toyta para modelos do tipo hatch");

		// 	Servico alinhamento2 = new Servico();
		// 	alinhamento2.setDescricao("Alinhamento das rodas do carro");
		// 	alinhamento2.setNome("Alinhamento de rodas");
		// 	alinhamento2.setValor(50.0);

		// 	Servico balanceamento = new Servico();
		// 	balanceamento.setDescricao("balanceamento das rodas do carro");
		// 	balanceamento.setNome("balanceamento de rodas");
		// 	balanceamento.setValor(30.0);

		// 	Venda venda2 = new Venda();
		// 	venda2.setDataCadastro(LocalDateTime.now());
		// 	venda2.setCliente(cliente);
		// 	venda2.getMercadorias().add(rodaLigaLeve2);
		// 	venda2.setIdentificacao("1234698749");
		// 	venda2.setFuncionario(funcionario);
		// 	venda2.getServicos().add(balanceamento);
		// 	venda2.getServicos().add(alinhamento2);
		// 	venda2.setVeiculo(veiculo);
		// 	veiculo.getVendas().add(venda2);

		// 	empresa.getVendas().add(venda2);

		// 	empresaRepositorio.save(empresa);
		// }

	@Override
	public void run(String... args) throws Exception {
		BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();

		criarUsuario("administrador", "admin", PerfilAcesso.ROLE_ADMIN, codificador);
		criarUsuario("gerente", "gerente", PerfilAcesso.ROLE_GERENTE, codificador);
		criarUsuario("vendedor", "vendedor", PerfilAcesso.ROLE_VENDEDOR, codificador);
		criarUsuario("cliente", "cliente", PerfilAcesso.ROLE_CLIENTE, codificador);
	}

	private void criarUsuario(String nome, String nomeUsuario, PerfilAcesso perfil, BCryptPasswordEncoder codificador) {
		Usuario usuario = new Usuario();
		usuario.setNome(nome);
		usuario.getPerfisAcesso().add(perfil);

		CredencialUsuarioSenha credencial = new CredencialUsuarioSenha();
		credencial.setNomeUsuario(nomeUsuario);
		credencial.setSenha(codificador.encode("123456"));
		credencial.setCriacao(LocalDateTime.now());
		credencial.setInativo(false);

		usuario.getCredenciais().add(credencial);
		repositorio.save(usuario);
	}
}