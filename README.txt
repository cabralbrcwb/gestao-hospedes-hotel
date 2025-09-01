### ✅ Checklist Final do Projeto: belavista-backend

Este documento serve como um checklist de todos os arquivos e classes criados e modificados durante o desenvolvimento do backend.

---

### 🏗️ Arquivos de Configuração e Build

- [X] `pom.xml`: Gerenciador de dependências do Maven, configurado com Spring Boot, Spring Data JPA, Web, Validation, PostgreSQL, Lombok e Springdoc.
- [X] `application.properties`: Configurações da aplicação, incluindo conexão com o banco de dados, estratégia de DDL (`create-drop`), e o `MessageSource`.
- [X] `messages.properties`: Arquivo central para todas as mensagens de erro e validação (i18n).

---

### ☕ Código Fonte Principal (`src/main/java`)

#### 📦 Pacote Principal (`br.com.senior.gestaohospedes.belavista`)
- [X] `BelavistaApplication.java`: Classe principal que inicia a aplicação Spring Boot e configura o bean do `Clock`.

#### 📦 Pacote `config`
- [X] `WebConfig.java`: Configuração do CORS para permitir a comunicação com o frontend Angular.

#### 📦 Pacote `controller`
- [X] `HospedeController.java`: Endpoints REST para o CRUD de Hóspedes.
- [X] `ReservaController.java`: Endpoints REST para o fluxo de Reservas (criar, check-in, check-out, buscar).

#### 📦 Pacote `dto` (Data Transfer Objects)
- [X] `ReservaRequestDTO.java`: DTO para receber os dados de criação de uma nova reserva.
- [X] `CheckoutResponseDTO.java`: DTO para estruturar a resposta detalhada do check-out.
- [X] `DetalheCustoDTO.java`: DTO para detalhar cada item de custo na fatura do check-out.
- [X] `ErrorResponseDTO.java`: DTO para padronizar as respostas de erro da API.

#### 📦 Pacote `entity`
- [X] `Hospede.java`: Entidade que representa o hóspede, com relacionamento `@OneToMany` para reservas.
- [X] `Reserva.java`: Entidade que representa a reserva, com relacionamento `@ManyToOne` para hóspede.
- [X] `StatusReserva.java`: Enum que define os status possíveis de uma reserva (`PENDENTE`, `CHECK_IN`, `CHECK_OUT`).

#### 📦 Pacote `exception`
- [X] `BusinessException.java`: Classe base abstrata para todas as exceções de negócio.
- [X] `GlobalExceptionHandler.java`: Handler central que captura todas as exceções e formata as respostas de erro usando o `MessageSource`.
- [X] `HospedeNaoEncontradoException.java`: Exceção para quando um hóspede não é encontrado.
- [X] `DocumentoDuplicadoException.java`: Exceção para tentativas de cadastrar um hóspede com documento já existente.
- [X] `HospedeComReservaAtivaException.java`: Exceção para impedir a exclusão de hóspedes com reservas ativas.
- [X] `ReservaNaoEncontradaException.java`: Exceção para quando uma reserva não é encontrada.
- [X] `DataInvalidaException.java`: Exceção para quando a data de saída é anterior à de entrada.
- [X] `ReservaSobrepostaException.java`: Exceção para impedir reservas com datas sobrepostas para o mesmo hóspede.
- [X] `ReservaStatusException.java`: Exceção para transições de status inválidas (ex: check-out sem check-in).
- [X] `CheckInForaDoHorarioException.java`: Exceção para tentativas de check-in antes do horário permitido.

#### 📦 Pacote `repository`
- [X] `HospedeRepository.java`: Interface Spring Data JPA para operações de banco de dados da entidade `Hospede`.
- [X] `ReservaRepository.java`: Interface Spring Data JPA para operações de banco de dados da entidade `Reserva`.

#### 📦 Pacote `service` e `service.impl`
- [X] `HospedeService.java`: Interface que define o contrato para os serviços de hóspede.
- [X] `HospedeServiceImpl.java`: Implementação da lógica de negócio para o CRUD de hóspedes, incluindo a validação de exclusão.
- [X] `ReservaService.java`: Interface que define o contrato para os serviços de reserva.
- [X] `ReservaServiceImpl.java`: Implementação da lógica de negócio para o fluxo de reservas, incluindo validações e o cálculo de custos.

---

### 🧪 Código de Teste (`src/test/java`)

- [X] `HospedeServiceImplTest.java`: Testes unitários para os principais cenários do serviço de hóspedes.
- [X] `ReservaServiceImplTest.java`: Testes unitários para os principais cenários do serviço de reservas, incluindo as validações de datas e os cálculos de custo.

---

### 📄 Documentação

- [X] `README.md`: Documento principal do projeto, com visão geral, tecnologias, funcionalidades e guia de execução.
- [X] `VISAO_GERAL_ARQUITETURA.md`: Documento detalhado da arquitetura, endpoints e exemplos de uso da API.
