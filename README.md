# AV4 - Desenvolvimento Web III Fatec SJC

**Requisitos: Java 17.** 

## Como rodar

```bash
> ./mvnw clean install
> ./mvnw spring-boot:run
```

Estará disponível em `http://localhost:8080/`.

Os dados serão salvos temporariamente no banco em memória H2.

## Nova implementação da versão 4 - JWT

Foi implementado um login que gera um token JWT e atualmente os endpoints possuem níveis de acesso, portanto é necessário gerar um token e passar via Bearer Token para utilizar a API.

## Endpoints

A aplicação possui as entidades: Cliente, Documento, Endereço e Telefone, Empresa, Email, Mercadoria, Servico, 
Usuario, Veiculo, Venda, Credencial, CredencialCodigoBarra e CredencialUsuarioSenha.

Cada entidade possui os endpoints para as ações CRUD, seguindo os níveis de maturidade RMM (Richardson Maturity Model), principalmente na aplicação de HATEOAS.

Endpoint para o login:

- `POST /login`: Para gerar um token de acesso para uma das roles.

Endpoints para credenciais de usuários, que podem ser acessadas como sub-rotas de usuarios:

- `GET /usuarios` ou `GET /usuarios/{id}`: Para ver os dados do usuario, incluindo o nomeUsuario da credencial
- `POST /usuarios/{id}`: Para cadastrar uma credencial

Exemplo para as rotas do Cliente:

- `POST /clientes`: Cria a entidade
- `GET /clientes/{id}`: Busca entidade do id passado
- `GET /clientes`: Lista todas as entidades criadas
- `PUT /clientes/{id}`: Atualiza a entidade do id passado 
- `DELETE /clientes/{id}`: Remove a entidade do id passado (Apenas para a entidade Cliente)

## Exemplos de Requisições

`POST /login`

```json
{
  "nomeUsuario": "admin",
  "senha": "123456"
}
```

`POST /usuarios/{id}/credenciais`

```json
{
  "nomeUsuario": "dompedrocliente00",
  "senha": "1234561"
}
```

`POST /clientes:`

```json
{
  "nome": "Gustavo Ribeiro da Rosa",
  "nomeSocial": "Gustavo",
  "dataNascimento": "1990-05-15",
  "documentos": [
    {
      "tipo": "RG",
      "numero": "0000000000"
    }
  ],
  "endereco": {
    "estado": "SP",
    "cidade": "São José dos Campos",
    "bairro": "Eugênio de Melo",
    "rua": "Avenida Cesare Monsueto Giulio Lattes",
    "numero": "100",
    "codigoPostal": "12247-014",
    "informacoesAdicionais": ""
  },
  "telefones": [
    {
      "ddd": "11",
      "numero": "91234-5678"
    },
    {
      "ddd": "12",
      "numero": "99876-5432"
    }
  ]
}
```

`PUT /telefones/1`

```json
{
  "ddd": "88",
  "numero": "456789123"
}
```


`PUT /enderecos/1`

```json
{
    "estado": "SP",
    "cidade": "São José dos Campos",
    "bairro": "Santana",
    "rua": "Av. Olivo Gomes",
    "numero": "100",
    "codigoPostal": "12211-420",
    "informacoesAdicionais": "Parque Roberto Burle Marx - Parque da Cidade"
}
```
