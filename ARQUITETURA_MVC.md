# Arquitetura MVC - Sistema de Gestão de Urgência Hospitalar

## Estrutura do Projeto

O projeto foi reestruturado seguindo o padrão **MVC (Model-View-Controller)** com camadas adicionais de **BLL (Business Logic Layer)** e **DAL (Data Access Layer)**.

```
src/main/java/com/example/lp1/
├── Main.java                    # Ponto de entrada da aplicação
├── Model/                       # Modelos de dados (Entidades)
│   ├── utente.java
│   ├── medico.java
│   ├── sintoma.java
│   └── especialidade.java
├── View/                        # Camada de apresentação
│   └── MenuView.java           # Interface com o utilizador
├── Controller/                  # Controladores
│   └── SimuladorController.java # Coordena View e BLL
├── BLL/                        # Lógica de negócio
│   └── SimuladorBLL.java       # Regras de negócio do simulador
├── DAL/                        # Acesso a dados
│   ├── especialidadeDAL.java
│   ├── medicoDAL.java
│   └── sintomaDAL.java
└── Utils/                      # Utilitários
    ├── Utils.java
    └── Enums.java

src/main/resources/
└── MenuView.txt                # Template do menu
```

## Camadas da Arquitetura

### 1. **Model (Modelo)**
**Localização:** `Model/`

**Responsabilidade:** Representar as entidades de dados do sistema.

**Classes:**
- `utente.java` - Representa um utente/paciente
- `medico.java` - Representa um médico
- `sintoma.java` - Representa um sintoma
- `especialidade.java` - Representa uma especialidade médica

**Características:**
- Apenas contêm atributos e getters/setters
- Não contêm lógica de negócio
- São POJOs (Plain Old Java Objects)

### 2. **View (Visão)**
**Localização:** `View/MenuView.java` + `resources/MenuView.txt`

**Responsabilidade:** Interação com o utilizador (entrada/saída).

**Funcionalidades:**
- `mostrarMenu()` - Exibe o menu principal (lê de resources)
- `lerOpcao()` - Lê opção do utilizador
- `lerTexto()` - Lê texto do utilizador
- `mostrarMensagem()` - Exibe mensagens
- `mostrarErro()` - Exibe erros
- `mostrarSucesso()` - Exibe mensagens de sucesso
- `aguardarEnter()` - Pausa para o utilizador

**Princípios:**
- **NÃO** contém lógica de negócio
- **NÃO** acede diretamente aos modelos
- Apenas responsável pela apresentação

### 3. **Controller (Controlador)**
**Localização:** `Controller/SimuladorController.java`

**Responsabilidade:** Coordenar entre View e BLL.

**Fluxo:**
```
Utilizador → View → Controller → BLL → DAL
                ↓                  ↓
            Resposta          Dados/Lógica
```

**Métodos principais:**
- `iniciar()` - Inicia o sistema
- `avancarHora()` - Coordena avanço de hora
- `registarUtente()` - Coordena registo de utente
- `realizarTriagem()` - Coordena triagem
- `listarUtentes()` - Coordena listagem

**Princípios:**
- Recebe input da View
- Chama métodos da BLL
- Processa respostas da BLL
- Envia output para a View
- **NÃO** contém lógica de negócio complexa

### 4. **BLL (Business Logic Layer)**
**Localização:** `BLL/SimuladorBLL.java`

**Responsabilidade:** Implementar toda a lógica de negócio do simulador.

**Funcionalidades:**
- Gestão do tempo (24 horas, reinício de dia)
- Gestão de filas (espera, triagem)
- Cálculo de urgência e especialidade
- Encaminhamento automático/manual
- Escalamento de urgência
- Controlo de disponibilidade de médicos
- Notificações de eventos

**Princípios:**
- Contém **TODA** a lógica de negócio
- **NÃO** interage diretamente com o utilizador
- Retorna dados estruturados (arrays de strings)
- Pode chamar DAL se necessário

### 5. **DAL (Data Access Layer)**
**Localização:** `DAL/`

