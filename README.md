<h1>Projeto HTTPMySQL</h1>
    Este projeto Java demonstra uma integração entre comunicação HTTP, MySQL e JDBC para armazenamento e recuperação de dados de endereços.
      Ele utiliza a API BrasilAPI para obter informações detalhadas de endereços com base no CEP fornecido pelo usuário.
      Os dados do endereço são então armazenados em um banco de dados MySQL local.
<h2>Funcionalidades</h2>
    <ul>
        <li>Solicitação de informações de endereço através da inserção do CEP pelo usuário.</li>
        <li>Comunicação HTTP assíncrona utilizando a classe HttpClient do Java.</li>
        <li>Armazenamento dos dados de endereço em um banco de dados MySQL local.</li>
        <li>Seleção e exibição de todos os dados da tabela de endereços.</li>
    </ul>
<h2>Pré-requisitos</h2>
    <ul>
        <li>JDK 11 ou superior instalado</li>
        <li>MySQL Server instalado e em execução localmente</li>
        <li>Dependências do Maven configuradas (se estiver usando o Maven)</li>
    </ul>
 <h2>Configuração</h2>
    <ol>
        <li>Clone este repositório em sua máquina local.</li>
        <li>Certifique-se de ter configurado o MySQL com as credenciais corretas no arquivo <code>Sistema.java</code>.</li>
        <li>Execute o script <code>create_database.sql</code> para criar o banco de dados e a tabela necessária.</li>
        <li>Abra o projeto em sua IDE preferida e execute o arquivo <code>Sistema.java</code>.</li>
    </ol>

  <h2>Contribuição</h2>
    <p>Contribuições são bem-vindas! Sinta-se à vontade para propor melhorias, correções de bugs ou novas funcionalidades através de pull requests.</p>