**Responsabilidade:** Acesso e leitura de dados dos ficheiros.

**Classes:**
- `especialidadeDAL.java` - Carrega especialidades
- `medicoDAL.java` - Carrega médicos
- `sintomaDAL.java` - Carrega sintomas

**Princípios:**
- Responsável apenas por ler/escrever dados
- **NÃO** contém lógica de negócio
- Retorna arrays de objetos Model

### 6. **Utils (Utilitários)**
**Localização:** `Utils/`

**Classes:**
- `Utils.java` - Coordena carregamento de ficheiros e associações
- `Enums.java` - Enumerações (nivelUrgencia)

## Fluxo de Dados

### Exemplo: Registar Utente

```
1. Utilizador digita nome
   ↓
2. MenuView.lerTexto() captura input
   ↓
3. SimuladorController.registarUtente() recebe nome
   ↓
4. Controller valida input básico
   ↓
5. Controller chama SimuladorBLL.registarUtente(nome)
   ↓
6. BLL cria novo utente e adiciona à fila
   ↓
7. BLL retorna (implicitamente)
   ↓
8. Controller formata mensagem de sucesso
   ↓
9. Controller chama MenuView.mostrarSucesso()
   ↓
10. View exibe mensagem ao utilizador
```

### Exemplo: Avançar Hora

```
1. Utilizador escolhe opção 1
   ↓
2. SimuladorController.avancarHora()
   ↓
3. Controller chama SimuladorBLL.avancarHora()
   ↓
4. BLL executa lógica:
   - Incrementa hora
   - Verifica escalamento urgência
   - Atualiza disponibilidade médicos
   - Finaliza atendimentos
   - Verifica médicos fora horário
   ↓
5. BLL retorna array de notificações
   ↓
6. Controller processa cada notificação
   ↓
7. Controller chama View para exibir cada notificação
   ↓
8. View exibe notificações ao utilizador
```

## Vantagens da Arquitetura MVC

### ✅ Separação de Responsabilidades
- Cada camada tem uma função específica
- Facilita manutenção e debugging

### ✅ Reutilização de Código
- BLL pode ser usado por diferentes interfaces (CLI, GUI, Web)
- DAL pode ser substituído (ficheiros → BD) sem afetar BLL

### ✅ Testabilidade
- Cada camada pode ser testada independentemente
- BLL pode ser testada sem interface gráfica

### ✅ Escalabilidade
- Fácil adicionar novas funcionalidades
- Fácil adicionar novas views (ex: interface gráfica)

### ✅ Manutenibilidade
- Mudanças numa camada não afetam outras
- Código mais organizado e legível

## Princípios SOLID Aplicados

### Single Responsibility Principle (SRP)
- Cada classe tem uma única responsabilidade
- View → Apresentação
- Controller → Coordenação
- BLL → Lógica de negócio
- DAL → Acesso a dados

### Dependency Inversion Principle (DIP)
- Controller depende de abstrações (BLL, View)
- BLL não depende de implementações concretas

## Como Adicionar Nova Funcionalidade

### Exemplo: Adicionar "Listar Estatísticas"

1. **BLL** - Adicionar método `calcularEstatisticas()`
   ```java
   public String[] calcularEstatisticas() {
       // Lógica de cálculo
       return estatisticas;
   }
   ```

2. **Controller** - Adicionar método `listarEstatisticas()`
   ```java
   private void listarEstatisticas() {
       String[] stats = simuladorBLL.calcularEstatisticas();
       // Processar e enviar para View
   }
   ```

3. **View** - Adicionar opção no menu
   ```
   9 - Listar Estatísticas
   ```

4. **Controller** - Adicionar case no switch
   ```java
   case "9":
       listarEstatisticas();
       break;
   ```

## Conclusão

A arquitetura MVC implementada garante:
- ✅ Código organizado e modular
- ✅ Fácil manutenção e extensão
- ✅ Separação clara de responsabilidades
- ✅ Testabilidade de cada camada
- ✅ Conformidade com boas práticas de engenharia de software
